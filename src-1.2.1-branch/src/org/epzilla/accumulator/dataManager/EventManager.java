package org.epzilla.accumulator.dataManager;

import org.epzilla.accumulator.userinterface.AccumulatorUIControler;

import java.util.List;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 23, 2010
 * Time: 11:12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager {

    private static HashMap<String, Event> eventList = new HashMap<String, Event>();
    private static int numberofClusters = 2;
    private static int processed=0;


    public static void setEventSegement(String eventResultSegment) {
        try {
            String eventID = getEventID(eventResultSegment);

            if (eventList.containsKey(eventID)) {
                eventList.get(eventID).addSegment(eventResultSegment);
                if (eventList.get(eventID).getSegmentCount() == numberofClusters) {
                    sendNotificationtoClient();
                    processed++;
                    AccumulatorUIControler.appendEventprocessed(String.valueOf(processed));
                    eventList.remove(eventID);
                }

            } else {
                Event temp = new Event();
                temp.setEventID(eventID);
                temp.addSegment(eventResultSegment);
                eventList.put(eventID, temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getEventID(String event) {
        String eventID = "";
        StringTokenizer st = new StringTokenizer(event, ":");
        st.nextToken();  //Event
        st.nextToken(); //ClientID
        eventID = st.nextToken(); //EventID
        return eventID;
    }

    public static void sendNotificationtoClient() {

    }

}
