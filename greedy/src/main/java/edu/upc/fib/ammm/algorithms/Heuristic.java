package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;

abstract public class Heuristic {
    final Problem p;

    Heuristic(Problem p) {
        this.p = p;
    }

    abstract public Solution run();
}