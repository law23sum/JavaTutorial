

mvn clean install test -Denv=local -Dbrowser=chrome
mvn -Denv=staging -Dbrowser=firefox test
mvn exec:java@run-runner

gradle || ./gradlew test
gradle || ./gradlew test -Denv=staging -Dbrowser=firefox
gradle || ./gradlew test -Denv=prod -Dbrowser=chrome
gradle || ./gradlew clean runTutorial
gradle || ./gradlew --stop || true
gradle || ./gradlew clean test --no-configuration-cache
rm -rf ~/.gradle/caches ~/.gradle/wrapper/dists