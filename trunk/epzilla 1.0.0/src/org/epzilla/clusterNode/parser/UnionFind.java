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

import java.util.HashSet;


public class UnionFind {

    private HashSet<String> set = new HashSet<String>();

    public void add(String item) {
        this.set.add(item);
    }

    public void add(String[] items) {
        for (String item : items) {
            this.set.add(item);
        }
    }

    public String[] toArray(String firstColumn) {
        String[] out = new String[set.size()];
        boolean exist = set.remove(firstColumn);
        int i = 0;
        if (exist) {
            out[0] = firstColumn;
            i = 1;
        }

        for (String item : set) {
            out[i] = item;
            i++;
        }
        if (exist) {
            set.add(firstColumn);
        }
        return out;
    }


    public HashSet<String> getUnion() {
        return this.set;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String x: this.set) {
            sb.append(x).append(", ");
        }
        return sb.toString();
    }

}
