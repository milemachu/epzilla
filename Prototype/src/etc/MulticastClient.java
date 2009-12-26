package etc;

import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastClient {

    public static void startClient() throws IOException {

        MulticastSocket socket = new MulticastSocket(5006);
        InetAddress address = InetAddress.getByName("224.0.0.2");
        socket.joinGroup(address);

        DatagramPacket packet;

        // get a few quotes
        for (int i = 0; i < 5; i++) {

            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received mcast: " + received);
        }

        socket.leaveGroup(address);
        socket.close();
    }

}
