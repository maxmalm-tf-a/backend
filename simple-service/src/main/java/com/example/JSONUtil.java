/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

/**
 *
 * @author https://code.google.com/p/bonex-homerunning/source/browse/trunk/+bonex-homerunning/WEB-INF/src/java/com/bonex/sys/util/JSONUtil.java?spec=svn18&r=18
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
    /**
     * 转换ResultSet 到 JSONObject
     * @param rs
     * @return
     */
    public static final JSONObject ResultSet2JSONObject(ResultSet rs) {
        JSONObject element = null;
        JSONArray joa = new JSONArray();
        JSONObject jo = new JSONObject();
        int totalLength = 0;
        ResultSetMetaData rsmd = null;
        String columnName = null;
        String columnValue = null;
        try {
            rsmd = rs.getMetaData();
            while (rs.next()) {
                element = new JSONObject();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columnName = rsmd.getColumnName(i+1);
                    columnValue = rs.getString(columnName);
                    element.accumulate(columnName, columnValue);
                }
                joa.add(element);
                totalLength ++;
            }
            jo.accumulate("result", "success");
            jo.accumulate("rows", totalLength);
            jo.accumulate("data", joa);
        } catch (SQLException e) {
            jo.accumulate("result", "failure");
            jo.accumulate("error", e.getMessage());
        }
        return jo;
    }

    /**
     * 转换ResultSet 到 JSONString
     * @param rs
     * @return
     */
    public static final String ResultSet2JSONString(ResultSet rs) {
        return ResultSet2JSONObject(rs).toString();
    }

    public static final String Object2JSONString(Object o) {
        return null;
    }

    public static final String String2JSONString(String str) {
        return "{\"result\":\"success\",\"data\":\""+str+"\"}";
    }

    public static final String String2JSONStringError(String str) {
        return "{\"result\":\"error\",\"reason\":\""+str+"\"}";
    }

    public static final String Int2JSONString(int id) {
        return "{\"result\":\"success\",\"data\":"+id+"}";
    }
}