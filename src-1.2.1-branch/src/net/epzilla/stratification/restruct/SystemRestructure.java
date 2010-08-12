/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package net.epzilla.stratification.restruct;

import jstm.core.TransactedList;
import net.epzilla.stratification.dynamic.SystemVariables;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.clusterNode.clusterInfoObjectModel.NodeIPObject;
import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.dispatcher.clusterHandler.TriggerSender;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.*;

/**
 * responsible for performin operations related to reorgaizing the query base.
 */
public class SystemRestructure {

    private static SystemRestructure instance = new SystemRestructure();

    public static SystemRestructure getInstance() {
        return instance;
    }

    /**
     * reorganizes the query base.
     *
     * @throws InvalidSyntaxException
     */
    public void restructureSystem() throws InvalidSyntaxException {
        LinkedList<TriggerInfoObject> list = new LinkedList(TriggerManager.getTriggers());
        HashMap<String, LinkedList<TriggerInfoObject>> map = new HashMap();

        for (Object o : list) {
            if (o instanceof TriggerInfoObject) {
                TriggerInfoObject tio = (TriggerInfoObject) o;
                try {
                    if (!"OOOO".equals(tio.gettrigger()) && (tio.gettrigger().length() > 10)) {
                        LinkedList<TriggerInfoObject> clist = map.get(tio.getclientID());
                        if (clist == null) {
                            clist = new LinkedList();
                            map.put(tio.getclientID(), clist);
                        }
                        clist.add(tio);
                    }
                } catch (Exception e) {
                    Logger.error("", e);
                }
            }
        }


        LinkedList<LinkedList<LinkedList<Cluster>>> slist = new LinkedList();
        LinkedList<TriggerStrcutureManager> tsmlist = new LinkedList();
        for (String clientId : map.keySet()) {
            try {
                LinkedList<TriggerInfoObject> clientList = map.get(clientId);
                TriggerStrcutureManager tsm = new TriggerStrcutureManager();
                tsm.setClientId(clientId);
                slist.add(tsm.getVirtualStructure(clientList));
                tsmlist.add(tsm);

            } catch (Exception ex) {
                Logger.error("", ex);
            }
        }

        LinkedList<Cluster> total = new LinkedList<Cluster>();
        for (LinkedList<LinkedList<Cluster>> l1 : slist) {
            for (LinkedList<Cluster> l2 : l1) {
                total.addAll(l2);
            }
        }

        Collections.sort(total, new Cluster.ClusterComparator());
        redistribute(total);

        for (TriggerStrcutureManager tsx : tsmlist) {
            tsx.restructure();
        }

        // fully restructured.
    }

    /**
     * sends required information for clusters in order to finalize trigger redistribution.
     * @return
     */
    public boolean sendRestructureCommands() {

        try {
            TransactedList<TriggerInfoObject> trList = TriggerManager.getTriggers();

            HashMap<String, HashMap<String, HashMap<String, ArrayList<TriggerRepresentation>>>> remList = new HashMap();
            HashMap<String, HashMap<String, HashMap<String, ArrayList<TriggerRepresentation>>>> addList = new HashMap();

            for (TriggerInfoObject tio : trList) {

                if ((!tio.getclusterID().equals(tio.getoldClusterId())) || (!tio.getstratumId().equals(tio.getoldStratumId()))) {
                    HashMap<String, HashMap<String, ArrayList<TriggerRepresentation>>> remStratum = remList.get(tio.getoldStratumId());
                    HashMap<String, HashMap<String, ArrayList<TriggerRepresentation>>> addStratum = addList.get(tio.getstratumId());

                    if (remStratum == null) {
                        remStratum = new HashMap();
                        remList.put(tio.getoldStratumId(), remStratum);
                    }

                    if (addStratum == null) {
                        addStratum = new HashMap();
                        addList.put(tio.getstratumId(), addStratum);
                    }


                    HashMap<String, ArrayList<TriggerRepresentation>> remCluster = remStratum.get(tio.getoldClusterId());
                    HashMap<String, ArrayList<TriggerRepresentation>> addCluster = addStratum.get(tio.getclusterID());

                    if (remCluster == null) {
                        remCluster = new HashMap();
                        remStratum.put(tio.getoldClusterId(), remCluster);
                    }

                    if (addCluster == null) {
                        addCluster = new HashMap();
                        addStratum.put(tio.getclusterID(), addCluster);
                    }


                    ArrayList<TriggerRepresentation> rem = remCluster.get(tio.getclientID());
                    ArrayList<TriggerRepresentation> add = addCluster.get(tio.getclientID());

                    if (rem == null) {
                        rem = new ArrayList<TriggerRepresentation>();
//                        rem.setClientId(tio.getclientID());
                        remCluster.put(tio.getclientID(), rem);
                    }

                    if (add == null) {
                        add = new ArrayList<TriggerRepresentation>();
                        addCluster.put(tio.getclientID(), add);
                    }
                    TriggerRepresentation at = new TriggerRepresentation();
                    TriggerRepresentation rt = new TriggerRepresentation();
                    at.setClientId(tio.getclientID());
                    at.setTriggerId(tio.gettriggerID());
                    at.setTrigger(tio.gettrigger());

                    rt.setClientId(tio.getclientID());
                    rt.setTriggerId(tio.gettriggerID());

                    add.add(at);
                    rem.add(rt);
//                    rem.addTriggerIds(tio.gettriggerID());
                }
            }

            Logger.log("sending command.");

            for (String stratum : remList.keySet()) {
                TransactedList<NodeIPObject> tl = ClusterIPManager.getIpList();
                Logger.log("tl size:" + tl.size());
                for (NodeIPObject no : tl) {
                    String id = no.getclusterID();
                    if (remList.get(stratum).containsKey(id)) {
                        HashMap<String, ArrayList<TriggerRepresentation>> m = remList.get(stratum).get(id);
                        for (String user : m.keySet()) {
                            ArrayList<TriggerRepresentation> al = m.get(user);
                            TriggerSender.requestTriggerDeletion(no.getIP(), id, al, user);
                        }
                    }
                }
            }


            for (String stratum : addList.keySet()) {
                TransactedList<NodeIPObject> tl = ClusterIPManager.getIpList();
                for (NodeIPObject no : tl) {
                    String id = no.getclusterID();
                    if (addList.get(stratum).containsKey(id)) {
                        HashMap<String, ArrayList<TriggerRepresentation>> m = addList.get(stratum).get(id);
                        for (String user : m.keySet()) {
                            ArrayList<TriggerRepresentation> al = m.get(user);
                            TriggerSender.triggerAdding(no.getIP(), id, al, user);
                        }
                    }
                }
            }


        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * assigns real strata and clusters to the virtual structure.
     * @param total
     */
    private void redistribute(LinkedList<Cluster> total) {
        int i = 0;
        int j = 0;
        int sts = SystemVariables.getNumStrata();
        int stsm1 = sts - 1;
        boolean increasing = true;
        for (int x = 0; x < sts; x++) {
            int cls = SystemVariables.getClusters(x) - 1;
            j = 0;
            for (Cluster c : total) {
                if ((c.getStratum() == x) || (c.getStratum() > x && x == stsm1)) {
                    c.setRealStratum(x);
                    if (!c.isIndependent()) {
                        if ((j <= cls) && (j >= 0)) {
                            c.setRealCluster(j);
                        } else {
                            if (increasing) {
                                j = cls;
                                increasing = false;
                            } else {
                                j = 0;
                                increasing = true;
                            }
                            c.setRealCluster(j);

                        }
                        if (increasing) {
                            j++;
                        } else {
                            j--;
                        }
                    }
                }
            }
        }
    }

}
