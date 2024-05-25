package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Solution;

import java.util.ArrayList;

public class Greedy extends Heuristic {
    public Greedy(Problem p) {
        super(p);
    }

    public Solution run() {
        // Sort products based on a compound heuristic of price to side ratio divided by weight
        var products = new ArrayList<>(this.problem.getProducts());
        products.sort((p1, p2) -> Double.compare(p2.getQValue(), p1.getQValue()));

        int currentWidth = 0;
        int currentHeight = 0;
        int nextRowHeight = 0;
        var solution = new Solution(problem.getBox());

        for (var product : products) {
            if (solution.canPlaceProductInCurrentRow(currentWidth, currentHeight, product)) {
                // If the product fits in the current row, place it there
                solution.placeProductOnPosition(product, currentWidth, currentHeight);

                currentWidth += product.side();
                nextRowHeight = Math.max(nextRowHeight, product.side());
            } else if (solution.canPlaceProductInNewRow(currentHeight, nextRowHeight, product)) {
                // If the product doesn't fit in the current row, move to the next row
                currentHeight += nextRowHeight;
                currentWidth = 0;

                solution.placeProductOnPosition(product, currentWidth, currentHeight);

                currentWidth = product.side();
                nextRowHeight = product.side();
            }
        }

        return solution;
    }
}
