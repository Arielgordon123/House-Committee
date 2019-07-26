package House_Committee.db;
import House_Committee.Committee;
import House_Committee.Person;
import House_Committee.Server.Server;
import House_Committee.Tenant;

import java.sql.*;
import java.util.HashMap;


public class sqlHandler {

    private static Connection connect;
//    private static final String DBNAME = "heroku_e41c452f428bb7d";
    private static final String DBNAME = "house_committee";

    public static void delete_statement(){

        String sqldelete = "delete from student where h between ? and ? and name = ?";

        try {
            PreparedStatement pst = connect.prepareStatement(sqldelete);

            pst.setString(1, "180");
            pst.setString(2, "190");
            pst.setString(3, "effi");

            pst.execute();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void update_statement(String name){

        String sqlupdate = "UPDATE student SET name=?  WHERE id =? ";

        try {
            PreparedStatement pst = connect.prepareStatement(sqlupdate);

            pst.setString(1, name);
            pst.setString(2, "3344");
            //	pst.setString(3, "66127762");

            pst.executeUpdate();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void insert_statement(String s1 , String s2,String s3,String s4,String s5,String s6 ){

        String sqlInsert = "insert into test_inc.student (idhr,hr_name,hr_phone,hr_age,hr_address,hr_mail) values (?,?,?,?,?,?)";

        try {
            PreparedStatement pst = connect.prepareStatement(sqlInsert);
            pst.setString(1, s1);
            pst.setString(2, s2);
            pst.setString(3, s3);
            pst.setString(4, s4);
            pst.setString(5, s5);
            pst.setString(6, s6);


            pst.execute();



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public static Boolean[] userLogin(String userName, String hashedPassword) {
        Boolean[] b= new Boolean[]{false,false};
        try {
            String user = "select userName, hashedPassword, role from "+DBNAME+".users where userName = ?";
            PreparedStatement statement = connect.prepareStatement(user);
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                System.out.println("hashedPassword " +result.getString("hashedPassword"));
                if(result.getString("userName").equals(userName) && result.getString("hashedPassword").equals(hashedPassword))
                {
                    if(result.getString("role").equals("Tenant")) {
                        b[0] = true; // successful login
                        b[1] = false; // is Committee
                    }
                    else if(result.getString("role").equals("Committee")) {
                        b[0] = true; // successful login
                        b[1] = true; // is Committee
                    }
                }
            }


        } catch (SQLException e) {
              e.printStackTrace();
        }
        return b;
    }

    public static void insert_user(Person person) {

        String sqlInsert = "insert into "+DBNAME+".users (`userName`,`lastName`,`firstName`,`hashedPassword`,`registrationDate`,`lastLogin`,`buildingNumber`,`apartmentNumber`,`role`)" +
                " values (?,?,?,?,?,?,?,?,?)";
        System.out.println("person.getRole() "+person.getRole());
        try {
            PreparedStatement pst = connect.prepareStatement(sqlInsert);
            pst.setString(1, person.getUserName());
            pst.setString(2, person.getLastName());
            pst.setString(3, person.getFirstName());
            pst.setString(4, person.getHashedPassword());
            pst.setTimestamp(5,  person.getRegistrationDate());
            pst.setTimestamp(6,  person.getLastLogin());
            pst.setString(7, person.getBuildingNumber());
            pst.setString(8, person.getApartmentNumber());
            pst.setString(9, person.getRole());
            pst.execute();



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void set_monthly_payment(String monthlyPayment) {
        String query = "insert into "+DBNAME+".tenants (`userId`, `monthlyPayment`)"+
                " values(LAST_INSERT_ID(), ?)";
        try {
            PreparedStatement pst = connect.prepareStatement(query);
            pst.setString(1,monthlyPayment);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void set_seniority(String seniority) {
        String query = "insert into "+DBNAME+".committees (`userId`, `seniority`)"+
                " values(LAST_INSERT_ID(), ?)";
        try {
            PreparedStatement pst = connect.prepareStatement(query);
            pst.setString(1,seniority);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String printResultSet(ResultSet result, PreparedStatement statement) throws SQLException {
        String table ="";
        ResultSetMetaData meta = statement.getMetaData();
        int columnCount = meta.getColumnCount();
        int i=0;
        while(result.next())
            {
                if(i ==0) {
                    for (int column = 1; column <= columnCount; ++column) {

                        table += (meta.getColumnName(column) + " | ");
                    }
                }
                table += Server.SPACIALLINEBREAK;

                i++;
                for (int column = 1; column <= columnCount; ++column) {
                    Object value = result.getObject(column);
                    table += (value + "\t | ");
                }
//                System.out.println();

            }
        return  table;
    }
    public static Person getTenantByUserName(String userName)
    {

        String query = "select  `idCommittee` as \"id\",`firstName`,  `lastName`,  `userName`,  `hashedPassword`, `lastLogin`,  `registrationDate`,`seniority`, `apartmentNumber`,`buildingNumber`,  `role` , \"\" as \"monthlyPayment\"\n" +
                "from users\n" +
                "join committees on  users.userId = committees.userId\n" +
                "where userName = ? \n" +
                "union\n" +
                "select `idTenants`,`firstName`,  `lastName`,  `userName`,  `hashedPassword`, `lastLogin`,  `registrationDate`,''as \"seniority\", `apartmentNumber`,`buildingNumber`,  `role`, `monthlyPayment`\n" +
                "from users\n" +
                "join tenants on users.userId = tenants.userId\n" +
                "where userName = ?";


        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, userName);
            ResultSet result = statement.executeQuery();

            String type;
            ResultSetMetaData meta = statement.getMetaData();
            int columnCount = meta.getColumnCount();
            int i=0;
            while(result.next())
            {
                type = result.getString("role");
                if(type.equals("Tenant"))
                {
                    Person tenant = new Tenant(
                            result.getString("id"),
                            result.getString("firstName"),
                            result.getString("lastName"),
                            result.getString("userName"),
                            result.getString("hashedPassword"),
                            result.getTimestamp("lastLogin"),
                            result.getTimestamp("registrationDate"),
                            result.getString("apartmentNumber"),
                            result.getString("buildingNumber"),
                            result.getString("role"),
                            result.getDouble("monthlyPayment")
                            );
                    return tenant;
                }
                else if (type.equals("Committee"))
                {
                    Person committee = new Committee(
                            result.getString("id"),
                            result.getString("firstName"),
                            result.getString("lastName"),
                            result.getString("userName"),
                            result.getString("hashedPassword"),
                            result.getString("seniority"),
                            result.getTimestamp("lastLogin"),
                            result.getTimestamp("registrationDate"),
                            result.getString("apartmentNumber"),
                            result.getString("buildingNumber"),
                            result.getString("role")
                    );
                    return committee;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }
    // 1
    public static String getPaymentByTenantId(String id)
    {
        String query ="select paymentDate, paymentSum\n" +
                "from payments\n" +
                "where idTenants = ?\n" +
                "order by paymentDate";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, id);

            return select_query(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 2
    public static String getAllPaymentsByBuilding(String buildingNumber) {

        String query ="select *\n" +
                "from (\n" +
                "select paymentDate, paymentSum,users.apartmentNumber,concat(users.firstName,\" \", users.lastName) as \"name\", users.buildingNumber  -- tenants.idTenants as \"idTenants\"\n" +
                "from payments\n" +
                "join tenants on tenants.idTenants = payments.idTenants\n" +
                "join users on users.userId = tenants.userId\n" +
                ") as u\n" +
                "where u.buildingNumber = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, buildingNumber);

            return select_query(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 3
    public static void setPaymentByTenantId(String idTenants, Double paymentSum,String paymentDate ) {

        String query ="INSERT payments\n" +
                "(`paymentSum`,\n" +
                "`idTenants`,\n" +
                "`paymentDate`)\n" +
                "VALUES\n" +
                "(?,\n" +
                "?,\n" +
                "?)";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setDouble(1, paymentSum);
            statement.setString(2,idTenants);
            statement.setString(3,paymentDate);
            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    // 4
    public static String getSumPaymentsByBuilding(String buildingNumber) {

        String query ="select sum(paymentSum) as \"sum\", month(paymentDate) as \"month\"\n" +
                "from (\n" +
                "select paymentDate, paymentSum, tenants.idTenants as \"idTenants\", users.buildingNumber\n" +
                "from payments\n" +
                "join tenants on tenants.idTenants = payments.idTenants\n" +
                "join users on users.userId = tenants.userId\n" +
                ") as u\n" +
                "where u.buildingNumber = ?\n" +
                "group by month(u.paymentDate)";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, buildingNumber);

            return select_query(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String select_paymentByTenantId(String id, String buildingNumber)
    {
        String query = "select users.buildingNumber, users.apartmentNumber, payments.paymentSum, payments.paymentDate\n" +
                "from users\n" +
                "join tenants on tenants.userId = users.userId\n" +
                "join payments on tenants.idTenants = payments.idTenants\n" +
                "where payments.idTenants = ? and users.buildingNumber = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, buildingNumber);
            return select_query(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String select_query(PreparedStatement statement) {

        try {
          //  PreparedStatement statement = connect.prepareStatement("select * from " + DBNAME + ".users");
            ResultSet result = statement.executeQuery();
            return printResultSet(result, statement);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void connection()
    {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Works");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void ConectingToSQL ()
    {

        connection();
//        String host = "jdbc:mysql://us-cdbr-iron-east-02.cleardb.net/heroku_e41c452f428bb7d?reconnect=true";
//        String username = "b74f0d4bebea4b";
//        String password = "ce29cedbe787040"; //
        String host = "jdbc:mysql://127.0.0.1:3306/";
        String username = "root";
        String password = ""; //

        try {
            connect = DriverManager.getConnection(host, username, password);
            System.out.println("work");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


//    public static void main(String[] argv)
//    {
//        Scanner input = new Scanner(System.in);
//        ConectingToSQL();
//        select_query();

       // select_user("test");
//        for(int i=0 ; i < 1 ; i++)
//        {
//            System.out.println("enter idhr");
//            String idhr = input.nextLine();
//            System.out.println("enter name");
//            String hr_name = input.nextLine();
//            System.out.println("enter phone");
//            String hr_phone = input.nextLine();
//            System.out.println("enter age");
//            String hr_age = input.nextLine();
//            System.out.println("enter adress");
//            String hr_adress = input.nextLine();
//            System.out.println("enter mail");
//            String hr_mail = input.nextLine();
//
//            insert_statement(idhr, hr_name, hr_phone,hr_age,hr_adress, hr_mail );
//
//
//        }


        //select_query();
        //	update_statement();
        //	delete_statement();
        //	select_query();
  //  }


}
