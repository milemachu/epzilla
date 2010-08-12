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
package net.epzilla.util;

import java.util.Arrays;

/**
 * disjoint sets structure.
 */
public class DisjointSets {

    private int[] struct = null;

    public DisjointSets(int size) {
        struct = new int[size];
        Arrays.fill(struct, -1);
    }



    // returns the root of element.
    public int find(int target) {
        if (struct[target] < 0) {
            return target;
        } else {
            struct[target] = find(struct[target]);
            return struct[target];                                       // Return the root
        }
    }


    // both must be roots of relevant sets.
    // nothing happens if two roots are equal.
    public void union(int rootOne, int rootTwo) {
        if (rootOne != rootTwo) {
            if (struct[rootTwo] < struct[rootOne]) {
                struct[rootOne] = rootTwo;
            } else if (struct[rootOne] == struct[rootTwo]) {

                struct[rootOne]--;
                struct[rootTwo] = rootOne;
            } else {
                struct[rootTwo] = rootOne;
            }
        }
    }

    // returns a printable version of internal array.
    public String toString() {
        return Arrays.toString(this.struct);
    }

}
