package House_Committee;

import java.util.Date;

public abstract class Person {
    private String firstName;
    private String lastName;
    private String id;
    private String userName;
    private String hashedPassword;
    private Date registrationDate;
    private Date lastLogin;

    Person(String firstName, String lastName, String id, String userName, String hashedPassword, Date lastLogin, Date registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.userName = userName;
        this.hashedPassword = hashedPassword;

        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
    }

     private String getFirstName() {
        return firstName;
    }

     private String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }


    public String getFullName() {
        return getFirstName()+ " "+ getLastName();
    }

}
