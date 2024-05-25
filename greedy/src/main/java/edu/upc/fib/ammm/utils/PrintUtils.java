package edu.upc.fib.ammm.utils;

import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;

import java.util.List;

public final class PrintUtils {

    private PrintUtils() {
    }

    public static void printProducts(List<Product> products) {

        StringBuilder productDetails = new StringBuilder();

        productDetails.append("Products:                ");
        products.forEach(product -> productDetails.append(String.format("%2s ", product.letter())));
        productDetails.append("\n");

        productDetails.append("Product sizes:           ");
        products.forEach(product -> productDetails.append(String.format("%2d ", product.side())));
        productDetails.append("\n");

        productDetails.append("Product prices:          ");
        products.forEach(product -> productDetails.append(String.format("%2d ", product.price())));
        productDetails.append("\n");

        productDetails.append("Product weights:         ");
        products.forEach(product -> productDetails.append(String.format("%2d ", product.weight())));
        productDetails.append("\n");

        System.out.println(productDetails);
    }

    public static void printSolution(Solution sol, String algorithm, boolean printBox) {
        if (sol.getBoxDimension().length == 0) return;

        var solutionString = new StringBuilder();
        solutionString.append(String.format("Solution Summary (%s)%n", algorithm));
        solutionString.append(String.format("==============================%n"));
        solutionString.append(String.format("(Box Dimensions: %d (x) x %d (y), Capacity: %d)%n",
            sol.getBoxDimension().length,
            sol.getBoxDimension()[0].length,
            sol.getBox().maxWeight()));

        if (printBox) {
            for (char[] row : sol.getBoxDimension()) {
                for (char cell : row) {
                    solutionString.append(cell == '\u0000' ? '_' : cell);  // If cell is empty, print underscore
                }
                solutionString.append("\n");
            }
        }

        solutionString.append("\n");
        solutionString.append(String.format("Total weight: %d\n", sol.getWeight()));
        solutionString.append(String.format("Total price (OBJECTIVE FUNCTION): %d\n", sol.getCost()));

        System.out.println(solutionString);
    }
}
