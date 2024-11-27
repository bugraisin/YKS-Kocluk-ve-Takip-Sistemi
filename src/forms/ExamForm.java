package forms;

import javax.swing.*;
import java.awt.*;
import models.Exam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamForm extends JDialog {
    public interface ExamFormListener {
        void onExamSubmitted(Exam exam);
    }

    public ExamForm(ExamFormListener listener) {
        setTitle("Yeni Sınav Ekle");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setModal(true);

        // Form alanları
        add(new JLabel("Öğrenci ID:"));
        JTextField studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("Sınav Tarihi (YYYY-MM-DD):"));
        JTextField examDateField = new JTextField();
        add(examDateField);

        add(new JLabel("Sınav Süre (HH:MM):"));
        JTextField examTimeField = new JTextField();
        add(examTimeField);

        // Kaydet butonu
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                Exam exam = new Exam();
                exam.setStudentID(Integer.parseInt(studentIDField.getText()));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date examDate = dateFormat.parse(examDateField.getText());
                exam.setExamDate(new java.sql.Date(examDate.getTime()));

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date examTime = timeFormat.parse(examTimeField.getText());
                exam.setExamTime(new java.sql.Time(examTime.getTime()));

                listener.onExamSubmitted(exam);
                dispose();
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doğru girin!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public ExamForm(Exam exam, ExamFormListener listener) {
        this(listener);
        setTitle("Sınavı Düzenle");

        // Mevcut bilgileri form alanlarına doldur
        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(exam.getStudentID()));
        ((JTextField) getContentPane().getComponent(3)).setText(exam.getExamDate().toString());
        ((JTextField) getContentPane().getComponent(5)).setText(exam.getExamTime().toString());
    }
}
