package users;


import db.sqlHandler;
import House_Committee.Committee;
import House_Committee.Tenant;
import House_Committee.Person;
//import House_Committee.db.sqlHandler;
//import House_Committee.db.sql;

public class UserManager {

    public House_Committee.Person Login(String userName, String password) throws Exception {
       // return true;
        throw new Exception("rttr");
    }

    public void Register(Person person)
    {
        if(person instanceof Committee)
        {
           sqlHandler.insert_Committee(person);
        }
        else if (person instanceof Tenant)
        {
            //sql.insert_Tenant(person);
        }


    }

}
