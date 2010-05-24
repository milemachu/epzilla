package net.epzilla.node;

import net.epzilla.node.query.Query;
import net.epzilla.node.query.QuerySyntaxException;
import net.epzilla.node.parser.QueryParser;
import net.epzilla.node.parser.QueryExecuter;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 8, 2010
 * Time: 1:34:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws QuerySyntaxException {
//         String query = EventTriggerGenerator.getNextTrigger();
         String query = "SELECT CarDetails.Year WHERE CarDetails.Year=1994 RETAIN 11 events OUTPUT AS CarDetailsResults";
         String query2 = "SELECT CarDetails.Model WHERE CarDetails.Year=1994 RETAIN 10 minutes OUTPUT AS CarDetailsResults";
        System.out.println(query);
        QueryParser qp = new QueryParser();
        Query q = qp.parseQuery(query);
        QueryExecuter qe = new QueryExecuter();
        qe.addQuery(q);
        qe.addQuery(qp.parseQuery(query2));
        String x  = qe.processEvents("Title,Model,Year\nCarDetails,Civic,1994\nCarDetails,Corolla,1994\nBusses,Tata,1994\nCarDetails,Corona,1994");
        System.out.println("pROCESSED:" + x);
        System.out.println(q);
        System.out.println(EventTriggerGenerator.getNextEvent());

    }


}
