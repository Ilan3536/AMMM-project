package edu.upc.fib.ammm;

import edu.upc.fib.ammm.algorithms.GRASP;
import edu.upc.fib.ammm.model.Solution;
import edu.upc.fib.ammm.utils.Globals;
import edu.upc.fib.ammm.utils.Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Slf4j
public class GraspExperiment {
    private static final double[] alphaValues = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
    private static final int[] iterationValues = {100, 500, 1000, 5000};

    public static void main(String[] args) {
        try (var files = Files.list(Paths.get(Globals.PROBLEMS_DIR))) {
            files
                .filter(path -> path.toString().endsWith(".dat"))
                .sorted(Comparator.comparing(Path::getFileName))
                .forEach(GraspExperiment::findBestGraspParameters);
        } catch (IOException e) {
            log.error("Error reading directory", e);
        }
    }

    public static void findBestGraspParameters(Path filePath) {
        try {
            var problem = Parser.parseFile(filePath.toString());
            log.info("Loaded dat file: {}", filePath);

            Solution bestOverallSolution = null;
            double bestAlpha = 0.0;
            int bestMaxIterations = 100;

            for (double alpha : alphaValues) {
                for (int maxIterations : iterationValues) {
                    Solution bestForThisCombo = null;

                    // Run multiple trials to account for variability
                    for (int trial = 0; trial < 10; trial++) {
                        GRASP grasp = new GRASP(problem, maxIterations, alpha);
                        Solution currentSolution = grasp.run();
                        if (bestForThisCombo == null || currentSolution.getCost() > bestForThisCombo.getCost()) {
                            bestForThisCombo = currentSolution;
                        }
                    }

                    if (bestOverallSolution == null || bestForThisCombo.getCost() > bestOverallSolution.getCost()) {
                        bestOverallSolution = bestForThisCombo;
                        bestAlpha = alpha;
                        bestMaxIterations = maxIterations;
                    }
                }
            }

            System.out.printf("File: %s, Best Alpha: %.1f, Best Iterations: %d, Best Solution Cost: %d%n",
                filePath.getFileName(), bestAlpha, bestMaxIterations, bestOverallSolution.getCost());
        } catch (IOException e) {
            log.error("Error loading dat file: {}", filePath, e);
        }
    }
}
