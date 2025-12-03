package com.tutorial.automation;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * One-file tour of **Advanced/Complex Java Collections in Test Automation**
 * Patterns: MultiMap, BiMap, LRU caches, Priority scheduling, DAG/topo-sort,
 * FSM workflows, Token-bucket rate limiting, Bucketing/Grouping, Indexes,
 * and Event Bus — all runnable with a tiny fake driver.
 * Target: Java 17+
 */
public class AdvancedCollection {

    /*───────────────────────────── Tiny Fake WebDriver (standalone runnable) ─────────────────────────────*/
    static final class By {
        private final String using;
        private By(String using){ this.using = using; }
        public static By id(String id){ return new By("id:"+id); }
        public static By css(String css){ return new By("css:"+css); }
        @Override public String toString(){ return using; }
    }
    static final class WebElement {
        private final By by; String value = "";
        WebElement(By by){ this.by = by; }
        void clear(){ value=""; } void sendKeys(String v){ value=v; } void click(){}
        String getText(){ return "text("+by+")"; }
        @Override public String toString(){ return "Element{"+by+", v="+value+"}"; }
    }
    static final class FakeDriver {
        WebElement findElement(By by){ return new WebElement(by); }
        List<WebElement> findElements(By by){ return List.of(new WebElement(by), new WebElement(by)); }
    }
    static final FakeDriver driver = new FakeDriver();

    /* ============================================================================================================
       1) MULTIMAP (Map<K, List<V>>) — Feature → Ordered Steps
       ============================================================================================================ */
    record Step(By locator, String input) {}
    static void multimap_featureSteps() {
        Map<String, List<Step>> stepsByFeature = new HashMap<>();
        stepsByFeature.put("signup", new ArrayList<>(List.of(
                new Step(By.id("first"), "Ada"),
                new Step(By.id("last"),  "Lovelace"),
                new Step(By.id("email"), "ada@math.org")
        )));
        stepsByFeature.computeIfAbsent("checkout", k -> new ArrayList<>())
                .addAll(List.of(new Step(By.id("card"), "4242 4242 4242 4242")));

        for (Map.Entry<String, List<Step>> e : stepsByFeature.entrySet()) {
            System.out.printf("[MultiMap] feature=%s steps=%d%n", e.getKey(), e.getValue().size());
            for (Step s : e.getValue()) {
                WebElement el = driver.findElement(s.locator());
                el.clear(); el.sendKeys(s.input());
            }
        }
    }

    /* ============================================================================================================
       2) BI-MAP (Two synchronized maps) — Email <-> UserId (uniqueness both ways)
       ============================================================================================================ */
    static final class BiMap<K,V> {
        private final Map<K,V> forward = new HashMap<>();
        private final Map<V,K> reverse = new HashMap<>();
        public void put(K k, V v){
            if (forward.containsKey(k) || reverse.containsKey(v))
                throw new IllegalArgumentException("Duplicate key or value");
            forward.put(k, v); reverse.put(v, k);
        }
        public V getForward(K k){ return forward.get(k); }
        public K getReverse(V v){ return reverse.get(v); }
        public int size(){ return forward.size(); }
    }
    static void bimap_userIndex() {
        BiMap<String, Integer> users = new BiMap<>();
        users.put("ada@math.org", 1001);
        users.put("grace@comp.org", 1002);
        System.out.printf("[BiMap] idOf(ada)= %d, emailOf(1002)= %s, size=%d%n",
                users.getForward("ada@math.org"), users.getReverse(1002), users.size());
    }

    /* ============================================================================================================
       3) LRU CACHE via LinkedHashMap — recently used elements (e.g., tokens/pages)
       ============================================================================================================ */
    static final class LruCache<K,V> extends LinkedHashMap<K,V> {
        private final int cap;
        LruCache(int cap){ super(16, 0.75f, true); this.cap = cap; }
        @Override protected boolean removeEldestEntry(Map.Entry<K,V> eldest){ return size() > cap; }
    }
    static void lru_cache_demo() {
        LruCache<String,String> tokenCache = new LruCache<>(2);
        tokenCache.put("admin", "t-admin");
        tokenCache.put("viewer", "t-viewer");
        tokenCache.get("admin");          // access admin → becomes MRU
        tokenCache.put("guest", "t-guest"); // evicts "viewer"
        System.out.printf("[LRU] keys=%s%n", tokenCache.keySet()); // [admin, guest]
    }

    /* ============================================================================================================
       4) PRIORITY SCHEDULING — PriorityQueue for risk-based test execution
       ============================================================================================================ */
    record TestTask(String name, int riskScore, int estMs) {}
    static void priority_queue_scheduler() {
        Comparator<TestTask> byRiskHighFirst = Comparator.<TestTask>comparingInt(t -> t.riskScore()).reversed()
                .thenComparingInt(TestTask::estMs);
        Queue<TestTask> queue = new PriorityQueue<>(byRiskHighFirst);
        queue.addAll(List.of(new TestTask("checkout", 9, 800),
                new TestTask("login",    7, 200),
                new TestTask("profile",  5, 300)));
        while (!queue.isEmpty()) {
            TestTask t = queue.poll();
            System.out.printf("[Priority] run=%s risk=%d ms=%d%n", t.name(), t.riskScore(), t.estMs());
        }
    }

    /* ============================================================================================================
       5) TEST DAG + TOPOLOGICAL SORT — Resolve dependencies between suites
       ============================================================================================================ */
    static List<String> topoSort(Map<String, Set<String>> deps) {
        Map<String, Integer> indeg = new HashMap<>();
        for (String n : deps.keySet()) indeg.putIfAbsent(n, 0);
        for (Set<String> ds : deps.values()) for (String d : ds) indeg.merge(d, 1, Integer::sum);

        Deque<String> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue() == 0) q.add(e.getKey());

        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String n = q.removeFirst(); order.add(n);
            for (String d : deps.getOrDefault(n, Set.of())) {
                indeg.put(d, indeg.get(d) - 1);
                if (indeg.get(d) == 0) q.add(d);
            }
        }
        if (order.size() != indeg.size()) throw new IllegalStateException("Cycle detected");
        return order;
    }
    static void dag_toposort_demo() {
        Map<String, Set<String>> deps = new HashMap<>();
        deps.put("login", Set.of("core"));
        deps.put("checkout", Set.of("login", "catalog"));
        deps.put("catalog", Set.of("core"));
        deps.put("core", Set.of());

        List<String> order = topoSort(deps);
        System.out.printf("[TopoSort] order=%s%n", order);
    }

    /* ============================================================================================================
       6) FINITE STATE MACHINE (FSM) — Page workflow using Map<State, Map<Event, State>>
       ============================================================================================================ */
    enum State { HOME, LOGIN, DASH, LOCKED }
    enum Event { CLICK_LOGIN, SUBMIT_OK, SUBMIT_BAD, LOGOUT }

    static void fsm_workflow() {
        Map<State, Map<Event, State>> fsm = new EnumMap<>(State.class);
        fsm.put(State.HOME, Map.of(Event.CLICK_LOGIN, State.LOGIN));
        fsm.put(State.LOGIN, Map.of(Event.SUBMIT_OK, State.DASH, Event.SUBMIT_BAD, State.LOCKED));
        fsm.put(State.DASH, Map.of(Event.LOGOUT, State.HOME));
        fsm.put(State.LOCKED, Map.of(Event.LOGOUT, State.HOME));

        State s = State.HOME;
        List<Event> script = List.of(Event.CLICK_LOGIN, Event.SUBMIT_BAD, Event.LOGOUT);
        for (Event e : script) {
            s = fsm.getOrDefault(s, Map.of()).getOrDefault(e, s);
            System.out.printf("[FSM] on %s → %s%n", e, s);
        }
    }

    /* ============================================================================================================
       7) TOKEN-BUCKET RATE LIMITER — Queue of timestamps for API throttling
       ============================================================================================================ */
    static final class TokenBucket {
        private final int capacity;
        private final Duration window;
        private final Deque<Instant> stamps = new ArrayDeque<>();
        TokenBucket(int capacity, Duration window){ this.capacity = capacity; this.window = window; }

        synchronized boolean tryAcquire() {
            Instant now = Instant.now();
            while (!stamps.isEmpty() && Duration.between(stamps.peekFirst(), now).compareTo(window) > 0) {
                stamps.removeFirst();
            }
            if (stamps.size() < capacity) { stamps.addLast(now); return true; }
            return false;
        }
    }
    static void tokenBucket_demo() throws InterruptedException {
        TokenBucket bucket = new TokenBucket(3, Duration.ofSeconds(1));
        for (int i=0; i<5; i++) {
            boolean ok = bucket.tryAcquire();
            System.out.printf("[RateLimit] call=%d allowed=%b%n", i, ok);
            if (!ok) Thread.sleep(350); // backoff simulate
        }
    }

    /* ============================================================================================================
       8) GROUPING / BUCKETING — Partition results for reporting
       ============================================================================================================ */
    record TestResult(String name, boolean pass, int durationMs) {}
    static void grouping_report() {
        List<TestResult> results = List.of(
                new TestResult("login", true, 120),
                new TestResult("checkout", false, 780),
                new TestResult("profile", true, 300),
                new TestResult("refund", false, 420)
        );

        Map<Boolean, List<TestResult>> byPass = results.stream()
                .collect(Collectors.groupingBy(TestResult::pass));
        Map<String, Long> counts = results.stream()
                .collect(Collectors.groupingBy(r -> r.pass() ? "PASS" : "FAIL", TreeMap::new, Collectors.counting()));

        System.out.printf("[Group] byPass=%s%n", byPass);
        System.out.printf("[Group] counts=%s%n", counts);
    }

    /* ============================================================================================================
       9) INVERTED INDEX — Build fast lookup from text → tests (searchable tagging)
       ============================================================================================================ */
    static void inverted_index() {
        Map<String, Set<String>> index = new HashMap<>();
        Map<String, String> docs = Map.of(
                "testLogin", "auth login happy",
                "testCheckout", "payments checkout card",
                "testRefund", "payments refund"
        );

        for (var e : docs.entrySet()) {
            String test = e.getKey();
            for (String token : e.getValue().split("\\s+")) {
                index.computeIfAbsent(token, k -> new HashSet<>()).add(test);
            }
        }
        System.out.printf("[Index] payments → %s%n", index.getOrDefault("payments", Set.of()));
    }

    /* ============================================================================================================
       10) EVENT BUS (pub/sub) — Map<Topic, List<Subscriber>> + fan-out notify
       ============================================================================================================ */
    enum Topic { TEST_START, TEST_END, ERROR }
    interface Subscriber { void on(Topic t, String payload); }

    static final class EventBus {
        private final Map<Topic, List<Subscriber>> subs = new EnumMap<>(Topic.class);
        void subscribe(Topic t, Subscriber s){ subs.computeIfAbsent(t, k -> new ArrayList<>()).add(s); }
        void publish(Topic t, String payload){ for (Subscriber s : subs.getOrDefault(t, List.of())) s.on(t, payload); }
    }
    static void eventBus_demo() {
        EventBus bus = new EventBus();
        bus.subscribe(Topic.TEST_START, (t,p) -> System.out.println("[Event] start: " + p));
        bus.subscribe(Topic.TEST_END,   (t,p) -> System.out.println("[Event] end: " + p));
        bus.subscribe(Topic.ERROR,      (t,p) -> System.out.println("[Event] error: " + p));

        bus.publish(Topic.TEST_START, "login");
        bus.publish(Topic.ERROR, "Null on banner");
        bus.publish(Topic.TEST_END, "login");
    }

    /* ============================================================================================================
       11) ADAPTIVE RETRY REGISTRY — Map<Error, List<DelayMs>> with backoffs
       ============================================================================================================ */
    static void adaptive_retry_registry() {
        Map<String, List<Integer>> backoffByError = new HashMap<>();
        backoffByError.put("HTTP_429", List.of(200, 400, 800));
        backoffByError.put("ELEMENT_NOT_INTERACTABLE", List.of(100, 200, 300));

        String err = "HTTP_429";
        for (Integer ms : backoffByError.getOrDefault(err, List.of(100))) {
            System.out.printf("[Retry] err=%s delay=%dms%n", err, ms);
            // sleep(ms) in real scenario
        }
    }

    /* ============================================================================================================
       12) STRATEGY REGISTRY — Map<String, Function<Ctx,Boolean>> pluggable checks
       ============================================================================================================ */
    record Ctx(FakeDriver d, Map<String,String> env) {}
    static void strategy_registry() {
        Map<String, Function<Ctx, Boolean>> checks = new HashMap<>();
        checks.put("bannerExists", ctx -> {
            WebElement el = ctx.d.findElement(By.id("banner"));
            return el.getText() != null;
        });
        checks.put("regionIsUS", ctx -> "US".equals(ctx.env.get("region")));

        Ctx ctx = new Ctx(driver, Map.of("region","US"));
        for (String name : List.of("bannerExists", "regionIsUS")) {
            boolean ok = checks.get(name).apply(ctx);
            System.out.printf("[Strategy] %s => %b%n", name, ok);
        }
    }

    /*───────────────────────────── MAIN ─────────────────────────────*/
    public static void main(String[] args) throws Exception {
        multimap_featureSteps();
        bimap_userIndex();
        lru_cache_demo();
        priority_queue_scheduler();
        dag_toposort_demo();
        fsm_workflow();
        tokenBucket_demo();
        grouping_report();
        inverted_index();
        eventBus_demo();
        adaptive_retry_registry();
        strategy_registry();
    }
}
