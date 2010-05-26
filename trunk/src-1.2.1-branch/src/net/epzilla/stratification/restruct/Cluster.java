package net.epzilla.stratification.restruct;

import java.util.Comparator;



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
