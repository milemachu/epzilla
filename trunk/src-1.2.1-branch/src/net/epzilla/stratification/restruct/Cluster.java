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
package net.epzilla.stratification.restruct;

import java.util.Comparator;


/**
 * represents a virtual cluster of queries.
 */

public class Cluster {

    public static class ClusterComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Cluster c1 = (Cluster) o1;
            Cluster c2 = (Cluster) o2;
            if (c1.getLoad() > c2.getLoad()) {
                return -1;
            } else if (c1.getLoad() == c2.getLoad()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    int stratum;
    int cluster;
    String clientId;
    int realStratum;
    int realCluster;
    int load;
    boolean independent;

    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getStratum() {
        return stratum;
    }

    public void setStratum(int stratum) {
        this.stratum = stratum;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getRealStratum() {
        return realStratum;
    }

    public void setRealStratum(int realStratum) {
        this.realStratum = realStratum;
    }

    public int getRealCluster() {
        return realCluster;
    }

    public void setRealCluster(int realCluster) {
        this.realCluster = realCluster;
    }
}
