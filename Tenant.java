package House_Committee;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;

public class Tenant extends Person {


    private double monthlyPayment;
    private ArrayList<HashMap<String ,String>> paymentsArr;

    public Tenant(String firstName, String lastName, String userName, String hashedPassword, Timestamp lastLogin, Timestamp registrationDate,String apartmentNumber,String buildingNumber) {
        // call to main Person Object Constractor
        super(firstName, lastName, userName, hashedPassword,lastLogin,registrationDate,apartmentNumber,buildingNumber);

    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public int getAllPayment() {

        return 0;
    }
}
