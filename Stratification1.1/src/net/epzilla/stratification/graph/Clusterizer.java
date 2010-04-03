package net.epzilla.stratification.graph;

import net.epzilla.stratification.query.Query;

import java.util.*;


public class Clusterizer {

    public void clusterize(ArrayList<Integer> stratum, ArrayList<Query> queries) {
        HashSet<TreeSet<String>> disjointSets = new HashSet<TreeSet<String>>();

        for (Query q : queries) {
            if (q.getRetainCriterion() != Query.NO_RETAIN && stratum.contains(q.getId())) {
                String[] retainItems = q.getRetainingQueryTypes();
                HashSet<TreeSet<String>> toJoin = new HashSet<TreeSet<String>>();

                for (String retain : retainItems) {
                    for (TreeSet<String> set : disjointSets) {
                        if (set.contains(retain)) {
                            toJoin.add(set);
                        }
                    }
                }


                if (toJoin.size() > 0) {
                    TreeSet[] arr = toJoin.toArray(new TreeSet[toJoin.size()]);
                    TreeSet currSet = arr[0];
                    disjointSets.remove(currSet);
                    for (int i = 1; i < arr.length; i++) {
                        currSet.addAll(arr[i]);
                        disjointSets.remove(arr[i]);
                    }
                    for (String retain : retainItems) {
                        currSet.add(retain);
                    }
                    disjointSets.add(currSet);

                } else {
                    TreeSet<String> singleQuerySet = new TreeSet<String>();
                    for (String retain : retainItems) {
                        singleQuerySet.add(retain);
                    }
                    disjointSets.add(singleQuerySet);
                }
            }
        }

        TreeSet[] disjointSetArray = disjointSets.toArray(new TreeSet[disjointSets.size()]);

        ArrayList[] clusters = new ArrayList[disjointSetArray.length];
        ArrayList<Integer> independent = new ArrayList<Integer>();
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new ArrayList();

        }

        for (Query q : queries) {
            if (stratum.contains(q.getId())) {
                if (q.getRetainCriterion() != Query.NO_RETAIN) {
                    String[] retainItems = q.getRetainingQueryTypes();

                    outer:
                    for (int i = 0; i < disjointSetArray.length; i++) {
                        for (String retain : retainItems) {
                            if (disjointSetArray[i].contains(retain)) {
                                clusters[i].add(q.getId());
                                q.setCluster(i + 1);
                                break outer;
                            }
                        }
                    }
                } else {
                    independent.add(q.getId());
                    q.setCluster(0);
                }
            }
        }

//        for (TreeSet<String> set : disjointSets) {
//            System.out.println("new disjoint set:");
//            for (String item : set) {
//                System.out.println(item);
//            }
//            System.out.println("");
//        }
//        System.out.println("disjoint keyword sets:" + disjointSets.size());
        System.out.println(Arrays.toString(clusters));
        if (independent.size() > 0)
            System.out.println(independent.toString());

    }
}
