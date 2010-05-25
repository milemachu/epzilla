package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.InvalidSyntaxException;
import org.epzilla.util.Logger;


public class RestructuringDaemon {
    private static boolean alive = false;

    public static void start() {
        alive = true;
        Thread t = new Thread() {
            public void run() {
                while (alive) {

                    try {
                        if (alive) {
                            SystemRestructure.getInstance().restructureSystem();
                        }
                    } catch (InvalidSyntaxException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        Logger.error("", e);
                    }
                }
            }
        };
        t.start();
    }

    public static void stop() {
        alive = false;
    }


}
