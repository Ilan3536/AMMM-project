package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;

abstract public class Heuristic {
    final Problem problem;

    Heuristic(Problem problem) {
        this.problem = problem;
    }

    abstract public Solution run();

    public String toString() {
        return this.getClass().getSimpleName();
    }
}