package org.example.dao;

import org.example.driver.ConnectionMySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DAO {
    protected Connection conn;
    protected PreparedStatement pre;
    protected Statement st;
    protected ResultSet rs;

    public DAO() {
        try {
            conn = ConnectionMySql.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void connect() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionMySql.getConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
