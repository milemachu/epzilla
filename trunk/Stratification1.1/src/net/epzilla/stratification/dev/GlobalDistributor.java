package net.epzilla.stratification.dev;

import net.epzilla.stratification.query.InvalidSyntaxException;
import net.epzilla.stratification.query.BasicQueryParser;
import net.epzilla.stratification.query.Query;

import java.util.Vector;
import java.util.ArrayList;
import java.io.*;


public class GlobalDistributor implements Serializable {

//    private QueryParser parser = new BasicQueryParser();
    private Vector<Query> queries = new Vector<Query>();
    private Object lock = new Object();
    private Object fetchLock = new Object();
    private int maxId = 0;
    private int lastFetch = 0;

    public void addQuery(String query) throws InvalidSyntaxException {
        this.addQuery(query, 0);
    }

    public static void main(String[] args) throws IOException, InvalidSyntaxException {
        GlobalDistributor gd = new GlobalDistributor();
        BufferedReader br = new BufferedReader(new FileReader("./src/query/queries.txt"));
        ArrayList<String> list = new ArrayList<String>(50);
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
                list.add(line);
                gd.addQuery(line);
                if (i % 5 == 0) {
                    Vector<Query> list1 = gd.getDistributedQueries();
                    System.out.println(list1.size());
                    if (list1.size() > 0)
                        System.out.println(list1.get(0).getQueryString());
                }
                i++;
            }
        }
    }

    public void addQuery(String query, int clientId) throws InvalidSyntaxException {
        Query q = new BasicQueryParser().parseString(query);
        int id = 0;
        synchronized (lock) {
            id = ++maxId;
            q.setId(id);
        }
        q.setClientId(clientId);
        q.setQueryString(query);
        synchronized (fetchLock) {
            queries.add(q);
        }
    }

    public Vector<Query> getDistributedQueries(int startFrom) {
        Vector<Query> v = new Vector<Query>();
        for (Query q : queries) {
            if (q.getId() >= startFrom) {
                v.add(q);
            }
        }
        return v;
    }

    public Vector<Query> getDistributedQueries() {
        Vector<Query> v = new Vector<Query>();

        synchronized (fetchLock) {
            int max = lastFetch;
            int id = 0;
            for (Query q : queries) {
                if ((id = q.getId()) > lastFetch) {
                    v.add(q);
                    if (max < id) {
                        max = id;
                    }
                }
            }
            lastFetch = max;
        }
        return v;
    }
}
