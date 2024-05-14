package edu.upc.fib.ammm;

import java.util.Collections;

public class Greedy extends Heuristic {
    Greedy(Problem p) {
        super(p);
    }

    public Solution run() {
        var solution = new char[p.box.width()][p.box.height()];

        // Sort products based on a compound heuristic of price to side ratio divided by weight
        Collections.sort(p.products, (p1, p2) -> Double.compare(
            (double) p2.price() / p2.side() / p2.weight(),
            (double) p1.price() / p1.side() / p1.weight()
        ));
        System.out.println("The sorted products are: " + p.products);

        int totalPrice = 0;
        int totalWeight = 0;
        int currentWidth = 0;
        int currentHeight = 0;
        int nextRowHeight = 0;

        var box = p.box;
        for (var product : p.products) {
            if (currentWidth + product.side() <= box.width() && currentHeight + product.side() <= box.height()
                && totalWeight + product.weight() <= box.maxWeight()) {
                // Place the product in the current position
                for (int y = currentHeight; y < currentHeight + product.side(); ++y) {
                    for (int x = currentWidth; x < currentWidth + product.side(); ++x) {
                        solution[y][x] = product.letter();
                    }
                }
                currentWidth += product.side();
                nextRowHeight = Math.max(nextRowHeight, product.side());
            } else if (currentHeight + nextRowHeight + product.side() <= box.height()
                && totalWeight + product.weight() <= box.maxWeight()) {
                // Start a new row
                currentHeight += nextRowHeight;
                currentWidth = 0;
                // Place the product starting at the new row
                for (int y = currentHeight; y < currentHeight + product.side(); ++y) {
                    for (int x = currentWidth; x < currentWidth + product.side(); ++x) {
                        solution[y][x] = product.letter();
                    }
                }
                currentWidth = product.side();
                nextRowHeight = product.side();
            } else {
                continue;  // Skip this product as it doesn't fit
            }

            totalWeight += product.weight();
            totalPrice += product.price();
        }

        printSolution(solution, totalPrice, totalWeight, p);

        return new Solution(solution, totalPrice);
    }

    private void printSolution(char[][] solution, int totalPrice, int totalWeight, Problem p) {
        System.out.println("Greedy solution (Box w: " + p.box.width() + ", h: " + p.box.height() + ", maxWeight: " + p.box.maxWeight() + "):");
        for (char[] row : solution) {
            System.out.println(new String(row));
        }
        System.out.println("Total price: " + totalPrice + ", total weight: " + totalWeight);
    }
}
