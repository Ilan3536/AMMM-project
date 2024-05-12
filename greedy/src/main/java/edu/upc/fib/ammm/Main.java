package edu.upc.fib.ammm;

public class Main {
    public static void main(String[] args) throws Exception {
        Problem p = Parser.parseFile(args[0]);
        System.out.println("Loaded dat file:");
        System.out.println(p);

        Heuristic[] algorithms = new Heuristic[] {
                new Greedy(p),
                new GreedyLocalSearch(p),
                new GRASP(p)
        };

        for (Heuristic algo : algorithms) {
            System.out.println("Running: " + algo.getClass().getSimpleName());
            Solution s = algo.run();
            System.out.println("Solution:");
            System.out.println(s);
        }
    }
}