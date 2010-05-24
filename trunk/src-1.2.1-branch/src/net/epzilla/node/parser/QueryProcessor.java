package net.epzilla.node.parser;

import net.epzilla.node.query.Query;

import java.util.Vector;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 8:45:06 AM
 * To change this template use File | Settings | File Templates.
 */    /*
public class QueryProcessor {
    Vector<Query> queries = new Vector<Query>();

    public StringBuilder processEvents(String events) {
        StringBuilder sb = new StringBuilder("");
        String[] e = events.split("\n");
        String[][] csv = new String[e.length - 1][];
        String[] row = null;
        String[] params = e[0].split(",");

        for (int i = 1; i < e.length; i++) {
            csv[i] = e[i].split(",");

        }

        Query q = null;
        ArrayList<String[][]> results = new ArrayList<String[][]>();
        ArrayList<String[][]> headers = new ArrayList<String[][]>();

        for (int j = 0; j < queries.size(); j++) {
            q = queries.get(j);

            if (q != null) {

            }
        }

        return null;
    }

    public ArrayList<String[]> getResults(Query q, String[][] events, String[] params) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        String[][] outNames = q.getOutputs();
        String[] out = new String[outNames.length];
        out[0] = outNames[0][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = outNames[i][1];
        }
        list.add(out);

        int[] pos  = new int[out.length + 1];
        int i  = 1;

        for (String[] in: q.getInputs()) {
            pos[i] = this.indexOf(in[1], params);
                              i++;
        }

//        pos[0] = indexOf("")
        for (String[] event : events) {
            if (checkPredicate(q, event, params)) {
                out = new String[outNames.length];

            }
        }
        return null;
    }

    public boolean checkPredicate(Query q, String[] event, String[] params) {
        int header = indexOf("Title", params);

        String[][] cn = q.getConditions();
        boolean condition = false;
        for (String[] ar : cn) {
            if (params[header].equals(event[header])) {
                int pos = indexOf(ar[1], params);
                if (!("=".equals(ar[2]) && event[pos].equals(ar[3]))) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private int indexOf(String item, String[] array) {
        int x = 0;
        for (String a : array) {
            if (item.equals(a)) {
                return x;
            }
            x++;
        }
        return -1;
    }

    public void addQuery(Query q) {
        this.queries.add(q);
    }

    public void removeQuery(Query q) {
        this.queries.remove(q);
    }
}
        */