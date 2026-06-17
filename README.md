# JavaTutorial

A curriculum-style Java repo. One Maven module, three pillars:

| Pillar | Package root | What it teaches |
| --- | --- | --- |
| **Java fundamentals** | `com.javatutorial.fundamentals` · `com.javatutorial.oop` · `com.javatutorial.datastructures` · `com.javatutorial.algorithms` | Primitives, references, wrappers, strings, OOP, collections, classic algorithm patterns |
| **Spring Boot demo** | `com.javatutorial.springapp` | A tiny REST + JPA + Thymeleaf app over H2 |
| **Selenium / TestNG framework** | `com.javatutorial.automation` (under `src/test/java`) | Page Object Model, custom annotations, custom exceptions, TestNG lifecycle, `@DataProvider` |

Every tutorial class has a header comment explaining the concept and a runnable
`main(String[])` so you can step through it from your IDE.

---

## Project layout

```
src/main/java/com/javatutorial/
├── JavaTutorialApplication.java        # @SpringBootApplication entry point
│
├── fundamentals/                       # The Types
│   ├── primitives/PrimitivesDemo.java
│   ├── references/ReferencesDemo.java
│   ├── wrappers/WrapperClassesDemo.java
│   ├── strings/{StringsDemo, StringBuilderDemo}.java
│   ├── operators/OperatorsDemo.java
│   └── controlflow/ControlFlowDemo.java
│
├── oop/                                # OOP pillars
│   ├── classes/{RootClass,BaseClass,SubClass}.java
│   ├── interfaces/{Flyable,Bird,Airplane}.java
│   ├── encapsulation/BankAccount.java
│   ├── inheritance/{Animal,Dog}.java
│   ├── polymorphism/{CompileTime,Runtime}Polymorphism.java
│   └── shapes/{Shape,Circle,Rectangle}.java
│
├── datastructures/
│   ├── lists/{ArrayList,LinkedList}Demo.java
│   ├── sets/{HashSet,LinkedHashSet,TreeSet}Demo.java
│   ├── queues/{PriorityQueue,ArrayDeque}Demo.java
│   ├── maps/{HashMap,LinkedHashMap,TreeMap}Demo.java
│   └── linkedlist/ListNode.java
│
├── algorithms/
│   ├── arrays/{TwoSum, MaxSubArrayKadane, MoveZerosToEnd, MissingNumberXor}.java
│   ├── strings/{LongestSubstringNoRepeat, ValidAnagram, LongestCommonPrefix}.java
│   ├── linkedlist/ReverseLinkedList.java
│   ├── trees/{TreeNode, LevelOrderTraversal}.java
│   ├── searching/BinarySearch.java
│   ├── dp/LongestIncreasingSubsequence.java
│   ├── stocks/BestTimeToBuySellStock.java
│   └── graph/Dijkstra.java
│
└── springapp/                          # Spring Boot demo
    ├── config/AppConfig.java
    ├── controller/{Home,User}Controller.java
    ├── service/UserService.java
    ├── repository/UserRepository.java
    ├── model/User.java
    └── exception/{UserNotFoundException, GlobalExceptionHandler}.java
```

```
src/test/java/com/javatutorial/
├── fundamentals/                       # JUnit 5: wrapper cache, String basics
├── algorithms/                         # JUnit 5: one test class per algorithm
├── springapp/UserControllerTest.java   # @SpringBootTest + MockMvc
└── automation/                         # Selenium + TestNG framework + tests
    ├── annotations/TestCategory.java   # custom @interface
    ├── exceptions/{DataNotFoundException, ElementNotClickableException}.java
    ├── driver/DriverFactory.java       # WebDriverManager + Chrome
    ├── pages/{BasePage, LoginPage, BankStatementPage}.java
    ├── utils/FrameworkUtils.java
    ├── lifecycle/TestNgLifecycleDemoTest.java
    ├── dataproviders/LoginDataProviderTest.java
    └── tests/{AssertionCategoryTest, BankStatementAnalyzerTest}.java

src/test/resources/testng.xml          # TestNG suite (UI tests in group "ui")
src/main/resources/application.yml     # H2 + server.port
src/main/resources/templates/index.html
```

---

## Running things

Prerequisites: **JDK 17+**, **Maven 3.9+**. Chrome is only required if you opt
into the UI tests.

### Spring Boot demo app

```sh
mvn spring-boot:run
```

Then visit:

- <http://localhost:8080/> — Thymeleaf landing page
- <http://localhost:8080/api/users> — REST endpoint
- <http://localhost:8080/h2> — H2 console

### Run any tutorial class standalone

Every demo class has a `main`. From IntelliJ/VS Code, right-click → Run, or:

```sh
mvn exec:java -Dexec.mainClass=com.javatutorial.algorithms.arrays.TwoSum
```

### Tests

```sh
# Default: JUnit unit tests + non-UI TestNG tests (no browser needed)
mvn test

# Include the Selenium UI suite (requires Chrome)
mvn test -Pui
```

---

## Curriculum cross-reference

If you want to study a specific topic from your notes, here's where to look:

| Topic | Class(es) |
| --- | --- |
| Primitives vs References | `fundamentals/primitives`, `fundamentals/references` |
| Wrapper classes & Integer cache | `fundamentals/wrappers/WrapperClassesDemo`, `WrapperClassesTest` |
| String / StringBuilder | `fundamentals/strings/*` |
| Big-O reasoning | header comments on every algorithm + collection demo |
| Classes / Interfaces / OOP pillars | `oop/*` |
| Collections (List/Set/Queue/Map) | `datastructures/*` |
| Two Sum, Kadane, Move Zeros, Missing # | `algorithms/arrays/*` |
| Longest Substring, Anagram, LCP | `algorithms/strings/*` |
| Reverse linked list | `algorithms/linkedlist/ReverseLinkedList` |
| Level-order traversal (BFS) | `algorithms/trees/LevelOrderTraversal` |
| Binary search | `algorithms/searching/BinarySearch` |
| LIS, Best time to buy/sell stock | `algorithms/dp/*`, `algorithms/stocks/*` |
| Dijkstra's shortest path | `algorithms/graph/Dijkstra` |
| Selenium fundamentals + POM | `automation/driver`, `automation/pages` |
| Custom annotations | `automation/annotations/TestCategory` + `tests/AssertionCategoryTest` |
| Custom exceptions + multi-catch | `automation/exceptions/*`, `automation/utils/FrameworkUtils` |
| TestNG lifecycle | `automation/lifecycle/TestNgLifecycleDemoTest` |
| `@DataProvider` | `automation/dataproviders/LoginDataProviderTest` |
