package House_Committee.Server;

import House_Committee.Committee;
import House_Committee.Person;
import House_Committee.Tenant;
import House_Committee.db.sqlHandler;



import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashMap;

public class socketHandler extends Thread {
    Socket incoming;
    static sqlHandler sql;

    Boolean isLoggedIn = false;
    String[] options = new String[]{"Login"};
    private final int PAYMET_PER_ROOM = 70;

    public socketHandler(Socket _in, sqlHandler _sql) {
        incoming = _in;
        sql = _sql;

    }

    public void run() {
        String fromClient;
        HashMap<String, String> details = new HashMap<>();
        String[] clientArr = null;

        Committee committee;
        Tenant tenant;

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
                    for(String s : clientArr)
                    {
                        String key, value;
                        String[] f = s.split(":");
                        key = f[0];
                        value = f[1];
                        details.put(key,value);
                    }
                    switch (details.get("Operation"))
                    {
                        case "Login":
                            System.out.println("Login");
                            isLoggedIn = sqlHandler.userLogin(details.get("userName"),details.get("Password"));
                            outToClient.writeBytes(isLoggedIn+"\n");
                            break;
                        case "Register":
                            System.out.println("register");

                            sqlHandler.insert_user(new Person(
                                    details.get("firstName"),
                                    details.get("lastName"),
                                    details.get("userName"),
                                    details.get("Password"),
                                    new Timestamp(System.currentTimeMillis()),
                                    new Timestamp(System.currentTimeMillis()),
                                    details.get("apartmentNumber"),
                                    details.get("buildingNumber")));
                            if(details.get("type").equals("Committee"))
                            {
                                // add seniority to db
                                sqlHandler.set_seniority(details.get("seniority"));
                                committee = new Committee(details.get("firstName"),details.get("lastName"), details.get("userName"),
                                        details.get("Password"),details.get("seniority"), new Timestamp(System.currentTimeMillis()),
                                        new Timestamp(System.currentTimeMillis()),details.get("apartmentNumber"),
                                        details.get("buildingNumber"));
                            }
                            else if(details.get("type").equals("Tenant"))
                            {
                                // add monthly payment to db
                                sqlHandler.set_monthly_payment(Integer.parseInt(details.get("monthlyPayment")) * PAYMET_PER_ROOM+ "");
                                tenant = new Tenant(details.get("firstName"),details.get("lastName"), details.get("userName"),
                                        details.get("Password"), new Timestamp(System.currentTimeMillis()),
                                        new Timestamp(System.currentTimeMillis()),details.get("apartmentNumber"),
                                        details.get("buildingNumber"));
                                tenant.setMonthlyPayment(Integer.parseInt(details.get("monthlyPayment")) * PAYMET_PER_ROOM);
                            }

                            outToClient.writeBytes("Registered\n");

                            break;
                        case "Menu":
                            outToClient.writeBytes("welcome to the House Committee Program!");

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
