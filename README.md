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

## Installing and Running the REPL

A REPL was introduced to be able to test the trading algorithm. These are the steps to build, install, and run the REPL.

1. On a Terminal, go to the project directory and issue the following command:
```
$ ./gradlew clean installDist
```
2. Then on the same command line, you can run the application via this command:
```
$ ./build/install/AutomatedTrader/bin/AutomatedTrader
```

## REPL Commands

You can always get help by issuing this command on the REPL: ```help()```

|Command|Description|
|-------|-----------|
|help() | Gives you the list of all commands |
|reset()| Resets the environment. The algorithm setup and the price feeds are wiped out.|
|exit() | Exit the REPL.|
|setup(String... productNames) | Sets up the algorithm to process these productNames. Any other productName is not tradable.|
|price(String productName, double quotedPrice) | This simulates a price feed and is given to the TradingAlgorithm setup in the environment. This may or may not result in a trade.|


