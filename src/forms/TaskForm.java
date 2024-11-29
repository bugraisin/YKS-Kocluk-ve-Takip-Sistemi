package forms;

import javax.swing.*;
import java.awt.*;
import models.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskForm extends JDialog {
    public interface TaskFormListener {
        void onTaskSubmitted(Task task);
    }

    public TaskForm(TaskFormListener listener) {
        setTitle("Create New Task");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setModal(true);

        add(new JLabel("StudentID:"));
        JTextField studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("AdvisorID:"));
        JTextField advisorIDField = new JTextField();
        add(advisorIDField);

        add(new JLabel("Text:"));
        JTextField taskTextField = new JTextField();
        add(taskTextField);

        add(new JLabel("DueDate(YYYY-MM-DD):"));
        JTextField dueDateField = new JTextField();
        add(dueDateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Task task = new Task();
                task.setStudentID(Integer.parseInt(studentIDField.getText()));
                task.setAdvisorID(Integer.parseInt(advisorIDField.getText()));
                task.setText(taskTextField.getText());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dueDate = dateFormat.parse(dueDateField.getText());
                task.setDueDate(new java.sql.Date(dueDate.getTime()));

                listener.onTaskSubmitted(task);
                dispose();
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public TaskForm(Task task, TaskFormListener listener) {
        this(listener);
        setTitle("Update the Task");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(task.getStudentID()));
        ((JTextField) getContentPane().getComponent(3)).setText(String.valueOf(task.getAdvisorID()));
        ((JTextField) getContentPane().getComponent(5)).setText(task.getText());
        ((JTextField) getContentPane().getComponent(7)).setText(task.getDueDate().toString());
    }
}
