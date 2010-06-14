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

public class SystemRestructure {

    private static SystemRestructure instance = new SystemRestructure();

    public static SystemRestructure getInstance() {
        return instance;
    }

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

            // todo - send to clusters.
            System.out.println("sending command.");

            // todo implement strata
            for (String stratum : remList.keySet()) {
                TransactedList<NodeIPObject> tl = ClusterIPManager.getIpList();
                System.out.println("tl size:" + tl.size());
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
                            TriggerSender.acceptTrigger(no.getIP(), id, al, user);
                        }
                    }
                }
            }


        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        BufferedReader br = new BufferedReader(new FileReader("./src/query/queries.txt"));
        ArrayList<String> list = new ArrayList<String>(50);
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
                for (int i = 0; i < 100; i++) {
                    list.add(line);
                }
            }
        }


        TriggerStrcutureManager s = new TriggerStrcutureManager();
//            DependencyInjector di = new DependencyInjector("./src/impl.ioc");  // todo - remove unnecessary IoC stuff.
//            QueryParser qp = (QueryParser) di.createInstance("Parser");
        QueryParser qp = new BasicQueryParser();
        long stat = System.currentTimeMillis();

        Query q = null;
        int queryId = 0;
        int x = 0;
        List<TriggerInfoObject> ll = new LinkedList();
        TriggerInfoObject tx = null;
        for (String item : list) {
            tx = new TriggerInfoObject();
//                q = qp.parseString(item);
            tx.settrigger(item);
            tx.settriggerID(String.valueOf(queryId));
//                q.setId(queryId);
            ll.add(tx);
//                s.addQuery(q);
            queryId++;
        }

        TriggerManager.setTriggers(new TransactedList());
        TriggerManager.getTriggers().addAll(ll);
//        System.out.println("adding to tm");
        SystemRestructure sr = new SystemRestructure();
        sr.restructureSystem();

        System.out.println("no clus" + SystemVariables.getClusters(0));
        int one = 0;
        int two = 0;
        for (TriggerInfoObject ttx : TriggerManager.getTriggers()) {
            System.out.print("item:");
            System.out.println(ttx.getstratumId() + ":" + ttx.getclusterID());
            if ("1".equals(ttx.getclusterID())) {
                one++;
            } else {
                two++;
            }

        }
        System.out.println("one:zero" + one + ":" + two);
        long sss = System.currentTimeMillis();
        sr.sendRestructureCommands();
        System.out.println("time e: " + (System.currentTimeMillis() - sss));
    }


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
//                    System.out.println("setting stratum:" + x + c.isIndependent());
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
