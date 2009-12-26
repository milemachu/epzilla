package net.epzilla.stratification.graph;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Dec 2, 2009
 * Time: 7:36:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Stratifier {

    /**
     * does the stratification.
     *
     * @param graph graph with dependecies marked.
     * @return ArrayList containing stratas.
     */
    public static ArrayList<ArrayList<Integer>> stratify(boolean[][] graph) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        boolean[] added = new boolean[graph.length];

        boolean changed = true;
        do {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int i = 0; i < graph.length; i++) {
                if (!added[i]) {
                    boolean depends = false;
                    for (int j = 0; j < graph[i].length; j++) {
                        if (graph[i][j]) {
                            depends = true;
                            break;
                        }
                    }
                    if (!depends) {
                        temp.add(i);
                        added[i] = true;
                    }
                }
            }
            if (temp.size() > 0) {
                list.add(temp);
                changed = true;
                // changing the dependencies...
                for (int x : temp) {
                    for (int y = 0; y < graph.length; y++) {
                        graph[y][x] = false;
                    }
                }
            } else {
                changed = false;
            }
        } while (changed);

        return list;
    }
}
