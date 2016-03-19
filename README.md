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

### REPL Commands

You can always get help by issuing this command on the REPL: ```help()```

|Command|Description|
|-------|-----------|
|help() | Gives you the list of all commands |
|reset()| Resets the environment. The algorithm setup and the price feeds are wiped out.|
|exit() | Exit the REPL.|
|setup(String... productNames) | Sets up the algorithm to process these productNames. Any other productName is not tradable.|
|price(String productName, double quotedPrice) | This simulates a price feed and is given to the TradingAlgorithm setup in the environment. This may or may not result in a trade.|

### REPL Sample Session

```
$ ./build/install/AutomatedTrader/bin/AutomatedTrader
Welcome to the AutomatedTrader REPL

>>>	help()
* Help Menu
	help()	this message
	reset()	reset the environment
	exit()	REPL exits loop
	setup(String... productNames)	Sets up the TradingAlgorithm. Only productNames specified are tradable.
	price(String productName, double quotedPrice)	Offer a price feed to the TradingAlgorithm.

>>>	price(a, 1)
ERROR: Algorithm not setup. Please setup via setupTradingAlgorithm(String... productNames) command first before price feed.

>>>	setup(a)
* BuyWhenBullishTradingAlgorithm{productNames=[a]}

>>>	price(a, 1)
* no trade

>>>	price(a, 2)
* no trade

>>>	price(a, 3)
* no trade

>>>	price(a, 4)
* Trade{ productName: a, tradeDirection: BUY, priceValue: 4.0, quantity: 1000 }

>>>	reset()
* Environment reset.

>>>	price(a, 5)
ERROR: Algorithm not setup. Please setup via setupTradingAlgorithm(String... productNames) command first before price feed.

>>>	setup(a)
* BuyWhenBullishTradingAlgorithm{productNames=[a]}

>>>	price(a, 6)
* no trade

>>>	price(a, 7)
* no trade

>>>	price(a, 8)
* no trade

>>>	price(a, 9)
* Trade{ productName: a, tradeDirection: BUY, priceValue: 9.0, quantity: 1000 }

>>>	exit()
$
```

## Library Attributions

This code is dependent on some open source libraries during runtime.

|Library|Description of Use|License|
|--------|------------------|-------|
| [Cofoja][1] | Design by contract framework used by the code. It is packaged together with the build binaries. But it is disabled on build runtime but enabled during test runtime. | [GPL 3.0][2] |
| [Guava][3]  | Convenient collection factory as well as some functional programming constructs | [Apache License v2][4] |

[1]:https://github.com/nhatminhle/cofoja
[2]:https://github.com/nhatminhle/cofoja/blob/master/COPYING
[3]:https://github.com/google/guava
[4]:https://github.com/google/guava/blob/master/COPYING

