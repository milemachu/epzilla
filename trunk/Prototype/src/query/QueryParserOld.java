package query;

import net.epzilla.stratification.query.Query;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Nov 27, 2009
 * Time: 8:03:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryParserOld {
    String[] keywords = new String[]{"select", "retain", "findOccurrences", "count", "output", "where", "as"};
    String[] keywordTokens;
    String[] units = new String[]{"events", "minutes"};
    String[] unitTokens;
    String[] functions = new String[]{"min", "max", "avg", "sum", "total"};
    String[] functionTokens;
    String[] operatorTokens = new String[]{"+", "=", "-", "/", "*", "<", ">"};

    public QueryParserOld() {
        ArrayList<String> keyList = new ArrayList<String>(25);
        for (String key : keywords) {
//           keyList.add(key);
            keyList.add(" " + key + " ");
            keyList.add(" " + key + "\n");
        }
        keywordTokens = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keywordTokens[i] = keyList.get(i);
        }


        ArrayList<String> unitList = new ArrayList<String>();
        for (String unit : units) {
            unitList.add(" " + unit + " ");
            unitList.add(" " + unit + ";");
        }
        unitTokens = new String[unitList.size()];
        for (int i = 0; i < unitList.size(); i++) {
            unitTokens[i] = unitList.get(i);
        }


        ArrayList<String> functionList = new ArrayList<String>();
        for (String cal : functions) {
            functionList.add(cal + "(");
        }
        functionTokens = new String[functionList.size()];
        for (int i = 0; i < functionList.size(); i++) {
            functionTokens[i] = functionList.get(i);
        }
    }

    public Query parseString(String query) {

        String[] parts = query.trim().split(" ");
        boolean[] reswords = new boolean[parts.length];
        outer:
        for (int i = 0; i < parts.length; i++) {
            for (String keywordToken : keywordTokens) {
                if (parts[i].equalsIgnoreCase(keywordToken)) {
                    reswords[i] = true;
                    continue outer;
                }
            }
        }

        

        return null;
    }

    private String[][] getInputs(String q) {
        return null;

    }

    private String[][] getOutputs(String q) {
        return null;

    }


}
