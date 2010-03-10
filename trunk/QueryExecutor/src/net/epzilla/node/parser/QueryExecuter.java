package net.epzilla.node.parser;

import net.epzilla.node.query.Query;

import java.util.Vector;

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
        String[] csvRows = events.split("\n");
        String[] headers = csvRows[0].split(",");

        String[][] ev = new String[csvRows.length - 1][];
//        System.out.println(ev.length );
        UnionFind uf = new UnionFind();
        ResultAssembler ra = new ResultAssembler();
        ra.setUnionFind(uf);
        uf.add("Title");
        boolean[] added = new boolean[queries.size()];
        int queryIndex = 0;
        for (int i = 1; i <= ev.length; i++) {
            ev[i - 1] = csvRows[i].split(",");
            queryIndex = 0;
            for (Query q : this.queries) {
                SingleQueryResult sq = new SingleQueryResult();

                int[] mapping = getMapping(q.getConditionLeft(), headers);
                int[] contentMap = getMapping(q.getInputs(), headers);
                sq.setHeaders(this.copyContent(headers, contentMap));
//                print(mapping);
                if (this.validate(q, ev[i - 1], mapping)) {
                    if (!added[queryIndex]) {
                        uf.add(q.getInputs());
//                        uf.add(q.getInputTitle());
                        added[queryIndex] = true;
                    }
//                    System.out.println("validated.");
//                    print(sq.getHeaders());
                    sq.addEvent(copyContent(ev[i - 1], contentMap));
                }
                queryIndex++;
                      ra.addResult(sq);
            }
        }

        System.out.println("UF" + uf.toString());
        System.out.println("to array");
        print(uf.toArray("Title"));


        return ra.assembleResults().toString();

    }

    private String[] copyContent(String[] source, int[] placesToCopy) {
        String[] target = new String[placesToCopy.length];
        int i = 0;
        for (int place : placesToCopy) {
            target[i] = source[place];
            i++;
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

//        int title = indexOf("Title", headers);
        if (q.getInputTitle().equals(event[mapping[title]])) {
//            System.out.println("title ok");
            String[][] conditions = q.getConditions();
            int i = 1;
            for (String[] condition : conditions) {
                if ("=".equals(condition[1])) {
//                    System.out.println("= ok");
//                    System.out.println(event[mapping[i]] +" "+ condition[2]);
                    if (event[mapping[i]].equals(condition[2])) {
                        System.out.println("condition ok");
                        return true;
                    } else {
                        return false;
                    }
                } else if ("!=".equals(conditions[1])) {
                    if (event[mapping[i]].equals(condition[2])) {
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
