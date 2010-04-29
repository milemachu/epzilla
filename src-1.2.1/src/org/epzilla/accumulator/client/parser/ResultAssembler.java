package org.epzilla.accumulator.client.parser;


import java.util.ArrayList;


public class ResultAssembler {

    private ArrayList<SingleQueryResult> results = new ArrayList<SingleQueryResult>();
    private UnionFind finder = null;


    public ArrayList<SingleQueryResult> getResults() {
        return results;
    }

    public void addResult(SingleQueryResult sqr) {
        this.results.add(sqr);
    }

    public StringBuilder assembleResults() {
        StringBuilder builder = new StringBuilder("");
        String[] headers = this.finder.toArray("Title");
        boolean middle = false;
        for (String head : headers) {
            if (middle) {
                builder.append(",");
            }
            middle = true;
            builder.append(head);
        }
        builder.append("\n");
        int[] mapping = new int[headers.length];
        for (SingleQueryResult res : results) {
            mapping = Util.getMapping(headers, res.getHeaders(), false);
            middle = false;
            for (String[] event : res.getEvents()) {
                middle = false;
                for (int i : mapping) {
                    if (middle) {
                        builder.append(",");
                    }
                    middle = true;
                    if (i > -1) {
                        builder.append(event[i]);
                    } else {
                        // not needed for basic version.
                        builder.append("-");
                    }
                }
                builder.append("\n");
            }
        }

        return builder;
    }

    public void setResults(ArrayList<SingleQueryResult> results) {
        this.results = results;
    }

    public UnionFind getUnionFind() {
        return finder;
    }

    public void setUnionFind(UnionFind uf) {
        this.finder = uf;
    }
}
