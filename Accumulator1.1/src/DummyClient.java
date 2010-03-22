import net.epzilla.accumulator.service.AccumulatorService;
import net.epzilla.accumulator.global.DerivedEvent;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 28, 2010
 * Time: 10:35:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DummyClient {

    public static void main(String[] argv) {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager() {
                    public void checkConnect(String host, int port) {
                    }

                    public void checkConnect(String host, int port, Object context) {
                    }
                });
            }


            AccumulatorService serv = (AccumulatorService) Naming.lookup("rmi://127.0.0.1:1099/AccumulatorService");
            serv.receiveDerivedEvent(new DerivedEvent());
        } catch (Exception e) {
            System.out.println("rmi client exception: " + e);
        }
    }

}
