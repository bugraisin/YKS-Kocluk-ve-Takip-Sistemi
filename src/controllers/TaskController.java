package controllers;

import models.Task;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskController {

    // Görev Ekleme
    public void addTask(Task task) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Task (StudentID, AdvisorID, Text, DueDate) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, task.getStudentID());
                stmt.setInt(2, task.getAdvisorID());
                stmt.setString(3, task.getText());
                stmt.setDate(4, task.getDueDate());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            task.setTaskID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Görevi Düzenleme
    public void updateTask(Task task) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Task SET StudentID = ?, AdvisorID = ?, Text = ?, DueDate = ? WHERE TaskID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, task.getStudentID());
                stmt.setInt(2, task.getAdvisorID());
                stmt.setString(3, task.getText());
                stmt.setDate(4, task.getDueDate());
                stmt.setInt(5, task.getTaskID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Görevi Silme
    public void deleteTask(int taskID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Task WHERE TaskID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, taskID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Görevleri Al
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Task";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setTaskID(rs.getInt("TaskID"));
                    task.setStudentID(rs.getInt("StudentID"));
                    task.setAdvisorID(rs.getInt("AdvisorID"));
                    task.setText(rs.getString("Text"));
                    task.setDueDate(rs.getDate("DueDate"));
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Görev ID'sine Göre Görev Bulma
    public Task getTaskById(int taskID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Task WHERE TaskID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, taskID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Task task = new Task();
                        task.setTaskID(rs.getInt("TaskID"));
                        task.setStudentID(rs.getInt("StudentID"));
                        task.setAdvisorID(rs.getInt("AdvisorID"));
                        task.setText(rs.getString("Text"));
                        task.setDueDate(rs.getDate("DueDate"));
                        return task;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
