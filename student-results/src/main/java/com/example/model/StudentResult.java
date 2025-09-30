package com.example.model;

public class StudentResult {
  public final String rollNo, className, batch, department, course;
  public final int semester;
  public final double marks;

  public StudentResult(String rollNo, String className, String batch,
                       String department, String course,
                       int semester, double marks) {
    this.rollNo = rollNo;
    this.className = className;
    this.batch = batch;
    this.department = department;
    this.course = course;
    this.semester = semester;
    this.marks = marks;
  }
}
