package controllers;

import models.Topic;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicController {

    // Yeni Konu Ekleme
    public void addTopic(Topic topic) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Topic (CourseID, TopicName, Difficulty) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, topic.getCourseID());
                stmt.setString(2, topic.getTopicName());
                stmt.setString(3, topic.getDifficulty());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            topic.setTopicID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Konuyu Güncelleme
    public void updateTopic(Topic topic) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Topic SET CourseID = ?, TopicName = ?, Difficulty = ? WHERE TopicID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, topic.getCourseID());
                stmt.setString(2, topic.getTopicName());
                stmt.setString(3, topic.getDifficulty());
                stmt.setInt(4, topic.getTopicID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Konuyu Silme
    public void deleteTopic(int topicID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Topic WHERE TopicID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, topicID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Konuları Getir
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Topic";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Topic topic = new Topic();
                    topic.setTopicID(rs.getInt("TopicID"));
                    topic.setCourseID(rs.getInt("CourseID"));
                    topic.setTopicName(rs.getString("TopicName"));
                    topic.setDifficulty(rs.getString("Difficulty"));
                    topics.add(topic);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

    // Konu ID'sine Göre Konu Getir
    public Topic getTopicById(int topicID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Topic WHERE TopicID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, topicID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Topic topic = new Topic();
                        topic.setTopicID(rs.getInt("TopicID"));
                        topic.setCourseID(rs.getInt("CourseID"));
                        topic.setTopicName(rs.getString("TopicName"));
                        topic.setDifficulty(rs.getString("Difficulty"));
                        return topic;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
