package com.lange.trader.main;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.lange.trader.algo.BuyWhenBullishTradingAlgorithm;
import com.lange.trader.algo.TradingAlgorithm;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;
import com.lange.trader.struc.Pair;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                buffer.append("\n\tbatchSerial(String filename)\tRun a script file. The first line will need to be a setup() command.");
                buffer.append("\n\tbatchParallel(String filename)\tRun a script file. The first line will be executed first and should be a setup() command. The rest will be executed in parallel and thus no guarantee of order.");
                buffer.append("\n\tprice(String productName, double quotedPrice)\tOffer a price feed to the TradingAlgorithm.");

                return Pair.of(false, buffer.toString());
            });

            put("batchParallel", argumentsOpt -> {
                List<String> arguments = argumentsOpt.get();

                if (!argumentsOpt.isPresent()) {
                    return Pair.of(true, "Missing filename argument. Usage: batchParallel(String filename)");
                }

                if (arguments.size() != 1) {
                    return Pair.of(true, "Invalid filename argument. Usage: batchParallel(String filename)");
                }

                String filename = arguments.get(0);

                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String firstLine = reader.readLine();
                    Holder.INSTANCE.executeCommand(firstLine);

                    long start = System.nanoTime();
                    List<Supplier<String>> callables =
                            StreamUtils.zip(
                                    Stream.iterate(0, i -> i + 1),
                                    reader.lines(),
                                    (i, line) -> Pair.of(i, line))
                                    .map(indexedLine -> (Supplier<String>) (() ->
                                            String.format("Processing: [%s] %s :: %s",
                                                    indexedLine.key, indexedLine.value, Holder.INSTANCE.execute(indexedLine.value).value))
                                    ).collect(Collectors.toList());
                    int count = callables.parallelStream().map(supplier -> {
                        Holder.INSTANCE.report(supplier.get());
                        return 1;
                    }).reduce(Math::addExact).orElse(0);
                    long duration = System.nanoTime() - start;
                    return Pair.of(false, String.format("Batch Parallel completed processing %s feeds in %s ns", count, duration));
                } catch (IOException e) {
                    StringBuffer buffer = new StringBuffer("ERROR: I/O Exception executing batch parallel\n");
                    StringWriter sWriter = new StringWriter();
                    PrintWriter writer = new PrintWriter(sWriter);
                    e.printStackTrace(writer);
                    writer.flush();
                    buffer.append("STACK TRACE: ").append(sWriter.toString());
                    return Pair.of(true, buffer.toString());
                }

            });

            put("batchSerial", argumentsOpt -> {
                List<String> arguments = argumentsOpt.get();

                if (!argumentsOpt.isPresent()) {
                    return Pair.of(true, "Missing filename argument. Usage: batchSerial(String filename)");
                }

                if (arguments.size() != 1) {
                    return Pair.of(true, "Invalid filename argument. Usage: batchSerial(String filename)");
                }

                String filename = arguments.get(0);

                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String firstLine = reader.readLine();
                    Holder.INSTANCE.executeCommand(firstLine);

                    long start = System.nanoTime();
                    StreamUtils.zip(Stream.iterate(0, i -> i + 1),
                            reader.lines(),
                            (i, line) -> Pair.of(i, line))
                            .forEach(indexedLine -> {
                                Holder.INSTANCE.report(String.format("Processing: [%s] %s :: %s",
                                        indexedLine.key, indexedLine.value, Holder.INSTANCE.execute(indexedLine.value).value));
                            });
                    long duration = System.nanoTime() - start;
                    return Pair.of(false, String.format("Batch Serial completed in %s ns", duration));
                } catch (IOException e) {
                    StringBuffer buffer = new StringBuffer("ERROR: I/O Exception executing batch serial\n");
                    StringWriter sWriter = new StringWriter();
                    PrintWriter writer = new PrintWriter(sWriter);
                    e.printStackTrace(writer);
                    writer.flush();
                    buffer.append("STACK TRACE: ").append(sWriter.toString());
                    return Pair.of(true, buffer.toString());
                }
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
            executeCommand(entry);
        }

        void executeCommand(String command) {
            Pair<Boolean, String> result = execute(command);
            if (result.key) {
                showError(result.value);
            } else {
                report(result.value);
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
            if (!DSL.containsKey(commandPair.key)) {
                return Pair.of(true, "Invalid command: " + commandPair.key);
            }

            Function<Optional<List<String>>, Pair<Boolean, String>> commandFunction = DSL.get(commandPair.key);
            return commandFunction.apply(commandPair.value);
        }
    }

    static class Parser {
        static Optional<Pair<String, Optional<List<String>>>> parse(String input) {
            String regex="([a-zA-Z0-9]+)*\\(([ _,.a-zA-Z0-9]+)*\\)\\s*";
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
                arguments = Optional.fromNullable(
                        Arrays.stream(m.group(2).split(","))
                                .map(argument -> argument.trim())
                                .collect(Collectors.toList()));
            }
            return Optional.of(Pair.of(command.get(), arguments));
        }
    }

    static class ZippingSpliterator<L, R, O> implements Spliterator<O> {

        static <L, R, O> Spliterator<O> zipping(Spliterator<L> lefts, Spliterator<R> rights, BiFunction<L, R, O> combiner) {
            return new ZippingSpliterator<>(lefts, rights, combiner);
        }

        private final Spliterator<L> lefts;
        private final Spliterator<R> rights;
        private final BiFunction<L, R, O> combiner;
        private boolean rightHadNext = false;

        private ZippingSpliterator(Spliterator<L> lefts, Spliterator<R> rights, BiFunction<L, R, O> combiner) {
            this.lefts = lefts;
            this.rights = rights;
            this.combiner = combiner;
        }

        @Override
        public boolean tryAdvance(Consumer<? super O> action) {
            rightHadNext = false;
            boolean leftHadNext = lefts.tryAdvance(l ->
                    rights.tryAdvance(r -> {
                        rightHadNext = true;
                        action.accept(combiner.apply(l, r));
                    }));
            return leftHadNext && rightHadNext;
        }

        @Override
        public Spliterator<O> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return Math.min(lefts.estimateSize(), rights.estimateSize());
        }

        @Override
        public int characteristics() {
            return lefts.characteristics() & rights.characteristics()
                    & ~(Spliterator.DISTINCT | Spliterator.SORTED);
        }
    }
}
