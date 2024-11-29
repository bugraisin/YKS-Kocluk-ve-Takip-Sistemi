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
        setTitle("CreateNewExam");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setModal(true);

        // Form alanları
        add(new JLabel("StudentID:"));
        JTextField studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("ExamDate(YYYY-MM-DD):"));
        JTextField examDateField = new JTextField();
        add(examDateField);

        add(new JLabel("ExamTime(HH:MM):"));
        JTextField examTimeField = new JTextField();
        add(examTimeField);

        // Kaydet butonu
        JButton saveButton = new JButton("Save");
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
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public ExamForm(Exam exam, ExamFormListener listener) {
        this(listener);
        setTitle("Update the Exam");

        // Mevcut bilgileri form alanlarına doldur
        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(exam.getStudentID()));
        ((JTextField) getContentPane().getComponent(3)).setText(exam.getExamDate().toString());
        ((JTextField) getContentPane().getComponent(5)).setText(exam.getExamTime().toString());
    }
}
