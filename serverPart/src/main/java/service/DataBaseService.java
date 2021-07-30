package service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.List;

public class DataBaseService {

    private static final String DB_NAME = "GB_cloud";
    private static final String TABLE_USERS = "Users";
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;
    private static final Logger LOGGER = LogManager.getLogger(DataBaseService.class.getName());

    public static void createUserTable() {
        connectDB();

        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
     //       LOGGER.error("Error: Can not create statement!");
        }


        StringBuilder stringCreator = new StringBuilder();

        stringCreator.append("CREATE TABLE  IF NOT EXISTS  ")
                .append(TABLE_USERS)
                .append(" (id       INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(" Login    TEXT    UNIQUE NOT NULL,")
                .append(" Password TEXT    NOT NULL,")
                .append(" Nickname TEXT    NOT NULL); ");
        LOGGER.debug(stringCreator);


        try {
            statement.executeUpdate(String.valueOf(stringCreator));
        } catch (SQLException throwables) {
            LOGGER.error("Error in executeUpdate!" + throwables);
        }
        disconnectDB();
    }


    public static void connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException e) {
           LOGGER.error("Error: \"org.sqlite.JDBC\" could not load to memory!");
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME + ".db");
        } catch (SQLException throwables) {
            LOGGER.error("Error: Can not connect to DB!" + throwables);
        }
    }

    public static void disconnectDB() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                LOGGER.error("Error:  could not close statement! " + throwables);
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException throwables) {
                LOGGER.error("Error:  could not close PreparedStatement! " + throwables);
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                LOGGER.error("Error:  could not close connect to DB! " + throwables);
            }
        }
    }

    public static void createUsers(List<String[]> users) {
        connectDB();

        try {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement("insert into " + TABLE_USERS + "(Login, Password,Nickname) values (?, ?, ?)");
            for (String[] user : users) {
                ps.setString(1, user[0]);
                ps.setString(2, user[1]);
                ps.setString(3, user[2]);
                ps.execute();
            }
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
           LOGGER.error("Error of Autcommit or  prepareStatement :" + throwables);
        }

        disconnectDB();
    }

    public static String authentication(String login, String pass) {
        connectDB();
        String nickName = "";

        try {
            statement = connection.createStatement();
            String query = "select Nickname from " + TABLE_USERS + " where Login = '" + login + "'and Password ='" + pass + "';";
            LOGGER.debug(query);
            ResultSet rs = statement.executeQuery(query);
            nickName = rs.getString("Nickname");
        } catch (SQLException throwables) {
            LOGGER.error("Error of executeQuery :" + throwables);
        }
        disconnectDB();
        return nickName;
    }

    public static String authentication(String login) {
        connectDB();
        String nickName = "";

        try {
            statement = connection.createStatement();
            String query = "select Nickname from " + TABLE_USERS + " where Login = '" + login + "';";
            ResultSet rs = statement.executeQuery(query);
            nickName = rs.getString("Nickname");
        } catch (SQLException throwables) {
            LOGGER.error("Error of executeQuery :" + throwables);
        }
        disconnectDB();
        return nickName;
    }

}
