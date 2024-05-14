package edu.upc.fib.ammm;

public class GreedyLocalSearch extends Heuristic {
    GreedyLocalSearch(Problem p) {
        super(p);
    }

    public Solution run() {
        return new Solution(new char[0][0], 0);
    }
}
