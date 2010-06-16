package net.epzilla.stratification.restruct;

import net.epzilla.stratification.query.InvalidSyntaxException;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.util.HashSet;
import java.util.Hashtable;


public class RestructuringDaemon {
    private static boolean alive = false;
    private static boolean restructuring = false;
    public static int RESTRUCTURING_WAITING_TIME = 50000;


    public static boolean isRestructuring() {
        return restructuring;
    }

    
    public static void setRestructuring(boolean restructuring) {
        RestructuringDaemon.restructuring = restructuring;
    }

    public static void start() {
        alive = true;
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(RestructuringDaemon.RESTRUCTURING_WAITING_TIME);
                } catch (InterruptedException e) {
                    Logger.error("error sleeping", e);
                }

                while (alive) {
                    System.out.println("SystemRestructure....");
                    long st = System.currentTimeMillis();
                    try {

                        HashSet<String> disp = LeaderElectionInitiator.getDispatchers();

                        for (String ip: disp) {
                            
                        }

                        SystemRestructure.getInstance().restructureSystem();
                        SystemRestructure.getInstance().sendRestructureCommands();
                    } catch (Exception e) {
                        Logger.error("Error: invalid syntax? ", e);

                    }
                    System.out.println("SystemRestructure ended....." + (System.currentTimeMillis()  - st) + " ms");



                    try {
                        Thread.sleep(RestructuringDaemon.RESTRUCTURING_WAITING_TIME);
                    } catch (InterruptedException e) {
                        Logger.error("error sleeping", e);
                        
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
