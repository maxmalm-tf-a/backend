/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.sql.*;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author max_000
 */
public class Database {
    
    Connection connection = null;
    
    Database() {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/test", 
                    "test",
                    "test");
        }
        catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
        
    private String createToken(int userId) {
        String token = UUID.randomUUID().toString();
        try {
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO tokens VALUES (?, now() + '30 minutes', ?) LIMIT 1"
            );
            query.setString(1, token);
            query.setInt(2, userId);
            query.executeUpdate(); // http://stackoverflow.com/a/21276130
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        }
        return token;
    }
    
    public int checkToken(String token) {
        try {
            PreparedStatement query = connection.prepareStatement(
                "SELECT * FROM tokens WHERE token = ? LIMIT 1"
            );
            query.setString(1, token);
            ResultSet rs = query.executeQuery(); // http://stackoverflow.com/a/21276130
            if (!rs.isBeforeFirst() ) {
                System.out.println("No token found");
                throw new IllegalArgumentException("No token found");
            }
            rs.next();
            System.out.println(rs.getTimestamp("validUntil"));
            java.util.Date date= new java.util.Date();
            System.out.println(new Timestamp(date.getTime()));
            if(new Timestamp(date.getTime()).before(rs.getTimestamp("validuntil"))) {
                System.out.println("Token valid");
                extendToken(token); // extend the life of the token for 30 minutes
                return rs.getInt("userid");
            }
            else {
                System.out.println("Token expired");
                throw new IllegalArgumentException("Token expired");
            }
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        }
        return 0;
    }
    
    public void extendToken(String token) {
        try {
            PreparedStatement query = connection.prepareStatement(
                "UPDATE tokens SET validuntil = now() + '30 minutes' WHERE token = ?"
            );
            query.setString(1, token);
            query.executeUpdate();
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        }
        
    }
    
    public String verifyCredentials(String username, String password) {
        String token = null;
        try {
            PreparedStatement query = connection.prepareStatement(
                "SELECT email, password, userid FROM users WHERE email = ?"
            );
            query.setString(1, username);
            ResultSet rs = query.executeQuery();
            rs.next();
            System.out.println(rs.getString("password"));
            if (BCrypt.checkpw(password, rs.getString("password"))) {
                System.out.println("Correct password!");
                token = createToken(rs.getInt("userid"));
            }
            else {
                throw new IllegalArgumentException("Invalid credentials");
            }
            
        }
        catch(SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return token;
    }
    
    public String createUser(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String token = null;
        
        try {
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO users (email, password) VALUES (?, ?) LIMIT 1 RETURNING userid, email"
            );
            query.setString(1, username);
            query.setString(2, hashed);
            ResultSet rs = query.executeQuery(); // http://stackoverflow.com/a/21276130
            rs.next();
            token = createToken(rs.getInt("userid"));
            System.out.println(token);
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        }
        return token;
    }
    
    public boolean checkUserFree(String username) {
        boolean free = false;
        try {
            PreparedStatement query = connection.prepareStatement(
                "SELECT count(*) AS sum FROM users WHERE email = ?"
            );
            query.setString(1, username);
            ResultSet rs = query.executeQuery();
            rs.next();
            if(rs.getInt("sum") == 0) {
                free = true;
                System.out.println("free!");
            }
            
        }
        catch(SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return free;
    }
    
    public ResultSet getTransactions(String token) {
        int userid = checkToken(token);
        ResultSet rs = null;
        if(userid > 0) {
            try {
                PreparedStatement query = connection.prepareStatement(
                    "SELECT text, value, sum, date FROM transactions WHERE userid = ? ORDER BY transactionid Desc"
                );
                query.setInt(1, userid);
                rs = query.executeQuery(); // http://stackoverflow.com/a/21276130
            }
            catch(SQLException e) {
                System.out.print("Failed");
                e.printStackTrace();
            }
        }
        else {
            throw new IllegalArgumentException("Token invalid");
        }
        return rs;
    }
    
    public void insertTransactions(String token, String text, int value) {
        // select top 1 * from TABLE order by date desc
    }
}
