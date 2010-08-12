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
package org.epzilla.accumulator.global;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 11:40:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DerivedEvent extends Event {
    private ArrayList<Long> sourceEvents = null;
    private long srcId;

    public long getSrcId() {
        return srcId;
    }

    public void setSrcId(long srcId) {
        this.srcId = srcId;
    }

    public DerivedEvent() {

    }

    public ArrayList<Long> getSourceEvents() {
        return sourceEvents;
    }

    public void addSourceEventDescription(long sourceEvent) {
        if (sourceEvents == null) {
            sourceEvents = new ArrayList<Long>();
        }
        this.sourceEvents.add(sourceEvent);
    }
}
