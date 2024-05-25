package edu.upc.fib.ammm.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

import static java.util.Collections.unmodifiableList;

@Getter
public final class Problem {

    private final Box box;
    private final List<Product> products;

    public Problem(int width, int height, int maxWeight, int n, int[] weights, int[] sizes, int[] prices) {
        this.box = new Box(width, height, maxWeight);
        List<Product> productList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            productList.add(new Product(weights[i], sizes[i], prices[i], (char) ('A' + i)));
        }
        this.products = unmodifiableList(productList);
    }

    public Problem(Box box, List<Product> products) {
        this.box = box;
        this.products = List.copyOf(products);
    }

    public int getN() {
        return products.size();
    }

    public String toString() {
        return this.toDat();
    }

    public int[] getP() {
        return getArray(Product::price);
    }

    public int[] getW() {
        return getArray(Product::weight);
    }

    public int[] getS() {
        return getArray(Product::side);
    }

    private int[] getArray(ToIntFunction<Product> productToIntFunction) {
        return products.stream().mapToInt(productToIntFunction).toArray();
    };

    private String toDat() {
        var p = getP();
        var w = getW();
        var s = getS();

        return """
            x = %d;
            y = %d;
            c = %d;
            n = %d;

            p = %s;
            w = %s;
            s = %s;
            """.formatted(box.width(), box.height(), box.maxWeight(), getN(), Arrays.toString(p).replace(',', ' '), Arrays.toString(w).replace(',', ' '), Arrays.toString(s).replace(',', ' '));
    }
}
