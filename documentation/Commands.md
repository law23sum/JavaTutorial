

mvn clean install test verify
mvn -DskipTests exec:java
mvn test

gradle || ./gradlew test
gradle || ./gradlew clean runTutorial
gradle || ./gradlew --stop || true
rm -rf ~/.gradle/caches ~/.gradle/wrapper/dists