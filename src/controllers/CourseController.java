package controllers;

import models.Course;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseController {

    // Yeni Kurs Ekleme
    public void addCourse(Course course) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Course (StudentID, CourseName, Category) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, course.getStudentID());
                stmt.setString(2, course.getCourseName());
                stmt.setString(3, course.getCategory());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            course.setCourseID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kursu Güncelleme
    public void updateCourse(Course course) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Course SET StudentID = ?, CourseName = ?, Category = ? WHERE CourseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, course.getStudentID());
                stmt.setString(2, course.getCourseName());
                stmt.setString(3, course.getCategory());
                stmt.setInt(4, course.getCourseID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kursu Silme
    public void deleteCourse(int courseID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Course WHERE CourseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, courseID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Kursları Getir
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Course";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("CourseID"));
                    course.setStudentID(rs.getInt("StudentID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setCategory(rs.getString("Category"));
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Kurs ID'sine Göre Kurs Getir
    public Course getCourseById(int courseID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Course WHERE CourseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, courseID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Course course = new Course();
                        course.setCourseID(rs.getInt("CourseID"));
                        course.setStudentID(rs.getInt("StudentID"));
                        course.setCourseName(rs.getString("CourseName"));
                        course.setCategory(rs.getString("Category"));
                        return course;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
