package net.epzilla.stratification.restruct;

import net.epzilla.stratification.dynamic.SystemVariables;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;
import org.epzilla.util.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class TriggerStrcutureManager {

    LinkedList<Query> qlist = new LinkedList<Query>();
    HashSet<Integer>[] map = null;
    boolean[] present = null;
    HashMap<String, HashSet<Integer>> outMap = null;
    LinkedList<LinkedList<Integer>> strata = null;
    String clientId;
    LinkedList<LinkedList<Cluster>> mapping = null;
    List<TriggerInfoObject> trList = null;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public synchronized void addQuery(Query q) {
        qlist.add(q);
    }

    public synchronized void addQueries(List<Query> list) {
        qlist.addAll(list);
    }

    public void restructure() {
        HashMap<Integer, HashMap<Integer, Cluster>> m = new HashMap();

        int[][] lmap = new int[SystemVariables.getNumStrata()][];
        for (int i = 0; i < lmap.length; i++) {
            lmap[i] = new int[SystemVariables.getClusters(i)];
        }

        for (LinkedList<Cluster> cl : mapping) {
//            System.out.println("sz:" + cl.size());
            for (Cluster c : cl) {
                HashMap<Integer, Cluster> hm = m.get(c.getStratum());
//                System.out.println("getst:" + c.getStratum());
                if (hm == null) {
                    hm = new HashMap();
                    m.put(c.getStratum(), hm);
                }
                hm.put(c.getCluster(), c);
                if (!c.isIndependent()) {
                    lmap[c.getRealStratum()][c.getRealCluster()] += c.getLoad();
                }
            }
        }

        for (Query q : qlist) {
            Cluster c = m.get(q.getStratum()).get(q.getCluster());
//            System.out.println("gettin:" + q.getCluster());
            q.setStratum(c.getRealStratum());
            q.setCluster(c.getRealCluster());
        }

        Iterator<TriggerInfoObject> i = trList.iterator();
        Iterator<Query> j = this.getQueryList().iterator();

        int[] s = new int[SystemVariables.getNumStrata()];

        int[] lim = new int[s.length];
        for (int ii = 0; ii < lim.length; ii++) {
            lim[ii] = SystemVariables.getClusters(ii) - 1;
        }

        while (i.hasNext()) {
            TriggerInfoObject obj = i.next();
            Query qo = j.next();
            obj.setoldClusterId(obj.getclusterID());
            obj.setoldStratumId(obj.getstratumId());
            int st = qo.getStratum();
            if (qo.isIndependent()) {
                int ts = getMin(lmap[qo.getStratum()]);
//                System.out.println("min:" + ts);
                if (s[st] > lim[st]) {
                    s[st] = 0;
                }
                obj.setclusterID(String.valueOf(ts));
                lmap[qo.getStratum()][ts]++;

            } else {
                obj.setclusterID(String.valueOf(qo.getCluster()));
            }
            obj.setstratumId(String.valueOf(qo.getStratum()));
        }

    }

    private int getMin(int[] ar) {
        int m = 0;
        int i = 0;
        for (int x : ar) {
            if (x < ar[m]) {
                m = i;
            }
            i++;
        }
        return m;
    }

    public LinkedList<LinkedList<Integer>> markStrata() {
        int[] st = new int[qlist.size()];
        int i = 0;
        for (LinkedList<Integer> list : strata) {
            for (Integer in : list) {
                st[in] = i;
            }
            i++;
        }
        i = 0;
        for (Query q : qlist) {
            q.setStratum(st[i]);
            i++;
        }

        return strata;
    }

    public LinkedList<Query> getQueryList() {
        return this.qlist;
    }


    public LinkedList<LinkedList<Cluster>> getVirtualStructure(List<TriggerInfoObject> triggerList) throws InvalidSyntaxException {
        LinkedList<Query> list = new LinkedList();
        trList = triggerList;
        QueryParser qp = new BasicQueryParser();
        Query q = null;
        for (TriggerInfoObject tio : triggerList) {
            if (!"OOOO".equals(tio.gettrigger())) {
                q = qp.parseString(tio.gettrigger());
                Logger.log("tid:" + tio.gettriggerID());
                q.setId(Integer.parseInt(tio.gettriggerID()));
                list.add(q);
            }
        }

        this.addQueries(list);
        this.buildGraph();
        List<LinkedList<Integer>> lx = this.markStrata();
        LinkedList<LinkedList<Cluster>> clist = new LinkedList();

        // mark clusters.
        Clusterizer c = null;
        int i = 0;
        for (LinkedList<Integer> st : lx) {
            c = new Clusterizer();
            c.clusterize(st, this.getQueryList(), clientId, i, false);
            i++;
            clist.add(c.getVirtualClusterInfo());
//            for (Cluster ccx: c.getVirtualClusterInfo()) {
//                System.out.println("vcluster:" + ccx.getCluster());
//            }

        }
        mapping = clist;
        return clist;


        /*
      Iterator<TriggerInfoObject> i = triggerList.iterator();
      Iterator<Query> j = this.getQueryList().iterator();

      while (i.hasNext()) {
          TriggerInfoObject obj = i.next();
          Query qo = j.next();
          obj.setoldClusterId(obj.getclusterID());
          obj.setoldStratumId(obj.getoldStratumId());
          obj.setclusterID(String.valueOf(qo.getCluster()));
          obj.setstratumId(String.valueOf(qo.getStratum()));
      }
        */

    }


    public static void main(String[] args) throws Exception {
        try {
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

            LinkedList<LinkedList<Cluster>> res = new TriggerStrcutureManager().getVirtualStructure(ll);
            for (LinkedList<Cluster> cx : res) {
                for (Cluster cc : cx) {
                    System.out.println("LOAD:" + cc.getLoad());
                    System.out.println("cluster: " + cc.getCluster());
                }
            }

            /*
            System.out.println("parsed: " + (System.currentTimeMillis() - stat));
            // by default the client id in each query is '0'
            stat = System.currentTimeMillis();
            s.buildGraph();
            System.out.println("built: " + (System.currentTimeMillis() - stat));
            stat = System.currentTimeMillis();

            List<LinkedList<Integer>> lx = s.markStrata();
            System.out.println("marked: " + (System.currentTimeMillis() - stat));
            Clusterizer c = new Clusterizer();

//        ArrayList   lis =  new ArrayList(s.getQueryList());
//                  Collections.copy(lis, s.getQueryList());
            c.clusterize(lx.get(0), s.getQueryList());
//            System.out.println(Arrays.toString(m));
              */

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public synchronized void buildGraph() {
        map = new HashSet[qlist.size()];
        present = new boolean[qlist.size()];
        strata = new LinkedList<LinkedList<Integer>>();
        Arrays.fill(present, true);


        buildOutMap();
        buildDependencyMap();

        while (true) {

            LinkedList<Integer> list = new LinkedList<Integer>();

            for (int i = 0; i < map.length; i++) {
                if (present[i]) {
                    HashSet<Integer> set = map[i];
                    if (set.size() == 0) {
                        list.add(i);
//                        present[i] = false;
                    } else {
                        boolean hasDependency = false;
                        for (Integer val : set) {
                            if (present[val]) {
                                hasDependency = true;
                                break;
                            }
                        }

                        if (!hasDependency) {
                            list.add(i);
                        }
                    }
                }
            }

            if (list.size() == 0) {
                break;
            } else {
                this.strata.add(list);
                for (Integer i : list) {
                    present[i] = false;
                }
            }
        }


    }

    private void buildDependencyMap() {
        long st = System.currentTimeMillis();

        int i = 0;
        for (Query q : qlist) {
            String[] in = q.getInputs();
            HashSet set = new HashSet<Integer>();
            HashSet temp = null;
            for (String item : in) {
                temp = outMap.get(item);
                if (temp != null) {
                    set.addAll(temp);

//                    Collections.addAll(set, temp);
                }
            }
            this.map[i] = set;
            i++;
        }
        Logger.log("depend built: " + (System.currentTimeMillis() - st));

    }

    private void buildOutMap() {
        // building out map.
        this.outMap = new HashMap();
        int i = 0;
        HashSet<Integer> set = null;
        for (Query q : qlist) {
            String[] out = q.getOutputs();
            for (String item : out) {
                set = outMap.get(item);
                if (set == null) {
                    set = new HashSet<Integer>();
                    outMap.put(item, set);
                }
                set.add(i);
            }
            i++;
        }
    }
}
