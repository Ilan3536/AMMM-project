package edu.upc.fib.ammm.model;

public record PerformanceData(String name, Solution solution, float time) {
    @Override
    public String toString() {
        return "PerformanceData{" +
                "name='" + name + '\'' +
                ", objective=" + solution.getCost() +
                ", time=" + time +
                '}';
    }
}
