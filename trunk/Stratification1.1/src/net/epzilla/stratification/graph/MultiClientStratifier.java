package net.epzilla.stratification.graph;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 25, 2010
 * Time: 2:40:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiClientStratifier {

    private Hashtable<Integer, Stratifier> strataMap = new Hashtable<Integer, Stratifier>();

    public void addQuery(Query q) {
        Stratifier s = strataMap.get(q.getClientId());
        if (s == null) {
            s = new Stratifier();
            strataMap.put(q.getClientId(), s);
        }

        s.addQuery(q);
    }

    public void removeQuery(Query q) {
        Stratifier s = strataMap.get(q.getClientId());
        if (s != null) {
            s.removeQuery(q);
        }
    }

    public ArrayList<ArrayList<Integer>> stratify(int clientId) {
        Stratifier s = strataMap.get(clientId);
        if (s != null) {
            return s.stratify();
        }
        return null;
    }

    public ArrayList<ArrayList<Query>> getStratifiedQueries(int clientId) {
        Stratifier s = strataMap.get(clientId);
//        return s.getDependancyGraph().getQueries();
//
        return null;
    }

    public ArrayList<Query> getAllQueries(int clientId) {
        Stratifier s = strataMap.get(clientId);
        if (s != null) {
        return s.getDependancyGraph().getQueries();
        } else {
            return null;
        }
    }

    public int getStratumFor(Query q) {
        Stratifier s = strataMap.get(q.getClientId());
        if (s != null) {
            return s.getStratumFor(q);
        }
        return -1;
    }

}
