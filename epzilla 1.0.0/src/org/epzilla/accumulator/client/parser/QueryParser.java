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
package org.epzilla.accumulator.client.parser;

import org.epzilla.accumulator.client.query.Query;
import org.epzilla.accumulator.client.query.QuerySyntaxException;
import org.epzilla.accumulator.client.query.QueryType;


/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 8:44:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class QueryParser {
    static String[] operators = new String[]{">=", "<=", "!=", "<", ">", "="};

    public Query parseQuery(String query) throws QuerySyntaxException {
        query = query.trim();
        Query q = new Query();
        if (query.indexOf("select") == 0 || query.indexOf("SELECT") == 0) {
            q.setType(QueryType.select);
        }
        // todo other types.

        int whereIndex = query.indexOf("WHERE");
        int outputIndex = query.indexOf("OUTPUT AS");

        String inputs = query.substring(7, whereIndex);
        String wherePart = query.substring(whereIndex + 6, outputIndex);
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
            i++;
        }

        q.setInputTitle(title);
        q.setInputs(in);


        String outTitle = output.trim();
        q.setOutputTitle(outTitle);

        // todo expand this to support composite predicates..
        String delim = null;

        for (String it : operators) {
            if (wherePart.contains(it)) {
                delim = it;
                break;
            }
        }

        String[][] conditions = new String[1][4];
        temp = wherePart.split(delim);
        String[] temp2 = null;
        temp2 = temp[0].trim().split("\\.");
        conditions[0][0] = temp2[1].trim();
        conditions[0][1] = delim;
        conditions[0][2] = temp[1].trim();
        q.setConditions(conditions);

        return q;
    }
}
