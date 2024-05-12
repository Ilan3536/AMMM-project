package edu.upc.fib.ammm;

abstract class Heuristic {
    private final Problem p;

    Heuristic(Problem p) {
        this.p = p;
    }

    abstract public Solution run();
}