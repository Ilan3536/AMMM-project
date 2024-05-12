package edu.upc.fib.ammm;

public class Greedy extends Heuristic {
    Greedy(Problem p) {
        super(p);
    }

    public Solution run() {
        return new Solution(new int[0][0]);
    }
}
