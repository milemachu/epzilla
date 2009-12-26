package scheduling;


public class VirtualStm {
    public void add(Object item) {
        System.out.println("received:" + item.toString());          // will take some time
        try {
//            Thread.sleep(0, 10000);   // sleep 10 micro seconds.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
