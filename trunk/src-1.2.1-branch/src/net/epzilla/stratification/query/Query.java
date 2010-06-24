package net.epzilla.stratification.query;

import java.util.Arrays;
import java.util.HashSet;


/**
 * represents all components of a parsed query.
 */
public class Query {
    public static int NO_RETAIN = 0;
    public static int RETAIN_ON_TIME = 1;
    public static int RETAIN_ON_AMOUNT = 2;
    public static int RETAIN_ON_CONDITION = 3;
    private String[] retainTypes = null;
    private String queryString;
     private int stratum;
     private int cluster;

    /**
     * used when assigning clusters.
     * @return
     */
    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    private boolean independent;
//    private String clientId;

    public int getStratum() {
        return stratum;
    }

    /**
     * used in stratification.
     * @param stratum
     */
    public void setStratum(int stratum) {
        this.stratum = stratum;
    }

    public int getCluster() {
        return cluster;
    }

    /**
     * used in virtual clustering.
     * @param cluster
     */
    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    private int clientId;
    private int id;
    private String query = null;
    private String operation = null;

    public void setId(int id) {
        this.id = id;
    }

    /**
     * types of events to retain.
     * @return
     */
    public String[] getRetainingQueryTypes() {
        return this.retainTypes;
    }

    String[] outputs;
    String[] inputs;
    int retainCriterion;
    int retain;

    public Query(String query, String[] inputs, String[] outputs) {
        this(0, query, inputs, outputs, Query.NO_RETAIN, 0);
    }

    public Query(String query, String[] inputs, String[] outputs, int retainCriterion, int retain) {
        this(0, query, inputs, outputs, retainCriterion, retain);
    }



    public Query(int id, String query, String[] inputs, String[] outputs, int retainCriterion, int retain) {
        this.query = query;
        this.inputs = inputs;
        HashSet<String> types = new HashSet<String>();
        for (String item : inputs) {
            types.add(item);
        }

        this.retainTypes = new String[types.size()];
        int i = 0;
        for (String item : types) {
            retainTypes[i] = item;
            i++;
        }
        this.outputs = outputs;
        this.retainCriterion = retainCriterion;
        this.retain = retain;
    }

    public String getQuery() {
        return query;
    }

    public String getOperation() {
        return operation;
    }

    public String[] getOutputs() {
        return outputs;
    }

    public String[] getInputs() {
        return inputs;
    }

    /**
     * whether this contains a retain clause or not.
     * @return
     */
    public int getRetainCriterion() {
        return retainCriterion;
    }


    public int getId() {
        return id;
    }

    public int getRetain() {
        return retain;
    }

    @Override
    public String toString() {
        return "Query id=" + id +
                "Retain criterion: " + retainCriterion +
                "Retain for: " + retain +
                "\ninputs:\n" +  Arrays.toString(this.inputs) +
                "\noutputs:\n" +  Arrays.toString(this.outputs);
    }

    public int getClientId() {
        return this.clientId;  //To change body of created methods use File | Settings | File Templates.
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
