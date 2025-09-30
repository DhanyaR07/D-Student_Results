package com.example;

import com.example.dao.StudentDao;
import com.example.model.StudentResult;
import static spark.Spark.*;

import java.util.List;

public class WebServer {
  public static void main(String[] args) {
    port(4567);
    StudentDao dao = new StudentDao();

    get("/", (req, res) -> 
      "<h1>Student Results</h1>" +
      "<ul>" +
        "<li><a href='/students'>All Students</a></li>" +
        "<li><a href='/students/new'>Add Result</a></li>" +
        "<li><a href='/results'>Semester Results</a></li>" +
      "</ul>"
    );

    // List all students
    get("/students", (req, res) -> {
      List<StudentResult> all = dao.findAll();
      StringBuilder sb = new StringBuilder("<h1>All Students</h1><a href='/'>Home</a><ul>");
      for (StudentResult s : all) {
        sb.append("<li>")
          .append(s.rollNo).append(" - ").append(s.className)
          .append(" - Sem: ").append(s.semester)
          .append(" - Marks: ").append(s.marks)
          .append("</li>");
      }
      sb.append("</ul>");
      return sb.toString();
    });

    // Show form
    get("/students/new", (req, res) -> 
      "<h1>Add Student Result</h1>" +
      "<form method='post' action='/students'>" +
        "Roll No: <input name='roll_no'><br>" +
        "Class: <input name='class'><br>" +
        "Batch: <input name='batch'><br>" +
        "Department: <input name='department'><br>" +
        "Course: <input name='course'><br>" +
        "Semester: <input name='semester' type='number'><br>" +
        "Marks: <input name='marks' type='number' step='0.01'><br>" +
        "<button type='submit'>Save</button>" +
      "</form>" +
      "<a href='/'>Home</a>"
    );

    // Handle form submit
    post("/students", (req, res) -> {
      String rollNo = req.queryParams("roll_no");
      String className = req.queryParams("class");
      String batch = req.queryParams("batch");
      String department = req.queryParams("department");
      String course = req.queryParams("course");
      int semester = Integer.parseInt(req.queryParams("semester"));
      double marks = Double.parseDouble(req.queryParams("marks"));

      StudentResult s = new StudentResult(rollNo, className, batch, department, course, semester, marks);
      dao.save(s);
      res.redirect("/students");
      return "";
    });

    // Semester results (query ?sem=1) or show small form
    get("/results", (req, res) -> {
      String semStr = req.queryParams("sem");
      if (semStr == null) {
        return 
          "<h1>Semester Results</h1>" +
          "<form method='get' action='/results'>" +
            "Semester: <input name='sem' type='number'>" +
            "<button type='submit'>Show</button>" +
          "</form>" +
          "<a href='/'>Home</a>";
      } else {
        int sem = Integer.parseInt(semStr);
        List<StudentResult> list = dao.findBySemester(sem);
        StringBuilder sb = new StringBuilder("<h1>Semester " + sem + " Results</h1><ul>");
        for (StudentResult s : list) {
          sb.append("<li>").append(s.rollNo).append(" - ").append(s.marks).append("</li>");
        }
        sb.append("</ul><a href='/'>Home</a>");
        return sb.toString();
      }
    });

    // graceful shutdown on Ctrl+C
  }
}

