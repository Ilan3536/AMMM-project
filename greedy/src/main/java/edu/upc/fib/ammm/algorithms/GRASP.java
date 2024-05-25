package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Random;

@Slf4j
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
            Solution localOptimalSolution = new GreedyLocalSearch(problem)
                .firstImprovingStrategy(greedySolution);

            if (bestSolution == null || localOptimalSolution.getCost() > bestSolution.getCost()) {
                bestSolution = localOptimalSolution;
            }
        }

        return bestSolution;
    }

    private Solution constructGreedyRandomizedSolution() {
        var products = new ArrayList<>(problem.getProducts());
        products.sort((p1, p2) -> Double.compare(
            (double) p2.price() / p2.side() / p2.weight(),
            (double) p1.price() / p1.side() / p1.weight()
        ));

        var solution = new Solution(problem.getBox());
        int currentWidth = 0;
        int currentHeight = 0;
        int nextRowHeight = 0;

        while (!products.isEmpty()) {

            // [qmin, qmin + α(qmax - qmin)]
            int qmin = 0;
            int qmax = (int) (alpha * (products.size() - 1));
            int index = random.nextInt(qmax - qmin + 1) + qmin; // Randomly select a product within the range

            Product product = products.remove(index);

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

    @Override
    public String toString() {
        return "GRASP{" +
            "maxIterations=" + maxIterations +
            ", alpha=" + alpha +
            '}';
    }
}
