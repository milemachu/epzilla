package net.epzilla.stratification.immediate;

import jstm.core.TransactedList;
import jstm.core.TransactedSet;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerDependencyStructure;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 11, 2010
 * Time: 8:44:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApproximateDispatcher {

    public void assignClusters(ArrayList<TriggerInfoObject> triggerList, String clientId) throws InvalidSyntaxException {
        assignClusters(triggerList, Integer.parseInt(clientId));
    }


    public void assignClusters(ArrayList<TriggerInfoObject> triggerList, int clientId) throws InvalidSyntaxException {


        TriggerDependencyStructure tds = DynamicDependencyManager.getInstance().getDependencyStructure(clientId);

        // TList<TList<TSet<String>>>
        TransactedList strata = tds.getstructure();
//        TransactedList str = new TransactedList();

        for (TriggerInfoObject tio : triggerList) {
            QueryParser parser = new BasicQueryParser();
            Query q = parser.parseString(tio.gettrigger());
            String[] ins = q.getInputs();
            String[] outs = q.getOutputs();

            System.out.println(Arrays.toString(ins));
            System.out.println(Arrays.toString(outs));

            // determine
            if (strata.size() == 0) {
//                TransactedList list = new TransactedList();
                int nstrata = SystemVariables.getNumStrata();
                for (int i = 0; i < nstrata; i++) {

                    TransactedList clusterList = new TransactedList();
                    int nclusters = SystemVariables.getClusters(i);
                    for (int j = 0; j < nclusters; j++) {
                        clusterList.add(new TransactedSet());
                    }

                    strata.add(clusterList);

                }
//                list.add(new TransactedList());
//                strata.add(list);

            }

            int leastCluster = 0;
            int leastLoad = Integer.MAX_VALUE;
            int current = 0;

            outer:
            for (int i = strata.size() - 1; i >= 0; i--) {
                System.out.println("strata size:" + strata.size());
                TransactedList clusters = (TransactedList) strata.get(i);
                boolean found = false;


                inner:
                for (int j = clusters.size() - 1; j >= 0; j--) {

                    TransactedSet<String> set = (TransactedSet) clusters.get(j); // todo check validity

                    System.out.println("cluster size: " + clusters.size());
                    for (String event : set) {
                        for (String inputEvent : ins) {
                            if (inputEvent.equals(event)) {
                                found = true;
                                break;
                            }
                        }
                    }

                    if (found) {
                        if (i < (SystemVariables.getNumStrata() - 1)) {
                            int target = i + 1;

                            tio.setstratumId(String.valueOf(target));

                            Hashtable<Integer, Integer> loadMap = SystemVariables.getClusterLoads(target);
                            leastLoad = Integer.MAX_VALUE;

                            for (Integer key : loadMap.keySet()) {
                                current = loadMap.get(key);
                                if (current < leastLoad) {
                                    leastLoad = current;
                                    leastCluster = key;
                                }
                            }

                            System.out.println("target cluster:" + target + " : " + leastCluster);
                            tio.setstratumId(Integer.toString(target));
                            tio.setclusterID(Integer.toString(leastCluster));

                            TransactedList sList = (TransactedList) strata.get(target);
                            TransactedSet tset = (TransactedSet) sList.get(leastCluster);

                            for (String event : ins) {
//                            set.add(event);
                                tset.add(event);

                            }
                            break outer;
                        } else {
                            tio.setstratumId(String.valueOf(i));
                            tio.setclusterID(String.valueOf(j));
                            for (String event : ins) {
                                set.add(event);
                            }
                            break outer;
                        }
                    } else {

                        if (i > 0) {
                            continue outer;
                        }
                        int target = 0;
                        Hashtable<Integer, Integer> loadMap = SystemVariables.getClusterLoads(target);
                        leastLoad = Integer.MAX_VALUE;

                        for (Integer key : loadMap.keySet()) {
                            current = loadMap.get(key);
                            if (current < leastLoad) {
                                leastLoad = current;
                                leastCluster = key;
                            }
                        }

                        System.out.println("target cluster:" + target + " : " + leastCluster);
                        tio.setstratumId(Integer.toString(target));
                        tio.setclusterID(Integer.toString(leastCluster));

                        TransactedList sList = (TransactedList) strata.get(target);
                        TransactedSet tset = (TransactedSet) sList.get(leastCluster);

                        for (String event : ins) {
//                            set.add(event);
                            tset.add(event);

                        }
                        break outer;
                    }


                }
            }

        }
    }
}
