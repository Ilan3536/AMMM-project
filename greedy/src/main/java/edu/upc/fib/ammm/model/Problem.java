package edu.upc.fib.ammm.model;

import java.util.ArrayList;
import java.util.List;

public class Problem {

    public Solution solution;
    public List<Product> allProducts;

    public Problem(int x, int y, int c, int n, int[] w, int[] s, int[] p) {
        this.solution = new Solution(x, y, c);
        this.allProducts = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            this.allProducts.add(new Product(w[i], s[i], p[i], (char) ('A' + i)));
        }
    }

    public Problem(int x, int y, int c, List<Product> products){
        this.solution = new Solution(x, y, c);
        this.allProducts = new ArrayList<>(products);
    }

    public Problem cloneWithSubset(List<Product> newSelected) {
        return new Problem(this.solution.getWidth(), this.solution.getHeight(), this.solution.getMaxWeight(), newSelected);
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
