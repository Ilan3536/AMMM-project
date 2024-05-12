package edu.upc.fib.ammm;

public class Solution {
    // 0 means empty
    // 1 is first object, 2 second etc.
    private final int[][] matrix;

    Solution(int[][] matrix) {
        this.matrix = matrix;
    }

    // Print matrix with A,B,C instead of 1,2,3...
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int charStart = 64;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0)
                    builder.append(" ");
                else
                    builder.append((char) (charStart + matrix[i][j]));
            }
        }
        return builder.toString();
    }
}
