package controllers;

import models.Advisor;
import models.Exam;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamController {

    // Sınav Ekleme
    public void addExam(Exam exam) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Exam (StudentID, ExamDate, ExamTime) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, exam.getStudentID());
                stmt.setDate(2, exam.getExamDate());
                stmt.setTime(3, exam.getExamTime());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            exam.setExamID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sınavı Güncelleme
    public void updateExam(Exam exam) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Exam SET StudentID = ?, ExamDate = ?, ExamTime = ? WHERE ExamID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, exam.getStudentID());
                stmt.setDate(2, exam.getExamDate());
                stmt.setTime(3, exam.getExamTime());
                stmt.setInt(4, exam.getExamID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sınavı Silme
    public void deleteExam(int examID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Exam WHERE ExamID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, examID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Sınavları Getir
    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Exam";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Exam exam = new Exam();
                    exam.setExamID(rs.getInt("ExamID"));
                    exam.setStudentID(rs.getInt("StudentID"));
                    exam.setExamDate(rs.getDate("ExamDate"));
                    exam.setExamTime(rs.getTime("ExamTime"));
                    exams.add(exam);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    // Sınav ID'sine Göre Sınav Getir
    public Exam getExamById(int examID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Exam WHERE ExamID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, examID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Exam exam = new Exam();
                        exam.setExamID(rs.getInt("ExamID"));
                        exam.setStudentID(rs.getInt("StudentID"));
                        exam.setExamDate(rs.getDate("ExamDate"));
                        exam.setExamTime(rs.getTime("ExamTime"));
                        return exam;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Belirli bir öğrenciye ait sınavları getir
    public List<Exam> getExamsByStudentId(int studentID) {
        List<Exam> exams = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Exam WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Exam exam = new Exam();
                        exam.setExamID(rs.getInt("ExamID"));
                        exam.setStudentID(rs.getInt("StudentID"));
                        exam.setExamDate(rs.getDate("ExamDate"));
                        exam.setExamTime(rs.getTime("ExamTime"));
                        exams.add(exam);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

}
