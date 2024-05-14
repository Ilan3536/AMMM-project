package edu.upc.fib.ammm;

abstract class Heuristic {
    final Problem p;

    Heuristic(Problem p) {
        this.p = p;
    }

    abstract public Solution run();
}