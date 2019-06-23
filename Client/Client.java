package House_Committee.Client;


import House_Committee.Encoder;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import static House_Committee.Encoder.strEncoder;

public class Client {
    static String[] options = new String[]{"Login","Register", "Cancel"};
    static final int LOGINOPTSIZE = 10;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String argv[]) throws Exception
    {

//        strEncoder("123456","SHA-256");

        String modifiedSentence;
        Socket clientSocket = new Socket("localhost", 10000); // server ip and port
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // pipe for send data to the server
        BufferedReader   inFromServer= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// pipe for get data from the server
        try {

            while (true) {

                modifiedSentence = inFromServer.readLine(); // getting from server

              switch (modifiedSentence) {
                   // System.out.println(modifiedSentence.replaceAll("\\#\\$","\n"));// printing in the console
                    case "Login":
                        HashMap<String ,String> userDetails = loginOrRegister();
                        while(userDetails == null) // while user not connected
                            userDetails = loginOrRegister();
                        switch (userDetails.get("Operation"))
                        {
                            case "Exit":
                                // if the user close the form from the X or Cancel Button
                                inFromServer.close(); // close all the connection
                                outToServer.close();
                                System.exit(1); // Exit from the process
                                break;
                            case "Register": // get more info from the user by the command line
                                userDetails = getRegDetail(userDetails);
                                // there is no break because we need to send userDetails to the server in both cases

                            case "Login":
                                // in case the user choose login option
                                // convert the array to string and sent it to server
                                //
                                outToServer.writeBytes(arrToStr(userDetails));
                                // System.out.println(inFromServer.readLine().replaceAll("\\#\\$","\n"));
                                String ggg = inFromServer.readLine();
                                if(ggg.equals("true"))
                                {
                                    handleConnectedState(userDetails);
                                }
                                break;

                        }

                        break;
                  // in case of successful registration (this come only from the server!!)
                  case "Registered":
                      JOptionPane.showMessageDialog(null,
                              "Please log in to your account",
                              "Registered  Successfully ",
                              JOptionPane.OK_OPTION);
                      break;
                }

            }

        }
        catch (Exception e)
        {


        }
    }

    private static void handleConnectedState(HashMap<String, String> userDetails) {
       for (String s : userDetails.keySet())
       {
           System.out.println(userDetails.get(s));
       }
    }

    private static String arrToStr(HashMap<String, String> list)
    {
        String listString = "";

        for(String x : list.keySet())
        {
            listString += x+":"+list.get(x)+ " ";
        }
        return listString+ "\n";
    }


    private static HashMap<String ,String> getRegDetail(HashMap<String ,String> userDetails) {

        System.out.println("Please enter 1 for Committee or 2 for Tenant");
        String seniority = null, buildingNumber, apartmentNumber, fullName;

        String type = scanner.nextLine();
        System.out.println("Please enter your first Name");
        userDetails.put("firstName", scanner.nextLine());
        System.out.println("Please enter your last Name");
        userDetails.put("lastName", scanner.nextLine());

        switch(type)
        {
            case "1": // in case the choose is committee
                userDetails.put("type", "Committee");
                System.out.println("Please enter seniority years (Numbers Only)");
                try {
                    seniority = Integer.parseInt(scanner.nextLine()) + ""; // try the input to int in order to validate
                    userDetails.put("seniority", seniority);
                }
                catch (NumberFormatException e)
                {
                    System.out.println("not a valid input, please try again");
                    getRegDetail(userDetails);
                }
                break;

            case "2": // in case the choose is Tenant
                userDetails.put("type", "Tenant");
                System.out.println("Please enter num of rooms");
                userDetails.put("monthlyPayment", Integer.parseInt(scanner.nextLine())+"");
                break;
            default:
                System.out.println("not a valid input");
                getRegDetail(userDetails);
        }

        System.out.println("Please enter your Building Number");
        buildingNumber = scanner.nextLine();
        userDetails.put("buildingNumber", buildingNumber);
        System.out.println("Please enter your Apartment Number");
        apartmentNumber = scanner.nextLine();
        userDetails.put("apartmentNumber", apartmentNumber);
        // Committee or tenant

        return userDetails;
    }



    private static HashMap<String ,String> register() {

        HashMap<String ,String> res = new HashMap<>();
        JPanel regPanel = new JPanel();
        regPanel.setLayout(new GridLayout(0,1));
        JLabel userLabel = new JLabel("user name:");
        JLabel passLabel1 = new JLabel("Enter a password:");
        JLabel passLabel2 = new JLabel("Retype password:");

        JTextField userName = new JTextField(10);
        JPasswordField pass1 = new JPasswordField(10);
        JPasswordField pass2 = new JPasswordField(10);

        regPanel.add(userLabel);
        regPanel.add(userName);
        regPanel.add(passLabel1);
        regPanel.add(pass1);
        regPanel.add(passLabel2);
        regPanel.add(pass2);

        String[] options2 = new String[]{"OK", "Cancel"};
        int option2 = JOptionPane.showOptionDialog(null, regPanel, "Register Form",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options2, options2[1]);
        switch(option2) // pressing OK button
        {
            case 0:
                String password = new String(pass1.getPassword());
                String password2 = new String(pass2.getPassword());
//                System.out.println(userName.getText() +" Your password is: " + new String(password));
                if(password.equals(password2) && password.length()>0 )
                {
                    res.put("Operation", "Register");
                    res.put("userName", userName.getText());

                    res.put("Password", Encoder.strEncoder(password,"SHA-256"));
                    return res;
                }
                else{
                    JOptionPane.showMessageDialog(null,
                            "two Password must be equals",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                return res;

            case JOptionPane.CLOSED_OPTION: // on form close
                res.put("Operation", "Exit");
                return res;
            case 1: // on Cancel click
               return loginOrRegister();


        }


      return null;
    }

    private static HashMap<String ,String> loginOrRegister() {

        HashMap<String ,String> res = new HashMap<>();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        JLabel userlabel = new JLabel("user name:");
        JLabel label = new JLabel("Enter a password:");
        JTextField userName = new JTextField(10);
        JPasswordField pass = new JPasswordField(10);
        panel.add(userlabel);
        panel.add(userName);

        panel.add(label);
        panel.add(pass);

        mainPanel.add(panel);
        int option = JOptionPane.showOptionDialog(null, mainPanel, "Login Form",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);
        switch(option) // pressing OK button
        {

            case 0:
                char[] password = pass.getPassword();
                //System.out.println(userName.getText() +" Your password is: " + new String(password));
                res.put("Operation", "Login");
                res.put("userName", userName.getText());
                res.put("Password", Encoder.strEncoder(new String(pass.getPassword()),"SHA-256"));
                return res;
            case 1: // on click on Register
                return register();

            case JOptionPane.CLOSED_OPTION: // on form close
            case 2: // on Cancel
                res.put("Operation", "Exit");
               return res;
        }


        return null;
    }
}
