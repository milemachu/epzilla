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
package org.epzilla.accumulator.stm;

import org.epzilla.accumulator.generated.SharedDerivedEvent;
import org.epzilla.accumulator.global.DerivedEvent;


/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 7, 2010
 * Time: 10:18:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventConverter {

    public static SharedDerivedEvent toSharedDerivedEvent(DerivedEvent e) {
        SharedDerivedEvent sde = new SharedDerivedEvent();
        sde.setid(e.getEventId());
        sde.setclientId(e.getClientId());
        sde.setcontent(e.getContent());
        sde.setsrcId(e.getSrcId());
        return sde;
    }

     public static DerivedEvent toDerivedEvent(SharedDerivedEvent e) {
        DerivedEvent sde = new DerivedEvent();
         sde.setClientId(e.getclientId());
         sde.setSrcId(e.getsrcId());
         sde.setContent(e.getcontent());
         sde.setEventId(e.getid());
        return sde;
    }
}
