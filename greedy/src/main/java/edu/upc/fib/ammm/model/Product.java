package edu.upc.fib.ammm.model;

public record Product(int weight, int side, int price, char letter) {

    public double getQValue() {
        return (double) price / side / weight;
    }
}
