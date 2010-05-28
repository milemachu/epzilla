import stm.RunAsClient;
import stm.RunAsServer;
import stm.StmHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 28, 2010
 * Time: 11:24:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        StmHandler handler = new StmHandler();
        boolean state = handler.startClient("127.0.0.1");
        if (!state) {
            handler.startServer();
        }
    }
}
