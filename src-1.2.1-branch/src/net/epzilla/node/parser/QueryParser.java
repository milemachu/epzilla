package net.epzilla.node.parser;

import net.epzilla.node.query.Query;
import net.epzilla.node.query.QuerySyntaxException;
import net.epzilla.node.query.QueryType;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 8:44:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryParser {
    static String[] operators = new String[]{ ">=",  "<=", "!=", "<", ">", "="};

    // extremely dumb parser.
    public Query parseQuery(String query) throws QuerySyntaxException {
        query = query.trim();
        Query q = new Query();
        if (query.indexOf("select") == 0 || query.indexOf("SELECT") == 0) {
            q.setType(QueryType.select);
        }
        // todo other types.

        int whereIndex = query.indexOf("WHERE");
        int retainIndex = query.indexOf("RETAIN");
        int outputIndex = query.indexOf("OUTPUT");

        String inputs = query.substring(7, whereIndex);
//        int temp = outputIndex;
//        if (query.contains("RETAIN")) {
//
//        }
        
        String where = query.substring(whereIndex + 6, retainIndex);
        String output = query.substring(outputIndex + 10, query.length());

        String[] parts = inputs.trim().split(",");
        String[] temp = null;
        String[] in = new String[parts.length];
        int i = 0;
        String title = null;
        for (String item : parts) {
            temp = item.split("\\.");
            in[i] = temp[1].trim();
            if (title == null) {
                title = temp[0].trim();
            }
//            in[i][1] = temp[1].trim();
            i++;
        }

       q.setInputTitle(title);
       q.setInputs(in);



        String outTitle = output.trim();
//        parts = output.trim().split(",");
//        String[] out = new String[parts.length];
//        i = 0;
//        for (String item : parts) {
//            if (item.contains("\\.")) {
//                temp = item.split("\\.");
//                out[i] = temp[1].trim();
//                out[i][1] = temp[1].trim();
//            } else {
//                out[i][0] = item.trim();
//                out[i] = in[i];
//            }
//            i++;
//        }

        q.setOutputTitle(outTitle);

//        parts = where.trim().split("AND");
        // todo expand this to support composite predicates..
        String delim = null;

        for (String it : operators) {
            if (where.contains(it)) {
                delim = it;
                break;
            }
        }

        String[][] conditions = new String[1][4];
//        System.out.println(query);
//        System.out.println(inputs);
//        System.out.println(where);
//        System.out.println(output);
        temp = where.split(delim);
        String[] temp2 = null;
            temp2 = temp[0].trim().split("\\.");
//            conditions[0][0] = temp2[0].trim();
            conditions[0][0] = temp2[1].trim();
            conditions[0][1] = delim;
            conditions[0][2] = temp[1].trim();
        q.setConditions(conditions);


        return q;
    }
}
