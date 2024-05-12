package edu.upc.fib.ammm;

import java.util.Arrays;

public class Problem {
    public final int x; // Width box
    public final int y; // Height box
    public final int c; // Maximum weight
    public final int n; // Number of objects
    public final int[] w; // Weights of the objects
    public final int[] s; // Sides length of the objects
    public final int[] p; // Prices of the objects

    Problem(int x, int y, int c, int n, int[] w, int[] s, int[] p) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.n = n;
        this.w = w;
        this.s = s;
        this.p = p;
    }

    public String toString() {
        return this.toDat();
    }

    public String toDat() {
        return """
                x = %d;
                y = %d;
                c = %d;
                n = %d;
                
                p = %s;
                w = %s;
                s = %s;
                """.formatted(x, y, c, n, Arrays.toString(p).replace(',', ' '), Arrays.toString(w).replace(',', ' '), Arrays.toString(s).replace(',', ' '));
    }
}
