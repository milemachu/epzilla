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
package org.epzilla.clusterNode.parser;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 10, 2010
 * Time: 1:41:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class SingleQueryResult {

    private String[] headers;
    private ArrayList<String[]> events = new ArrayList<String[]>();
    private boolean isEmpty = true;

    public ArrayList<String[]> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String[]> events) {
        this.events = events;
    }

    public void addEvent(String[] event) {
        this.events.add(event);
        this.isEmpty = false;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }


}
