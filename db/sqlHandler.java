package House_Committee.db;
import House_Committee.Person;

import java.sql.*;
import java.util.HashMap;


public class sqlHandler {

    private static Connection connect;
    private static final String DBNAME = "heroku_e41c452f428bb7d";

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


    public static boolean userLogin(String userName, String hashedPassword) {
        try {
            String user = "select userName, hashedPassword from heroku_e41c452f428bb7d.users where userName = ?";
            PreparedStatement statement = connect.prepareStatement(user);
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                System.out.println("hashedPassword " +result.getString("hashedPassword"));
                if(result.getString("userName").equals(userName) && result.getString("hashedPassword").equals(hashedPassword))
                    return true;
            }


        } catch (SQLException e) {
            //  e.printStackTrace();
        }
        return false;
    }

    public static void insert_Committee(Person person) {

        String sqlInsert = "insert into "+DBNAME+".users (idhr,hr_name,hr_phone,hr_age,hr_address,hr_mail) values (?,?,?,?,?,?)";

        try {
            PreparedStatement pst = connect.prepareStatement(sqlInsert);
//            pst.setString(1, s1);
//            pst.setString(2, s2);
//            pst.setString(3, s3);
//            pst.setString(4, s4);
//            pst.setString(5, s5);
//            pst.setString(6, s6);


            pst.execute();



        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
                table += "#$";

                i++;
                for (int column = 1; column <= columnCount; ++column) {
                    Object value = result.getObject(column);
                    table += (value + "\t | ");
                }
//                System.out.println();

            }
        return  table;
    }

    public static String select_query() {

        try {

            PreparedStatement statement = connect.prepareStatement("select * from " + DBNAME + ".users");

            ResultSet result = statement.executeQuery();
            return printResultSet(result, statement);
//


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
        String host = "jdbc:mysql://us-cdbr-iron-east-02.cleardb.net/heroku_e41c452f428bb7d?reconnect=true";
        String username = "b74f0d4bebea4b";
        String password = "55e1cf92";

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
