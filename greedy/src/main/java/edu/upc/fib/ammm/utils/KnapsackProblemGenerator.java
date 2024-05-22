package edu.upc.fib.ammm.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class KnapsackProblemGenerator {

    private static final int NUM_EXAMPLES = 30;  // Number of examples to generate
    private static final int START_INDEX = 10;  // Starting index for file names

    private static final int MIN_PRODUCTS = 3;  // Minimum number of products
    private static final int MAX_PRODUCTS = 10; // Maximum number of products

    private static final int MIN_X = 10;
    private static final int MAX_X = 20;
    private static final int MIN_Y = 10;
    private static final int MAX_Y = 20;
    private static final int MIN_C = 50;
    private static final int MAX_C = 100;

    public static void main(String[] args) {
        for (int i = 0; i < NUM_EXAMPLES; i++) {
            generateExample(START_INDEX + i);
        }
    }

    private static void generateExample(int index) {
        Random random = new Random();

        int x = random.nextInt(MAX_X - MIN_X + 1) + MIN_X;
        int y = random.nextInt(MAX_Y - MIN_Y + 1) + MIN_Y;
        int c = random.nextInt(MAX_C - MIN_C + 1) + MIN_C;

        int n = random.nextInt(MAX_PRODUCTS - MIN_PRODUCTS + 1) + MIN_PRODUCTS;  // Random number of products

        int[] p = new int[n];
        int[] w = new int[n];
        int[] s = new int[n];

        // Generate product data
        for (int i = 0; i < n; i++) {
            p[i] = random.nextInt(91) + 10;  // Random price between 10 and 100 euros
            w[i] = random.nextInt(10) + 1;   // Random weight between 1 and 10 grams
            s[i] = random.nextInt(Math.min(x, y)) + 1;  // Random side between 1 and min(x, y) millimeters
        }

        // Check if the generated data is solvable
        if (isSolvable(x, y, c, p, w, s)) {
            saveToFile(index, x, y, c, p, w, s);
        } else {
            // Retry generating the example if it is not solvable
            generateExample(index);
        }
    }

    private static boolean isSolvable(int x, int y, int c, int[] p, int[] w, int[] s) {
        // Check if at least one product can fit in the suitcase and the total weight does not exceed capacity
        for (int i = 0; i < s.length; i++) {
            if (s[i] <= x && s[i] <= y && w[i] <= c) {
                return true;
            }
        }
        return false;
    }

    private static void saveToFile(int index, int x, int y, int c, int[] p, int[] w, int[] s) {
        String fileName = "C:\\ammm-project\\opl\\project." + index + ".dat";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("x = " + x + ";\n");
            writer.write("y = " + y + ";\n");
            writer.write("c = " + c + ";\n\n");
            writer.write("n = " + p.length + ";\n\n");
            for (int i = 0; i < p.length; i++) {
                writer.write((char)('A' + i) + "  ");
            }
            writer.write("\n");
            writer.write("p = [ ");
            for (int i = 0; i < p.length; i++) {
                writer.write(String.format("%2d ", p[i]));
            }
            writer.write("];\n");
            writer.write("w = [ ");
            for (int i = 0; i < w.length; i++) {
                writer.write(String.format("%2d ", w[i]));
            }
            writer.write("];\n");
            writer.write("s = [ ");
            for (int i = 0; i < s.length; i++) {
                writer.write(String.format("%2d ", s[i]));
            }
            writer.write("];\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
