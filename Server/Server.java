package House_Committee.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String argv[]) throws Exception
    {
        ServerSocket s = null;
        int port = 10000;
        try {
            s = new ServerSocket(port);
            System.out.println("Server is on, Port is: "+ port);
        } catch(IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        while (true) {
            Socket incoming = null;

            try {
                incoming = s.accept();
            } catch(IOException e) {
                System.out.println(e);
                continue;
            }

            new socketHandler(incoming).start();

        }
    }
}
