package etc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Nov 13, 2009
 * Time: 9:09:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiverThread extends Thread {
    @Override
    public void run() {
        try {
            MulticastClient.startClient();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
