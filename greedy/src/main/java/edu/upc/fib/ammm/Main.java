package edu.upc.fib.ammm;

import edu.upc.fib.ammm.algorithms.GRASP;
import edu.upc.fib.ammm.algorithms.Greedy;
import edu.upc.fib.ammm.algorithms.GreedyLocalSearch;
import edu.upc.fib.ammm.algorithms.Heuristic;
import edu.upc.fib.ammm.model.Solution;
import edu.upc.fib.ammm.parser.Parser;
import edu.upc.fib.ammm.utils.PrintUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args[0].equals("onlycost")){
            for (int i = 0; i < 40; i++){
                solvePrintOnlyCost("C:\\ammm-project\\opl\\project." + i + ".dat");
            }
        } else if (args[0].equals("all")){
            for (int i = 0; i < 40; i++){
                solve("C:\\ammm-project\\opl\\project." + i + ".dat");
            }
        } else {
            solve(args[0]);
        }

    }

    public static void solve(String filePath) throws IOException {
        var p = Parser.parseFile(filePath);

        System.out.println("Loaded dat file:");
        System.out.println(filePath);

        PrintUtils.printProducts(p.allProducts);

        var algorithms = new Heuristic[]{
                new Greedy(p),
                new GreedyLocalSearch(p),
                new GRASP(p, 10, 0),
                new GRASP(p, 10, 0.5),
                new GRASP(p, 10, 1)
        };

        for (var algo : algorithms) {
            System.out.println("Running: " + algo.getClass().getSimpleName());

            Solution s = algo.run();
            System.out.println(algo.getClass().getSimpleName() + " solution:");
            PrintUtils.printSolution(s);

        }

    }

    public static void solvePrintOnlyCost(String filePath) throws IOException {
        var p = Parser.parseFile(filePath);

        System.out.print("Loaded dat file:");
        System.out.println(filePath);


        var algorithms = new Heuristic[]{
                new Greedy(p),
                new GreedyLocalSearch(p),
                new GRASP(p, 100, 0),
                new GRASP(p, 100, 0.5),
                new GRASP(p, 100, 1)
        };

        for (var algo : algorithms) {

            Solution s = algo.run();
            if (algo instanceof GRASP grasp){
                System.out.print(algo.getClass().getSimpleName() + "(iters: " + grasp.getMaxIterations() + ", α: " + grasp.getAlpha() + ")"+ ": " + s.getCost());

            } else {
                System.out.print(algo.getClass().getSimpleName() + ": " + s.getCost());

            }
            System.out.println();


        }
    }
}