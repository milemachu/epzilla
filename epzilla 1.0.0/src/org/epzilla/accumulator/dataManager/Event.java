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
package org.epzilla.accumulator.dataManager;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 23, 2010
 * Time: 12:28:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Event {
    private String eventID="";
    private ArrayList segments = new ArrayList();

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void addSegment(String segment)
    {
        segments.add(segment);
    }

    public int getSegmentCount()
    {
      return segments.size();  
    }

}
