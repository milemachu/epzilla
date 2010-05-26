package net.epzilla.stratification.dynamic;

import jstm.core.Site;
import jstm.core.TransactedList;
import jstm.core.TransactedSet;
import jstm.core.Transaction;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerDependencyStructure;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ApproximateDispatcher {

    private static ApproximateDispatcher instance = new ApproximateDispatcher();

    public static ApproximateDispatcher getInstance() {
        return instance;
    }

    public void assignClusters(ArrayList<TriggerInfoObject> triggerList, String clientId) throws InvalidSyntaxException {
        assignClusters(triggerList, Long.parseLong(clientId));
    }


    public void assignClusters(ArrayList<TriggerInfoObject> triggerList, long clientId) throws InvalidSyntaxException {

        TriggerDependencyStructure tds = DynamicDependencyManager.getInstance().getDependencyStructure(clientId);

        // input output both.
        // TList<TList<TSet<String>>>
//        TransactedList str = new TransactedList();
        TransactedList inputStrata = tds.getInputStructure();
        TransactedList outputStrata = tds.getOutputStructure();


        for (TriggerInfoObject tio : triggerList) {
            QueryParser parser = new BasicQueryParser();
            Query q = parser.parseString(tio.gettrigger());
            String[] ins = q.getInputs();
            String[] outs = q.getOutputs();
            SystemVariables.triggerCount++;

//            System.out.println(Arrays.toString(ins));
//            System.out.println(Arrays.toString(outs));


            // determine
            if (inputStrata.size() == 0) {
//                TransactedList list = new TransactedList();
                int nstrata = SystemVariables.getNumStrata();
                for (int i = 0; i < nstrata; i++) {

                    TransactedList clusterList = new TransactedList();
                    int nclusters = SystemVariables.getClusters(i);
                    for (int j = 0; j < nclusters; j++) {
                        clusterList.add(new TransactedSet());
                    }

                    inputStrata.add(clusterList);

                }
            }

            if (outputStrata.size() == 0) {
                int nstrata = SystemVariables.getNumStrata();
                for (int i = 0; i < nstrata; i++) {

                    TransactedList clusterList = new TransactedList();
                    int nclusters = SystemVariables.getClusters(i);
                    for (int j = 0; j < nclusters; j++) {
                        clusterList.add(new TransactedSet());
                    }

                    outputStrata.add(clusterList);

                }
            }


            int leastCluster = 0;
            int leastLoad = Integer.MAX_VALUE;
            int current = 0;

            outer:
            for (int i = outputStrata.size() - 1; i >= 0; i--) {
//                System.out.println("strataList size:" + strataList.size());
                TransactedList clusters = (TransactedList) outputStrata.get(i);
                boolean found = false;


                inner:
                for (int j = clusters.size() - 1; j >= 0; j--) {

                    TransactedSet<String> set = (TransactedSet) clusters.get(j); // todo check validity

//                    System.out.println("cluster size: " + clusters.size());
                    for (String event : set) {
                        for (String inputEvent : ins) {
                            if (inputEvent.equals(event)) {
                                found = true;
                                break;
                            }
                        }
                    }

                    if (found) {
                        int target = 0;
                        if (i < (SystemVariables.getNumStrata() - 1)) {
                            target = i + 1;

                            tio.setstratumId(String.valueOf(target));

                        } else {
                            // last stratum.
                            target = i;
                            tio.setstratumId(String.valueOf(i));

                        }


                        TransactedList inputDependencyList = (TransactedList) inputStrata.get(Integer.parseInt(tio.getstratumId()));
                        int cluster = getCluster(target, inputDependencyList, ins);
                        tio.setclusterID(String.valueOf(cluster));
                        if (SystemVariables.triggerCount > SystemVariables.roundRobinLimit) {
                            addDependencies((TransactedSet) ((TransactedList) inputStrata.get(target)).get(cluster), ins);
                        }
                        addDependencies((TransactedSet) ((TransactedList) outputStrata.get(target)).get(cluster), outs);

//                        SystemVariables.triggerLoadMap.get(target)[cluster]++;
                        System.out.println("SV internal: " + SystemVariables.triggerLoadMap.get(target)[cluster]);

                        break outer;

                        // dependencies not found.
                    } else {

                        if (i > 0) {
                            continue outer;
                        }

                        int stratum = i;
                        String stringStratum = String.valueOf(stratum);
                        int cluster = getCluster(stratum, inputStrata, ins);

                        tio.setstratumId(stringStratum);
                        tio.setclusterID(String.valueOf(cluster));
                        if (SystemVariables.triggerCount > SystemVariables.roundRobinLimit) {

                            addDependencies((TransactedSet) ((TransactedList) inputStrata.get(stratum)).get(cluster), ins);
                        }
                        addDependencies((TransactedSet) ((TransactedList) outputStrata.get(stratum)).get(cluster), outs);

                        System.out.println("SV internal: " + SystemVariables.triggerLoadMap.get(stratum)[cluster]);
                        break outer;
                    }
                }
            }

        }
    }


    public static void addDependencies(TransactedSet cluster, String[] entries) {
        try {
            List list = Arrays.asList(entries);
            if (!cluster.containsAll(list)) {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    Site.getLocal().allowThread();
                    Transaction transaction = Site.getLocal().startTransaction();
                    cluster.addAll(list);
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            Logger.error("Error while committing dependencies:", e);
        }
    }


    public static int getCluster(int stratum, TransactedList<TransactedList<TransactedSet<String>>> inputStrata, String[] inputs) {
        TransactedList<TransactedSet<String>> is = inputStrata.get(stratum);
        int cluster = 0;

        if (SystemVariables.triggerCount > SystemVariables.roundRobinLimit) {
            for (TransactedSet<String> set : is) {
                for (String dependency : set) {
                    for (String entry : inputs) {
                        if (entry.equals(dependency)) {
                            return cluster;
                        }
                    }
                }
                cluster++;
            }
        }


        // no dependencies.
        if (SystemVariables.triggerCount < SystemVariables.roundRobinLimit) {
            // do round robin
            int[] clusters = SystemVariables.triggerLoadMap.get(stratum);

            int least = 0;
            for (int i = 0; i < clusters.length; i++) {
                if (clusters[least] > clusters[i]) {
                    least = i;
                }
            }
            SystemVariables.triggerLoadMap.get(stratum)[least]++;

            return least;
        } else {
            // no round robin
            Integer least = SystemVariables.leastLoadClusterMap.get(stratum);
            if (least != null) {
                return least;
            }
            return 0;

        }
    }
}
