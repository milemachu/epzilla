package net.epzilla.stratification.structuring;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 10, 2010
 * Time: 4:15:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DependencyGraph {

    public static int BLANK = -1;
    public static int END = -2;

    
    int n;
    int[] queryMap;
    boolean[][] graph;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(int[] queryMap) {
        this.queryMap = queryMap;
    }

    public boolean[][] getGraph() {
        return graph;
    }

    public void setGraph(boolean[][] graph) {
        this.graph = graph;
    }

    public ClientStratifier getS() {
        return s;
    }

    public void setS(ClientStratifier s) {
        this.s = s;
    }

    private ClientStratifier s;

    public DependencyGraph() {

    }

    public DependencyGraph(ClientStratifier stratifier) {
       this.s = stratifier;
    }

    public void setClientStratifier(ClientStratifier cs) {
        this.s = cs;
    }

    public boolean synchronize() {
        return false;
    }



}
