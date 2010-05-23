package net.epzilla.util;

import java.util.Arrays;

public class DisjointSets {

    private int[] struct = null;

    public DisjointSets(int size) {
        struct = new int[size];
        Arrays.fill(struct, -1);
    }



    // returns the root of element.
    public int find(int target) {
        if (struct[target] < 0) {
            return target;
        } else {
            struct[target] = find(struct[target]);
            return struct[target];                                       // Return the root
        }
    }


    // both must be roots of relevant sets.
    // nothing happens if two roots are equal.
    public void union(int rootOne, int rootTwo) {
        if (rootOne != rootTwo) {
            if (struct[rootTwo] < struct[rootOne]) {
                struct[rootOne] = rootTwo;
            } else if (struct[rootOne] == struct[rootTwo]) {

                struct[rootOne]--;
                struct[rootTwo] = rootOne;
            } else {
                struct[rootTwo] = rootOne;
            }
        }
    }

    // returns a printable version of internal array.
    public String toString() {
        return Arrays.toString(this.struct);
    }

    public static void main(String[] args) {
        DisjointSets ds = new DisjointSets(12);
        ds.union(1, 2);
        ds.union(1, 4);
        ds.union(ds.find(4), 1);
        System.out.println(ds.find(1));
    }
}
