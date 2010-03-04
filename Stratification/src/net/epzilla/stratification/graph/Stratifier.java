package net.epzilla.stratification.graph;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;


public class Stratifier {

    DependancyGraph dependancyGraph = new DependancyGraph();
    ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();

    /**
     * does the stratification.
     *
     * @return ArrayList containing stratas.
     */
    public ArrayList<ArrayList<Integer>> stratify() {

        dependancyGraph.buildGraph();
        boolean[][] graph = dependancyGraph.getGraph();

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

    public void addQuery(Query q) {
        dependancyGraph.addQuery(q);
    }

    public int getStratumFor(Query q) {
        int i = 0;
        for (ArrayList<Integer> ls : list) {
            for (Integer x : ls) {
                if (q.getId() == x) {
                    return i;
                }
            }
            i++;
        }
        return 0;
    }

    public void removeQuery(Query q) {
        dependancyGraph.removeQuery(q);
    }
}
