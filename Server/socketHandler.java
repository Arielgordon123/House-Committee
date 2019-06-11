package Server;

import House_Committee.Person;
import db.sqlHandler;
import Server.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class socketHandler extends Thread {
    Socket incoming;
    static sqlHandler sql;
    Boolean isLoggedIn = false;
    String[] options = new String[]{"Login"};
    public socketHandler(Socket _in, sqlHandler _sql) {
        incoming = _in;
        sql = _sql;

    }

    public void run() {
        String fromClient;
        String userName= null,password= null;
        String[] clientArr = null;
        String personFromClient= null;
        int num;
        int num2;
        try {

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(incoming.getInputStream()));

            DataOutputStream outToClient = new DataOutputStream(new DataOutputStream(incoming.getOutputStream()));
            outToClient.writeBytes("connected to server "+incoming.getInetAddress()+"\n");
            //outToClient.writeBytes("bye\n");

            while(true)
            {
                // First of all check if the user is logged in and if not Send him login Request
                if(!isLoggedIn)
                    outToClient.writeBytes("Login\n");
                fromClient = inFromClient.readLine();

                if(fromClient != null)
                {
                    System.out.println(fromClient);
                    clientArr = fromClient.split(" ");
                    switch (clientArr[0])
                    {
                        case "Login":
                            System.out.println("Login");
                            isLoggedIn = sqlHandler.userLogin(clientArr[1],clientArr[2]);
                            outToClient.writeBytes(isLoggedIn+"\n");
                            System.out.println("is logged in "+ isLoggedIn);
                            break;
                        case "Register":
                            System.out.println("register");

                            outToClient.writeBytes("Registered\n");
                            break;
                    }
                   // outToClient.writeBytes(sqlHandler.select_user(personFromClient)+"\n");
                   // outToClient.writeBytes(sqlHandler.select_query()+"\n");

                }
            }

//                outToClient.writeBytes(capitalizedSentence);

                //   outToClient.writeObject( num);


            }
        catch(SocketException e)
        {
            System.out.println("client Disconnected");
            synchronized (Server.waitObject)
            {
                Server.connected--;
            }
        }

        catch (IOException e) {

            e.printStackTrace();
        }

    }
}
