package com.lange.trader.main;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lange on 19/3/16.
 */
public class Parser {

    public static void main(String... args) {
        parse("properCase(ARG1, ARG2, class.otherArg)");
        parse("reset()");
        parse("(a, b, c)");
        parse("exit()\n");
        parse("x(abcd def gih)");
        parse("batch(x(1,2,3), y(2,4, 5))");
    }

    public static void parse(String input) {
        String regex="([a-zA-Z0-9]+)*\\(([ ,.a-zA-Z0-9]+)*\\)\\s*";
        //String regex="([a-zA-Z0-9]+)*\\(([ ,.a-zA-Z0-9\\(\\)\\s]+)*\\)\\s*";
        //String functionRegEx="([a-zA-Z0-9]+)*\\(([ .,'a-zA-Z0-9\\(\\\\_\\-)]*)\\)";
        String argumentRegEx="";

        Pattern funcPattern = Pattern.compile(regex);
        Matcher m = funcPattern.matcher(input);
        System.out.println(String.format("Processing [%s]", input));
        System.out.println("Match found: " + m.matches());
        System.out.println("Total matches are: " + m.groupCount());

        String command = "none";
        command = m.group(1);
        System.out.println(String.format("Command: %s", command));

        if (m.group(2) == null) {
            System.out.println("Arguments: none\n\n");
            return;
        }

        String[] arguments = m.group(2).split(",");
        System.out.println(String.format("Arguments: %s\n\n", Arrays.asList(arguments).stream().collect(Collectors.joining("::"))));
    }
}
