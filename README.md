# AutomatedTrader

This is an exercise to implement the TradingAlgorithm interface.

## Importing the Project via IntelliJ

1. Import the Project via IntelliJ
2. Configure the Project to use Java 8

## Running the Unit Test

### Running via Gradle in Command Line

```
$ ./gradlew test
```

### Running via IntelliJ

1. Add a run configuration for all JUnit tests on the project
2. Ensure that you pass the following VM options during test run:

```
-javaagent:lib/cofoja.asm-1.3-20160207.jar
```
