package org.epzilla.clusterNode.parser;


import java.util.Vector;
import org.epzilla.clusterNode.query.Query;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 9, 2010
 * Time: 9:37:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryExecuter {
    Vector<Query> queries = new Vector<Query>();

    public void addQuery(Query q) {
        this.queries.add(q);
    }

    public void removeQuery(Query q) {
        this.queries.remove(q);
    }

    public String processEvents(String events) {
        try {
            String[] csvRows = events.split("\n");
            String[] headers = csvRows[0].split(",");

            String[][] eventGrid = new String[csvRows.length - 1][];
            UnionFind uf = new UnionFind();
            ResultAssembler ra = new ResultAssembler();
            ra.setUnionFind(uf);
            uf.add("Title");
            boolean[] added = new boolean[queries.size()];
            int queryIndex = 0;
            for (int i = 1; i <= eventGrid.length; i++) {
                eventGrid[i - 1] = csvRows[i].split(",");
                queryIndex = 0;
                for (Query q : this.queries) {
                    SingleQueryResult sq = new SingleQueryResult();

                    int[] mapping = getMapping(q.getConditionLeft(), headers);
                    int[] contentMap = getMapping(q.getInputs(), headers);
                    sq.setHeaders(this.copyContent(headers, contentMap));
                    if (this.validate(q, eventGrid[i - 1], mapping)) {
                        if (!added[queryIndex]) {
                            uf.add(q.getInputs());
//                        uf.add(q.getInputTitle());
                            added[queryIndex] = true;
                        }
                        try {
                            sq.addEvent(copyContent(eventGrid[i - 1], contentMap));
                        } catch (Exception e) {
//                        System.out.println("exc:");
                        }
                    }
                    queryIndex++;
                    ra.addResult(sq);
                }
            }
            return ra.assembleResults().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String[] copyContent(String[] source, int[] placesToCopy) {
        String[] target = new String[placesToCopy.length];
        int i = 0;
        for (int place : placesToCopy) {
            if (place != -1) {
                target[i] = source[place];
                i++;

            }
        }
        return target;
    }


    private void print(int[] x) {
        for (int i : x) {
            System.out.print(i + ", ");
        }
        System.out.println("");
    }

    private void print(Object[] x) {
        for (Object i : x) {
            System.out.print(i.toString() + ", ");
        }
        System.out.println("");
    }

    public int[] getMapping(String[] src, String[] target) {
        int[] mapping = new int[src.length + 1];
        mapping[title] = indexOf("Title", target);
        int i = 1;
        for (String s : src) {
            mapping[i] = this.indexOf(s, target);
            i++;
        }
        return mapping;
    }

    int title = 0;

    public boolean validate(Query q, String[] event, int[] mapping) {

        if (q.getInputTitle().equals(event[mapping[title]])) {
            String[][] conditions = q.getConditions();
            int i = 1;
            for (String[] condition : conditions) {
                if ("=".equals(condition[1])) {
                    if (i < mapping.length && mapping[i] != -1 && mapping[i] < event.length && event[mapping[i]].equals(condition[2])) {
                        return true;
                    } else {
                        return false;
                    }
                } else if ("!=".equals(conditions[1])) {
                    if (i < mapping.length && mapping[i] != -1 && mapping[i] < event.length && event[mapping[i]].equals(condition[2])) {
                        return false;
                    } else {
                        return true;
                    }
                } else {

                }
                i++;
            }
        }
        return false;
    }

    private int indexOf(String item, String[] items) {
        int i = 0;
        for (String x : items) {
            if (item.equals(x)) {
                return i;
            }
            i++;
        }
        return -1;
    }


}
