/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.clusterNode.parser;

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
