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
    private int price;
    private int weight;

    public List<Product> products;

    public Solution(char[][] box){
        this.box = box;
        this.products = new ArrayList<>();
        this.price = calculatePrice();
        this.weight = calculateWeight();
    }

    public Solution(char[][] box, List<Product> products) {
        this.box = box;
        this.products = new ArrayList<>(products);
        this.price = calculatePrice();
        this.weight = calculateWeight();
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

    public Solution cloneSolution() {
        char[][] box = this.box;
        char[][] newBox = new char[box.length][];
        for (int i = 0; i < box.length; i++) {
            newBox[i] = box[i].clone();
        }
        return new Solution(newBox, this.products);
    }

    public void swapProducts(Product p1, Product p2) {

        Position position1 = this.findProductPosition(p1);
        Position position2 = this.findProductPosition(p2);

        if (position1 != null && position2 != null) {

            this.clearProductFromPosition(p1, position1);
            this.clearProductFromPosition(p2, position2);

            this.placeProductOnPosition(p2, position1);
            this.placeProductOnPosition(p1, position2);
        }

    }

    public Position findProductPosition(Product p) {
        char[][] box = this.box;
        for (int x = 0; x < box.length; x++) {
            for (int y = 0; y < box[x].length; y++) {
                if (box[x][y] == p.letter) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }
    public boolean canSwapProducts(Product p1, Product p2) {

        Position position1 = findProductPosition(p1);
        Position position2 = findProductPosition(p2);

        if (position1 == null || position2 == null) return false;

        int maxX = Math.max(position1.x + p2.getSide(), position2.x + p1.getSide());
        int maxY = Math.max(position1.y + p2.getSide(), position2.y + p1.getSide());

        return maxX <= box.length && maxY <= box[0].length;
    }
    private void clearProductFromPosition(Product p, Position position) {

        for (int x = position.x; x < position.x + p.side; x++) {
            for (int y = position.y; y < position.y + p.side; y++) {
                this.box[x][y] = '\0';
            }
        }

        products.remove(p);
    }
    public void placeProductOnPosition(Product product, Position pos) {
        for (int x = pos.x; x < pos.x + product.side; x++) {
            for (int y = pos.y; y < pos.y + product.side; y++) {
                this.box[x][y] = product.letter;
            }
        }
        products.add(product);
    }


    public void calculatePriceAndWeight(){
        calculatePrice();
        calculateWeight();
    }
    public int calculatePrice() {
        int totalPrice = 0;
        for (var product : products) {
            if (isProductPlaced(product.letter)) {
                totalPrice += product.price;
            }
        }
        this.price = totalPrice;
        return totalPrice;
    }

    public int calculateWeight() {
        int totalWeight = 0;
        for (var product : products) {
            if (isProductPlaced(product.letter)) {
                totalWeight += product.weight;
            }
        }
        this.weight = totalWeight;
        return totalWeight;
    }



    public boolean isValid(Problem p) {
        int totalWeight = 0;
        for (Product product : products) {
            totalWeight += product.getWeight();
        }
        if (totalWeight > p.box.maxWeight()) {
            return false;
        }

        int boxWidth = p.box.width();
        int boxHeight = p.box.height();
        for (Product product : products) {
            Position pos = findProductPosition(product);
            if (pos.x + product.getSide() > boxWidth || pos.y + product.getSide() > boxHeight) {
                return false;
            }
        }


        return true;
    }


    public boolean containsProduct(Product product) {
        for (Product p : products) {
            if (p.equals(product)) {
                return true;
            }
        }
        return false;
    }

}
