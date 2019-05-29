package House_Committee.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient_send extends Thread {// the client sending thread - send to server

    private Socket clientSocket;

    public TCPClient_send(Socket clientSocket, String name)
    {
        super (name);
        this.clientSocket = clientSocket ;
    }

    public void run()
    {
        String stringToServer;
        String stringFromServer;


        try {
            Scanner input = new Scanner ( System.in);

//            DataOutputStream outToServer =
//                    new DataOutputStream(clientSocket.getOutputStream());

            DataInputStream inFromServer =
                    new DataInputStream(clientSocket.getInputStream());
            stringFromServer = inFromServer.readUTF();
            while(true)
            {

                System.out.println(stringFromServer);// getting from user
                stringToServer = input.nextLine();





                if(stringFromServer.toLowerCase().equals("bye\n") || stringToServer.toLowerCase().equals("bye"))
                {
                    System.out.println("got bye");
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                }


            }
            clientSocket.close(); // close the client socket

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}