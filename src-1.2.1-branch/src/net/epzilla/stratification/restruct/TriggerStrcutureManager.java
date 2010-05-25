package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import org.epzilla.dispatcher.dispatcherObjectModel.TriggerInfoObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 24, 2010
 * Time: 10:01:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerStrcutureManager {

    LinkedList<Query> qlist = new LinkedList<Query>();
    HashSet<Integer>[] map = null;
    boolean[] present = null;
    HashMap<String, HashSet<Integer>> outMap = null;
    LinkedList<LinkedList<Integer>> strata = null;

    public synchronized void addQuery(Query q) {
        qlist.add(q);
    }

    public synchronized void addQueries(List<Query> list) {
        qlist.addAll(list);
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
        }
//        ArrayList<ArrayList<Integer>> tempList = new ArrayList();
//        for (LinkedList<Integer> lis: strata) {
//            ArrayList<Integer> t = new ArrayList();
//            t.addAll(lis);
//            tempList.add(t);
//        }

        return strata;
    }

    public void markClusters() {
        LinkedList<Query> stratum = null;
        for (int i = 0; i <= strata.size(); i++) {
            stratum = new LinkedList<Query>();
            for (Query q : qlist) {
                if (q.getStratum() == i) {
                    stratum.add(q);
                }
            }



        }
    }

    public LinkedList<Query> getQueryList() {
        return this.qlist;
    }

    public void restructure(List<TriggerInfoObject> triggerList) throws InvalidSyntaxException {
        LinkedList<Query> list = new LinkedList();
        QueryParser qp = new BasicQueryParser();
        Query q = null;
        for (TriggerInfoObject tio : triggerList) {
            q = qp.parseString(tio.gettrigger());
            q.setId(Integer.parseInt(tio.gettriggerID()));
            list.add(q);
        }

        this.addQueries(list);
        this.buildGraph();
        List<LinkedList<Integer>> lx = this.markStrata();

        // mark clusters.
        Clusterizer c = null;
        for (LinkedList<Integer> st: lx) {
            c = new Clusterizer();
            c.clusterize(st, this.getQueryList());
        }

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
            TriggerInfoObject tx  = null;
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

                             new TriggerStrcutureManager().restructure(ll);

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
        System.out.println("depend built: " + (System.currentTimeMillis() - st));

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
