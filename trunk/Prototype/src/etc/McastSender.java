package etc;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Nov 13, 2009
 * Time: 8:02:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class McastSender {
    
    public static void main(String[] args) {
        ReceiverThread th = new ReceiverThread();
        th.start();
        byte[] outbuf = "hello martin".getBytes();
            int port = 5005;
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress groupAddr = InetAddress.getByName("224.0.0.2");
                DatagramPacket packet = new DatagramPacket(outbuf, outbuf.length, groupAddr, port);
                socket.send(packet);
                Thread.sleep(1000);
                packet = new DatagramPacket(outbuf, outbuf.length, groupAddr, port);
                socket.send(packet);
                Thread.sleep(1000);
                
                packet = new DatagramPacket(outbuf, outbuf.length, groupAddr, port);
                socket.send(packet);

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
