package etc;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Nov 19, 2009
 * Time: 7:00:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5006);
            Socket s = ss.accept();
//            PrintWriter pw = new PrintWriter(s.getOutputStream());
//            pw.print("hello...");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            System.out.println(br.readLine());
            System.out.println(br.read());
            System.out.println(br.read());
            System.out.println(br.read());
//            pw.close();
//            s.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
