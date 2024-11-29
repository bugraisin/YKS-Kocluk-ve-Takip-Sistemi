package forms;

import javax.swing.*;
import java.awt.*;
import models.Course;

public class CourseForm extends JDialog {
    public interface CourseFormListener {
        void onCourseSubmitted(Course course);
    }

    public CourseForm(CourseFormListener listener) {
        setTitle("Create New Course");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setModal(true);

        add(new JLabel("StudentID:"));
        JTextField studentIDField = new JTextField();
        add(studentIDField);

        add(new JLabel("CourseName:"));
        JTextField courseNameField = new JTextField();
        add(courseNameField);

        add(new JLabel("Category:"));
        JTextField categoryField = new JTextField();
        add(categoryField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Course course = new Course();
                course.setStudentID(Integer.parseInt(studentIDField.getText()));
                course.setCourseName(courseNameField.getText());
                course.setCategory(categoryField.getText());

                listener.onCourseSubmitted(course);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public CourseForm(Course course, CourseFormListener listener) {
        this(listener);
        setTitle("Update the Course");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(course.getStudentID()));
        ((JTextField) getContentPane().getComponent(3)).setText(course.getCourseName());
        ((JTextField) getContentPane().getComponent(5)).setText(course.getCategory());
    }
}
