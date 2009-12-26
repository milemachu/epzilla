package scheduling;

public class ScheduledGenerator {
    static Integer[] randomValues = new Integer[1200];
    static VirtualStm stm;
    static double unitTimeMilliSec;

    public static void main(String[] args) {
        stm = new VirtualStm();

        for (int i = 0; i < randomValues.length; i++) {
            randomValues[i] = (int) (Math.random() * 1000);
        }
        new ScheduledGenerator().multiThreadedInit(10);
        simulate(300, 10);
    }

    // to be executed only after the sleep time is calculated.
    static void simulate(int numEventsPerSec, int secondsToRun) {
        try {
// unit time in mseconds
            double sleepTime = (1000.0 / numEventsPerSec) - unitTimeMilliSec;
//        String[] utime = Double.toString(sleepTime).split("\\.");
//        System.out.println(utime[1]);
            int sleepmsecs = (int) sleepTime;
            int nanosecs = (int) ((sleepTime - sleepmsecs) * 1000 * 1000);

            Integer[] vals = new Integer[numEventsPerSec];
            for (int i = 0; i < numEventsPerSec; i++) {
                vals[i] = (int) (Math.random() * 1000);
            }
            long started = System.currentTimeMillis();
            for (int i = 0; i < secondsToRun; i++) {
                for (int j = 0; j < numEventsPerSec; j++) {
                    stm.add(vals[j]);
                    Thread.sleep(sleepmsecs, nanosecs);
                }
            }
            long ended = System.currentTimeMillis();
            System.out.println("total time spent for 2nd part of simulation (msec):" + (ended - started));
            System.out.println("expected value msec:" + (secondsToRun*1000));
            

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static void singleThreadedInit(int numSecondsToExecute) {
        try {

//                                         long start = System.currentTimeMillis();
            long end = System.currentTimeMillis() + numSecondsToExecute * 1000;
            int rounds = 0;
            while (System.currentTimeMillis() < end) {
                for (int i = 0; i < 1000; i++) {
                    stm.add(randomValues[i]);
                }
                rounds++;
            }
            System.out.println("rounds:" + rounds);
            unitTimeMilliSec = (numSecondsToExecute * 1000) / ((double) rounds * 1000);
            System.out.println("unit time=" + unitTimeMilliSec);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////
    // multi threaded execution
    /////////////////////////////////////////////////////////////////////


    static boolean[] hasFinished = new boolean[2];
    static int[] roundsOf = new int[2];
    static int secstoexecute;

    public void multiThreadedInit(int nsecstoexecute) {
        try {
            secstoexecute = nsecstoexecute;

            ExecuterThread e1 = new ExecuterThread();
            e1.id = 0;
            ExecuterThread e2 = new ExecuterThread();
            e2.id = 1;
            e1.start();
            e2.start();
            while (!(hasFinished[1] && hasFinished[0])) {
                Thread.sleep(1500);
            }
            int totrounds = roundsOf[0] + roundsOf[1];
            unitTimeMilliSec = (secstoexecute * 1000 * 2) / ((double) totrounds * 200);

            System.out.println("rounds:" + totrounds);
            System.out.println("utime:" + unitTimeMilliSec);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    class ExecuterThread extends Thread {
        int id;

        public void run() {
            long end = System.currentTimeMillis() + secstoexecute * 1000;
            int rounds = 0;
            while (System.currentTimeMillis() < end) {
                for (int i = 0; i < 200; i++) {
                    stm.add(randomValues[i]);
                }
                roundsOf[id]++;
            }
            hasFinished[id] = true;
        }
    }

}
