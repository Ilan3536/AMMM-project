package edu.upc.fib.ammm.utils;

import edu.upc.fib.ammm.model.Problem;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Parser {
    public static Problem parseFile(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        return parse(content);
    }

    private static int[] parseArray(int n, String array) {
        int[] arr = new int[n];
        var values = Arrays.stream(array.split("\\s+")).filter(i -> !i.isEmpty()).mapToInt(Integer::parseInt).toArray();
        System.arraycopy(values, 0, arr, 0, n);
        return arr;
    }

    // Parse .dat OPL data for this specific problem
    public static Problem parse(String content) {
        int x = 0;
        int y = 0;
        int c = 0;
        int n = 0;
        int[] w = null;
        int[] s = null;
        int[] p = null;

        String[] lines = content.trim().split("\n");

        String assignment = "([a-z])\\s+=\\s+(\\[)?([0-9 ]+)(])?;";
        Pattern pattern = Pattern.compile(assignment);

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                String name = matcher.group(1);
                String val = matcher.group(3);

                switch (name) {
                    case "x" -> x = Integer.parseInt(val);
                    case "y" -> y = Integer.parseInt(val);
                    case "c" -> c = Integer.parseInt(val);
                    case "n" -> n = Integer.parseInt(val);
                    case "w" -> w = parseArray(n, val);
                    case "s" -> s = parseArray(n, val);
                    case "p" -> p = parseArray(n, val);
                }
            }
        }

        return new Problem(x, y, c, n, w, s, p);
    }
}
