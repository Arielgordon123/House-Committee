package House_Committee.Client;


import House_Committee.Encoder;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import static House_Committee.Encoder.strEncoder;

public class Client {
    static String[] options = new String[]{"Login", "Register", "Cancel"};
    static String LineBreak = "\\#\\$";
    static final int LOGINOPTSIZE = 10;
    static HashMap<String, String> userDetails = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    private static void exit(BufferedReader inFromServer, DataOutputStream outToServer) throws IOException {
        inFromServer.close(); // close all the connection
        outToServer.close();
        System.exit(1); // Exit from the process
    }

    public static void main(String argv[]) throws Exception {

//        strEncoder("123456","SHA-256");

        String strFromServer;
        Socket clientSocket = new Socket("localhost", 10000); // server ip and port
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // pipe for send data to the server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// pipe for get data from the server
        try {
            System.out.println(inFromServer.readLine()); // getting from server

            while (true) {
                strFromServer = inFromServer.readLine();
                //System.out.println("modifiedSentence " + strFromServer);
                switch (strFromServer) {
                    case "Login":
                        userDetails = loginOrRegister();
                        while (userDetails == null) // while user not connected
                            userDetails = loginOrRegister();
                        break;
                    // in case of successful registration (this come only from the server!!)
                    case "Registered":
                        JOptionPane.showMessageDialog(null,
                                "Please log in to your account",
                                "Registered  Successfully ",
                                JOptionPane.OK_OPTION);
                        break;
                    case "connected":
                        String resp = inFromServer.readLine();
                        if (resp.startsWith("true")) //&& userDetails.get("role").equals("Tenant")
                        {
                            getMainMenu(outToServer, inFromServer, resp);
                        }
                        break;
                    case "true true":
                    case "true false":
                        getMainMenu(outToServer, inFromServer, strFromServer);
                        break;

                }
                switch (userDetails.get("Operation")) {
                    case "Exit":
                        // if the user close the form from the X or Cancel Button
                        exit(inFromServer, outToServer);
                        break;
                    case "Register": // get more info from the user by the command line
                        userDetails = getRegDetail(userDetails);
                        // there is no break because we need to send userDetails to the server in both cases

                    case "Login":
                        // in case the user choose login option
                        // convert the array to string and sent it to server
                        //
                        outToServer.writeBytes(arrToStr(userDetails));
                        // System.out.println(inFromServer.readLine().replaceAll(LineBreak,"\n"));
                        String resp = inFromServer.readLine();
                        System.out.println(inFromServer.readLine());
                        System.out.println(inFromServer.readLine());

                        System.out.println("response from server " + resp);
                        if (resp.startsWith("true")) //&& userDetails.get("role").equals("Tenant")
                        {
                            getMainMenu(outToServer, inFromServer, resp);
                        } else
                            userDetails = null;

                        break;

                }

            }

        } catch (Exception e) {


        }
    }

    private static void getMainMenu(DataOutputStream outToServer, BufferedReader inFromServer, String resp) {
        try {
            String response;
            userDetails.put("Operation", "Menu");

            outToServer.writeBytes(arrToStr(userDetails)); // send to server the menu request
            response = inFromServer.readLine().replaceAll(LineBreak, "\n");
            while (!response.startsWith("Hi"))
                response = inFromServer.readLine().replaceAll(LineBreak, "\n");
            System.out.println(response); // read welcome message

            String modifiedSentence = scanner.nextLine();
            if (resp.split(" ")[1].equals("false")) { // if the user is Tenant

                switch (modifiedSentence.toLowerCase())
                {
                    case "0":
                        userDetails = changePassword(); // get new password from the client
                        outToServer.writeBytes("0:0 "+arrToStr(userDetails)); // send to server old and new passwords
                        userDetails.remove("oldPassword");
                        userDetails.remove("newPassword");
                        break;
                    case "1":
                        outToServer.writeBytes("1\n");
                        System.out.println(inFromServer.readLine().replaceAll(LineBreak, "\n"));
                        break;
                    case "logout":
                    case "exit":
                        System.out.println("BYE");
                        exit(inFromServer, outToServer);
                        break;
                }

            } else // if the user is committee
            {
                switch (modifiedSentence.toLowerCase()) {
                    case "0":
                        userDetails = changePassword();
                        outToServer.writeBytes("choice:0 "+arrToStr(userDetails));

                        userDetails.remove("oldPassword");
                        userDetails.remove("newPassword");
                        break;
                    case "1":
                        System.out.println("Please enter Tenant Id: ");
                        modifiedSentence = scanner.nextLine();
                        outToServer.writeBytes("choice:1 idTenant:" + modifiedSentence + "\n");

                        break;
                    case "2":
                        outToServer.writeBytes("choice:2\n");
                        break;
                    case "3":
                        System.out.println("Please enter Tenant Id: ");
                        modifiedSentence = scanner.nextLine();
                        outToServer.writeBytes("choice:3 idTenant:" + modifiedSentence);
                        System.out.println("Please enter payment Sum: ");
                        modifiedSentence = scanner.nextLine();
                        outToServer.writeBytes(" paymentSum:" + modifiedSentence);
                        System.out.println("Please enter payment Date: (yyyy-MM-dd) ");
                        modifiedSentence = scanner.nextLine();
                        outToServer.writeBytes(" paymentDate:" + modifiedSentence + "\n");
                        break;
                    case "4":
                        outToServer.writeBytes("choice:4\n");
                        break;
                    case "exit":
                    case "logout":
                        System.out.println("BYE");
                        exit(inFromServer, outToServer);
                        break;
                    default:
                        System.out.println("not a valid Choice");
                        break;

                }
                System.out.println(inFromServer.readLine().replaceAll(LineBreak, "\n"));
            }

            //handleConnectedState(userDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnectedState(HashMap<String, String> userDetails) {
        for (String s : userDetails.keySet()) {
            System.out.println(userDetails.get(s));
        }
    }

    private static String arrToStr(HashMap<String, String> list) {
        String listString = "";

        for (String x : list.keySet()) {
            listString += x + ":" + list.get(x) + " ";
        }
        return listString + "\n";
    }


    private static HashMap<String, String> getRegDetail(HashMap<String, String> userDetails) {

        System.out.println("Please enter 1 for Committee or 2 for Tenant");
        String seniority = null, buildingNumber, apartmentNumber, fullName;

        String type = scanner.nextLine();
        System.out.println("Please enter your first Name");
        userDetails.put("firstName", scanner.nextLine());
        System.out.println("Please enter your last Name");
        userDetails.put("lastName", scanner.nextLine());

        switch (type) {
            case "1": // in case the choose is committee
                userDetails.put("role", "Committee");
                System.out.println("Please enter seniority years (Numbers Only)");
                try {
                    seniority = Integer.parseInt(scanner.nextLine()) + ""; // try the input to int in order to validate
                    userDetails.put("seniority", seniority);
                } catch (NumberFormatException e) {
                    System.out.println("not a valid input, please try again");
                    getRegDetail(userDetails);
                }
                break;

            case "2": // in case the choose is Tenant
                userDetails.put("role", "Tenant");
                System.out.println("Please enter num of rooms");
                userDetails.put("monthlyPayment", Integer.parseInt(scanner.nextLine()) + "");
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
    private static HashMap<String, String> changePassword(){
        HashMap<String, String> res = new HashMap<>();
        JPanel regPanel = new JPanel();
        regPanel.setLayout(new GridLayout(0, 1));
        JLabel oldPassLabel = new JLabel("Old Password:");
        JLabel passLabel1 = new JLabel("Enter a new  password:");
        JLabel passLabel2 = new JLabel("Retype the new password:");

        JTextField oldPass = new JPasswordField(10);
        JPasswordField pass1 = new JPasswordField(10);
        JPasswordField pass2 = new JPasswordField(10);

        regPanel.add(oldPassLabel);
        regPanel.add(oldPass);
        regPanel.add(passLabel1);
        regPanel.add(pass1);
        regPanel.add(passLabel2);
        regPanel.add(pass2);
        String[] options2 = new String[]{"OK", "Cancel"};
        int option2 = JOptionPane.showOptionDialog(null, regPanel, "Register Form",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options2, options2[1]);
        switch (option2) // pressing OK button
        {
            case 0:
                String password = new String(pass1.getPassword());
                String password2 = new String(pass2.getPassword());
//                System.out.println(userName.getText() +" Your password is: " + new String(password));
                if (password.equals(password2) && password.length() > 0) {
                    res.put("Operation", "PasswordChange");

                    res.put("oldPassword",Encoder.strEncoder(oldPass.getText(),"SHA-256"));
                    res.put("newPassword", Encoder.strEncoder(password, "SHA-256"));
                    return res;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "two Password must be equals",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                }
                //return res;

            case JOptionPane.CLOSED_OPTION: // on form close
                res.put("Operation", "Exit");
                return res;
            case 1: // on Cancel click
                return loginOrRegister();


        }
        return null;
    }

    private static HashMap<String, String> register() {

        HashMap<String, String> res = new HashMap<>();
        JPanel regPanel = new JPanel();
        regPanel.setLayout(new GridLayout(0, 1));
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
        switch (option2) // pressing OK button
        {
            case 0:
                String password = new String(pass1.getPassword());
                String password2 = new String(pass2.getPassword());
//                System.out.println(userName.getText() +" Your password is: " + new String(password));
                if (password.equals(password2) && password.length() > 0) {
                    res.put("Operation", "Register");
                    res.put("userName", userName.getText());

                    res.put("Password", Encoder.strEncoder(password, "SHA-256"));
                    return res;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "two Password must be equals",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                }
                //return res;

            case JOptionPane.CLOSED_OPTION: // on form close
                res.put("Operation", "Exit");
                return res;
            case 1: // on Cancel click
                return loginOrRegister();


        }


        return null;
    }

    private static HashMap<String, String> loginOrRegister() {

        HashMap<String, String> res = new HashMap<>();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
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
        switch (option) // pressing OK button
        {

            case 0:
                char[] password = pass.getPassword();
                //System.out.println(userName.getText() +" Your password is: " + new String(password));
                res.put("Operation", "Login");
                res.put("userName", userName.getText());
                res.put("Password", Encoder.strEncoder(new String(pass.getPassword()), "SHA-256"));
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
