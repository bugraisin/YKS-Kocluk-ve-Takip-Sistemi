package controllers;

import models.Advisor;
import models.Student;
import utils.DatabaseManager;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    // Öğrenci Ekleme
    public void addStudent(Student student) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Student (FirstName, MidName, LastName, Email, PhoneNo, ClassNo, GoalUni, GoalMajor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, student.getFirstName());
                stmt.setString(2, student.getMidName());
                stmt.setString(3, student.getLastName());
                stmt.setString(4, student.getEmail());
                stmt.setString(5, student.getPhoneNo());
                stmt.setInt(6, student.getClassNo());
                stmt.setString(7, student.getGoalUni());
                stmt.setString(8, student.getGoalMajor());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    // ID'yi almak için
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            student.setStudentID(generatedKeys.getInt(1)); // Otomatik ID
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Öğrenciyi Düzenleme
    public void updateStudent(Student student) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE Student SET FirstName = ?, MidName = ?, LastName = ?, Email = ?, PhoneNo = ?, ClassNo = ?, GoalUni = ?, GoalMajor = ? WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, student.getFirstName());
                stmt.setString(2, student.getMidName());
                stmt.setString(3, student.getLastName());
                stmt.setString(4, student.getEmail());
                stmt.setString(5, student.getPhoneNo());
                stmt.setInt(6, student.getClassNo());
                stmt.setString(7, student.getGoalUni());
                stmt.setString(8, student.getGoalMajor());
                stmt.setInt(9, student.getStudentID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Öğrenciyi Silme
    public void deleteStudent(int studentID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM Student WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentID);

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Hata: Silinmek istenen öğrenci bulunamadı. Lütfen öğrenci ID'sini kontrol ediniz.",
                            "Silme Hatası",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "Hata: Bu öğrenci, başka bir kayıtla ilişkili olduğu için silinemiyor. İlgili kayıtları kontrol edin.",
                    "Silme Hatası",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Hata: Öğrenci silinirken bir sorun oluştu. Lütfen tekrar deneyiniz.\nDetay: " + e.getMessage(),
                    "Veritabanı Hatası",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Öğrenci Arama (Adına veya Soyadına göre arama)
    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Student WHERE FirstName LIKE ? OR LastName LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                String searchName = "%" + name + "%"; // Arama kriterini al
                stmt.setString(1, searchName);
                stmt.setString(2, searchName);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Student student = new Student();
                        student.setStudentID(rs.getInt("StudentID"));
                        student.setFirstName(rs.getString("FirstName"));
                        student.setMidName(rs.getString("MidName"));
                        student.setLastName(rs.getString("LastName"));
                        student.setEmail(rs.getString("Email"));
                        student.setPhoneNo(rs.getString("PhoneNo"));
                        student.setClassNo(rs.getInt("ClassNo"));
                        student.setGoalUni(rs.getString("GoalUni"));
                        student.setGoalMajor(rs.getString("GoalMajor"));
                        students.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Tüm Öğrencileri Al
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Student";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setStudentID(rs.getInt("StudentID"));
                    student.setFirstName(rs.getString("FirstName"));
                    student.setMidName(rs.getString("MidName"));
                    student.setLastName(rs.getString("LastName"));
                    student.setEmail(rs.getString("Email"));
                    student.setPhoneNo(rs.getString("PhoneNo"));
                    student.setClassNo(rs.getInt("ClassNo"));
                    student.setGoalUni(rs.getString("GoalUni"));
                    student.setGoalMajor(rs.getString("GoalMajor"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student getStudentById(int studentID) {
        Student student = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Student WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        student = new Student();
                        student.setStudentID(rs.getInt("StudentID"));
                        student.setFirstName(rs.getString("FirstName"));
                        student.setMidName(rs.getString("MidName"));
                        student.setLastName(rs.getString("LastName"));
                        student.setEmail(rs.getString("Email"));
                        student.setPhoneNo(rs.getString("PhoneNo"));
                        student.setClassNo(rs.getInt("ClassNo"));
                        student.setGoalUni(rs.getString("GoalUni"));
                        student.setGoalMajor(rs.getString("GoalMajor"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }
    // Öğrenciye Danışman Ekleme
    public void addAdvisor(Advisor advisor) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO Advisor (StudentID, FirstName, LastName, Email, PhoneNo, Major) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, advisor.getStudentID());
                stmt.setString(2, advisor.getFirstName());
                stmt.setString(3, advisor.getLastName());
                stmt.setString(4, advisor.getEmail());
                stmt.setString(5, advisor.getPhoneNo());
                stmt.setString(6, advisor.getMajor());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Öğrencinin ID'sine göre danışmanlarını alma
    public List<Advisor> getAdvisorsByStudentId(int studentID) {
        List<Advisor> advisors = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM Advisor WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentID);
                try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return advisors;
    }

    public List<Student> searchStudents(String firstName, String midName, String lastName, String email,
                                        String phone, String classNo, String goalUni, String goalMajor) {
        List<Student> students = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Student WHERE 1=1");

        if (!firstName.isEmpty()) {
            sqlBuilder.append(" AND FirstName LIKE ?");
        }
        if (!midName.isEmpty()) {
            sqlBuilder.append(" AND MidName LIKE ?");
        }
        if (!lastName.isEmpty()) {
            sqlBuilder.append(" AND LastName LIKE ?");
        }
        if (!email.isEmpty()) {
            sqlBuilder.append(" AND Email LIKE ?");
        }
        if (!phone.isEmpty()) {
            sqlBuilder.append(" AND PhoneNo LIKE ?");
        }
        if (!classNo.isEmpty()) {
            sqlBuilder.append(" AND ClassNo = ?");
        }
        if (!goalUni.isEmpty()) {
            sqlBuilder.append(" AND GoalUni LIKE ?");
        }
        if (!goalMajor.isEmpty()) {
            sqlBuilder.append(" AND GoalMajor LIKE ?");
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;
            if (!firstName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + firstName + "%");
            }
            if (!midName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + midName + "%");
            }
            if (!lastName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + lastName + "%");
            }
            if (!email.isEmpty()) {
                stmt.setString(paramIndex++, "%" + email + "%");
            }
            if (!phone.isEmpty()) {
                stmt.setString(paramIndex++, "%" + phone + "%");
            }
            if (!classNo.isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(classNo));
            }
            if (!goalUni.isEmpty()) {
                stmt.setString(paramIndex++, "%" + goalUni + "%");
            }
            if (!goalMajor.isEmpty()) {
                stmt.setString(paramIndex++, "%" + goalMajor + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setStudentID(rs.getInt("StudentID"));
                    student.setFirstName(rs.getString("FirstName"));
                    student.setMidName(rs.getString("MidName"));
                    student.setLastName(rs.getString("LastName"));
                    student.setEmail(rs.getString("Email"));
                    student.setPhoneNo(rs.getString("PhoneNo"));
                    student.setClassNo(rs.getInt("ClassNo"));
                    student.setGoalUni(rs.getString("GoalUni"));
                    student.setGoalMajor(rs.getString("GoalMajor"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }





}
