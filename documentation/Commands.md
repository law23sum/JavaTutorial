

mvn clean install test verify
mvn -DskipTests exec:java

gradle || ./gradlew test
gradle || ./gradlew clean runTutorial
gradle || ./gradlew --stop || true
gradle || ./gradlew clean test --no-configuration-cache
rm -rf ~/.gradle/caches ~/.gradle/wrapper/dists