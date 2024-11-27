package controllers;

import models.Advisor;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdvisorController {

    // Danışman Ekleme
    public void addAdvisor(Advisor advisor) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Advisor (StudentID, FirstName, MidName, LastName, Email, PhoneNo, Major) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, advisor.getStudentID());
                stmt.setString(2, advisor.getFirstName());
                stmt.setString(3, advisor.getMidName());
                stmt.setString(4, advisor.getLastName());
                stmt.setString(5, advisor.getEmail());
                stmt.setString(6, advisor.getPhoneNo());
                stmt.setString(7, advisor.getMajor());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            advisor.setAdvisorID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Danışmanı Güncelleme
    public void updateAdvisor(Advisor advisor) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Advisor SET StudentID = ?, FirstName = ?, MidName = ?, LastName = ?, Email = ?, PhoneNo = ?, Major = ? WHERE AdvisorID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, advisor.getStudentID());
                stmt.setString(2, advisor.getFirstName());
                stmt.setString(3, advisor.getMidName());
                stmt.setString(4, advisor.getLastName());
                stmt.setString(5, advisor.getEmail());
                stmt.setString(6, advisor.getPhoneNo());
                stmt.setString(7, advisor.getMajor());
                stmt.setInt(8, advisor.getAdvisorID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Danışmanı Silme
    public void deleteAdvisor(int advisorID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Advisor WHERE AdvisorID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, advisorID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Danışmanları Al
    public List<Advisor> getAllAdvisors() {
        List<Advisor> advisors = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Advisor";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Advisor advisor = new Advisor();
                    advisor.setAdvisorID(rs.getInt("AdvisorID"));
                    advisor.setStudentID(rs.getInt("StudentID"));
                    advisor.setFirstName(rs.getString("FirstName"));
                    advisor.setMidName(rs.getString("MidName"));
                    advisor.setLastName(rs.getString("LastName"));
                    advisor.setEmail(rs.getString("Email"));
                    advisor.setPhoneNo(rs.getString("PhoneNo"));
                    advisor.setMajor(rs.getString("Major"));
                    advisors.add(advisor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return advisors;
    }

    // Danışman ID'sine Göre Danışman Bulma
    public Advisor getAdvisorById(int advisorID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Advisor WHERE AdvisorID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, advisorID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Advisor advisor = new Advisor();
                        advisor.setAdvisorID(rs.getInt("AdvisorID"));
                        advisor.setStudentID(rs.getInt("StudentID"));
                        advisor.setFirstName(rs.getString("FirstName"));
                        advisor.setMidName(rs.getString("MidName"));
                        advisor.setLastName(rs.getString("LastName"));
                        advisor.setEmail(rs.getString("Email"));
                        advisor.setPhoneNo(rs.getString("PhoneNo"));
                        advisor.setMajor(rs.getString("Major"));
                        return advisor;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
