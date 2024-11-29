package controllers;

import models.Schedule;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleController {

    // Yeni Program Ekleme
    public void addSchedule(Schedule schedule) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Schedule (StudentID, StartDate, EndDate, Description, Title) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, schedule.getStudentID());
                stmt.setDate(2, schedule.getStartDate());
                stmt.setDate(3, schedule.getEndDate());
                stmt.setString(4, schedule.getDescription());
                stmt.setString(5, schedule.getTitle());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            schedule.setScheduleID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Program Güncelleme
    public void updateSchedule(Schedule schedule) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Schedule SET StudentID = ?, StartDate = ?, EndDate = ?, Description = ?, Title = ? WHERE ScheduleID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, schedule.getStudentID());
                stmt.setDate(2, schedule.getStartDate());
                stmt.setDate(3, schedule.getEndDate());
                stmt.setString(4, schedule.getDescription());
                stmt.setString(5, schedule.getTitle());
                stmt.setInt(6, schedule.getScheduleID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Program Silme
    public void deleteSchedule(int scheduleID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Schedule WHERE ScheduleID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, scheduleID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Programları Getir
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Schedule";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleID(rs.getInt("ScheduleID"));
                    schedule.setStudentID(rs.getInt("StudentID"));
                    schedule.setStartDate(rs.getDate("StartDate"));
                    schedule.setEndDate(rs.getDate("EndDate"));
                    schedule.setDescription(rs.getString("Description"));
                    schedule.setTitle(rs.getString("Title"));
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    // Program ID'sine Göre Program Getir
    public Schedule getScheduleById(int scheduleID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Schedule WHERE ScheduleID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, scheduleID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Schedule schedule = new Schedule();
                        schedule.setScheduleID(rs.getInt("ScheduleID"));
                        schedule.setStudentID(rs.getInt("StudentID"));
                        schedule.setStartDate(rs.getDate("StartDate"));
                        schedule.setEndDate(rs.getDate("EndDate"));
                        schedule.setDescription(rs.getString("Description"));
                        schedule.setTitle(rs.getString("Title"));
                        return schedule;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Belirli bir öğrenciye ait programları getir
    public List<Schedule> getSchedulesByStudentId(int studentID) {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Schedule WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Schedule schedule = new Schedule();
                        schedule.setScheduleID(rs.getInt("ScheduleID"));
                        schedule.setStudentID(rs.getInt("StudentID"));
                        schedule.setStartDate(rs.getDate("StartDate"));
                        schedule.setEndDate(rs.getDate("EndDate"));
                        schedule.setDescription(rs.getString("Description"));
                        schedule.setTitle(rs.getString("Title"));
                        schedules.add(schedule);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

}
