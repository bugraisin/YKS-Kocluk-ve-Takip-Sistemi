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
        setTitle("Yeni Öğrenci Ekle / Düzenle");
        setSize(400, 350);  // Yeterli büyüklükte bir form
        setLayout(new GridLayout(9, 2)); // 9 satır 2 sütun

        // Form alanları
        add(new JLabel("Ad:"));
        JTextField firstNameField = new JTextField();
        add(firstNameField);

        add(new JLabel("Ortanca İsim:"));
        JTextField midNameField = new JTextField();
        add(midNameField);

        add(new JLabel("Soyad:"));
        JTextField lastNameField = new JTextField();
        add(lastNameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Telefon Numarası:"));
        JTextField phoneNoField = new JTextField();
        add(phoneNoField);

        add(new JLabel("Sınıf:"));
        JTextField classField = new JTextField();
        add(classField);

        add(new JLabel("Hedef Üniversite:"));
        JTextField goalUniField = new JTextField();
        add(goalUniField);

        add(new JLabel("Hedef Bölüm:"));
        JTextField goalMajorField = new JTextField();
        add(goalMajorField);

        // Kaydet butonu
        JButton saveButton = new JButton("Kaydet");
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
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doğru girin!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }
}
