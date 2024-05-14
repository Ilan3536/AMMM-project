package edu.upc.fib.ammm;

public class Solution {
    private final char[][] box;
    private final int cost;

    Solution(char[][] box, int cost) {
        this.box = box;
        this.cost = cost;
    }

    // Print matrix with A,B,C instead of 1,2,3...
    public String toString() {
        return "";
//        StringBuilder builder = new StringBuilder();
//        int charStart = 64;
//        for(int i = 0; i < box.length; i++) {
//            for(int j = 0; j < box[i].length; j++) {
//                if (box[i][j] == 0)
//                    builder.append(" ");
//                else
//                    builder.append((char) (charStart + box[i][j]));
//            }
//        }
//        return builder.toString();
    }
}
