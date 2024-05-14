package edu.upc.fib.ammm;

public class GRASP extends Heuristic {
    GRASP(Problem p) {
        super(p);
    }

    public Solution run() {
        return new Solution(new char[0][0], 0);
    }
}
