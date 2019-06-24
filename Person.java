package House_Committee;

import java.sql.Timestamp;

public class Person {
    private String firstName;
    private String lastName;
    // id is auto genereted by sql
    private String id;
    private String userName;
    private String hashedPassword;
    private Timestamp registrationDate;
    private Timestamp lastLogin;
    private String role;

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    private String apartmentNumber;
    private String buildingNumber;
    public Person(String firstName, String lastName, String userName, String hashedPassword, Timestamp lastLogin, Timestamp registrationDate, String apartmentNumber, String buildingNumber, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
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

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public String getRole() {
        return role;
    }


}
