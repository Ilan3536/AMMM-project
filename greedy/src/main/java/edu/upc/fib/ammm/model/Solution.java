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

    private final char[][] box;
    private int width;
    private int height;
    private int cost;
    private int weight;
    private int maxWeight;
    public List<Product> selectedProducts;

    public Solution(int x, int y, int maxWeight){
        this.width = x;
        this.height = y;
        this.box = new char[width][height];
        this.maxWeight = maxWeight;
        this.selectedProducts = new ArrayList<>();
    }


    private boolean isProductPlaced(char letter) {
        for (char[] row : this.box) {
            for (char cell : row) {
                if (cell == letter) {
                    return true;
                }
            }
        }
        return false;
    }

    public void placeProductOnPosition(Product product, int width, int height) {
        for (int x = width; x < width + product.side; x++) {
            for (int y = height; y < height + product.side; y++) {
                this.box[x][y] = product.letter;
            }
        }
        selectedProducts.add(product);
    }

    public void calculatePriceAndWeight() {
        calculateCost();
        calculateWeight();
    }

    public int calculateCost() {
        int totalPrice = 0;
        for (var product : selectedProducts) {
            if (isProductPlaced(product.letter)) {
                totalPrice += product.price;
            }
        }
        this.cost = totalPrice;
        return totalPrice;
    }

    public int calculateWeight() {
        int totalWeight = 0;
        for (var product : selectedProducts) {
            if (isProductPlaced(product.letter)) {
                totalWeight += product.weight;
            }
        }
        this.weight = totalWeight;
        return totalWeight;
    }


    public List<Product> removeProduct(Product product) {
        List<Product> newProductList = new ArrayList<>(selectedProducts);
        newProductList.remove(product);
        return newProductList;
    }

}
