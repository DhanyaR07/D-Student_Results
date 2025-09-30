package com.example.db;
import java.sql.*;
public class Database {
private static final String URL = "jdbc:sqlite:student.db";
static {
try (Connection c = DriverManager.getConnection(URL);
Statement st = c.createStatement()) {
st.executeUpdate("PRAGMA foreign_keys = ON");
st.executeUpdate("CREATE TABLE IF NOT EXISTS student_results (" +
"id INTEGER PRIMARY KEY AUTOINCREMENT," +
"roll_no TEXT NOT NULL," +
"class TEXT NOT NULL," +
"batch TEXT NOT NULL," +
"department TEXT NOT NULL," +
"course TEXT NOT NULL," +
"semester INTEGER NOT NULL," +
"marks REAL NOT NULL" +
")");
} catch (SQLException e) {
throw new RuntimeException(e);
}
}
public static Connection getConnection() throws SQLException {
return DriverManager.getConnection(URL);
}
}
