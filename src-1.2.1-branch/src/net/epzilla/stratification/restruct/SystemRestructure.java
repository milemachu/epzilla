package net.epzilla.stratification.restruct;

import jstm.core.TransactedList;
import net.epzilla.stratification.immediate.SystemVariables;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 25, 2010
 * Time: 12:52:25 PM
 * To change this template use File | Settings | File Templates.
 */
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
                    LinkedList<TriggerInfoObject> clist = map.get(tio.getclientID());
                    if (clist == null) {
                        clist = new LinkedList();
                        map.put(tio.getclientID(), clist);
                    }
                    clist.add(tio);
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

        for (TriggerStrcutureManager tsx: tsmlist) {
            tsx.restructure();
        }

        // fully restructured.
        
        
    }


    public static void main(String[] args) throws Exception {
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
        for (TriggerInfoObject ttx: TriggerManager.getTriggers()) {
            System.out.print("item:");
            System.out.println(ttx.getstratumId()+ ":"+ttx.getclusterID() );
            if ("1".equals(ttx.getclusterID())) {
                one++;
            }         else {
                two++;
            }

        }
        System.out.println("one:zero" + one + ":" + two);
    }


    private void redistribute(LinkedList<Cluster> total) {
        int i = 0;
        int j = 0;
        int sts = SystemVariables.getNumStrata();
        int stsm1 = sts - 1;
        for (int x = 0; x < sts; x++) {
            int cls = SystemVariables.getClusters(x) - 1;
            j = 0;
            for (Cluster c : total) {
                if ((!c.isIndependent()) && c.getStratum() == x || (c.getStratum() > x && x >= stsm1)) {
                    c.setRealStratum(x);

                    if (j <= cls) {
                        c.setRealCluster(j);
//                        System.out.println("set:" + c.getRealCluster());
//                        System.out.println("set stratum:" + c.getRealStratum());
                    } else {
                        j = 0;
                        c.setRealCluster(j);
//                        System.out.println("set:" + c.getRealCluster());
//                        System.out.println("set stratum:" + c.getRealStratum());
                        
                        
                    }
                    j++;
                }
            }
        }
    }

}
