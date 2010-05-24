package net.epzilla.node.query;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 8:44:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class Query {

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

    QueryType type = null;
    String inputTitle = null;
    String outputTitle = null;
    String[] inputs = null;
    String[] outputs = null;

    public String[] getConditionLeft() {
        return conditionLeft;
    }

    public void setConditionLeft(String[] conditionLeft) {
        this.conditionLeft = conditionLeft;
    }

    String[] conditionLeft = null;
//    String[][] inputs = null;
    //    String[][] outputs = null;
    String[][] conditions = null;
//    int[] predicateValues = null;
//    String[] srcEvents = null;
//    String[] conditionEvents = null;

    static Pattern p = Pattern.compile("(\\d)+");

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

//        sb.append(" Outputs: [");
//        sb.append(this.outputTitle);
//        sb.append(": ");
//        for (String x: this.outputs) {
//           sb.append(x).append(" ");
//        }
//        sb.append("] ");
        return sb.toString();
    }


}
