

mvn clean install test verify
mvn -DskipTests exec:java

./gradlew --stop || true
rm -rf ~/.gradle/caches ~/.gradle/wrapper/dists