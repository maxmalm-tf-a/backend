/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.sql.*;
import java.util.UUID;

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
        
    private String createToken(String userId) {
        String token = UUID.randomUUID().toString();
        try {
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO tokens VALUES (?, now() + '30 minutes', ?) LIMIT 1"
            );
            query.setString(1, token);
            query.setString(2, userId);
            query.executeUpdate(); // http://stackoverflow.com/a/21276130
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        }
        return token;
    }
    
    private String checkToken(String token) {
        try {
            PreparedStatement query = connection.prepareStatement(
                "SELECT * FROM tokens WHERE token = ? LIMIT 1"
            );
            query.setString(1, token);
            ResultSet rs = query.executeQuery(); // http://stackoverflow.com/a/21276130
            rs.next();
            System.out.println(rs.getTimestamp("validUntil"));
            java.util.Date date= new java.util.Date();
            System.out.println(new Timestamp(date.getTime()));
            if(new Timestamp(date.getTime()).before(rs.getTimestamp("validuntil"))) {
                System.out.println("Token valid");
                extendToken(token); // extend the life of the token for 30 minutes
                return rs.getString("userId");
            }
            else {
                System.out.println("Token expired");
            }
        }
        catch(SQLException e) {
            System.out.print("Failed");
            e.printStackTrace();
        } 
        return "error";
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
        if(username.equals("max") && password.equals("javaproffs")) {
            return createToken(username);
        }
        else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }
}
