package edu.upc.fib.ammm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Problem {

    record Box(int width, int height, int maxWeight) {
    }

    record Product(int weight, int side, int price, char letter) {
    }

    public final Box box;
    public final List<Product> products;

    Problem(int x, int y, int c, int n, int[] w, int[] s, int[] p) {
        this.box = new Box(x, y, c);
        this.products = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.products.add(new Product(w[i], s[i], p[i], (char) ('A' + i)));
        }
    }

//    public String toString() {
//        return this.toDat();
//    }
//
//    public String toDat() {
//        return """
//            x = %d;
//            y = %d;
//            c = %d;
//            n = %d;
//
//            p = %s;
//            w = %s;
//            s = %s;
//            """.formatted(x, y, c, n, Arrays.toString(p).replace(',', ' '), Arrays.toString(w).replace(',', ' '), Arrays.toString(s).replace(',', ' '));
//    }
}
