package forms;

import javax.swing.*;
import java.awt.*;
import models.Advisor;

public class AdvisorForm extends JDialog {
    public interface AdvisorFormListener {
        void onAdvisorSubmitted(Advisor advisor);
    }

    public AdvisorForm(AdvisorFormListener listener) {
        setTitle("Advisor Form");
        setSize(400, 300);
        setLayout(new GridLayout(8, 2));
        setModal(true);

        // Form alanları

        add(new JLabel("StudentID:"));
        JTextField studentIdField = new JTextField();
        add(studentIdField);

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

        add(new JLabel("Major:"));
        JTextField majorField = new JTextField();
        add(majorField);

        // Kaydet butonu
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            Advisor advisor = new Advisor();
            advisor.setStudentID(Integer.parseInt(studentIdField.getText()));
            advisor.setFirstName(firstNameField.getText());
            advisor.setMidName(midNameField.getText());
            advisor.setLastName(lastNameField.getText());
            advisor.setEmail(emailField.getText());
            advisor.setPhoneNo(phoneNoField.getText());
            advisor.setMajor(majorField.getText());

            listener.onAdvisorSubmitted(advisor);
            dispose();
        });
        add(saveButton);
    }

    public AdvisorForm(Advisor advisor, AdvisorFormListener listener) {
        this(listener);
        setTitle("Update the Advisor");

        // Mevcut bilgileri form alanlarına doldur
        ((JTextField) getContentPane().getComponent(1)).setText(advisor.getFirstName());
        ((JTextField) getContentPane().getComponent(3)).setText(advisor.getMidName());
        ((JTextField) getContentPane().getComponent(5)).setText(advisor.getLastName());
        ((JTextField) getContentPane().getComponent(7)).setText(advisor.getEmail());
        ((JTextField) getContentPane().getComponent(9)).setText(advisor.getPhoneNo());
        ((JTextField) getContentPane().getComponent(11)).setText(advisor.getMajor());
    }
}
