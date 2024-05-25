package edu.upc.fib.ammm;

import edu.upc.fib.ammm.algorithms.GRASP;
import edu.upc.fib.ammm.algorithms.Greedy;
import edu.upc.fib.ammm.algorithms.GreedyLocalSearch;
import edu.upc.fib.ammm.algorithms.ILP;
import edu.upc.fib.ammm.model.PerformanceData;
import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;
import edu.upc.fib.ammm.utils.Globals;
import edu.upc.fib.ammm.utils.Parser;
import edu.upc.fib.ammm.utils.PlotPerformance;
import edu.upc.fib.ammm.utils.PrintUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

@Slf4j
public class Main {

    public static void main(String[] args) {
        var results = new LinkedHashMap<Problem, List<PerformanceData>>();

        if (args.length == 0) {
            processAllFilesInDirectory(Path.of(Globals.PROBLEMS_DIR), results);
        } else {
            var path = Path.of(args[0]).toAbsolutePath();
            var file = path.toFile();

            if (file.isDirectory()) {
                processAllFilesInDirectory(path, results);
            } else {
                solveFile(path, results);
            }
        }

        var plotter = new PlotPerformance(results);
        plotter.plot();
    }

    public static void processAllFilesInDirectory(Path directory, Map<Problem, List<PerformanceData>> results) {
        try (var files = Files.list(directory)) {
            files
                .filter(path -> path.toString().endsWith(".dat"))
                .sorted(Comparator.comparing(Path::getFileName))
                .forEach(path -> {
                    solveFile(path, results);
                });
        } catch (IOException e) {
            log.error("Error reading directory: {}", directory);
        }
    }

    public static void solveFile(Path path, Map<Problem, List<PerformanceData>> results) {
        try {
            var p = Parser.parseFile(path.toString());
            var performances = solveProblem(p);
            results.put(p, performances);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("Loaded dat file: {}", path);
    }

    public static List<PerformanceData> solveProblem(Problem p) {
        var performances = new ArrayList<PerformanceData>();
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

            log.debug("{} took {} ms to run.", algo, elapsedTime);
            var perfData = new PerformanceData(algo.toString(), s, elapsedTime);
            performances.add(perfData);
        }

        if (Globals.ENABLE_ILP) {
            var ilp = new ILP();
            var ilpPerf = ilp.solve(p);
            performances.add(ilpPerf);
        }

        return performances;
    }
}