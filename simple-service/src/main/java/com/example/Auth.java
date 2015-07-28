/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.sql.*;

/**
 *
 * @author max_000
 */
public class Auth {
    public String test = "works";
    
    public boolean verifyCredentials(String username, String password) {
        return username.equals("max") && password.equals("javaproffs");
    }
}
