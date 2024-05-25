package edu.upc.fib.ammm;

import edu.upc.fib.ammm.algorithms.GRASP;
import edu.upc.fib.ammm.algorithms.Greedy;
import edu.upc.fib.ammm.algorithms.GreedyLocalSearch;
import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;
import edu.upc.fib.ammm.utils.Parser;
import edu.upc.fib.ammm.utils.PrintUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            processAllFilesInDirectory("data");
        } else {
            var file = Path.of(args[0]).toFile();

            if (file.isDirectory()) {
                processAllFilesInDirectory(file.getAbsolutePath());
            } else {
                solve(file.getAbsolutePath());
            }
        }
    }

    public static void processAllFilesInDirectory(String directory) {
        try (var files = Files.list(Paths.get(directory))) {
            files
                .filter(path -> path.toString().endsWith(".dat"))
                .sorted(Comparator.comparing(Path::getFileName))
                .forEach(path -> solve(path.toString()));
        } catch (IOException e) {
            log.error("Error reading directory: {}", directory);
        }
    }

    public static void solve(String filePath) {
        try {
            var p = Parser.parseFile(filePath);
            log.info("Loaded dat file: {}", filePath);

            PrintUtils.printProducts(p.getProducts());

            var algorithms = List.of(
                new Greedy(p),
                new GreedyLocalSearch(p),
                new GRASP(p, 100, 0),
                new GRASP(p, 100, 0.5),
                new GRASP(p, 100, 1)
            );

            for (var algo : algorithms) {
                long startTime = System.currentTimeMillis();
                Solution s = algo.run();
                long elapsedTime = System.currentTimeMillis() - startTime;

                PrintUtils.printSolution(s, algo.toString(), true);

                log.info("{} took {} ms to run.", algo, elapsedTime);
            }
        } catch (IOException e) {
            log.error("Error loading dat file: {}", filePath);
        }
    }
}