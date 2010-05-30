package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.Query;

import java.util.*;


public class Clusterizer {

    LinkedList<Cluster> clusterMeta = new LinkedList<Cluster>();

    public LinkedList<Cluster> getVirtualClusterInfo() {
        return this.clusterMeta;
    }

    public void clusterize(List<Integer> stratum, List<Query> queries, String clientId, int stratumId, boolean multipleStrata) {
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
                    // no dependencies.
                    // new set is created.
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
                    q.setIndependent(true);
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
        LinkedList<Cluster> clist = new LinkedList();
        Cluster c = null;
        int i = 1;
        for (ArrayList clust : clusters) {
            c = new Cluster();
            c.setClientId(clientId);
            c.setLoad(clust.size());
            c.setStratum(stratumId);
            clist.add(c);
            c.setCluster(i);
            i++;
        }

        c = new Cluster();
        c.setClientId(clientId);
        c.setLoad(independent.size());
        c.setIndependent(true);
        c.setStratum(stratumId);
        clist.add(c);
        c.setCluster(0);
        this.clusterMeta = clist;

//       for (Cluster ccc:  clusterMeta) {
//           System.out.println("cmeta: "+ ccc.getCluster());
//       }
//        System.out.println("clusters:" + clusters.length);
//        System.out.println(Arrays.toString(clusters));
//        if (independent.size() > 0)
//            System.out.println(independent.toString());

    }
}
