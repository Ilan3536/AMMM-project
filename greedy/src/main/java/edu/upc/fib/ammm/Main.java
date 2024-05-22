package edu.upc.fib.ammm;

import edu.upc.fib.ammm.algorithms.Greedy;
import edu.upc.fib.ammm.algorithms.GreedyLocalSearch;
import edu.upc.fib.ammm.algorithms.Heuristic;
import edu.upc.fib.ammm.model.Solution;
import edu.upc.fib.ammm.parser.Parser;
import edu.upc.fib.ammm.utils.PrintUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args[0].equals("all")){
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
                //new GRASP(p)
        };

        int greedyCost = 0;
        for (var algo : algorithms) {
            System.out.println("Running: " + algo.getClass().getSimpleName());

            Solution s = algo.run();
            System.out.println(algo.getClass().getSimpleName() + " solution:");
            PrintUtils.printSolution(s);

            if (algo instanceof Greedy){
                greedyCost = s.getCost();
            }
            if (algo instanceof GreedyLocalSearch){
                if (greedyCost != s.getCost()){
                    System.out.println("SOLUTIONS DIFFER!: " + "greedy: " + greedyCost + ", localSearch: " + s.getCost());
                }
            }


        }
    }
}