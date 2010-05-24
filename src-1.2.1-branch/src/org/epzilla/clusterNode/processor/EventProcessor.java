package org.epzilla.clusterNode.processor;

import net.epzilla.node.parser.QueryExecutor;
import net.epzilla.node.parser.QueryParser;
import net.epzilla.node.query.QuerySyntaxException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 24, 2010
 * Time: 1:37:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventProcessor {
  private  ConcurrentLinkedQueue cq = new ConcurrentLinkedQueue();
  private Hashtable<String, QueryExecutor> clientExecutors = new Hashtable();


    private static EventProcessor instance = new EventProcessor();

   static boolean mt = false;

    public static EventProcessor getInstance() {

        return instance;
    }

    public void addTrigger(String trigger, String clientId) throws QuerySyntaxException {
        QueryExecutor q = clientExecutors.get(clientId);
        if (q == null) {
            q = new QueryExecutor();
            clientExecutors.put(clientId, q);
        }
        q.addQuery(new QueryParser().parseQuery(trigger));
    }

    public void addTriggers(ArrayList<String> triggers, String clientId) throws QuerySyntaxException {
           QueryExecutor q = clientExecutors.get(clientId);
        if (q == null) {
            q = new QueryExecutor();
            clientExecutors.put(clientId, q);
        }

        for (String t: triggers) {
            q.addQuery(new QueryParser().parseQuery(t));
        }
    }

    public String processEvent(String event) {
        // content, client id, event id
        StringTokenizer tok = new StringTokenizer(event, ":");
        String cont =                                    tok.nextToken();
        String clientId = tok.nextToken();
        String eventId = tok.nextToken();
        QueryExecutor q = this.clientExecutors.get(clientId);
        if (q != null) {
            String res = q.processEvents(cont);
            StringBuilder sb = new StringBuilder("");
            sb.append(res);
            sb.append(":");
            sb.append(clientId);
            sb.append(":");
            sb.append(eventId);
            
            return sb.toString();
        }

        return ":"+ clientId + ":" + eventId;
    }

    

}
