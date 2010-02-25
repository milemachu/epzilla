package net.epzilla.stratification.query;


public class Query {
    public static int NO_RETAIN = 0;
    public static int RETAIN_ON_TIME = 1;
    public static int RETAIN_ON_AMOUNT = 2;
    public static int RETAIN_ON_CONDITION = 3;

    int id;
    String query = null;
    String operation = null;

    public void setId(int id) {
        this.id = id;
    }

    String[][] outputs;
    String[][] inputs;
    int retainCriterion;
    int retain;

    public Query(String query, String[][] inputs, String[][] outputs) {
        this(0, query, inputs, outputs, Query.NO_RETAIN, 0);
    }

    public Query(String query, String[][] inputs, String[][] outputs, int retainCriterion, int retain) {
        this(0, query, inputs, outputs, retainCriterion, retain);
    }

    public Query(int id, String query, String[][] inputs, String[][] outputs, int retainCriterion, int retain) {
        this.query = query;
        this.inputs = inputs;
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

    public String[][] getOutputs() {
        return outputs;
    }

    public String[][] getInputs() {
        return inputs;
    }

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
                "Retain for: " + retain;
    }
}
