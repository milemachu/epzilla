package org.epzilla.clusterNode.parser;

import java.util.HashSet;


public class UnionFind {

    private HashSet<String> set = new HashSet<String>();

    public void add(String item) {
        this.set.add(item);
    }

    public void add(String[] items) {
        for (String item : items) {
            this.set.add(item);
        }
    }

    public String[] toArray(String firstColumn) {
        String[] out = new String[set.size()];
        boolean exist = set.remove(firstColumn);
        int i = 0;
        if (exist) {
            out[0] = firstColumn;
            i = 1;
        }

        for (String item : set) {
            out[i] = item;
            i++;
        }
        if (exist) {
            set.add(firstColumn);
        }
        return out;
    }


    public HashSet<String> getUnion() {
        return this.set;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String x: this.set) {
            sb.append(x).append(", ");
        }
        return sb.toString();
    }

}
