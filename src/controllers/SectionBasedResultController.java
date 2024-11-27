package controllers;

import models.SectionBasedResult;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionBasedResultController {

    // Yeni Bölüm Bazlı Sonuç Ekleme
    public void addSectionBasedResult(SectionBasedResult result) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO SectionBasedResult (ExamID, SectionName, TrueNum, FalseNum, Net) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, result.getExamID());
                stmt.setString(2, result.getSectionName());
                stmt.setInt(3, result.getTrueNum());
                stmt.setInt(4, result.getFalseNum());
                stmt.setFloat(5, result.getNet());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Bölüm Bazlı Sonuç Güncelleme
    public void updateSectionBasedResult(SectionBasedResult result) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE SectionBasedResult SET TrueNum = ?, FalseNum = ?, Net = ? WHERE ExamID = ? AND SectionName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, result.getTrueNum());
                stmt.setInt(2, result.getFalseNum());
                stmt.setFloat(3, result.getNet());
                stmt.setInt(4, result.getExamID());
                stmt.setString(5, result.getSectionName());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Bölüm Bazlı Sonuç Silme
    public void deleteSectionBasedResult(int examID, String sectionName) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM SectionBasedResult WHERE ExamID = ? AND SectionName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, examID);
                stmt.setString(2, sectionName);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Bölüm Bazlı Sonuçları Getir
    public List<SectionBasedResult> getAllSectionBasedResults() {
        List<SectionBasedResult> results = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM SectionBasedResult";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    SectionBasedResult result = new SectionBasedResult();
                    result.setExamID(rs.getInt("ExamID"));
                    result.setSectionName(rs.getString("SectionName"));
                    result.setTrueNum(rs.getInt("TrueNum"));
                    result.setFalseNum(rs.getInt("FalseNum"));
                    result.setNet(rs.getFloat("Net"));
                    results.add(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // ExamID ve SectionName'e Göre Bölüm Bazlı Sonuç Getir
    public SectionBasedResult getSectionBasedResultById(int examID, String sectionName) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM SectionBasedResult WHERE ExamID = ? AND SectionName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, examID);
                stmt.setString(2, sectionName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        SectionBasedResult result = new SectionBasedResult();
                        result.setExamID(rs.getInt("ExamID"));
                        result.setSectionName(rs.getString("SectionName"));
                        result.setTrueNum(rs.getInt("TrueNum"));
                        result.setFalseNum(rs.getInt("FalseNum"));
                        result.setNet(rs.getFloat("Net"));
                        return result;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
