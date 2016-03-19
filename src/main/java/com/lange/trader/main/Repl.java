package com.lange.trader.main;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.lange.trader.algo.BuyWhenBullishTradingAlgorithm;
import com.lange.trader.algo.TradingAlgorithm;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lange on 19/3/16.
 */
public class Repl {

    public static void main(String... args) {
        ReplClient client = Holder.INSTANCE;

        System.out.println("Welcome to the AutomatedTrader REPL\n");
        while(true) {
            client.prompt();
        }
    }

    static class Environment {

        Map<String, Object> identifierMap = Maps.newHashMap();

        public Object set(String identifier, Object value) {
            identifierMap.put(identifier, value);
            return value;
        }

        public Object get(String identifier) {
            return identifierMap.get(identifier);
        }

        public void clear() {
            identifierMap.clear();
        }
    }

    static class Holder {
        static ReplClient INSTANCE = new ReplClient();
    }

    static class ReplClient {
        final Environment environment = new Environment();

        final Scanner scanner = new Scanner(System.in);

        static Map<String, Function<Optional<List<String>>, Pair<Boolean, String>>> DSL = new HashMap<String, Function<Optional<List<String>>, Pair<Boolean, String>>>(){{

            put("help", argumentsOpt -> {
                StringBuffer buffer = new StringBuffer("Help Menu");
                buffer.append("\n\thelp()\tthis message");
                buffer.append("\n\treset()\treset the environment");
                buffer.append("\n\texit()\tREPL exits loop");
                buffer.append("\n\tsetup(String... productNames)\tSets up the TradingAlgorithm. Only productNames specified are tradable.");
                buffer.append("\n\tprice(String productName, double quotedPrice)\tOffer a price feed to the TradingAlgorithm.");

                return Pair.of(false, buffer.toString());
            });

            put("setup", argumentsOpt -> {
                if (!argumentsOpt.isPresent()) {
                    return Pair.of(true, "Missing productNames. Usage: setup(String... productNames)");
                }

                BuyWhenBullishTradingAlgorithm.create(argumentsOpt.get());
                Holder.INSTANCE.environment.set("algo", BuyWhenBullishTradingAlgorithm.create(argumentsOpt.get()));
                return Pair.of(false, Holder.INSTANCE.environment.get("algo").toString());
            });

            put("exit", argumentsOpt -> {
                System.exit(0);
                return Pair.of(false, "");
            });

            put("reset", argumentsOpt -> {
                Holder.INSTANCE.environment.clear();
                return Pair.of(false, "Environment reset.");
            });

            put("price", argumentsOpt -> {
                TradingAlgorithm tradingAlgorithm = (TradingAlgorithm) Holder.INSTANCE.environment.get("algo");
                if (tradingAlgorithm == null) {
                    return Pair.of(true, "Algorithm not setup. Please setup via setupTradingAlgorithm(String... productNames) command first before price feed.");
                }

                if (!argumentsOpt.isPresent()) {
                    return Pair.of(true, "Missing price data. Usage: price(String productName, double quotedPrice)");
                }

                List<String> argumentsList = argumentsOpt.get();
                if (argumentsList.isEmpty() || argumentsList.size() != 2) {
                    return Pair.of(true, "Missing price data. Usage: price(String productName, double quotedPrice)");
                }

                String productName = argumentsList.get(0);
                if (productName == null || productName.trim().isEmpty()) {
                    return Pair.of(true, "productName should not be empty");
                }

                String quotedPriceStr = argumentsList.get(1);
                if (quotedPriceStr == null || quotedPriceStr.trim().isEmpty()) {
                    return Pair.of(true, "quotedPriceStr should not be empty");
                }

                double quotedPrice = 0;
                try {
                    quotedPrice = Double.parseDouble(quotedPriceStr);
                } catch (NumberFormatException e) {
                    return Pair.of(true, "quotedPrice is not numeric");
                }

                Trade trade = tradingAlgorithm.buildTrades(Price.create(productName, quotedPrice));
                return Pair.of(false, trade != null ? trade.toString() : "no trade");
            });

        }};

        void prompt() {
            System.out.print(">>>\t");
            String entry = scanner.nextLine();
            Pair<Boolean, String> result = execute(entry);
            if (result.getKey()) {
                showError(result.getRight());
            } else {
                report(result.getRight());
            }
        }

        public void showError(String errorMessage) {
            System.err.println(String.format("ERROR: %s\n", errorMessage));
        }

        public void report(String message) {
            System.out.println(String.format("* %s\n", message));
        }

        Pair<Boolean, String> execute(String input) {
            Optional<Pair<String, Optional<List<String>>>> parse = Parser.parse(input);
            if (!parse.isPresent()) {
                return Pair.of(true, "REPL cannot understand the command.");
            }

            Pair<String, Optional<List<String>>> commandPair = parse.get();
            if (!DSL.containsKey(commandPair.getKey())) {
                return Pair.of(true, "Invalid command: " + commandPair.getKey());
            }

            Function<Optional<List<String>>, Pair<Boolean, String>> commandFunction = DSL.get(commandPair.getKey());
            return commandFunction.apply(commandPair.getRight());
        }
    }

    static class Parser {
        static Optional<Pair<String, Optional<List<String>>>> parse(String input) {
            String regex="([a-zA-Z0-9]+)*\\(([ ,.a-zA-Z0-9]+)*\\)\\s*";
            Pattern funcPattern = Pattern.compile(regex);
            Matcher m = funcPattern.matcher(input);

            if (!m.matches()) {
                return Optional.absent();
            }

            Optional<String> command = Optional.fromNullable(m.group(1));
            if (!command.isPresent()) {
                return Optional.absent();
            }

            Optional<List<String>> arguments = Optional.absent();
            if (m.group(2) != null) {
                arguments = Optional.fromNullable(Arrays.asList(m.group(2).split(",")));
            }
            return Optional.of(Pair.of(command.get(), arguments));
        }
    }
}
