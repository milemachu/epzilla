package net.epzilla.stratification;

import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import net.epzilla.stratification.query.BasicQueryParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 25, 2010
 * Time: 9:22:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

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
            /*
            MultiClientStratifier s = new MultiClientStratifier();
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

            System.out.println("size: ");
            // by default the client id in each query is '0'
            ArrayList<ArrayList<Integer>> stratifiedQueries = s.stratify(0);
            printStratas(stratifiedQueries);
            Clusterizer c = new Clusterizer();
            int index = 0;
            for (ArrayList<Integer> st : stratifiedQueries) {
                System.out.println("Groups in stratum:" + index);
                c.clusterize(st, s.getAllQueries(0));
                index++;
                System.out.println("");
            }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static void printStratas(ArrayList<ArrayList<Integer>> stratifiedQueries) {
        int numQueries = 0;
        for (ArrayList strataList : stratifiedQueries) {
            numQueries += strataList.size();
        }
        System.out.println("Queries analyzed: " + numQueries);
        System.out.println("Number of Stratas:" + stratifiedQueries.size());
        System.out.println("\nQuery distribution:\n");

        int current = 0;
        for (ArrayList<Integer> ll : stratifiedQueries) {
            System.out.println("stratum " + current + ":");
            for (int ints : ll) {
                System.out.print(ints + ", ");
            }
            System.out.println("\n");
            current++;
        }     */
        }  catch (Exception e) {
            
        }
    }
}