package controllers;

import models.Resource;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceController {

    // Kaynak Ekleme
    public void addResource(Resource resource) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Resource (TopicID, Type, Title, URL) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, resource.getTopicID());
                stmt.setString(2, resource.getType());
                stmt.setString(3, resource.getTitle());
                stmt.setString(4, resource.getURL());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            resource.setResourceID(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kaynağı Güncelleme
    public void updateResource(Resource resource) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Resource SET TopicID = ?, Type = ?, Title = ?, URL = ? WHERE ResourceID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, resource.getTopicID());
                stmt.setString(2, resource.getType());
                stmt.setString(3, resource.getTitle());
                stmt.setString(4, resource.getURL());
                stmt.setInt(5, resource.getResourceID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kaynağı Silme
    public void deleteResource(int resourceID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Resource WHERE ResourceID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, resourceID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tüm Kaynakları Alma
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Resource";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Resource resource = new Resource();
                    resource.setResourceID(rs.getInt("ResourceID"));
                    resource.setTopicID(rs.getInt("TopicID"));
                    resource.setType(rs.getString("Type"));
                    resource.setTitle(rs.getString("Title"));
                    resource.setURL(rs.getString("URL"));
                    resources.add(resource);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    // ResourceID'ye Göre Kaynak Bulma
    public Resource getResourceById(int resourceID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Resource WHERE ResourceID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, resourceID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Resource resource = new Resource();
                        resource.setResourceID(rs.getInt("ResourceID"));
                        resource.setTopicID(rs.getInt("TopicID"));
                        resource.setType(rs.getString("Type"));
                        resource.setTitle(rs.getString("Title"));
                        resource.setURL(rs.getString("URL"));
                        return resource;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
