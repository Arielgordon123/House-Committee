package House_Committee;


import java.sql.PreparedStatement;
import java.util.Date;

public class Committee extends Person{

    private int seniority;

    Committee(String firstName, String lastName, String id, String userName, String hashedPassword, int seniority, Date lastLogin, Date registrationDate) {
        super(firstName, lastName, id, userName, hashedPassword,lastLogin,registrationDate);
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
