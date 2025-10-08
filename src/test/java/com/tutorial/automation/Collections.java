package com.tutorial.automation;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * One-file tour of **Java Collections in Test Automation**
 * Phase 1: Collections with WRAPPER CLASSES (Integer, Boolean, etc.)
 * Phase 2: Collections with OBJECTS (custom domain types / mini page objects)
 * Target: Java 17+
 */
public class Collections {

    /*───────────────────────────── Tiny Fake WebDriver (so this file runs standalone) ─────────────────────────────*/
    static final class By {
        private final String using;
        private By(String using){ this.using = using; }
        public static By id(String id){ return new By("id:"+id); }
        public static By css(String css){ return new By("css:"+css); }
        @Override public String toString(){ return using; }
    }
    static final class WebElement {
        private final By by;
        private String value = "";
        WebElement(By by){ this.by = by; }
        void clear(){ value = ""; }
        void sendKeys(String v){ value = v; }
        void click(){ /* no-op */ }
        String getText(){ return "text("+by+")"; }
        @Override public String toString(){ return "Element{" + by + ", value=" + value + "}"; }
    }
    static final class FakeDriver {
        WebElement findElement(By by){ return new WebElement(by); }
        List<WebElement> findElements(By by){ return List.of(new WebElement(by), new WebElement(by)); }
    }
    static final FakeDriver driver = new FakeDriver();

    /* ============================================================================================================
       PHASE 1 — COLLECTIONS USING WRAPPER CLASSES (no custom objects yet)
       ============================================================================================================ */

    // ───────────────────────────── 1) LIST<Integer> — retry intervals for flaky steps ─────────────────────────────
    static void wrappers_list_of_integers() {
        List<Integer> retryMs = List.of(100, 250, 500, 1000); // backoff plan
        for (Integer ms : retryMs) {
            // pretend: wait(ms)
            System.out.printf("[Wrapper-List<Integer>] backoff=%dms%n", ms);
        }
    }

    // ───────────────────────────── 2) SET<Character> — feature flags seen in UI (dedupe) ──────────────────────────
    static void wrappers_set_of_characters() {
        Set<Character> seenFlags = new HashSet<>(List.of('A','B','B','C','A'));
        System.out.printf("[Wrapper-Set<Character>] uniqueFlags=%s size=%d%n", seenFlags, seenFlags.size());
    }

    // ───────────────────────────── 3) MAP<String, Boolean> — locale toggle matrix ────────────────────────────────
    static void wrappers_map_string_boolean() {
        Map<String, Boolean> localeToggles = new HashMap<>();
        localeToggles.put("en", Boolean.TRUE);
        localeToggles.put("es", Boolean.FALSE);
        localeToggles.put("fr", Boolean.TRUE);

        String current = "fr";
        Boolean enabled = localeToggles.getOrDefault(current, Boolean.FALSE);
        System.out.printf("[Wrapper-Map<String,Boolean>] %s-enabled=%b%n", current, enabled);
    }

    // ───────────────────────────── 4) MAP<Integer, Double> — HTTP code → SLO seconds ─────────────────────────────
    static void wrappers_map_int_double() {
        Map<Integer, Double> sloByHttp = Map.of(200, 0.8, 302, 1.2, 500, 2.5);
        int code = 200;
        double slo = sloByHttp.get(code);
        System.out.printf("[Wrapper-Map<Integer,Double>] http=%d slo=%.2fs%n", code, slo);
    }

    // ───────────────────────────── 5) CONCURRENTMAP<Long, String> — parallel token cache ─────────────────────────
    static void wrappers_concurrent_map() {
        ConcurrentMap<Long, String> tokens = new ConcurrentHashMap<>();
        String t1 = tokens.computeIfAbsent(1L, k -> "token-admin");
        String t2 = tokens.computeIfAbsent(2L, k -> "token-viewer");
        System.out.printf("[Wrapper-ConcurrentMap<Long,String>] size=%d %s|%s%n", tokens.size(), t1, t2);
    }

    // ───────────────────────────── 6) LINKEDHASHMAP<By, String> — ordered form fill (wrappers for text) ──────────
    static void wrappers_linkedHashMap_form() {
        Map<By, String> steps = new LinkedHashMap<>();
        steps.put(By.id("firstName"), "Ada");
        steps.put(By.id("lastName"),  "Lovelace");
        steps.put(By.id("email"),     "ada@math.org");

        for (Map.Entry<By, String> e : steps.entrySet()) {
            WebElement el = driver.findElement(e.getKey());
            el.clear(); el.sendKeys(e.getValue());
            System.out.printf("[Wrapper-LinkedHashMap] %s <= \"%s\"%n", e.getKey(), e.getValue());
        }
        driver.findElement(By.css("button[type=submit]")).click();
    }

    /* ============================================================================================================
       PHASE 2 — COLLECTIONS USING OBJECTS (custom domain types + page-ish abstractions)
       ============================================================================================================ */

    // Domain objects
    public record TestCase(String name, String email, String pass, boolean expectSuccess) {}
    public record Expectation(String locale, String bannerText) {}

    // ───────────────────────────── 7) LIST<TestCase> — data-driven auth cases ────────────────────────────────────
    static void objects_list_testcases() {
        List<TestCase> cases = List.of(
                new TestCase("happy", "ok@site.com", "good", true),
                new TestCase("badEmail", "bad@", "x", false),
                new TestCase("locked", "taken@site.com", "good", false)
        );

        for (TestCase tc : cases) {
            // login(tc.email(), tc.pass())
            String result = tc.expectSuccess() ? "HOME" : "ERROR";
            System.out.printf("[Objects-List<TestCase>] %s → %s%n", tc.name(), result);
        }
    }

    // ───────────────────────────── 8) SET<TestCase> — enforce uniqueness by name/email ───────────────────────────
    static void objects_set_dedupe_cases() {
        Set<TestCase> unique = new HashSet<>(List.of(
                new TestCase("happy","ok@site.com","good",true),
                new TestCase("happy","ok@site.com","good",true), // duplicate by value → dedup
                new TestCase("edge","a@b.c","",false)
        ));
        System.out.printf("[Objects-Set<TestCase>] size=%d values=%s%n", unique.size(), unique);
    }

    // Page-like object
    static final class LoginPage {
        private final FakeDriver d;
        private final Map<String, By> ui = Map.of(
                "email",  By.id("email"),
                "pass",   By.id("password"),
                "submit", By.css("button[type=submit]"),
                "banner", By.id("banner")
        );
        LoginPage(FakeDriver d){ this.d = d; }
        LoginPage email(String v){ d.findElement(ui.get("email")).sendKeys(v); return this; }
        LoginPage pass(String v){  d.findElement(ui.get("pass")).sendKeys(v);  return this; }
        String submit(){ d.findElement(ui.get("submit")).click(); return d.findElement(ui.get("banner")).getText(); }
    }

    // ───────────────────────────── 9) MAP<String, LoginPage> — keyed mini page registry ───────────────────────────
    static void objects_map_page_registry() {
        Map<String, Supplier<LoginPage>> pages = Map.of(
                "login", () -> new LoginPage(driver)
        );
        LoginPage lp = pages.get("login").get();
        String banner = lp.email("ok@site.com").pass("good").submit();
        System.out.printf("[Objects-Map<String,Page>] banner=%s%n", banner);
    }

    // ───────────────────────────── 10) MAP<String, List<Expectation>> — locale assertions table ───────────────────
    static void objects_map_list_expectations() {
        Map<String, List<Expectation>> expected = Map.of(
                "authErrors", List.of(
                        new Expectation("en", "Invalid credentials"),
                        new Expectation("es", "Credenciales inválidas"),
                        new Expectation("fr", "Identifiants invalides")
                )
        );
        String locale = "es";
        String expectedBanner = expected.get("authErrors").stream()
                .filter(e -> e.locale().equals(locale))
                .findFirst().map(Expectation::bannerText).orElse("???");
        String actualBanner = "Credenciales inválidas"; // pretend read
        System.out.printf("[Objects-Map<String,List<Expectation>>] expect=\"%s\" actual=\"%s\" match=%b%n",
                expectedBanner, actualBanner, Objects.equals(expectedBanner, actualBanner));
    }

    // ───────────────────────────── 11) MAP<String, Set<TestCase>> — feature→case coverage matrix ─────────────────
    static void objects_map_set_coverage() {
        Map<String, Set<TestCase>> coverage = new HashMap<>();
        coverage.put("checkout", new HashSet<>(Set.of(
                new TestCase("happy-checkout","u@x.com","good",true),
                new TestCase("declined","u@x.com","bad",false)
        )));
        coverage.computeIfAbsent("profile", k -> new HashSet<>()).add(new TestCase("update-email","u@x.com","good",true));

        coverage.forEach((feature, cases) ->
                System.out.printf("[Objects-Coverage] %s → %s%n", feature, cases.stream().map(TestCase::name).toList())
        );
    }

    // ───────────────────────────── 12) TREE MAP/SET with OBJECTS — deterministic reporting ───────────────────────
    static void objects_tree_collections_reporting() {
        // Stable ordering by feature key
        Map<String, Integer> counts = new TreeMap<>();
        counts.put("errors", 2);
        counts.put("passes", 9);
        counts.put("skips", 1);

        // Stable ordering of case names
        Set<String> caseNames = new TreeSet<>(Set.of("z-last","a-first","m-mid"));
        System.out.printf("[Objects-TreeMap] %s%n", counts);
        System.out.printf("[Objects-TreeSet] %s%n", caseNames);
    }

    // ───────────────────────────── 13) END-TO-END — wrappers → objects handoff (realistic path) ──────────────────
    static void e2e_wrappers_to_objects() {
        // WRAPPER inputs: numeric thresholds and toggles
        Map<Integer, Double> httpSlo = Map.of(200, 0.8, 500, 2.5);
        Boolean runSlowPaths = Boolean.TRUE;

        // OBJECT inputs: cases + page + expectations
        List<TestCase> cases = List.of(
                new TestCase("ok","ok@site.com","good",true),
                new TestCase("bad","bad@","x",false)
        );
        LoginPage lp = new LoginPage(driver);
        Map<String, String> expectByLocale = Map.of("en", "Invalid credentials");

        for (TestCase tc : cases) {
            String banner = lp.email(tc.email()).pass(tc.pass()).submit();
            boolean ok = tc.expectSuccess() || banner.equals(expectByLocale.get("en"));
            double allowed = httpSlo.getOrDefault(200, 1.0);
            System.out.printf("[E2E] case=%s slowPaths=%b allowedSLO=%.2fs result=%s banner=%s%n",
                    tc.name(), runSlowPaths, allowed, ok ? "PASS" : "FAIL", banner);
        }
    }

    /*───────────────────────────── MAIN ─────────────────────────────*/
    public static void main(String[] args) {
        // Phase 1: WRAPPERS
        wrappers_list_of_integers();
        wrappers_set_of_characters();
        wrappers_map_string_boolean();
        wrappers_map_int_double();
        wrappers_concurrent_map();
        wrappers_linkedHashMap_form();

        // Phase 2: OBJECTS
        objects_list_testcases();
        objects_set_dedupe_cases();
        objects_map_page_registry();
        objects_map_list_expectations();
        objects_map_set_coverage();
        objects_tree_collections_reporting();

        // Bridge demo
        e2e_wrappers_to_objects();
    }
}
