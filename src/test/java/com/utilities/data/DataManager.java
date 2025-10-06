package com.utilities.data;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.function.*;

/**
 * One-file tour: DataManager from basics → complex
 * - Pure JDK (no external deps).
 * - Pluggable DataSource (Memory, Properties, CSV, REST, DB-stub).
 * - Read-through LRU+TTL cache.
 * - Precedence stacking (env → file → memory, etc.).
 * - Thread-safe reads/writes.
 * - Minimal “flat JSON” parser (demo-grade) — swap with Jackson later w/o changing callers.
 * - Tiny Faker for test data generation.
 *
 * Usage: run main() to see the demos.
 */
public class DataManager {

    /* ───────────────────────────── 0) CORE TYPES ───────────────────────────── */

    /** Read-only key → value contract. Values are Objects; callers can cast or use helpers. */
    public interface DataSource {
        Optional<Object> get(String key);
        default Map<String, Object> getAll(String prefix) { // naive prefix filter
            Map<String, Object> out = new LinkedHashMap<>();
            keys().stream().filter(k -> k.startsWith(prefix)).forEach(k -> get(k).ifPresent(v -> out.put(k, v)));
            return out;
        }
        Set<String> keys();
    }

    /** Writable variant used by top-level stores (e.g., Memory). */
    public interface WritableDataSource extends DataSource {
        void put(String key, Object value);
        default void putAll(Map<String, ?> map) { map.forEach(this::put); }
    }

    /** Compose multiple sources with left-to-right precedence. Writes go to the first writable. */
    public static final class CompositeDataSource implements WritableDataSource {
        private final List<DataSource> sources; // order matters
        private final Optional<WritableDataSource> primaryWritable;

        public CompositeDataSource(List<DataSource> sources) {
            this.sources = List.copyOf(sources);
            this.primaryWritable = sources.stream().filter(s -> s instanceof WritableDataSource).map(s -> (WritableDataSource) s).findFirst();
        }
        @Override public Optional<Object> get(String key) {
            for (DataSource s : sources) {
                Optional<Object> v = s.get(key);
                if (v.isPresent()) return v;
            }
            return Optional.empty();
        }
        @Override public Set<String> keys() {
            LinkedHashSet<String> ks = new LinkedHashSet<>();
            for (DataSource s : sources) ks.addAll(s.keys());
            return ks;
        }
        @Override public void put(String key, Object value) {
            primaryWritable.orElseThrow(() -> new IllegalStateException("No writable source in composite")).put(key, value);
        }
        @Override public void putAll(Map<String, ?> map) { map.forEach(this::put); }
    }

    /* ───────────────────────────── 1) BASIC: IN-MEMORY STORE ───────────────────────────── */

    public static final class MemoryDataSource implements WritableDataSource {
        private final Map<String, Object> map = new ConcurrentHashMap<>();
        @Override public Optional<Object> get(String key) { return Optional.ofNullable(map.get(key)); }
        @Override public Set<String> keys() { return map.keySet(); }
        @Override public void put(String key, Object value) { map.put(key, value); }
    }

    /* ───────────────────────────── 2) FILE BACKED: PROPERTIES ───────────────────────────── */

    public static final class PropertiesDataSource implements DataSource {
        private final Map<String, Object> data = new LinkedHashMap<>();
        public PropertiesDataSource(Path propertiesFile) {
            try (Reader r = Files.newBufferedReader(propertiesFile, StandardCharsets.UTF_8)) {
                Properties p = new Properties();
                p.load(r);
                for (String k : p.stringPropertyNames()) data.put(k, p.getProperty(k));
            } catch (IOException e) { throw new UncheckedIOException(e); }
        }
        @Override public Optional<Object> get(String key) { return Optional.ofNullable(data.get(key)); }
        @Override public Set<String> keys() { return data.keySet(); }
    }

    /* ───────────────────────────── 3) FILE BACKED: CSV (simple) ─────────────────────────────
       Expect a simple 2-column CSV: key,value with header optional.
       Designed for small config/fixture lists. For real CSV/Excel, swap to OpenCSV/POI. */
    public static final class CsvDataSource implements DataSource {
        private final Map<String, Object> data = new LinkedHashMap<>();
        public CsvDataSource(Path csvFile) {
            try {
                List<String> lines = Files.readAllLines(csvFile, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.strip().isEmpty() || line.startsWith("#")) continue;
                    String[] parts = line.split(",", 2);
                    if (parts.length < 2) continue;
                    String k = parts[0].trim();
                    String v = parts[1].trim();
                    data.put(k, v);
                }
            } catch (IOException e) { throw new UncheckedIOException(e); }
        }
        @Override public Optional<Object> get(String key) { return Optional.ofNullable(data.get(key)); }
        @Override public Set<String> keys() { return data.keySet(); }
    }

    /* ───────────────────────────── 4) FILE BACKED: JSON (flat demo parser) ─────────────────────────────
       Minimal JSON parser that supports a flat object: {"a":"x","b":123,"c":true}
       Replace with Jackson later; keep the interface the same. */
    public static final class JsonDataSource implements DataSource {
        private final Map<String, Object> data = new LinkedHashMap<>();
        public JsonDataSource(Path jsonFile) {
            try {
                String s = Files.readString(jsonFile, StandardCharsets.UTF_8).trim();
                data.putAll(parseFlatJsonObject(s));
            } catch (IOException e) { throw new UncheckedIOException(e); }
        }
        @Override public Optional<Object> get(String key) { return Optional.ofNullable(data.get(key)); }
        @Override public Set<String> keys() { return data.keySet(); }

        private static Map<String, Object> parseFlatJsonObject(String s) {
            Map<String, Object> out = new LinkedHashMap<>();
            if (!s.startsWith("{") || !s.endsWith("}")) return out;
            String inner = s.substring(1, s.length()-1).trim();
            // naive split on commas not inside quotes
            List<String> pairs = smartSplit(inner);
            for (String p : pairs) {
                int idx = p.indexOf(':');
                if (idx < 0) continue;
                String k = unquote(p.substring(0, idx).trim());
                String v = p.substring(idx+1).trim();
                out.put(k, coerceJsonLiteral(v));
            }
            return out;
        }
        private static List<String> smartSplit(String s) {
            List<String> out = new ArrayList<>();
            boolean inQ = false; StringBuilder cur = new StringBuilder();
            for (int i=0;i<s.length();i++){
                char c = s.charAt(i);
                if (c=='"' && (i==0 || s.charAt(i-1)!='\\')) inQ = !inQ;
                if (c==',' && !inQ) { out.add(cur.toString()); cur.setLength(0); }
                else cur.append(c);
            }
            if (cur.length()>0) out.add(cur.toString());
            return out;
        }
        private static String unquote(String t) {
            if (t.startsWith("\"") && t.endsWith("\"")) return t.substring(1, t.length()-1).replace("\\\"", "\"");
            return t;
        }
        private static Object coerceJsonLiteral(String raw) {
            String t = raw.trim();
            if (t.startsWith("\"") && t.endsWith("\"")) return unquote(t);
            if ("true".equals(t) || "false".equals(t)) return Boolean.valueOf(t);
            try { if (t.contains(".")) return Double.valueOf(t); else return Long.valueOf(t); }
            catch (NumberFormatException e) { return t; }
        }
    }

    /* ───────────────────────────── 5) SERVICE BACKED: REST (read-only) ───────────────────────────── */

    public static final class RestDataSource implements DataSource {
        private final HttpClient client = HttpClient.newHttpClient();
        private final URI uri; // Returns a flat JSON object
        public RestDataSource(URI uri) { this.uri = uri; }
        @Override public Optional<Object> get(String key) {
            try {
                HttpRequest req = HttpRequest.newBuilder(uri).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                Map<String, Object> flat = JsonDataSource.parseFlatJsonObject(res.body());
                return Optional.ofNullable(flat.get(key));
            } catch (Exception e) {
                return Optional.empty(); // In tests, prefer resilience over explosions
            }
        }
        @Override public Set<String> keys() {
            try {
                HttpRequest req = HttpRequest.newBuilder(uri).GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                return JsonDataSource.parseFlatJsonObject(res.body()).keySet();
            } catch (Exception e) { return Set.of(); }
        }
    }

    /* ───────────────────────────── 6) DATABASE BACKED (stub for pattern) ─────────────────────────────
       Swap JDBC details for your env. This demo reads from a table kv_store(key TEXT, value TEXT). */
    public static final class DbDataSource implements DataSource {
        private final Supplier<Connection> connectionSupplier;
        public DbDataSource(Supplier<Connection> connectionSupplier) { this.connectionSupplier = connectionSupplier; }
        @Override public Optional<Object> get(String key) {
            String sql = "SELECT value FROM kv_store WHERE key = ?";
            try (Connection c = connectionSupplier.get();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.ofNullable(rs.getString(1));
                    return Optional.empty();
                }
            } catch (SQLException e) { return Optional.empty(); }
        }
        @Override public Set<String> keys() {
            String sql = "SELECT key FROM kv_store";
            Set<String> ks = new LinkedHashSet<>();
            try (Connection c = connectionSupplier.get();
                 PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ks.add(rs.getString(1));
            } catch (SQLException ignored) {}
            return ks;
        }
    }

    /* ───────────────────────────── 7) CACHE: LRU + TTL (read-through) ───────────────────────────── */

    public static final class CacheLayer implements DataSource {
        private static final class Entry { final Object v; final long exp; Entry(Object v,long exp){this.v=v;this.exp=exp;} }
        private final DataSource delegate;
        private final long ttlMillis;
        private final int maxEntries;
        private final Map<String, Entry> cache; // access-ordered for LRU
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        public CacheLayer(DataSource delegate, Duration ttl, int maxEntries) {
            this.delegate = delegate;
            this.ttlMillis = ttl.toMillis();
            this.maxEntries = maxEntries;
            this.cache = new LinkedHashMap<>(16, 0.75f, true) {
                @Override protected boolean removeEldestEntry(Map.Entry<String, Entry> eldest) { return size() > CacheLayer.this.maxEntries; }
            };
        }

        @Override public Optional<Object> get(String key) {
            long now = System.currentTimeMillis();
            lock.readLock().lock();
            try {
                Entry e = cache.get(key);
                if (e != null && (e.exp == 0 || e.exp > now)) return Optional.ofNullable(e.v);
            } finally { lock.readLock().unlock(); }

            Optional<Object> fresh = delegate.get(key);
            lock.writeLock().lock();
            try {
                long exp = (ttlMillis <= 0) ? 0 : now + ttlMillis;
                cache.put(key, new Entry(fresh.orElse(null), exp));
            } finally { lock.writeLock().unlock(); }
            return fresh;
        }

        @Override public Set<String> keys() {
            // Don’t cache key sets; delegate directly.
            return delegate.keys();
        }
    }

    /* ───────────────────────────── 8) ENCRYPTION (placeholder) ─────────────────────────────
       For secrets: swap with a real KMS or JCE cipher. We use Base64 demo only. */
    public static final class Crypto {
        public static String encode(String plain) { return Base64.getEncoder().encodeToString(plain.getBytes(StandardCharsets.UTF_8)); }
        public static String decode(String b64) { return new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8); }
    }

    /* ───────────────────────────── 9) DATAMANAGER FACADE ─────────────────────────────
       Thread-safe, read-through cache, pluggable sources, write to primary. */

    public static final class Manager {
        private final CompositeDataSource sources;
        private final DataSource cache; // may be identity passthrough

        private final ReadWriteLock rw = new ReentrantReadWriteLock();

        private Manager(CompositeDataSource sources, DataSource cache) {
            this.sources = sources;
            this.cache = cache;
        }

        /** Get as Optional<Object>. */
        public Optional<Object> get(String key) {
            rw.readLock().lock();
            try { return cache.get(key); }
            finally { rw.readLock().unlock(); }
        }

        /** Get as String with default. */
        public String getString(String key, String def) {
            return get(key).map(Object::toString).orElse(def);
        }

        /** Get coerced numbers/bools (best-effort). */
        public Integer getInt(String key, Integer def) {
            return get(key).map(v -> coerceInt(v)).orElse(def);
        }
        public Double getDouble(String key, Double def) {
            return get(key).map(v -> coerceDouble(v)).orElse(def);
        }
        public Boolean getBool(String key, Boolean def) {
            return get(key).map(v -> coerceBool(v)).orElse(def);
        }

        /** Put writes to the primary writable (e.g., Memory). */
        public void put(String key, Object value) {
            rw.writeLock().lock();
            try { sources.put(key, value); }
            finally { rw.writeLock().unlock(); }
        }

        /** Bulk read by prefix. */
        public Map<String, Object> getAll(String prefix) {
            rw.readLock().lock();
            try { return sources.getAll(prefix); }
            finally { rw.readLock().unlock(); }
        }

        public Set<String> keys() {
            rw.readLock().lock();
            try { return sources.keys(); }
            finally { rw.readLock().unlock(); }
        }

        /* Helpers */
        private static Integer coerceInt(Object v) {
            if (v instanceof Number n) return n.intValue();
            try { return Integer.valueOf(v.toString()); } catch (Exception e) { return null; }
        }
        private static Double coerceDouble(Object v) {
            if (v instanceof Number n) return n.doubleValue();
            try { return Double.valueOf(v.toString()); } catch (Exception e) { return null; }
        }
        private static Boolean coerceBool(Object v) {
            if (v instanceof Boolean b) return b;
            String s = String.valueOf(v).toLowerCase(Locale.ROOT);
            return switch (s) {
                case "true","1","yes","y","on" -> true;
                case "false","0","no","n","off" -> false;
                default -> null;
            };
        }
    }

    /* ───────────────────────────── 10) BUILDER ───────────────────────────── */

    public static final class Builder {
        private final List<DataSource> ordered = new ArrayList<>();
        private Duration cacheTtl = Duration.ZERO;
        private int cacheSize = 256;

        /** First writable source wins for puts. Typically Memory first. */
        public Builder withMemory(Map<String, ?> seed) {
            MemoryDataSource mem = new MemoryDataSource();
            if (seed != null) mem.putAll(seed);
            ordered.add(mem);
            return this;
        }
        public Builder withProperties(Path file) { ordered.add(new PropertiesDataSource(file)); return this; }
        public Builder withCsv(Path file) { ordered.add(new CsvDataSource(file)); return this; }
        public Builder withJson(Path file) { ordered.add(new JsonDataSource(file)); return this; }
        public Builder withRest(URI uri) { ordered.add(new RestDataSource(uri)); return this; }
        public Builder withDatabase(Supplier<Connection> supplier) { ordered.add(new DbDataSource(supplier)); return this; }
        public Builder withCache(Duration ttl, int maxEntries) { this.cacheTtl = ttl; this.cacheSize = maxEntries; return this; }

        public Manager build() {
            if (ordered.isEmpty()) withMemory(Map.of()); // ensure at least one writable
            CompositeDataSource composite = new CompositeDataSource(ordered);
            DataSource cached = (cacheTtl.isZero() ? composite : new CacheLayer(composite, cacheTtl, cacheSize));
            return new Manager(composite, cached);
        }
    }

    /* ───────────────────────────── 11) TINY TEST DATA GENERATOR ───────────────────────────── */

    public static final class Faker {
        private static final String[] FIRST = {"Ava","Ben","Cleo","Dax","Eli","Fia","Gus","Hana"};
        private static final String[] LAST  = {"Stone","Rivera","Khan","Osei","Ito","Silva","Novak","Patel"};
        private static final Random R = new Random();

        public static String name() { return FIRST[R.nextInt(FIRST.length)]+" "+LAST[R.nextInt(LAST.length)]; }
        public static String email(String name) {
            String user = name.toLowerCase(Locale.ROOT).replace(" ",".");
            return user + "@example.test";
        }
        public static Map<String,Object> userRecord() {
            String n = name();
            return Map.of("name", n, "email", email(n), "active", R.nextBoolean(), "age", 18 + R.nextInt(40));
        }
    }

    /* ───────────────────────────── 12) DEMO MAIN ───────────────────────────── */

    public static void main(String[] args) throws Exception {
        // Prepare tiny demo files in /tmp (works cross-OS via default temp dir)
        Path tmp = Files.createTempDirectory("dm_demo");
        Path props = tmp.resolve("config.properties");
        Path csv = tmp.resolve("kv.csv");
        Path json = tmp.resolve("flat.json");
        Files.writeString(props, """
                base.url=https://api.example.test
                retries=3
                featureX.enabled=true
                """, StandardCharsets.UTF_8);
        Files.writeString(csv, """
                plan,enterprise
                region,us-east-1
                timeout.ms,1500
                """, StandardCharsets.UTF_8);
        Files.writeString(json, """
                {"threshold":0.85,"maxUsers":1000,"darkMode":false,"greeting":"hello"}
                """, StandardCharsets.UTF_8);

        // Build a DataManager with precedence:
        // 1) Memory (env overrides) → 2) Properties → 3) CSV → 4) JSON
        // Read-through cache: 2 minutes, 512 entries
        Manager dm = new Builder()
                .withMemory(Map.of(
                        "featureX.enabled", "false",     // override props
                        "secret.apiKey", Crypto.encode("super-secret-key") // demo secret
                ))
                .withProperties(props)
                .withCsv(csv)
                .withJson(json)
                .withCache(Duration.ofMinutes(2), 512)
                .build();

        // BASIC GETS
        System.out.println("=== BASIC GETS ===");
        System.out.println("base.url         = " + dm.getString("base.url", "http://localhost"));
        System.out.println("retries (int)    = " + dm.getInt("retries", 0));
        System.out.println("threshold (dbl)  = " + dm.getDouble("threshold", 0.0));
        System.out.println("featureX.enabled = " + dm.getBool("featureX.enabled", null)); // overridden by Memory
        System.out.println("plan (csv)       = " + dm.getString("plan", "free"));
        System.out.println("greeting (json)  = " + dm.getString("greeting", "hi"));

        // PUT / WRITE (goes to primary Memory)
        dm.put("runtime.sessionId", UUID.randomUUID().toString());
        System.out.println("sessionId        = " + dm.getString("runtime.sessionId", "none"));

        // BULK by prefix (naive)
        System.out.println("\n=== ALL 'base.' KEYS ===");
        dm.getAll("base.").forEach((k,v) -> System.out.println(k+" = "+v));

        // SECRET demo (Base64 placeholder only)
        System.out.println("\n=== SECRET (demo) ===");
        String b64 = dm.getString("secret.apiKey", "");
        System.out.println("decoded secret   = " + Crypto.decode(b64));

        // TINY FAKER for test data
        System.out.println("\n=== FAKE USER ===");
        Map<String,Object> user = Faker.userRecord();
        dm.put("user.name", user.get("name"));
        dm.put("user.email", user.get("email"));
        System.out.println("user.name        = " + dm.getString("user.name", ""));
        System.out.println("user.email       = " + dm.getString("user.email", ""));

        // REST and DB layers (optional demo)
        // Wire these as needed; shown here as pattern you can plug into Builder.
        // REST example (flat JSON): URI.create("http://localhost:8080/config")
        // DB example: supplier returning a pooled JDBC Connection.

        // Mini verification
        assert "enterprise".equals(dm.getString("plan", ""));
        assert Boolean.FALSE.equals(dm.getBool("featureX.enabled", true));
        System.out.println("\nAll basic checks passed.");
        System.out.println("\nTemp files at: " + tmp);
    }
}
