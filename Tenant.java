package House_Committee;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Tenant extends Person {

    private String apartmentNumber;
    private double monthlyPayment;
    private ArrayList<HashMap<String ,String>> paymentsArr;

    public Tenant(String firstName, String lastName, String id, String userName, String hashedPassword, Date lastLogin, Date registrationDate) {
        // call to main Person Object Constractor
        super(firstName, lastName, id, userName, hashedPassword,lastLogin,registrationDate);

    }


    public int getAllPayment() {

        return 0;
    }
}
