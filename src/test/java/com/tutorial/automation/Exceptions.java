package com.tutorial.automation;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * One-file tour of **Custom Exception Handling for a Java Automation Framework**.
 * Mirrors the style of the DataTypes demo: compact sections, runnable, Java 17+.
 */
public class Exceptions {

    // ───────────────────────────── 0) LITTLE VALUE OBJECTS ─────────────────────────────
    public record TestId(String value) {
        public TestId {
            if (value == null || value.isBlank()) throw new IllegalArgumentException("TestId blank");
        }
        @Override public String toString(){ return value; }
    }
    public record Url(String value) {
        public Url {
            if (value == null || !value.matches("^https?://.+")) throw new IllegalArgumentException("Bad URL");
        }
        @Override public String toString(){ return value; }
    }
    public record Locator(String strategy, String query) {
        public Locator {
            if (strategy==null || strategy.isBlank() || query==null || query.isBlank())
                throw new IllegalArgumentException("Locator parts blank");
        }
        @Override public String toString(){ return strategy+":"+query; }
    }

    // ───────────────────────────── 1) CORE EXCEPTION HIERARCHY ─────────────────────────────
    enum Severity { INFO, WARN, ERROR, FATAL }
    enum Area { CONFIG, DRIVER, LOCATOR, WAIT, NETWORK, ASSERTION, DATA, PARALLEL, IO, UNKNOWN }

    /** Base framework runtime exception with metadata. */
    public static class FxException extends RuntimeException {
        private final Severity severity;
        private final Area area;
        private final Map<String, Object> meta;
        public FxException(String msg, Severity sev, Area area){ this(msg, sev, area, null, Map.of()); }
        public FxException(String msg, Severity sev, Area area, Throwable cause, Map<String,Object> meta){
            super(msg, cause);
            this.severity = Objects.requireNonNull(sev);
            this.area = Objects.requireNonNull(area);
            this.meta = meta==null ? Map.of() : Map.copyOf(meta);
        }
        public Severity severity(){ return severity; }
        public Area area(){ return area; }
        public Map<String,Object> meta(){ return meta; }
    }

    // Specific leaves (extend as your framework grows)
    public static final class ConfigException extends FxException { public ConfigException(String m){ super(m, Severity.ERROR, Area.CONFIG); } }
    public static final class DriverLaunchException extends FxException { public DriverLaunchException(String m, Throwable c){ super(m, Severity.FATAL, Area.DRIVER, c, null); } }
    public static final class LocatorNotFoundException extends FxException { public LocatorNotFoundException(Locator loc){ super("Locator not found: "+loc, Severity.ERROR, Area.LOCATOR); } }
    public static final class StaleElementRetriedException extends FxException { public StaleElementRetriedException(Locator loc, int attempts, Throwable c){ super("Stale element after "+attempts+" attempts: "+loc, Severity.WARN, Area.WAIT, c, Map.of("attempts", attempts, "locator", loc.toString())); } }
    public static final class TimeoutException extends FxException { public TimeoutException(String m){ super(m, Severity.ERROR, Area.WAIT); } }
    public static final class NetworkRetryExhausted extends FxException { public NetworkRetryExhausted(String url, int attempts, Throwable c){ super("HTTP retries exhausted @ "+url+" attempts="+attempts, Severity.ERROR, Area.NETWORK, c, Map.of("url", url, "attempts", attempts)); } }
    public static final class SoftAssertionAggregate extends FxException { public SoftAssertionAggregate(List<String> failures){ super("Soft failures: "+failures.size(), Severity.ERROR, Area.ASSERTION, null, Map.of("failures", failures)); } }
    public static final class ParallelDeadlockDetected extends FxException { public ParallelDeadlockDetected(String m){ super(m, Severity.FATAL, Area.PARALLEL); } }
    public static final class ScreenshotFailure extends FxException { public ScreenshotFailure(String path, Throwable c){ super("Screenshot failed -> "+path, Severity.WARN, Area.IO, c, Map.of("path", path)); } }

    // ───────────────────────────── 2) REQUIRE (preconditions) ─────────────────────────────
    static final class Require {
        static <T> T nonNull(T v, String name){ if (v==null) throw new ConfigException(name+" is null"); return v; }
        static void state(boolean ok, String msg){ if (!ok) throw new ConfigException(msg); }
        static void positive(long n, String name){ if (n<=0) throw new ConfigException(name+" must be > 0"); }
    }

    // ───────────────────────────── 3) RESULT (no-throw flows) ─────────────────────────────
    sealed interface Result<T> permits Ok, Err {
        boolean isOk();
        T orElse(T fallback);
        <U> Result<U> map(Function<? super T, ? extends U> f);
        <U> Result<U> flatMap(Function<? super T, Result<U>> f);
        Throwable errorOrNull();
        static <T> Result<T> ok(T v){ return new Ok<>(v); }
        static <T> Result<T> err(Throwable e){ return new Err<>(e); }
    }
    static final class Ok<T> implements Result<T> {
        final T v; Ok(T v){ this.v=v; }
        public boolean isOk(){ return true; }
        public T orElse(T fallback){ return v; }
        public <U> Result<U> map(Function<? super T,? extends U> f){ return new Ok<>(f.apply(v)); }
        public <U> Result<U> flatMap(Function<? super T, Result<U>> f){ return Objects.requireNonNull(f.apply(v)); }
        public Throwable errorOrNull(){ return null; }
        @Override public String toString(){ return "Ok(" + v + ")"; }
    }
    static final class Err<T> implements Result<T> {
        final Throwable e; Err(Throwable e){ this.e=e; }
        public boolean isOk(){ return false; }
        public T orElse(T fallback){ return fallback; }
        public <U> Result<U> map(Function<? super T,? extends U> f){ return new Err<>(e); }
        public <U> Result<U> flatMap(Function<? super T, Result<U>> f){ return new Err<>(e); }
        public Throwable errorOrNull(){ return e; }
        @Override public String toString(){ return "Err(" + e.getClass().getSimpleName() + ":" + e.getMessage() + ")"; }
    }

    // ───────────────────────────── 4) RETRY POLICY (jittered backoff + classifier) ─────────────────────────────
    @FunctionalInterface interface ThrowingSupplier<T> { T get() throws Exception; }
    @FunctionalInterface interface ThrowingRunnable { void run() throws Exception; }

    static final class Retry {
        interface Classifier { boolean retryOn(Throwable t); }

        static Result<Void> run(ThrowingRunnable work, int maxAttempts, Duration baseDelay, Classifier classifier){
            Require.positive(maxAttempts, "maxAttempts");
            Objects.requireNonNull(classifier); Objects.requireNonNull(baseDelay);
            int attempt = 0; Throwable last = null;
            while (++attempt <= maxAttempts){
                try { work.run(); return Result.ok(null); }
                catch (Throwable t){
                    last = t;
                    if (!classifier.retryOn(t) || attempt==maxAttempts) break;
                    sleep(jitter(baseDelay, attempt));
                }
            }
            return Result.err(last);
        }

        static <T> Result<T> get(ThrowingSupplier<T> work, int maxAttempts, Duration baseDelay, Classifier classifier){
            Require.positive(maxAttempts, "maxAttempts");
            Objects.requireNonNull(classifier); Objects.requireNonNull(baseDelay);
            int attempt = 0; Throwable last = null;
            while (++attempt <= maxAttempts){
                try { return Result.ok(work.get()); }
                catch (Throwable t){
                    last = t;
                    if (!classifier.retryOn(t) || attempt==maxAttempts) break;
                    sleep(jitter(baseDelay, attempt));
                }
            }
            return Result.err(last);
        }

        private static void sleep(Duration d){ try { Thread.sleep(d.toMillis()); } catch (InterruptedException ie){ Thread.currentThread().interrupt(); } }
        private static Duration jitter(Duration base, int attempt){
            long backoff = (long)(base.toMillis() * Math.pow(2, attempt-1));
            long jitter = ThreadLocalRandom.current().nextLong(Math.max(1, backoff/4));
            return Duration.ofMillis(backoff + jitter);
        }
    }

    // ───────────────────────────── 5) DRIVER INTERFACE ─────────────────────────────
    interface Driver extends Closeable {
        void navigate(Url url) throws IOException;
        boolean exists(Locator locator) throws IOException;
        String text(Locator locator) throws IOException;
        void click(Locator locator) throws IOException;
        byte[] screenshot() throws IOException;
        @Override void close() throws IOException;
    }

    // ───────────────────────────── 6) PORT ALLOCATION & RESILIENT LAUNCH ─────────────────────────────
    static final class PortAllocator {
        static int freeEphemeral() {
            try (ServerSocket s = new ServerSocket(0)) {
                s.setReuseAddress(true);
                return s.getLocalPort();
            } catch (IOException e) {
                throw new ConfigException("Unable to allocate ephemeral port");
            }
        }
    }

    static final class DriverLauncher {
        /** Simulate a driver process that needs a port; throws if "in use". */
        static Driver start(int port) {
            boolean portBusy = ThreadLocalRandom.current().nextInt(5)==0; // 20% simulated busy
            if (portBusy) throw new DriverLaunchException("port in use", new IOException("port in use"));
            return new FakeDriver();
        }
        /** Retry on "port in use": pick a new free port each attempt with exponential backoff. */
        static Driver startResilient(int maxAttempts, Duration baseDelay) {
            int attempt = 0; Throwable last = null;
            while (++attempt <= maxAttempts) {
                int port = PortAllocator.freeEphemeral();
                try {
                    return start(port);
                } catch (DriverLaunchException e) {
                    String msg = Optional.ofNullable(e.getMessage()).orElse("");
                    boolean portInUse =
                            msg.toLowerCase().contains("port in use") ||
                                    Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("").toLowerCase().contains("port in use");
                    if (!portInUse || attempt == maxAttempts)
                        throw new DriverLaunchException("Driver failed after "+attempt+" attempts", e);
                    try { Thread.sleep((long)(baseDelay.toMillis() * Math.pow(2, attempt-1))); }
                    catch (InterruptedException ie){ Thread.currentThread().interrupt(); }
                    last = e;
                }
            }
            throw new DriverLaunchException("Driver launch retry exhausted", last);
        }
    }

    // ───────────────────────────── 7) FAKE DRIVER (simulates common + edge cases) ─────────────────────────────
    static final class FakeDriver implements Driver {
        private boolean open = false;
        public FakeDriver(){ open = true; }
        public void navigate(Url url) throws IOException {
            if (!open) throw new IOException("session closed");
            if (url.value().contains("500")) throw new IOException("HTTP 500");
        }
        public boolean exists(Locator locator) throws IOException {
            if (locator.query().contains("stale")) throw new IOException("stale element ref");
            return !locator.query().contains("missing");
        }
        public String text(Locator locator) throws IOException {
            if (!exists(locator)) throw new IOException("no such element");
            if (locator.query().contains("flaky") && ThreadLocalRandom.current().nextBoolean())
                throw new IOException("stale element ref");
            return "OK:" + locator.query();
        }
        public void click(Locator locator) throws IOException {
            if (!exists(locator)) throw new IOException("no such element");
        }
        public byte[] screenshot() throws IOException {
            if (!open) throw new IOException("cannot screenshot closed");
            if (ThreadLocalRandom.current().nextInt(5)==0) throw new IOException("GPU readback failed");
            return "PNG".getBytes(StandardCharsets.UTF_8);
        }
        public void close() throws IOException { open = false; }
    }

    // ───────────────────────────── 8) EXCEPTION MAPPER (IO → Framework) ─────────────────────────────
    static FxException mapIoToFx(IOException io, Area area, Map<String,Object> meta){
        String m = Optional.ofNullable(io.getMessage()).orElse("IO error");
        if (m.contains("no such element")) return new LocatorNotFoundException(new Locator("mapped","unknown"));
        if (m.contains("stale")) return new StaleElementRetriedException(new Locator("mapped","stale?"), 1, io);
        if (m.contains("HTTP 500")) return new NetworkRetryExhausted("last-url", 1, io);
        return new FxException("IO→FX: "+m, Severity.ERROR, area, io, meta);
    }

    // ───────────────────────────── 9) SOFT ASSERT COLLECTOR ─────────────────────────────
    static final class Soft implements AutoCloseable {
        private final List<String> fails = new ArrayList<>();
        void that(boolean cond, String msg){ if (!cond) fails.add(msg); }
        void equals(Object exp, Object act, String label){
            if (!Objects.equals(exp, act)) fails.add(label+" expected="+exp+" actual="+act);
        }
        @Override public void close(){
            if (!fails.isEmpty()) throw new SoftAssertionAggregate(List.copyOf(fails));
        }
    }

    // ───────────────────────────── 10) TIMEOUT WRAPPER & SCREENSHOT SAFETY ─────────────────────────────
    static <T> T withTimeout(Duration d, ThrowingSupplier<T> work){
        final long end = System.nanoTime() + d.toNanos();
        try { return work.get(); }
        catch (Exception first){
            if (System.nanoTime() > end) throw new TimeoutException("Operation timed out after "+d);
            try { return work.get(); }
            catch (Exception second){
                second.addSuppressed(first);
                if (second instanceof IOException io) throw mapIoToFx(io, Area.UNKNOWN, null);
                throw new FxException("Timeout-wrapped failure", Severity.ERROR, Area.UNKNOWN, second, null);
            }
        }
    }
    static void safeScreenshot(Driver d, String path){
        try {
            byte[] png = d.screenshot();
            if (png.length < 2) throw new IOException("empty png");
        } catch (IOException ioe){
            throw new ScreenshotFailure(path, ioe);
        }
    }

    // ───────────────────────────── 11) FLOWS (resilient page operations) ─────────────────────────────
    static final Retry.Classifier RETRY_STALE_OR_500 = t -> {
        String m = Optional.ofNullable(t.getMessage()).orElse("");
        if (t instanceof IOException) return m.contains("stale") || m.contains("HTTP 500");
        if (t instanceof StaleElementRetriedException) return true;
        return false;
    };

    static Result<String> getTextWithResilience(Driver d, Locator loc){
        return Retry.get(
                () -> {
                    try { return d.text(loc); }
                    catch (IOException io){ throw mapIoToFx(io, Area.DRIVER, Map.of("locator", loc.toString())); }
                },
                3, Duration.ofMillis(100), RETRY_STALE_OR_500
        );
    }

    static void clickOrThrow(Driver d, Locator loc){
        Result<Void> r = Retry.run(
                () -> {
                    try {
                        if (!d.exists(loc)) throw new IOException("no such element");
                        d.click(loc);
                    } catch (IOException io){
                        throw mapIoToFx(io, Area.DRIVER, Map.of("locator", loc.toString()));
                    }
                },
                2, Duration.ofMillis(80), RETRY_STALE_OR_500
        );
        if (!r.isOk()) throw new LocatorNotFoundException(loc);
    }

    // ───────────────────────────── 12) DEMOS: common → edge + port recovery ─────────────────────────────
    static void demoHappy(){
        try (Driver d = new FakeDriver()){
            d.navigate(new Url("https://example.com/home"));
            Locator title = new Locator("css","h1.title");
            clickOrThrow(d, title);
            var txt = getTextWithResilience(d, title);
            System.out.printf("[Happy] text=%s%n", txt.orElse("<none>"));
        } catch (IOException ioe){
            throw mapIoToFx(ioe, Area.DRIVER, Map.of("phase","happy"));
        }
    }

    static void demoMissingLocator(){
        try (Driver d = new FakeDriver()){
            d.navigate(new Url("https://example.com/page"));
            clickOrThrow(d, new Locator("css","div.missing")); // escalates
        } catch (LocatorNotFoundException lnfe){
            log("MissingLocator", lnfe);
            printRoot(lnfe);
        } catch (IOException ioe){
            throw mapIoToFx(ioe, Area.DRIVER, Map.of("phase","missing"));
        }
    }

    static void demoStaleThenRecover(){
        try (Driver d = new FakeDriver()){
            d.navigate(new Url("https://example.com/page"));
            Result<String> text = getTextWithResilience(d, new Locator("css","span.flaky-stale"));
            System.out.printf("[StaleRecover] %s%n", text);
        } catch (IOException ioe){
            throw mapIoToFx(ioe, Area.DRIVER, Map.of("phase","stale"));
        }
    }

    static void demoNetwork500(){
        try (Driver d = new FakeDriver()){
            try {
                withTimeout(Duration.ofMillis(50), () -> { d.navigate(new Url("https://example.com/500")); return null; });
            } catch (FxException fx){
                log("Network500", fx);
            }
        } catch (IOException ioe){
            throw mapIoToFx(ioe, Area.DRIVER, Map.of("phase","net500"));
        }
    }

    static void demoSoftAggregates(){
        try (Soft s = new Soft()){
            s.equals("Login", "Log in", "Title");
            s.that(2+2==5, "Math is broken");
        } catch (SoftAssertionAggregate agg){
            log("SoftAggregate", agg);
            System.out.printf("[SoftAggregate] meta=%s%n", agg.meta());
        }
    }

    static void demoScreenshotFailure(){
        try (Driver d = new FakeDriver()){
            d.close(); // force closed state
            safeScreenshot(d, "out/snap.png");
        } catch (ScreenshotFailure | IOException e){
            log("Screenshot", e);
        }
    }

    static void demoPortInUseRecovery(){
        try {
            Driver d = DriverLauncher.startResilient(4, Duration.ofMillis(100)); // up to 4 attempts
            try (d) {
                d.navigate(new Url("https://example.com/ok"));
                System.out.println("[PortRecovery] driver launched and navigated");
            }
        } catch (DriverLaunchException e){
            log("PortInUse", e);
            printRoot(e);
        } catch (IOException io){
            throw mapIoToFx(io, Area.DRIVER, Map.of("phase","portRecovery"));
        }
    }

    // ───────────────────────────── 13) LOGGERS (chain, root, suppressed) ─────────────────────────────
    static void log(String tag, Throwable t){
        System.out.printf("[%s] %s | area=%s sev=%s%n", tag, t,
                (t instanceof FxException fx) ? fx.area() : Area.UNKNOWN,
                (t instanceof FxException fx) ? fx.severity() : Severity.ERROR);
        Throwable c = t.getCause(); int depth=0;
        while (c!=null && depth<6){
            System.out.printf("  ↳ cause[%d]: %s%n", depth++, c);
            c = c.getCause();
        }
        for (Throwable s : t.getSuppressed()){
            System.out.printf("  … suppressed: %s%n", s);
        }
    }
    static void printRoot(Throwable t){
        Throwable r=t; while (r.getCause()!=null) r=r.getCause();
        System.out.printf("[Root] %s%n", r);
    }

    // ───────────────────────────── MAIN ─────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Automation Exception Demos ===");
        demoHappy();               // common flow
        demoMissingLocator();      // not found → mapped
        demoStaleThenRecover();    // flaky → retry → Ok/Err
        demoNetwork500();          // server error → mapped
        demoSoftAggregates();      // collect & throw at end
        demoScreenshotFailure();   // IO edge
        demoPortInUseRecovery();   // resilient port handling
        System.out.println("=== Done ===");
    }
}
