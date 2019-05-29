package House_Committee;

public abstract class Person {
    private String firstName;
    private String lastName;
    private String id;
    private String userName;
    private String hashedPassword;

    Person(String firstName, String lastName, String id, String userName, String hashedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
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
