package org.epzilla.dispatcher;

import generatedObjectModels.triggerInfoObject;
import jstm.core.TransactedList;
import org.epzilla.dispatcher.RandomStringGenerator;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 20, 2010
 * Time: 8:57:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerManager {

    public static TransactedList<triggerInfoObject> triggers = new TransactedList<triggerInfoObject>();

    // Code For Testing Only -Dishan
    public static void acceptTriggerStream() {
        final java.util.Timer timer1 = new java.util.Timer();
        final Random gen = new Random();

        timer1.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                if (triggers != null) {
                    String item = String.valueOf(gen.nextInt());
                    triggerInfoObject obj = new triggerInfoObject();
                    obj.settriggerID(String.valueOf(count));
                    obj.settrigger(RandomStringGenerator.nextString());
                    triggers.add(obj);
                    count++;

                    if (count == 200)
                        timer1.cancel();
                }
            }
        }, 0, 50);
    }


}
