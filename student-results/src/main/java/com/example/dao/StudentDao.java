package com.example.dao;

import com.example.db.Database;
import com.example.model.StudentResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
  private static final String INSERT_SQL =
    "INSERT INTO student_results (roll_no, class, batch, department, course, semester, marks) VALUES (?, ?, ?, ?, ?, ?, ?)";

  private static final String SELECT_ALL = "SELECT * FROM student_results";
  private static final String SELECT_BY_SEM = "SELECT * FROM student_results WHERE semester = ?";

  public void save(StudentResult s) {
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {
      ps.setString(1, s.rollNo);
      ps.setString(2, s.className);
      ps.setString(3, s.batch);
      ps.setString(4, s.department);
      ps.setString(5, s.course);
      ps.setInt(6, s.semester);
      ps.setDouble(7, s.marks);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<StudentResult> findAll() {
    List<StudentResult> list = new ArrayList<>();
    try (Connection c = Database.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(SELECT_ALL)) {
      while (rs.next()) {
        list.add(map(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  public List<StudentResult> findBySemester(int sem) {
    List<StudentResult> list = new ArrayList<>();
    try (Connection c = Database.getConnection();
         PreparedStatement ps = c.prepareStatement(SELECT_BY_SEM)) {
      ps.setInt(1, sem);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) list.add(map(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  private StudentResult map(ResultSet rs) throws SQLException {
    return new StudentResult(
      rs.getString("roll_no"),
      rs.getString("class"),      // column is named 'class' in DB
      rs.getString("batch"),
      rs.getString("department"),
      rs.getString("course"),
      rs.getInt("semester"),
      rs.getDouble("marks")
    );
  }
}

