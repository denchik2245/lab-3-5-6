package com.example.servlet;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class Db {
    private static final DataSource DS;

    static {
        try {
            DS = (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/FileMgrDS");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Cannot init datasource: " + e);
        }
    }

    private Db() { }

    public static Connection get() throws SQLException {
        return DS.getConnection();
    }
}
