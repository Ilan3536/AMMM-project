package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GreedyLocalSearch extends Heuristic {

    public enum Strategy {
        TASK_EXCHANGE,
        REASSIGNMENT
    }

    private final boolean firstImprovement; // first improvement or best improvement
    private final Strategy strategy;

    public static final boolean defaultFirstImprovement = true;
    public static final Strategy defaultStrategy = Strategy.REASSIGNMENT;

    public GreedyLocalSearch(Problem p) {
        this(p, GreedyLocalSearch.defaultFirstImprovement, GreedyLocalSearch.defaultStrategy);
    }

    public GreedyLocalSearch(Problem p, boolean firstImprovement, Strategy strategy) {
        super(p);
        this.firstImprovement = firstImprovement;
        this.strategy = strategy;
    }

    @Override
    public Solution run() {
        Heuristic greedy = new Greedy(problem);
        Solution greedySolution = greedy.run();
        log.trace("Start GreedyLocalSearch with initial solution cost: {}", greedySolution.getCost());

        return solve(greedySolution);
    }

    public Solution solve(Solution initialSolution) {
        Solution bestSolution = initialSolution;
        boolean improved = true;

        while (improved) {
            improved = false;
            Solution newSolution = exploreNeighborhood(bestSolution);

            if (newSolution.getCost() > bestSolution.getCost()) {
                log.trace("Found a better solution with cost: {}, before cost: {}", newSolution.getCost(), bestSolution.getCost());
                bestSolution = newSolution;
                improved = true;
                if (firstImprovement) {
                    return bestSolution;
                }
            }
        }

        return bestSolution;
    }

    private Solution exploreNeighborhood(Solution initialSolution) {
        return switch (strategy) {
            case TASK_EXCHANGE -> exploreExchange(initialSolution);
            case REASSIGNMENT -> exploreReassignment(initialSolution);
        };
    }

    private Solution exploreExchange(Solution bestSolution) {
        List<Product> selectedProducts = bestSolution.getSelectedProducts();
        Solution currentBestSolution = bestSolution;

        // Explore a possible exchange starting from the worst product
        for (int i = selectedProducts.size() - 1; i >= 0; i--) {
            var selectedProduct = selectedProducts.get(i);
            var newProductList = new ArrayList<>(selectedProducts);
            newProductList.remove(selectedProduct);

            // Explore a possible exchange with a better product
            var unselectedProductList = this.problem.getProducts().stream()
                    .filter(p -> !selectedProducts.contains(p) && p.price() > selectedProduct.price())
                    .sorted((p1, p2) -> Double.compare(p2.getQValue(), p1.getQValue())) // Sort by Q-value descending
                    .toList();

            for (var unselectedProduct : unselectedProductList) {
                newProductList.add(unselectedProduct);
                // Explore a possible exchange with the unselected product and the selected product
                Solution newSolution = findSolutionForProductList(newProductList);
                if (newSolution.getCost() > currentBestSolution.getCost()) {
                    currentBestSolution = newSolution;

                    if (firstImprovement) {
                        return currentBestSolution;
                    }
                }
                newProductList.remove(unselectedProduct); // undo the exchange
            }
        }
        return currentBestSolution;
    }

    private Solution exploreReassignment(Solution bestSolution) {
        List<Product> selectedProducts = bestSolution.getSelectedProducts();
        Solution currentBestSolution = bestSolution;

        for (int i = selectedProducts.size() - 1; i >= 0; i--) {
            var selectedProduct = selectedProducts.get(i);
            var newProductList = new ArrayList<>(problem.getProducts());
            newProductList.remove(selectedProduct); // Remove the selected product

            // Explore a possible reassignment without the selected product
            Solution newSolution = findSolutionForProductList(newProductList);
            if (newSolution.getCost() > currentBestSolution.getCost()) {
                currentBestSolution = newSolution;

                if (firstImprovement) {
                    return currentBestSolution;
                }
            }
        }
        return currentBestSolution;
    }

    private Solution findSolutionForProductList(List<Product> productList) {
        var newProblem = new Problem(this.problem.getBox(), productList);
        return new Greedy(newProblem).run();
    }

    @Override
    public String toString() {
        return "GreedyLocalSearch{" +
                "firstImprovement=" + firstImprovement +
                ", strategy=" + strategy +
                '}';
    }
}