package edu.upc.fib.ammm;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.utils.Globals;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class InstanceGenerator {

    private static final int NUM_PROBLEMS = 1;  // Number of examples to generate
    private static final int START_INDEX = 40;  // Starting index for file names

    private static final int MIN_PRODUCTS = 18;  // Minimum number of products
    private static final int MAX_PRODUCTS = 18; // Maximum number of products

    private static final int MIN_X = 10;
    private static final int MAX_X = 30;
    private static final int MIN_Y = 10;
    private static final int MAX_Y = 30;
    private static final int MIN_C = 50;
    private static final int MAX_C = 100;

    private static final int MAX_TRIES = 10000;

    private static final String DEFAULT_OUT_FOLDER = Globals.PROBLEMS_DIR;

    public static void main(String[] args) {
        var out = Path.of(args.length == 0 ? DEFAULT_OUT_FOLDER : args[0]).toAbsolutePath();

        log.info("Generating {} problems from {} to {} in {}", NUM_PROBLEMS, START_INDEX, START_INDEX+NUM_PROBLEMS, out);

        for (int i = START_INDEX; i < START_INDEX+NUM_PROBLEMS; i++) {
            for (int t = 0; t < MAX_TRIES; t++) {
                var problem = generateProblem(i);
                if (problem.isPresent()) {
                    saveToFile(out, i, problem.get());
                    break;
                } else {
                    if (t == MAX_TRIES - 2) {
                        throw new RuntimeException("Cannot generator problem " + i);
                    }
                }
            }
        }
    }

    private static Optional<Problem> generateProblem(int index) {
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

        var problem = new Problem(x,y,c,n,w,s,p);

        // Check if the generated data is solvable
        if (isSolvable(problem)) {
            return Optional.of(problem);
        } else {
            return Optional.empty();
        }
    }

    private static boolean isSolvable(Problem problem) {
        // Check if at least one product can fit in the suitcase and the total weight does not exceed capacity
        var box = problem.getBox();
        return problem.getProducts().stream().allMatch(product ->
            product.side() <= box.width() && product.side() <= box.height() && product.weight() <= box.maxWeight()
        );
    }

    private static void saveToFile(Path out, int index, Problem problem) {
        var name = String.format("project.%d.dat", index);
        var fileName = Path.of(out.toString(), name);
        log.info("Saving problem {} to {}", name, fileName);
        try (FileWriter writer = new FileWriter(fileName.toString())) {
            writer.write(problem.toString());
        } catch (IOException e) {
            log.error("Error while saving problem {}", name, e);
        }
    }
}
