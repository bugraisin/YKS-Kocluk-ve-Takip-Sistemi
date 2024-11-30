package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.CourseController;
import models.Course;
import forms.CourseForm;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CoursesPanel extends JPanel {
    private JTable coursesTable;
    private CourseController courseController;
    private JButton editButton;
    private JButton deleteButton;

    public CoursesPanel() {
        courseController = new CourseController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Courses", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("CourseID");
        tableModel.addColumn("StudentID");
        tableModel.addColumn("CourseName");
        tableModel.addColumn("Category");

        coursesTable = new JTable(tableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateCoursesTable();

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Course");

        addButton.addActionListener(e -> {
            CourseForm courseForm = new CourseForm(course -> {
                courseController.addCourse(course);
                updateCoursesTable();
            });
            courseForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedCourse());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedCourse());
        deleteButton.setEnabled(false);

        coursesTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = coursesTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateCoursesTable() {
        List<Course> courses = courseController.getAllCourses();
        DefaultTableModel tableModel = (DefaultTableModel) coursesTable.getModel();
        tableModel.setRowCount(0);

        for (Course course : courses) {
            Object[] row = new Object[4];
            row[0] = course.getCourseID();
            row[1] = course.getStudentID();
            row[2] = course.getCourseName();
            row[3] = course.getCategory();

            tableModel.addRow(row);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        coursesTable.setRowSorter(sorter);
    }

    private void editSelectedCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow != -1) {
            int courseID = (int) coursesTable.getValueAt(selectedRow, 0);
            Course course = courseController.getCourseById(courseID);

            CourseForm courseForm = new CourseForm(courseToEdit -> {
                course.setStudentID(courseToEdit.getStudentID());
                course.setCourseName(courseToEdit.getCourseName());
                course.setCategory(courseToEdit.getCategory());

                courseController.updateCourse(course);
                updateCoursesTable();
            });

            courseForm.setVisible(true);
        }
    }

    private void deleteSelectedCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu dersi silmek istediğinizden emin misiniz?",
                    "Ders Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int courseID = (int) coursesTable.getValueAt(selectedRow, 0);
                courseController.deleteCourse(courseID);
                updateCoursesTable();
            }
        }
    }
}
