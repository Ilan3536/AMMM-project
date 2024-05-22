package edu.upc.fib.ammm.utils;

import edu.upc.fib.ammm.model.Product;
import edu.upc.fib.ammm.model.Solution;

import java.util.List;

public class PrintUtils {

    public static void printProducts(List<Product> products){
        StringBuilder sortedProductLetters = new StringBuilder();
        StringBuilder sortedProductSizes = new StringBuilder();
        StringBuilder sortedProductPrices = new StringBuilder();
        StringBuilder sortedProductWeights = new StringBuilder();

        for (Product product : products) {
            sortedProductLetters.append(product.getLetter()).append(" ");
            sortedProductSizes.append(product.getSide()).append(" ");
            sortedProductPrices.append(product.getPrice()).append(" ");
            sortedProductWeights.append(product.getWeight()).append(" ");
        }

        System.out.println("The sorted products are: " + sortedProductLetters);
        System.out.println("Product sizes:           " + sortedProductSizes);
        System.out.println("Product prices:          " + sortedProductPrices);
        System.out.println("Product weights:         " + sortedProductWeights);
        System.out.println("\n");

    }

    public static void printSolution(Solution sol) {
        if (sol.getBox().length == 0) return;

        StringBuilder solutionString = new StringBuilder();
        solutionString.append("(Box w: ").append(sol.getBox().length)
                .append(", h: ").append(sol.getBox()[0].length)
                .append(", maxWeight: ").append(sol.getMaxWeight()).append("):\n");
        for (char[] row : sol.getBox()) {
            for (char cell : row) {
                solutionString.append(cell == '\u0000' ? '_' : cell); // If cell is empty, print space
            }
            solutionString.append("\n");
        }
        solutionString.append("Total price: ").append(sol.getCost()).append(", total weight: ").append(sol.getWeight()).append("\n");

        System.out.println(solutionString);
    }


}
