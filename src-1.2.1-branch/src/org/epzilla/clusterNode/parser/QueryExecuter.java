package org.epzilla.clusterNode.parser;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.HashSet;

import org.epzilla.clusterNode.query.Query;
import org.epzilla.clusterNode.query.QuerySyntaxException;
import org.epzilla.util.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 9, 2010
 * Time: 9:37:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryExecuter {
    private Vector<Query> queries = new Vector<Query>();
    private Vector<Query> mirror = new Vector<Query>();
    private static Object lock = new Object();
    private QueryParser parser = new QueryParser();
    private HashSet<String> querySet = new HashSet<String>();


    public void addQuery(Query q) {
        synchronized (lock) {
            this.mirror.add(q);
            this.queries = (Vector<Query>) this.mirror.clone();
        }
    }

    public void addQuery(String query) throws QuerySyntaxException {
        if (!querySet.contains(query)) {
            querySet.add(query);
            Query q = parser.parseQuery(query);
            synchronized (lock) {
                this.mirror.add(q);
                this.queries = (Vector<Query>) this.mirror.clone();
            }
        }
    }


    public void removeQuery(Query q) {
        synchronized (lock) {
            this.mirror.remove(q);
            this.queries = (Vector<Query>) this.mirror.clone();
        }
    }

    public static void main(String[] args) throws QuerySyntaxException {
        QueryParser qp = new QueryParser();
        Query q = qp.parseQuery("SELECT avg(StockTrades.price), StockTrades.last  , min(StockTrades.price), StockTrades.amount RETAIN 10 EVENTS OUTPUT StkTrades");
         QueryExecuter qe = new QueryExecuter();
        qe.addQuery(q);
        System.out.println("ev:" +qe.processEvent("StockTrades\nprice,amount\n100,2\n23,2"));

    }

    public String processEvent(String event) {
        String[] lines = event.split("\n");
        String[] eventHeaders = lines[1].split(",");
        HashMap<String, Integer> hash = new HashMap();
        int i = 0;

        for (String item : eventHeaders) {
            hash.put(item, i);
            i++;
        }

        String title = lines[0].trim();
        StringBuilder sb = new StringBuilder("");
        Vector<Query> queryRef = this.queries;
        String[][] csv = new String[lines.length - 2][eventHeaders.length];
        for (i = 0; i < csv.length; i++) {
            csv[i] = new String[eventHeaders.length];
        }


        for (i = 2; i < lines.length; i++) {
            String[] temp = lines[i].split(",");
            for (int j = 0; j < temp.length; j++) {
                csv[i - 2][j] = temp[j];
            }
        }

        HashMap<String, Integer> inputPositions = new HashMap();

        i = 0;
        for (String head : eventHeaders) {
            inputPositions.put(head, i);
        }

        for (Query q : queryRef) {
            if (title.equals(q.getInputTitle())) {
                
                sb.append(q.getOutputTitle());
                sb.append("\n");
                String[] resHeaders = q.getResultHeaders();
                i = 0;
                int[] ops = q.getOperations();
                for (String input : q.getInputs()) {
                    Integer pos = inputPositions.get(input);
                    if (pos == null) {
                        sb.append(resHeaders[i]).append("=[N/A]\n");
                    } else {
                        double store = 0;
                        int index = pos;
                        switch (ops[i]) {
                            case Query.avg:
                                for (int j = 0; j < csv.length; j++) {
                                    store += Double.parseDouble(csv[j][index]);
                                }
                                sb.append(resHeaders[i]).append("=").append(store / csv.length);
                                break;
                            case Query.min:
                                if (csv.length > 0) {
                                    store = Double.parseDouble(csv[0][index]);
                                }
                                double td;
                                for (int j = 0; j < csv.length; j++) {
                                    td = Double.parseDouble(csv[j][index]);
                                    if (td < store) {
                                        store = td;
                                    }
                                }
                                sb.append(resHeaders[i]).append("=").append(store );

                                break;
                            case Query.max:
                                    if (csv.length > 0) {
                                    store = Double.parseDouble(csv[0][index]);
                                }
                                
                                for (int j = 0; j < csv.length; j++) {
                                    td = Double.parseDouble(csv[j][index]);
                                    if (td > store) {
                                        store = td;
                                    }
                                }
                                sb.append(resHeaders[i]).append("=").append(store );
                                
                                break;
                            case Query.sum:
                                for (int j = 0; j < csv.length; j++) {
                                    store += Double.parseDouble(csv[j][index]);
                                }
                                sb.append(resHeaders[i]).append("=").append(store);

                                break;
                            case Query.pass:
                                String[] cols = new String[csv.length];
                                for (int j = 0; j < csv.length; j++) {
                                    cols[j] = csv[j][index];
                                }
                                sb.append(resHeaders[i]).append("=").append(Arrays.toString(cols));

                                break;
                            case Query.copyRow:
                        }
                        sb.append("\n");
                    }
                    i++;
                }
//                String[][] result = new String[q.getInputs().length][2];


            }
        }

        return sb.toString();
    }


    @Deprecated
    public String processEvents(String events) {
        try {
            String[] csvRows = events.split("\n");
            String[] headers = csvRows[0].split(",");

            String[][] eventGrid = new String[csvRows.length - 1][];
            UnionFind uf = new UnionFind();
            ResultAssembler ra = new ResultAssembler();
            ra.setUnionFind(uf);
            uf.add("Title");
            Vector<Query> queryRef = this.queries;
            boolean[] added = new boolean[queryRef.size()];
            int queryIndex = 0;
            for (int i = 1; i <= eventGrid.length; i++) {
                eventGrid[i - 1] = csvRows[i].split(",");
                queryIndex = 0;
                for (Query q : queryRef) {
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
        Logger.log("");
    }

    private void print(Object[] x) {
        for (Object i : x) {
            System.out.print(i.toString() + ", ");
        }
        Logger.log("");
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
