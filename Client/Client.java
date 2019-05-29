package House_Committee.Client;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String argv[]) throws Exception
    {

        String modifiedSentence,choice;

        Socket clientSocket = new Socket("localhost", 10000); // call the server

        TCPClient_send s1 = new TCPClient_send(clientSocket , clientSocket.toString());
        s1.start(); // split the client - for send and receive thread
//        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // sent data to server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // get data from server

        while(true)
        {
            modifiedSentence = inFromServer.readLine(); // getting from server
            if(modifiedSentence != null)
            {
                System.out.println(modifiedSentence);// printing in the console
            }
        }


    }
}
