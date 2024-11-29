package forms;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import models.Schedule;

public class ScheduleForm extends JDialog {
    public interface ScheduleFormListener {
        void onScheduleSubmitted(Schedule schedule);
    }

    public ScheduleForm(ScheduleFormListener listener) {
        setTitle("Create New Schedule");
        setSize(400, 400);
        setLayout(new GridLayout(6, 2));
        setModal(true);

        add(new JLabel("StudentID:"));
        JTextField studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("StartDate(YYYY-MM-DD):"));
        JTextField startDateField = new JTextField();
        add(startDateField);

        add(new JLabel("EndDate(YYYY-MM-DD):"));
        JTextField endDateField = new JTextField();
        add(endDateField);

        add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        add(titleField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Schedule schedule = new Schedule();
                schedule.setStudentID(Integer.parseInt(studentIDField.getText()));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse(startDateField.getText());
                Date endDate = dateFormat.parse(endDateField.getText());
                schedule.setStartDate(new java.sql.Date(startDate.getTime()));
                schedule.setEndDate(new java.sql.Date(endDate.getTime()));

                schedule.setDescription(descriptionField.getText());
                schedule.setTitle(titleField.getText());

                listener.onScheduleSubmitted(schedule);
                dispose();
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public ScheduleForm(Schedule schedule, ScheduleFormListener listener) {
        this(listener);
        setTitle("Update the Schedule");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(schedule.getStudentID()));
        ((JTextField) getContentPane().getComponent(3)).setText(schedule.getStartDate().toString());
        ((JTextField) getContentPane().getComponent(5)).setText(schedule.getEndDate().toString());
        ((JTextField) getContentPane().getComponent(7)).setText(schedule.getDescription());
        ((JTextField) getContentPane().getComponent(9)).setText(schedule.getTitle());
    }
}
