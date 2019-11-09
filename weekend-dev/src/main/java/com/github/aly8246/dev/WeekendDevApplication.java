package com.github.aly8246.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.*;


@SpringBootApplication(scanBasePackages = "com.github.aly8246.dev")

public class WeekendDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeekendDevApplication.class, args);
    }

    @Bean
    public void test() {
        Connection con = null;
        try {
            Class.forName("mongodb.jdbc.MongoDriver");
            String url = "jdbc:mongo://148.70.16.82:27017/weekend-dev?rebuildschema=false";
            con = DriverManager.getConnection(url, "", "");
            String sql = "SELECT * FROM user_info";
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            System.out.println("Executed Mongo query: " + stmt.toString());

            rst.close();
            stmt.cancel();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
