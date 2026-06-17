# AGENTS.md

## Purpose and scope
- This is a single-module Maven training repo with **three pillars**: core Java/OOP/algorithms (`src/main/java/com/javatutorial/{fundamentals,oop,datastructures,algorithms}`), a small Spring Boot app (`src/main/java/com/javatutorial/springapp`), and a Selenium/TestNG framework (`src/test/java/com/javatutorial/automation`).
- Treat examples as instructional code first: most classes are intentionally small and heavily commented (see `TwoSum.java`, `WrapperClassesDemo.java`).

## Big-picture architecture
- Spring app flow is `controller -> service -> repository -> H2` using `UserController`, `UserService`, `UserRepository`, `User`.
- Error mapping is centralized in `GlobalExceptionHandler` (`@RestControllerAdvice`), so service exceptions like `UserNotFoundException` become HTTP responses without controller-level try/catch.
- Startup seed data is created in `AppConfig` via `CommandLineRunner`; tests and manual API checks assume at least 2 initial users.
- MVC and REST coexist: `HomeController` returns Thymeleaf view `templates/index.html`, while `/api/users` is JSON REST.

## Build, run, and test workflows
- Prereqs used by the repo: Java 17 + Maven (see `pom.xml` properties and plugin config).
- Run app: `mvn spring-boot:run` (serves on port `8080`, configured in `src/main/resources/application.yml`).
- Run tutorial class directly: `mvn exec:java -Dexec.mainClass=com.javatutorial.algorithms.arrays.TwoSum`.
- Default tests: `mvn test` runs JUnit 5 plus TestNG, but excludes TestNG group `ui` via Surefire property `testng.excluded.groups`.
- UI tests opt-in: `mvn test -Pui` (or narrow with `-Dtest=BankStatementAnalyzerTest`).

## Test strategy and boundaries
- JUnit 5 is used for fundamentals/algorithms and Spring API tests (`src/test/java/com/javatutorial/{fundamentals,algorithms,springapp}`).
- `UserControllerTest` is a full Spring context + `MockMvc` integration test (`@SpringBootTest`, `@AutoConfigureMockMvc`) against in-memory H2.
- TestNG suite config lives in `src/test/resources/testng.xml` and includes lifecycle/data-provider demos plus UI group wiring.
- Selenium tests are example-style and may need environment edits (e.g., placeholder URL in `BankStatementAnalyzerTest`).

## Project-specific coding patterns
- Tutorial classes generally include JavaDoc with concept + complexity notes and a runnable `main(String[])` demo (e.g., `TwoSum.java`).
- Keep Spring controllers thin and delegate behavior to service methods (`UserController` vs `UserService`).
- Use domain-specific exceptions in automation helpers/pages (e.g., `ElementNotClickableException`, `DataNotFoundException`) instead of leaking raw Selenium/SQL exceptions.
- Page Object Model convention: locators/actions stay in `automation/pages/*`, tests call intent-level methods like `loginAs(...)`.
- Browser setup is centralized in `DriverFactory`; only Chrome is supported, controlled by `-Dbrowser` and `-Dheadless` system properties.

## Integration points and external dependencies
- Spring Boot starters: Web, Thymeleaf, Data JPA, Validation (`pom.xml`).
- Persistence is in-memory H2 (`jdbc:h2:mem:tutorialdb`) with console enabled at `/h2`.
- Test automation relies on Selenium + TestNG + WebDriverManager; driver binaries are managed automatically in code.
- `documentation/Commands.md` contains mixed historical commands (including Gradle), but Maven commands in `README.md`/`pom.xml` are the source of truth for this repo.
