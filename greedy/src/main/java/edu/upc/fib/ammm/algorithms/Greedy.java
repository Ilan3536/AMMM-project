package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Greedy extends Heuristic {
    public Greedy(Problem p) {
        super(p);
    }

    public Solution run() {
        List<Product> products = new ArrayList<>();

        // Sort products based on a compound heuristic of price to side ratio divided by weight
        Collections.sort(p.allProducts, (p1, p2) -> Double.compare(
                (double) p2.price / p2.side / p2.weight,
                (double) p1.price / p1.side / p1.weight
        ));


        int totalWeight = 0;
        int currentWidth = 0;
        int currentHeight = 0;
        int nextRowHeight = 0;


        for (var product : p.allProducts) {
            if (currentWidth + product.side <= p.solution.getWidth() && currentHeight + product.side <= p.solution.getHeight()
                    && totalWeight + product.weight <= p.solution.getMaxWeight()) {

                p.solution.placeProductOnPosition(product, currentWidth, currentHeight);

                currentWidth += product.side;
                nextRowHeight = Math.max(nextRowHeight, product.side);
            } else if (currentHeight + nextRowHeight + product.side <= p.solution.getHeight()
                    && totalWeight + product.weight <= p.solution.getMaxWeight()) {
                // Start a new row
                currentHeight += nextRowHeight;
                currentWidth = 0;

                p.solution.placeProductOnPosition(product, currentWidth, currentHeight);

                currentWidth = product.side;
                nextRowHeight = product.side;
            } else {
                continue;  // Skip this product as it doesn't fit
            }

            totalWeight += product.weight;
            products.add(product);
        }

        p.solution.selectedProducts = new ArrayList<>(products);
        p.solution.calculatePriceAndWeight();
        return p.solution;
    }

}
