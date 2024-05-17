package edu.upc.fib.ammm.algorithms;


import edu.upc.fib.ammm.model.Problem;
import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;

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

    public Solution localSearch(Solution solution) {
        boolean improved = true;
        Product p1, p2;

        while (improved) {
            improved = false;
            for (int i = 0; i < solution.getProducts().size(); i++) {

                p1 = solution.getProducts().get(i);
                for (int j = i + 1; j < solution.getProducts().size(); j++) {

                    p2 = solution.getProducts().get(j);
                    if (!solution.canSwapProducts(p1, p2)) continue;

                    Solution neighborSolution = solution.cloneSolution();
                    neighborSolution.swapProducts(p1, p2);

                    // Evaluate the neighbor solution
                    if (neighborSolution.isValid(p)) {
                        int neighborTotalPrice = neighborSolution.calculatePrice();
                        int currentTotalPrice = solution.calculatePrice();

                        // If the neighbor solution improves upon the current solution, update the current solution
                        if (neighborTotalPrice > currentTotalPrice) {
                            solution = neighborSolution.cloneSolution();
                            improved = true;
                        }
                    }
                }
            }
        }

        return solution;
    }
    public Solution firstImprovingStrategy(Solution initialSolution) {
        Solution currentSolution = initialSolution.cloneSolution();
        int currentCost = currentSolution.getPrice();
        Solution bestSolution = currentSolution.cloneSolution();

        for (Product selectedProduct : currentSolution.getProducts()) {
            for (Product unselectedProduct : p.products) {

                if (currentSolution.containsProduct(unselectedProduct)) continue;
                if (!currentSolution.canSwapProducts(selectedProduct, unselectedProduct)) continue;

                Solution newSolution = currentSolution.cloneSolution();
                newSolution.swapProducts(selectedProduct, unselectedProduct);

                newSolution.calculatePriceAndWeight();
                int newCost = newSolution.getPrice();
                System.out.println("p1: " + selectedProduct.letter + ", p2: " + unselectedProduct.letter + ", " +
                        "new price: " + newCost + ", old price: " + currentCost);
                if (newCost > currentCost) {
                    bestSolution = newSolution.cloneSolution();
                    currentCost = newCost;
                }
            }

        }

        return bestSolution;
    }




}
