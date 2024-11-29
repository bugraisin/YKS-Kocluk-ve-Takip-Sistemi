package forms;

import javax.swing.*;
import java.awt.*;
import models.Student;

public class StudentForm extends JDialog {
    public interface StudentFormListener {
        void onStudentSubmitted(Student student);
    }

    public StudentForm(StudentFormListener listener) {
        // Dialog penceresini başlatma
        setTitle("Crate New Student / Update");
        setSize(400, 350);  // Yeterli büyüklükte bir form
        setLayout(new GridLayout(9, 2)); // 9 satır 2 sütun

        // Form alanları
        add(new JLabel("FirstName:"));
        JTextField firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("MidName:"));
        JTextField midNameField = new JTextField();
        add(midNameField);

        add(new JLabel("LastName:"));
        JTextField lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("PhoneNo:"));
        JTextField phoneNoField = new JTextField();
        add(phoneNoField);

        add(new JLabel("ClassNo:"));
        JTextField classField = new JTextField();
        add(classField);

        add(new JLabel("GoalUni:"));
        JTextField goalUniField = new JTextField();
        add(goalUniField);

        add(new JLabel("GoalMajor:"));
        JTextField goalMajorField = new JTextField();
        add(goalMajorField);

        // Kaydet butonu
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Formdan alınan verilerle yeni öğrenci nesnesi oluşturma
            try {
                Student student = new Student();
                student.setFirstName(firstNameField.getText());
                student.setMidName(midNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setEmail(emailField.getText());
                student.setPhoneNo(phoneNoField.getText());
                student.setClassNo(Integer.parseInt(classField.getText()));
                student.setGoalUni(goalUniField.getText());
                student.setGoalMajor(goalMajorField.getText());

                // Veriyi geri çağırma ile gönderme
                listener.onStudentSubmitted(student);
                dispose(); // Formu kapatma
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }
}
