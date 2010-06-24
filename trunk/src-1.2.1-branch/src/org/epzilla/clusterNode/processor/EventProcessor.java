package org.epzilla.clusterNode.processor;


import org.epzilla.clusterNode.parser.QueryExecuter;
import org.epzilla.clusterNode.parser.QueryParser;
import org.epzilla.clusterNode.query.QuerySyntaxException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * encapsulates a query executor. assigned with a particular client.
 */
public class EventProcessor {
    private ConcurrentLinkedQueue cq = new ConcurrentLinkedQueue();
    private Hashtable<String, QueryExecuter> clientExecutors = new Hashtable();


    private static EventProcessor instance = new EventProcessor();

    static boolean mt = false;

    public static EventProcessor getInstance() {

        return instance;
    }

    public void addTrigger(String trigger, String clientId) throws QuerySyntaxException {
        org.epzilla.util.Logger.log("adding trigger" + clientId);
        QueryExecuter q = clientExecutors.get(clientId);
        if (q == null) {
            q = new QueryExecuter();
            clientExecutors.put(clientId, q);
        }
        q.addQuery(new QueryParser().parseQuery(trigger));
    }


    public void reloadTriggers(List<String> triggers, String clientId) throws QuerySyntaxException {
        QueryExecuter q = new QueryExecuter();
        clientExecutors.put(clientId, q);

        for (String t : triggers) {
            q.addQuery(new QueryParser().parseQuery(t));
        }
    }

    public void cleanTriggers(String clientId) {
        try {
            clientExecutors.remove(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addTriggers(ArrayList<String> triggers, String clientId) throws QuerySyntaxException {
        QueryExecuter q = clientExecutors.get(clientId);
        if (q == null) {
            q = new QueryExecuter();
            clientExecutors.put(clientId, q);
        }

        for (String t : triggers) {
            q.addQuery(new QueryParser().parseQuery(t));
        }
    }

    public String processEvent(String event) {
        // content, client id, event id
        StringTokenizer tok = new StringTokenizer(event, ":");
        String cont = tok.nextToken();
        String clientId = tok.nextToken();
        String eventId = tok.nextToken();
        org.epzilla.util.Logger.log("processing event: " + clientId + " : " + eventId);
        org.epzilla.util.Logger.log("event: " + cont);

        QueryExecuter q = this.clientExecutors.get(clientId);
        if (q != null) {
            String res = q.processEvent(cont);
            StringBuilder sb = new StringBuilder("");
            sb.append(res);
            sb.append(":");
            sb.append(clientId);
            sb.append(":");
            sb.append(eventId);

            return sb.toString();
        }

        return ":" + clientId + ":" + eventId;
    }


}
