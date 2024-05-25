package edu.upc.fib.ammm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Solution {

    private final char[][] boxDimension;
    private final Box box;
    private int cost;
    private int weight;
    private List<Product> selectedProducts;

    public Solution(Box box) {
        this.box = box;
        this.boxDimension = new char[box.width()][box.height()];
        this.selectedProducts = new ArrayList<>();
    }

    public void placeProductOnPosition(Product product, int width, int height) {
        for (int x = width; x < width + product.side(); x++) {
            for (int y = height; y < height + product.side(); y++) {
                this.boxDimension[x][y] = product.letter();
            }
        }
        cost += product.price();
        weight += product.weight();
        selectedProducts.add(product);
    }

    public boolean canPlaceProductInCurrentRow(int currentWidth, int currentHeight, Product product) {
        return currentWidth + product.side() <= box.width()
            && currentHeight + product.side() <= box.height()
            && weight + product.weight() <= box.maxWeight();
    }

    public boolean canPlaceProductInNewRow(int currentHeight, int nextRowHeight, Product product) {
        return currentHeight + nextRowHeight + product.side() <= box.height()
            && weight + product.weight() <= box.maxWeight();
    }
}
