package net.epzilla.stratification.query;

import net.epzilla.stratification.query.InvalidSyntaxException;

import java.util.HashSet;


public class BasicQueryParser implements QueryParser {

    QueryType type;

    String inputString, outputString;
    String[] inputs;
    String[] outputs;
    String[][] inputMap;
    String[][] outputMap;

    private void clear() {
        this.type = null;
        this.inputs = this.outputs = null;
        this.inputString = this.outputString = null;
        this.inputMap = this.outputMap = null;
    }

    public Query parseString(String query) throws InvalidSyntaxException {
        clear();
        query = query.trim();
        if (query.length() == 0) {
            type = QueryType.empty;
        } else if (query.startsWith("//")) {
            type = QueryType.comment;
        } else {
            if (query.startsWith("SELECT")) {
                type = QueryType.select;
            } else if (query.startsWith("COUNT")) {
                type = QueryType.count;
            } else if (query.startsWith("FINDOCCURRENCES")) {
                type = QueryType.findOcurrences;
            } else {
                throw new InvalidSyntaxException();
            }

            int startpos = LanguageDefinitions.keywords[type.ordinal()].length();
            inputString = query.substring(startpos, nextKeywordPosition(query, startpos)).trim();

            int outindex = query.indexOf(" OUTPUT AS ");
            if (outindex < 0) {
                outindex = query.indexOf(" OUTPUT ");
                outindex += 8;
            } else {
                outindex += 11;
            }
            outputString = query.substring(outindex, query.length() - 1);

            parseInputs();
            parseOutputs();
        }
        int retainCriterion = 0;
        int retainAmount = 0;
        if ((retainCriterion = getRetainCriterion(query)) != Query.NO_RETAIN) {
            retainAmount = getRetainAmount(query);
        }
        createInputMap();
        createOutputMap();

        Query q =  new Query(0, query, inputMap, outputMap, retainCriterion, retainAmount);
        q.setQueryString(query);
        return q;
    }

    private void createInputMap() {
        inputMap = new String[inputs.length][2];
        for (int i = 0; i < inputs.length; i++) {
            int x = inputs[i].indexOf('.');
            if (x > 0) {
                inputMap[i][0] = inputs[i].substring(0, x).trim();
                inputMap[i][1] = inputs[i].substring(x + 1, inputs[i].length()).trim();

            } else {
                inputMap[i][0] = inputs[i].trim();
                inputMap[i][1] = null;
            }
        }
    }

    private void createOutputMap() {
        outputMap = new String[outputs.length][2];
        for (int i = 0; i < outputs.length; i++) {
            String[] parts = outputs[i].split("\\.");
            outputMap[i][0] = parts[0];
            outputMap[i][1] = parts[1];
        }
    }

    private int getRetainCriterion(String query) {
        if (query.contains(" RETAIN ")) {
            if (query.contains(" EVENTS ")) {
                return Query.RETAIN_ON_AMOUNT;
            } else if (query.contains(" MINUTES ")) {
                return Query.RETAIN_ON_TIME;
            }
        } else {
            return Query.NO_RETAIN;
        }
        return Query.NO_RETAIN;
    }

    private int getRetainAmount(String query) {
        int i = query.indexOf(" RETAIN ");
        String val = "";
        i += 8;
        for (; query.charAt(i) == ' '; i++) ; // remove whitespaces.

        for (; query.charAt(i) != ' '; i++) {
            val += query.charAt(i);
        }
        return Integer.parseInt(val);
    }

    private void parseInputs() {
        for (String token : LanguageDefinitions.functionTokens) {
            inputString = inputString.replaceAll(token, " ");
        }
        inputString = inputString.replaceAll("\\)", ",");
        for (String op: LanguageDefinitions.operators) {
            inputString = inputString.replaceAll(op, ",");
        }
        inputs = inputString.trim().split(",");
        
        HashSet<String> validStrings = new HashSet<String>();
        for (String str : inputs) {
            str = str.trim();
            if (str.length() > 0) {
                validStrings.add(str);
            }
        }
        inputs = validStrings.toArray(new String[validStrings.size()]);
    }

    private void parseOutputs() {
        outputs = outputString.split(",");
        HashSet<String> out = new HashSet<String>();
        for (String x : outputs) {
            x = x.trim();
            if (x.length() > 0) {
                out.add(x);
            }
        }
        outputs = out.toArray(new String[out.size()]);
    }

    private int nextKeywordPosition(String query, int startAt) {
        int min = 1000000;
        for (String key : LanguageDefinitions.keywords) {
            int now = query.indexOf(key, startAt);
            if (now > 0 && now < min) {
                min = now;
            }
        }
        return min;
    }


}
