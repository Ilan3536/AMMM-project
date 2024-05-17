package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;

public class GRASP extends Heuristic {
    public GRASP(Problem p) {
        super(p);
    }

    public Solution run() {
        return new Solution(new char[0][0]);
    }
}
