package edu.upc.fib.ammm.algorithms;

import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;

import java.util.List;

public class GreedyLocalSearch extends Heuristic {
    public GreedyLocalSearch(Problem p) {
        super(p);
    }

    @Override
    public Solution run() {
        Heuristic greedy = new Greedy(p);
        Solution greedySolution = greedy.run();

        return firstImprovingStrategy(greedySolution);
    }

    public Solution firstImprovingStrategy(Solution initialSolution) {
        Solution bestSolution = initialSolution;
        boolean improved = true;

        while (improved) {
            improved = false;
            for (var product : bestSolution.selectedProducts) {

                //remove one product
                List<Product> newProductList = bestSolution.removeProduct(product);

                for (Product unselectedProduct : p.allProducts) {
                    if (!newProductList.contains(unselectedProduct)) {

                        //add one product
                        newProductList.add(unselectedProduct);

                        Solution newSolution = findSolutionForProductList(newProductList);

                        if (newSolution != null && newSolution.getCost() > bestSolution.getCost()) {
                            bestSolution = newSolution;
                            improved = true;
                            break;
                        }
                        newProductList.remove(unselectedProduct);
                    }
                }
                if (improved) {
                    break;
                }
            }
        }
        bestSolution.calculatePriceAndWeight();
        return bestSolution;
    }

    public Solution findSolutionForProductList(List<Product> newProductList) {
        Problem newProblem = p.cloneWithSubset(newProductList);
        return new Greedy(newProblem).run();
    }
}
