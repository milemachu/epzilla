package net.epzilla.stratification;

import net.epzilla.stratification.query.Query;
import net.epzilla.stratification.query.QueryParser;
import net.epzilla.stratification.graph.DependancyGraph;
import net.epzilla.stratification.graph.Stratifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Nov 19, 2009
 * Time: 8:04:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        try {
//            String x = "avg\\\\s*(";
//          boolean b =  "avg ()".startsWith(x);
//            System.out.println(b);
            BufferedReader br = new BufferedReader(new FileReader("./src/query/queries.txt"));
            ArrayList<String> list = new ArrayList<String>(50);
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    list.add(line);
                }
            }
            DependancyGraph graph = new DependancyGraph();
            QueryParser an = new QueryParser();
            for (String item : list) {
                System.out.println(item);
                Query q = an.parseString(item);
                graph.addQuery(q);
                System.out.println(q);
            }

            graph.buildGraph();
            boolean[][] g = graph.getGraph();
            for (boolean[] a : g) {
                for (boolean x : a) {
                    System.out.print((x ? "1" : "0") + " ");
                }
                System.out.println("");
            }

            ArrayList<ArrayList<Integer>> stratifiedQueries = Stratifier.stratify(g);
            System.out.println("\n\n");
            int numQueries = 0;
            for (ArrayList strataList: stratifiedQueries) {
                  numQueries += strataList.size();
            }
            System.out.println("Queries analyzed: " + numQueries);
            System.out.println("Number of Stratas:" + stratifiedQueries.size());
            System.out.println("\nQuery distribution:\n");
            int current = 1;
            for (ArrayList<Integer> ll : stratifiedQueries) {
                System.out.println("stratum " + current + ":");
                for (int ints: ll) {
                    System.out.print(ints + ", ");
                }
                System.out.println("\n");
                current++;
            }
//                QueryParserOld parser = new QueryParserOld();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
