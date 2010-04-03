package net.epzilla.stratification.graph;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;


public class DependancyGraph  {
    String nullset = "<<nullset>>";

    // graph[x][y] == true  : means that query x depends on query y.
    boolean[][] graph;
    ArrayList<Query> queries = new ArrayList<Query>(100);
    Hashtable<String, Hashtable<String, HashSet<Integer>>> queryOutputsMap = new Hashtable<String, Hashtable<String, HashSet<Integer>>>();

    public void addQuery(Query query) {
        this.queries.add(query);
    }

    public void removeQuery(Query q) {
        //To change body of implemented methods use File | Settings | File Templates.
        this.queries.remove(q);
    }


    public ArrayList<Query> getQueries() {
        return queries;
    }

    public boolean[][] getGraph() {
        return graph;
    }

    public void buildGraph() {
        int size = queries.size();   // last place to indicate dependancy on raw input - i.e. no dependency on queries.
        graph = new boolean[size][size];
        buildOutputMap();
        findDependencies();
//        System.out.println("graph built");
    }

    private void findDependencies() {

        for (int i = 0; i < queries.size(); i++) {
            String[][] inmap = queries.get(i).getInputs();
            for (String[] item : inmap) {
                String param = item[1];
                if (param == null) {
                    param = nullset;
                }
//                System.out.println("fetching: " + item[0] + "  : " + param);
                HashSet<Integer> outmap = this.getOutputIndexSet(item[0], param);
                if (outmap != null) {
                    for (Integer x : outmap) {
                        this.graph[i][x] = true;
                    }
                } else {

                }
            }
        }
//        System.out.println("");
    }

    private void buildOutputMap() {
        for (int i = 0; i < queries.size(); i++) {
            String[][] outmap = queries.get(i).getOutputs();
            for (String[] item : outmap) {
                String param = item[1];
                if (param == null) {
                    param = nullset;
                }
                addOutputIndex(item[0], param, i);
//                System.out.println("adding output index");
            }
        }
    }

    private void addOutputIndex(String event, String param, Integer index) {
        Hashtable<String, HashSet<Integer>> eventMap = queryOutputsMap.get(event);
        if (eventMap == null) {
            eventMap = new Hashtable<String, HashSet<Integer>>();
            queryOutputsMap.put(event, eventMap);
        }
        HashSet<Integer> indexMap = eventMap.get(param);
        if (indexMap == null) {
            indexMap = new HashSet<Integer>();
            eventMap.put(param, indexMap);
        }
        indexMap.add(index);
    }

    private HashSet<Integer> getOutputIndexSet(String event, String param) {
        Hashtable<String, HashSet<Integer>> paramMap = queryOutputsMap.get(event);
        if (paramMap != null) {
            return paramMap.get(param);
        }
        return null;
    }
}
