package net.epzilla.stratification.graph;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;


public class Stratifier {

    DependancyGraph dependancyGraph = new DependancyGraph();

    public DependancyGraph getDependancyGraph() {
        return dependancyGraph;
    }

    ArrayList<ArrayList<Integer>> strataList = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> globalIdList = new ArrayList<ArrayList<Integer>>();

    /**
     * does the stratification.
     *
     * @return ArrayList containing stratas.
     */
    public ArrayList<ArrayList<Integer>> stratify() {
        strataList = new ArrayList<ArrayList<Integer>>();
        globalIdList = new ArrayList<ArrayList<Integer>>();

        dependancyGraph.buildGraph();
        boolean[][] graph = dependancyGraph.getGraph();
        ArrayList<Query> queryList = dependancyGraph.getQueries();

        boolean[] added = new boolean[graph.length];

        boolean changed = true;
        do {
            ArrayList<Integer> stratum = new ArrayList<Integer>();
            ArrayList<Integer> idList = new ArrayList<Integer>();

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
                        stratum.add(i);
                        idList.add(queryList.get(i).getId());
                        added[i] = true;
                    }
                }
            }
            if (stratum.size() > 0) {
                strataList.add(stratum);
                globalIdList.add(idList);
                changed = true;
                // changing the dependencies...
                for (int x : stratum) {
                    for (int y = 0; y < graph.length; y++) {
                        graph[y][x] = false;
                    }
                }
            } else {
                changed = false;
            }
        } while (changed);
        ArrayList<ArrayList<Integer>> strataQueryIds = new ArrayList<ArrayList<Integer>>();
        
        return strataList;
    }



    public ArrayList<ArrayList<Integer>> getStrataIdMap() {
        return this.globalIdList;
    }

    public void addQuery(Query q) {
        dependancyGraph.addQuery(q);
    }

    public int getStratumFor(Query q) {
        int i = 0;
        for (ArrayList<Integer> ls : strataList) {
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
