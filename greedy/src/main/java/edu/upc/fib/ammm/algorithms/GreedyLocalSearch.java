package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GreedyLocalSearch extends Heuristic {

    public GreedyLocalSearch(Problem p) {
        super(p);
    }

    @Override
    public Solution run() {
        Heuristic greedy = new Greedy(problem);
        Solution greedySolution = greedy.run();
        log.debug("Start GreedyLocalSearch with initial solution cost: {}", greedySolution.getCost());

        return firstImprovingStrategy(greedySolution);
    }

    public Solution firstImprovingStrategy(Solution initialSolution) {
        Solution bestSolution = initialSolution;
        boolean improved = true;

        while (improved) {
            improved = false;

            for (var selectdProduct : bestSolution.getSelectedProducts()) {

                var newProductList = new ArrayList<>(bestSolution.getSelectedProducts());
                newProductList.remove(selectdProduct); // Explore alternative solutions without the selected product

                var unselectedProductList = this.problem.getProducts().stream()
                    .filter(p -> !newProductList.contains(p))
                    .toList();

                for (var unselectedProduct : unselectedProductList) {
                    newProductList.add(unselectedProduct); // Add the new product to the list
                    Solution newSolution = findSolutionForProductList(newProductList);

                    if (newSolution.getCost() > bestSolution.getCost()) {
                        bestSolution = newSolution;
                        improved = true;
                        break;
                    }
                    newProductList.remove(unselectedProduct); // undo the change
                }

                if (improved) {
                    log.debug("Found a better solution with cost: {}", bestSolution.getCost());
                    break;
                }
            }
        }

        return bestSolution;
    }

    private Solution findSolutionForProductList(List<Product> productList) {
        var newProblem = new Problem(this.problem.getBox(), productList);
        return new Greedy(newProblem).run();
    }
}
