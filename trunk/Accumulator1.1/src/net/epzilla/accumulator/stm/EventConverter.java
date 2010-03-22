package net.epzilla.accumulator.stm;

import net.epzilla.accumulator.global.DerivedEvent;
import net.epzilla.accumulator.generated.SharedDerivedEvent;

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
