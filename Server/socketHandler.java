package House_Committee.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class socketHandler extends Thread {
    Socket incoming;

    public socketHandler(Socket _in) {
        incoming = _in;
    }

    public void run() {
        String[] clientSentence;
        String capitalizedSentence = null,st;
        int num;
        int num2;
        try {

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(incoming.getInputStream()));

            DataOutputStream outToClient = new DataOutputStream(incoming.getOutputStream());
            outToClient.writeBytes("bye\n");

//                outToClient.writeBytes(capitalizedSentence);

                //   outToClient.writeObject( num);


            }
        catch (IOException e) {

        }

    }
}
