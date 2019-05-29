package House_Committee;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

public class Committee extends Person{

    private int seniority;

    Committee(String firstName, String lastName, String id, String userName, String hashedPassword, int seniority) {
        super(firstName, lastName, id, userName, hashedPassword);
        this.seniority = seniority;
    }

    public void getMonthlyPaymentById(int apartmentNumber)
    {

    }
    public void getAllMonthlyPayments()
    {

    }
    public void getPaymentByMonth()
    {

    }
    public void setPayments()
    {

    }
}
