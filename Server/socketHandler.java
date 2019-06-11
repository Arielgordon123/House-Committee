package House_Committee.Server;

import House_Committee.Person;
import House_Committee.db.sqlHandler;

import java.io.*;
import java.net.NoRouteToHostException;
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
        Object fromClient;
        String userName= null,password= null;
        String[] clientArr = null;
        Person personFromClient= null;
        int num;
        int num2;
        try {

            ObjectInputStream inFromClient = new ObjectInputStream(incoming.getInputStream());

            DataOutputStream outToClient = new DataOutputStream(new DataOutputStream(incoming.getOutputStream()));
            outToClient.writeBytes("connected to server "+incoming.getInetAddress()+"\n");
            //outToClient.writeBytes("bye\n");

            while(true)
            {
                if(!isLoggedIn)
                    outToClient.writeBytes("Login\n");
                fromClient = inFromClient.readObject();
                personFromClient = (Person)fromClient;
                if(personFromClient[0] != null)
                {
                    System.out.println(personFromClient);
                    clientArr = personFromClient[0].split(" ");
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
