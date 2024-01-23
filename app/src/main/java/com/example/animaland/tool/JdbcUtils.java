package com.example.animaland.tool;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {
    static String driver="com.mysql.jdbc.Driver";
    static String url="jdbc:mysql://139.159.189.52:3306/animaland?useCharacter=utf8" ;
//    &useSSL=true
    static String username="root";
    static String password="02021989";

    public static Connection getConn() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
            Connection conn = null;
            //驱动只加载一次
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url,username,password);
            return conn;
    }

    public static void close(Connection conn){

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    }


