package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;

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
public class Restructuring {

    LinkedList<Query> qlist = new LinkedList<Query>();
    HashSet<Integer>[] map = null;
    boolean[] present = null;
    HashMap<String, HashSet<Integer>> outMap = null;
    LinkedList<LinkedList<Integer>> strata = null;

    public synchronized void addQuery(Query q) {
        qlist.add(q);
    }

    public synchronized void addQueries(List<Query> list) {
        for (Query q : list) {
            qlist.add(q);
        }
    }

    public int[] markStrata() {
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
        return st;
    }

    public LinkedList<Query> getQueryList() {
        return this.qlist;
    }

    

    public static void main(String[] args) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader("./src/query/queries.txt"));
            ArrayList<String> list = new ArrayList<String>(50);
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    list.add(line);
                }
            }

            // parse queries
            // add each query to stratifier
            // then call stratify method.

            Restructuring s = new Restructuring();
//            DependencyInjector di = new DependencyInjector("./src/impl.ioc");  // todo - remove unnecessary IoC stuff.
//            QueryParser qp = (QueryParser) di.createInstance("Parser");
            QueryParser qp = new BasicQueryParser();


            Query q = null;
            int queryId = 0;
            for (String item : list) {
                q = qp.parseString(item);
                q.setId(queryId);
                s.addQuery(q);
                queryId++;
            }

            System.out.println("parsed: ");
            // by default the client id in each query is '0'
            s.buildGraph();

            int m[] = s.markStrata();
            System.out.println(Arrays.toString(m));


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
        int i = 0;
        for (Query q : qlist) {
            String[] in = q.getInputs();
            HashSet set = new HashSet<Integer>();
            HashSet temp = null;
            for (String item : in) {
                temp = outMap.get(item);
                if (temp != null) {
                    set.addAll(temp);
                }
            }
            this.map[i] = set;
            i++;
        }
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
