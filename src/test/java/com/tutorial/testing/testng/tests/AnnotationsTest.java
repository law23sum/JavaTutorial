package com.tutorial.testing.testng.tests;

import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Realistic TestNG demo covering:
 * - Environment-driven config (@Parameters)
 * - Suite/Class/Method lifecycle hooks
 * - Data-driven testing (@DataProvider)
 * - Groups, dependencies, soft asserts
 * - Negative cases (validation + lockout)
 * - Transient failure with retry analyzer
 * - Feature flag toggles
 */
public class AnnotationsTest {

    // --- "System under test" (tiny in-memory domain) ------------------------
    static class Config {
        final String env;
        final String baseUrl;
        final Duration apiTimeout;

        Config(String env) {
            this.env = env == null ? "local" : env;
            this.baseUrl = switch (this.env) {
                case "staging" -> "https://staging.example.com";
                case "prod" -> "https://example.com";
                default -> "http://localhost:8080";
            };
            this.apiTimeout = switch (this.env) {
                case "prod" -> Duration.ofSeconds(2);
                case "staging" -> Duration.ofSeconds(3);
                default -> Duration.ofSeconds(5);
            };
        }
    }

    static class FeatureFlags {
        private final Set<String> enabled = new HashSet<>();

        void enable(String flag) {
            enabled.add(flag);
        }

        void disable(String flag) {
            enabled.remove(flag);
        }

        boolean isOn(String flag) {
            return enabled.contains(flag);
        }
    }

    static class User {
        final String email;
        final String role;
        final String passwordHash;
        AtomicInteger failedLogins = new AtomicInteger(0);
        boolean locked = false;

        User(String email, String role, String password) {
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email: " + email);
            }
            this.email = email.trim();
            this.role = role;
            this.passwordHash = "hash:" + password;
        }

        private static boolean isValidEmail(String email) {
            if (email == null) return false;
            String e = email.trim();
            // Simple, pragmatic validation:
            // - one '@'
            // - non-empty local and domain
            // - domain contains a dot not at edges
            int at = e.indexOf('@');
            if (at <= 0 || at != e.lastIndexOf('@')) return false;
            String local = e.substring(0, at);
            String domain = e.substring(at + 1);
            if (domain.isEmpty()) return false;
            int dot = domain.indexOf('.');
            if (dot <= 0 || dot == domain.length() - 1) return false;
            return true;
        }
    }


    static class UserRepository {
        private final Map<String, User> users = new HashMap<>();

        void save(User u) {
            users.put(u.email, u);
        }

        Optional<User> find(String email) {
            return Optional.ofNullable(users.get(email));
        }

        void clear() {
            users.clear();
        }
    }

    static class AuthService {
        private final UserRepository repo;
        private final int maxFailures;

        AuthService(UserRepository repo, int maxFailures) {
            this.repo = repo;
            this.maxFailures = maxFailures;
        }

        boolean login(String email, String password) {
            User user = repo.find(email).orElseThrow(() -> new NoSuchElementException("User not found"));
            if (user.locked) throw new SecurityException("Account locked");
            boolean ok = Objects.equals(user.passwordHash, "hash:" + password);
            if (ok) {
                user.failedLogins.set(0);
                return true;
            } else {
                int fails = user.failedLogins.incrementAndGet();
                if (fails >= maxFailures) {
                    user.locked = true;
                }
                return false;
            }
        }
    }

    static class PaymentGateway {
        private final Random rnd = new Random(42); // deterministic for CI
        private final double successRate;

        PaymentGateway() { this(0.90); }           // 90% success
        PaymentGateway(double successRate) { this.successRate = successRate; }

        boolean chargeCents(int cents) {
            return rnd.nextDouble() < successRate;
        }
    }


    // --- Retry Analyzer for flaky/transient tests ---------------------------
    public static class TransientRetry implements IRetryAnalyzer {
        private int attempt = 0;
        private static final int MAX_RETRIES = 2; // total tries = 1 (original) + 2 (retries)

        @Override
        public boolean retry(ITestResult result) {
            // only retry if the failure looks transient (Simple heuristic: AssertionError)
            if (result.getThrowable() instanceof AssertionError && attempt < MAX_RETRIES) {
                attempt++;
                return true;
            }
            return false;
        }
    }

    // --- Test fixtures ------------------------------------------------------
    private Config config;
    private FeatureFlags flags;
    private UserRepository users;
    private AuthService auth;
    private PaymentGateway payments;

    @Parameters({"env"}) // pass -Denv=staging (from Maven/Gradle) or set in your TestNG suite XML
    @BeforeSuite
    public void beforeSuite(@org.testng.annotations.Optional("local") String env) {
        config = new Config(env);
        System.out.println("@BeforeSuite - env=" + config.env + " baseUrl=" + config.baseUrl + " apiTimeout=" + config.apiTimeout);
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("@AfterSuite - suite cleanup complete");
    }

    @BeforeClass
    public void beforeClass() {
        flags = new FeatureFlags();
        users = new UserRepository();
        auth = new AuthService(users, 3);
        payments = new PaymentGateway();

        // Seed users as a realistic fixture
        users.save(new User("admin@example.com", "ADMIN", "correct-horse-battery-staple"));
        users.save(new User("qa@example.com", "QA", "qa-pass"));
        users.save(new User("user@example.com", "USER", "user-pass"));

        // Flags often vary per env; pretend a rollout in staging
        if ("staging".equals(config.env)) {
            flags.enable("search.v2");
        }
        System.out.println("@BeforeClass - seeded users & flags");
    }

    @AfterClass
    public void afterClass() {
        users.clear();
        System.out.println("@AfterClass - repository cleared");
    }

    @BeforeMethod
    public void beforeMethod() {
        // reset per-test state that could leak between tests (common in UI/API suites)
        // example: ensure search.v2 is off by default unless test enables it
        if (!"staging".equals(config.env)) flags.disable("search.v2");
        System.out.println("@BeforeMethod - state normalized");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("@AfterMethod - test finished");
    }

    // --- Data providers -----------------------------------------------------
    @DataProvider(name = "validLogins")
    public Object[][] validLogins() {
        return new Object[][]{{"admin@example.com", "correct-horse-battery-staple", "ADMIN"}, {"qa@example.com", "qa-pass", "QA"}, {"user@example.com", "user-pass", "USER"},};
    }

    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmails() {
        return new Object[][]{{null}, {""}, {"no-at-symbol"}, {"user@bad"}, {"@missingLocal"}};
    }

    // --- Tests: Auth happy path with roles ---------------------------------
    @Test(groups = {"smoke", "auth"}, dataProvider = "validLogins")
    public void login_allRoles_happyPath(String email, String password, String expectedRole) {
        boolean ok = auth.login(email, password);
        Assert.assertTrue(ok, "Expected login to succeed");
        User u = users.find(email).orElseThrow();
        Assert.assertEquals(u.role, expectedRole, "Role should match");
    }

    // --- Tests: Lockout after repeated failures -----------------------------
    @Test(groups = {"auth", "negative"})
    public void login_lockout_afterThreeFailures() {
        String email = "user@example.com";
        // 3 bad attempts
        Assert.assertFalse(auth.login(email, "wrong1"), "bad attempt #1 should fail");
        Assert.assertFalse(auth.login(email, "wrong2"), "bad attempt #2 should fail");
        Assert.assertFalse(auth.login(email, "wrong3"), "bad attempt #3 should fail (and lock)");
        // Now account should be locked
        try {
            auth.login(email, "user-pass");
            Assert.fail("Expected SecurityException (locked)");
        } catch (SecurityException expected) {
            // good
        }
        User u = users.find(email).orElseThrow();
        Assert.assertTrue(u.locked, "User should be locked after 3 failures");
    }

    // --- Tests: Input validation (service throws) ---------------------------
    @Test(groups = {"validation", "negative"}, dataProvider = "invalidEmails", expectedExceptions = IllegalArgumentException.class)
    public void user_creation_invalidEmail_rejected(String badEmail) {
        // Simulate validation at creation time (common in domain/service/unit tests)
        users.save(new User(badEmail, "USER", "pass")); // should throw
    }

    // --- Tests: Feature flags (rollout behavior) ----------------------------
    @Test(groups = {"feature"})
    public void search_v2_featureFlag_controlsBehavior() {
        SoftAssert softly = new SoftAssert();

        // v1 (prefix)
        flags.disable("search.v2");
        softly.assertFalse(flags.isOn("search.v2"), "v2 should be OFF");
        softly.assertEquals(fakeSearch("tea", flags), List.of("tea", "team", "teal"),
                "old search should prefix match");

        // v2 (infix) — compare as sets (order-agnostic) + size guard
        flags.enable("search.v2");
        softly.assertTrue(flags.isOn("search.v2"), "v2 should be ON now");

        List<String> v2 = fakeSearch("tea", flags);
        softly.assertEquals(new java.util.HashSet<>(v2),
                new java.util.HashSet<>(List.of("tea", "team", "teal")),
                "v2 search should support infix and return the same members");
        softly.assertEquals(v2.size(), 3, "v2 should not include extras like 'late' or 'eat'");

        softly.assertAll();
    }



    private List<String> fakeSearch(String term, FeatureFlags flags) {
        List<String> corpus = List.of("tea", "team", "teal", "late", "eat");
        if (flags.isOn("search.v2")) {
            // infix + simple ranking: longer first, then lexicographic
            return corpus.stream().filter(s -> s.contains(term)).sorted(Comparator.comparingInt(String::length).reversed().thenComparing(Comparator.naturalOrder())).toList(); // => [team, teal, tea]
        } else {
            // prefix, keep natural order
            return corpus.stream().filter(s -> s.startsWith(term)).toList();
        }
    }


    // --- Tests: Transient failure with retry (payments, networks, etc.) -----
    @Test(groups = {"payments", "flaky"}, retryAnalyzer = TransientRetry.class)
    public void payment_charge_transientFlakes_areRetried() {
        // We expect at least one attempt to eventually succeed (70% success each try)
        boolean ok = payments.chargeCents(1999);
        // This may fail on the first attempt; the retry analyzer will re-run it up to 2 times
        Assert.assertTrue(ok, "Payment should eventually succeed after transient retries");
    }

    // --- Tests: Dependent flows (login -> privileged action) ----------------
    @Test(groups = {"flow"}, dependsOnMethods = "login_allRoles_happyPath")
    public void admin_only_privilegedAction_requiresLogin() {
        // pretend the previous test ensured admin login works; now assert privilege
        User admin = users.find("admin@example.com").orElseThrow();
        Assert.assertEquals(admin.role, "ADMIN");
        boolean allowed = canDeleteUser(admin);
        Assert.assertTrue(allowed, "Admin should be allowed to delete users");
    }

    private boolean canDeleteUser(User requester) {
        return "ADMIN".equals(requester.role);
    }
}
