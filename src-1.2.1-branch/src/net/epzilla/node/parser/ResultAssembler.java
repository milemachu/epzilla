package net.epzilla.node.parser;

import java.util.ArrayList;


public class ResultAssembler {

    private ArrayList<SingleQueryResult> results = new ArrayList<SingleQueryResult>();
    private UnionFind uf = null;


    public ArrayList<SingleQueryResult> getResults() {
        return results;
    }

    public void addResult(SingleQueryResult sqr) {
        this.results.add(sqr);
    }

    public StringBuilder assembleResults() {
        StringBuilder sb = new StringBuilder("");
        String[] headers = this.uf.toArray("Title");
        boolean middle = false;
        for (String head : headers) {
            if (middle) {
                sb.append(",");
            }
            middle = true;
            sb.append(head);
        }
        int[] mapping = new int[headers.length];
        for (SingleQueryResult res : results) {
            mapping = Util.getMapping(headers, res.getHeaders(), false);
            middle = false;
            for (String[] event : res.getEvents()) {
                middle = false;
                for (int i : mapping) {
                    if (middle) {
                        sb.append(",");
                    }
                    middle = true;
                    if (i > -1) {
                        sb.append(event[i]);
                    } else {
//                        sb.append("");
                    }
                }
                sb.append("\n");
            }
        }

        return sb;
    }

    public void setResults(ArrayList<SingleQueryResult> results) {
        this.results = results;
    }

    public UnionFind getUnionFind() {
        return uf;
    }

    public void setUnionFind(UnionFind uf) {
        this.uf = uf;
    }
}
