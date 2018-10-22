package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWorker {

    private static Connection conn;
    private static String SQL_REQUEST_PROGRAMS = "select * from PROGRAMS";
    private static String SQL_REQUEST_PROPERTIES = "select * from PROPERTY";
    private static String SQL_CREATE_PROGRAMS = "CREATE TABLE PROGRAMS(NAME NVARCHAR2(200) PRIMARY KEY, WORK_TIME NUMBER(1) DEFAULT 0, REST_TIME NUMBER(1) DEFAULT 0, IS_CHECKED NUMBER(1) DEFAULT 0)";
    private static String SQL_CREATE_PROPERTY = "CREATE TABLE PROPERTY(OPTION_NAME NVARCHAR2(20) PRIMARY KEY, OPTION_VALUE NUMBER(1) DEFAULT 0)";

    public static boolean connectDB() {

        try {
            String host = "jdbc:oracle:thin:@localhost:1521:XE";
            String uName = "winservice";
            String uPass = "17061999";
            conn = DriverManager.getConnection(host, uName, uPass);
        } catch (SQLException err) {
            return false;
        }
        return true;
    }

    public static List<Property> getProperties() {
        Statement statement;
        List<Property> options = new ArrayList<Property>();
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_REQUEST_PROPERTIES);
            while (resultSet.next()) {
                Property temp_prop = new Property(resultSet.getString(1), resultSet.getInt(2));
                options.add(temp_prop);
            }
            statement.close();
        } catch (SQLException err) {
            try {
                statement = conn.createStatement();
                statement.executeQuery(SQL_CREATE_PROPERTY);
            } catch (SQLException error) {
            }
        }
        if (options.size() == 0) {
            try {
                statement = conn.createStatement();
                String temp = "\'" + "TRAY" + "\'";
                statement.executeQuery("INSERT INTO PROPERTY (OPTION_NAME, OPTION_VALUE) VALUES (" + temp + ", " + 0 + ")");
                temp = "\'" + "WINDOWS_OPEN" + "\'";
                statement.executeQuery("INSERT INTO PROPERTY (OPTION_NAME, OPTION_VALUE) VALUES (" + temp + ", " + 0 + ")");
                temp = "\'" + "TIME_NOT_OPENED" + "\'";
                statement.executeQuery("INSERT INTO PROPERTY (OPTION_NAME, OPTION_VALUE) VALUES (" + temp + ", " + 0 + ")");
                statement.close();
            } catch (SQLException err) {
            }
            options.add(new Property("TRAY", 0));
            options.add(new Property("WINDOWS_OPEN", 0));
            options.add(new Property("TIME_NOT_OPENED", 0));
        }
        return options;
    }

    public static List<Program> getPrograms() {
        Statement statement;
        List<Program> programs = new ArrayList<Program>();
        try {
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_REQUEST_PROGRAMS);
            while (resultSet.next()) {
                Program temp_prop = new Program(resultSet.getString(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
                programs.add(temp_prop);
            }
            statement.close();
        } catch (SQLException err) {
            try {
                statement = conn.createStatement();
                statement.executeQuery(SQL_CREATE_PROGRAMS);
            } catch (SQLException error) {
                System.out.println(error);
            }
        }
        return programs;
    }

    public static boolean updateProperty(List<Property> pr) {
        Statement statement;
        try {
            statement = conn.createStatement();
            for (Property prop :
                    pr) {
                try {
                    String temp = "\'" + prop.getOption() + "\'";
                    statement.executeQuery("UPDATE PROPERTY SET OPTION_VALUE = " + prop.getValue() + " WHERE OPTION_NAME = " + temp);
                } catch (Exception e) {
                    System.out.println(e);

                }
            }
            statement.close();
        } catch (SQLException err) {
            return false;
        }
        return true;
    }

    public static boolean updatePrograms(List<Program> pr) {
        Statement statement;
        try {
            statement = conn.createStatement();
            for (Program prop :
                    pr) {
                String temp = "\'" + prop.getName() + "\'";
                try {
                    statement.executeQuery("INSERT INTO PROGRAMS (NAME, WORK_TIME, REST_TIME, IS_CHECKED) VALUES (" + temp + ", " + prop.getWork_time() + ", " + prop.getRest_time() + ", " + prop.getIs_checked() + ")");
                } catch (Exception e) {
                    statement.executeQuery("UPDATE PROGRAMS SET WORK_TIME = " + prop.getWork_time() + ", REST_TIME = " + prop.getRest_time() + ", IS_CHECKED = " + prop.getIs_checked() + " WHERE NAME = " + temp);
                }
            }
            statement.close();
        } catch (SQLException err) {
            return false;
        }
        return true;
    }

}