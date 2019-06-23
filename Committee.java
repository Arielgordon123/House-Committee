package House_Committee;


import java.sql.Date;
import java.sql.Timestamp;

public class Committee extends Person{

    private String seniority;

    public Committee(String firstName, String lastName, String userName, String hashedPassword,
                     String seniority, Timestamp lastLogin, Timestamp registrationDate,String apartmentNumber,String buildingNumber) {
        super(firstName, lastName, userName, hashedPassword,lastLogin,registrationDate,apartmentNumber,buildingNumber);
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
