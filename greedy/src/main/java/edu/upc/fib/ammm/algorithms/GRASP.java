package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter
public class GRASP extends Heuristic {
    private final int maxIterations;
    private final double alpha;
    private final Random random;

    public GRASP(Problem p, int maxIterations, double alpha) {
        super(p);
        this.maxIterations = maxIterations;
        this.alpha = alpha;
        this.random = new Random();
    }

    @Override
    public Solution run() {
        Solution bestSolution = null;

        for (int iteration = 0; iteration < maxIterations; iteration++) {

            Solution greedySolution = constructGreedyRandomizedSolution();
            Solution improvedSolution = new GreedyLocalSearch(p).firstImprovingStrategy(greedySolution);

            if (bestSolution == null || improvedSolution.getCost() > bestSolution.getCost()) {
                bestSolution = improvedSolution;
            }
        }

        return bestSolution;
    }

    private Solution constructGreedyRandomizedSolution() {
        List<Product> products = new ArrayList<>(p.allProducts);

        Collections.sort(products, (p1, p2) -> Double.compare(
                (double) p2.price / p2.side / p2.weight,
                (double) p1.price / p1.side / p1.weight
        ));

        Solution solution = new Solution(p.solution.getWidth(), p.solution.getHeight(), p.solution.getMaxWeight());


        int totalWeight = 0;
        int currentWidth = 0;
        int currentHeight = 0;
        int nextRowHeight = 0;

        while (!products.isEmpty()) {

            //[qmin, qmin + α(qmax - qmin)]
            int qmin = 0;
            int qmax = (int) (alpha * (products.size() - 1));

            // Randomly select a product within the range
            int index = random.nextInt(qmax - qmin + 1) + qmin;
            Product product = products.get(index);
            products.remove(index);

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
            solution.selectedProducts.add(product);
        }

        solution.calculatePriceAndWeight();
        return solution;
    }
}
