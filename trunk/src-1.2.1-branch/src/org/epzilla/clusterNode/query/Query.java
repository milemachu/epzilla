package org.epzilla.clusterNode.query;

import java.util.Hashtable;
import java.util.regex.Pattern;


/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 8:44:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class Query implements Comparable {
    // all digits regexp.
    public static final int pass = 0;
    public static final int sum = 1;
    public static final int avg = 2;
    public static final int min = 3;
    public static final int max = 4;
    public static final int copyRow = 5;

    public static Hashtable<String, Integer> operatorMap = new Hashtable();

    static {
        operatorMap.put("pass", 0);
        operatorMap.put("sum", 1);
        operatorMap.put("avg", 2);
        operatorMap.put("min", 3);
        operatorMap.put("max", 4);
        operatorMap.put("copyRow", 5);
    }

    public int[] getOperations() {
        return operations;
    }

    public void setOperations(int[] operations) {
        this.operations = operations;
    }

    static Pattern p = Pattern.compile("(\\d)+");

    private String[] resultHeaders = null;

    public String[] getResultHeaders() {
        return resultHeaders;
    }

    public void setResultHeaders(String[] resultHeaders) {
        this.resultHeaders = resultHeaders;
    }

    private QueryType type = null;
    private String inputTitle = null;
    private String outputTitle = null;
    private String[] inputs = null;
    private String[] outputs = null;
    private String[] conditionLeft = null;
    private String[][] conditions = null;
    private int[] operations = null;

    public String getInputTitle() {
        return inputTitle;
    }

    public void setInputTitle(String inputTitle) {
        this.inputTitle = inputTitle;
    }

    public String getOutputTitle() {
        return outputTitle;
    }

    public void setOutputTitle(String outputTitle) {
        this.outputTitle = outputTitle;
    }

    public String[] getInputs() {
        return inputs;
    }

    public void setInputs(String[] inputs) {
        this.inputs = inputs;
    }

    public String[] getOutputs() {
        return outputs;
    }

    public void setOutputs(String[] outputs) {
        this.outputs = outputs;
    }


    public String[] getConditionLeft() {
        return conditionLeft;
    }

    public void setConditionLeft(String[] conditionLeft) {
        this.conditionLeft = conditionLeft;
    }


    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }


    public String[][] getConditions() {
        return conditions;
    }

    public void setConditions(String[][] conditions) {
        this.conditions = conditions;
        this.conditionLeft = new String[conditions.length];
        int i = 0;
        for (String[] x : conditions) {
            this.conditionLeft[i] = x[0];
        }

    }


    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Type: ").append(this.type.toString());
        sb.append(" Inputs: [");
        sb.append(this.inputTitle);
        sb.append(": ");
        for (String x : this.inputs) {
            sb.append(x).append("  ");
        }
        sb.append("] ");

        sb.append(" Conditions: [");
        for (String[] x : this.conditions) {
            sb.append(x[0]).append(" ").append(x[1]).append(" ").append(x[2]);
        }
        sb.append("] ");
        sb.append(" output [").append(this.outputTitle).append("]");

        return sb.toString();
    }


    @Override
    public int compareTo(Object o) {
        Query q = (Query) o;
        return q.getOutputTitle().compareTo(this.getOutputTitle());

    }
}
