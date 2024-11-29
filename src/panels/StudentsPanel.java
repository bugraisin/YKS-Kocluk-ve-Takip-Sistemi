package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.*;
import forms.StudentForm;
import models.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class StudentsPanel extends JPanel {
    private final JTable studentsTable;
    private final StudentController studentController;
    private final ExamController examController;
    private final CourseController courseController;
    private final ScheduleController scheduleController;
    private final TaskController taskController;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton viewExamsButton;
    private final JButton viewCoursesButton;
    private final JButton viewAdvisorsButton;
    private final JButton viewSchedulesButton;
    private final JButton viewTasksButton;
    private final JTextField searchFirstNameField = new JTextField(10);
    private final JTextField searchMidNameField = new JTextField(10);
    private final JTextField searchLastNameField = new JTextField(10);
    private final JTextField searchEmailField = new JTextField(10);
    private final JTextField searchPhoneField = new JTextField(10);
    private final JTextField searchClassNoField = new JTextField(10);
    private final JTextField searchGoalUniField = new JTextField(10);
    private final JTextField searchGoalMajorField = new JTextField(10);
    private final JButton searchButton = new JButton("Search");

    public StudentsPanel() {
        studentController = new StudentController();
        examController = new ExamController();
        courseController = new CourseController();
        scheduleController = new ScheduleController();
        taskController = new TaskController();
        setLayout(new BorderLayout());

        // Başlık
        JLabel title = new JLabel("Students", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // JTable için model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("StudentID");
        tableModel.addColumn("FirstName");
        tableModel.addColumn("MidName");
        tableModel.addColumn("LastName");
        tableModel.addColumn("Email");
        tableModel.addColumn("PhoneNo");
        tableModel.addColumn("ClassNo");
        tableModel.addColumn("GoalUni");
        tableModel.addColumn("GoalMajor");

        studentsTable = new JTable(tableModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateStudentTable();

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Student");

        addButton.addActionListener(e -> {
            StudentForm studentForm = new StudentForm(student -> {
                studentController.addStudent(student);
                updateStudentTable();
            });
            studentForm.setVisible(true);
        });

        // Düzenle ve Sil butonları
        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedStudent());
        editButton.setEnabled(false);  // Başlangıçta devre dışı

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        deleteButton.setEnabled(false);  // Başlangıçta devre dışı

        // Öğrenciye ait sınavları, dersleri, danışmanı ve programları getirme butonları
        viewExamsButton = new JButton("Show Exams");
        viewExamsButton.addActionListener(e -> viewExamsOfSelectedStudent());
        viewExamsButton.setEnabled(false);

        viewCoursesButton = new JButton("Show Courses");
        viewCoursesButton.addActionListener(e -> viewCoursesOfSelectedStudent());
        viewCoursesButton.setEnabled(false);

        viewAdvisorsButton = new JButton("Show Advisors");
        viewAdvisorsButton.addActionListener(e -> viewAdvisorsOfSelectedStudent());
        viewAdvisorsButton.setEnabled(false);

        viewSchedulesButton = new JButton("Show Schedules");
        viewSchedulesButton.addActionListener(e -> viewSchedulesOfSelectedStudent());
        viewSchedulesButton.setEnabled(false);

        viewTasksButton = new JButton("Show Tasks");
        viewTasksButton.addActionListener(e -> viewTasksOfSelectedStudent());
        viewTasksButton.setEnabled(false);

        // Seçili satır değiştiğinde butonların durumunu güncelleme
        studentsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = studentsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
            viewExamsButton.setEnabled(isRowSelected);
            viewCoursesButton.setEnabled(isRowSelected);
            viewAdvisorsButton.setEnabled(isRowSelected);
            viewSchedulesButton.setEnabled(isRowSelected);
            viewTasksButton.setEnabled(isRowSelected);
        });

        // Arama Paneli
        JPanel searchPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Student"));

        searchPanel.add(new JLabel("FirstName:"));
        searchPanel.add(searchFirstNameField);
        searchPanel.add(new JLabel("MidName:"));
        searchPanel.add(searchMidNameField);
        searchPanel.add(new JLabel("LastName:"));
        searchPanel.add(searchLastNameField);

        searchPanel.add(new JLabel("Email:"));
        searchPanel.add(searchEmailField);
        searchPanel.add(new JLabel("PhoneNo:"));
        searchPanel.add(searchPhoneField);
        searchPanel.add(new JLabel("ClassNo:"));
        searchPanel.add(searchClassNoField);

        searchPanel.add(new JLabel("GoalUni:"));
        searchPanel.add(searchGoalUniField);
        searchPanel.add(new JLabel("GoalMajor:"));
        searchPanel.add(searchGoalMajorField);

// Arama butonuna tıklama işlemi
        searchButton.addActionListener(e -> searchStudents());
        add(searchPanel, BorderLayout.NORTH);


        // Butonları panelde düzenle
        buttonPanel.add(searchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewExamsButton);
        buttonPanel.add(viewCoursesButton);
        buttonPanel.add(viewAdvisorsButton);
        buttonPanel.add(viewSchedulesButton);
        buttonPanel.add(viewTasksButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Öğrenci tablosunu güncelleme
    private void updateStudentTable() {
        List<Student> students = studentController.getAllStudents();
        DefaultTableModel tableModel = (DefaultTableModel) studentsTable.getModel();
        tableModel.setRowCount(0);  // Tablodaki mevcut satırları temizle

        for (Student student : students) {
            Object[] row = new Object[9];
            row[0] = student.getStudentID();
            row[1] = student.getFirstName();
            row[2] = student.getMidName();
            row[3] = student.getLastName();
            row[4] = student.getEmail();
            row[5] = student.getPhoneNo();
            row[6] = student.getClassNo();
            row[7] = student.getGoalUni();
            row[8] = student.getGoalMajor();

            tableModel.addRow(row);  // Satırı ekle
        }

        // Sıralama desteği ekle
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentsTable.setRowSorter(sorter);
    }


    // Seçili öğrenciyi düzenleme işlemi
    private void editSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            Student student = studentController.getStudentById(studentID);

            StudentForm studentForm = new StudentForm(studentToEdit -> {
                student.setFirstName(studentToEdit.getFirstName());
                student.setMidName(studentToEdit.getMidName());
                student.setLastName(studentToEdit.getLastName());
                student.setEmail(studentToEdit.getEmail());
                student.setPhoneNo(studentToEdit.getPhoneNo());
                student.setClassNo(studentToEdit.getClassNo());
                student.setGoalUni(studentToEdit.getGoalUni());
                student.setGoalMajor(studentToEdit.getGoalMajor());

                studentController.updateStudent(student);
                updateStudentTable();
            });

            studentForm.setVisible(true);
        }
    }

    // Öğrenci arama işlemi
    private void searchStudents() {
        String firstName = searchFirstNameField.getText().trim();
        String midName = searchMidNameField.getText().trim();
        String lastName = searchLastNameField.getText().trim();
        String email = searchEmailField.getText().trim();
        String phone = searchPhoneField.getText().trim();
        String classNo = searchClassNoField.getText().trim();
        String goalUni = searchGoalUniField.getText().trim();
        String goalMajor = searchGoalMajorField.getText().trim();

        // Filtrelenmiş öğrencileri getir
        List<Student> filteredStudents = studentController.searchStudents(
                firstName, midName, lastName, email, phone, classNo, goalUni, goalMajor
        );

        // Tabloyu güncelle
        DefaultTableModel tableModel = (DefaultTableModel) studentsTable.getModel();
        tableModel.setRowCount(0); // Tablodaki mevcut satırları temizle

        for (Student student : filteredStudents) {
            Object[] row = new Object[9];
            row[0] = student.getStudentID();
            row[1] = student.getFirstName();
            row[2] = student.getMidName();
            row[3] = student.getLastName();
            row[4] = student.getEmail();
            row[5] = student.getPhoneNo();
            row[6] = student.getClassNo();
            row[7] = student.getGoalUni();
            row[8] = student.getGoalMajor();
            tableModel.addRow(row);
        }
    }



    // Seçili öğrenciyi silme işlemi
    private void deleteSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu öğrenciyi silmek istediğinizden emin misiniz?",
                    "Öğrenci Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
                studentController.deleteStudent(studentID);
                updateStudentTable();
            }
        }
    }

    // Seçili öğrenciye ait danışmanları görüntüleme işlemi
    private void viewAdvisorsOfSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            List<Advisor> advisors = studentController.getAdvisorsByStudentId(studentID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("AdvisorID");
            tableModel.addColumn("FirstName");
            tableModel.addColumn("MidName");
            tableModel.addColumn("LastName");
            tableModel.addColumn("PhoneNo");
            tableModel.addColumn("Email");
            tableModel.addColumn("Major");

            for (Advisor advisor : advisors) {
                Object[] row = {
                        advisor.getAdvisorID(),
                        advisor.getFirstName(),
                        advisor.getMidName(),
                        advisor.getLastName(),
                        advisor.getPhoneNo(),
                        advisor.getEmail(),
                        advisor.getMajor()
                };
                tableModel.addRow(row);
            }

            JTable advisorsTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(advisorsTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Advisors");
            dialog.setSize(800, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }


    // Seçili öğrenciye ait sınavları görüntüleme işlemi
    private void viewExamsOfSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            List<Exam> exams = examController.getExamsByStudentId(studentID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ExamID");
            tableModel.addColumn("ExamDate");
            tableModel.addColumn("ExamTime");

            for (Exam exam : exams) {
                Object[] row = {
                        exam.getExamID(),
                        exam.getExamDate(),
                        exam.getExamTime()
                };
                tableModel.addRow(row);
            }

            JTable examsTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(examsTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Exams");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }

    // Seçili öğrenciye ait dersleri görüntüleme işlemi
    private void viewCoursesOfSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            List<Course> courses = courseController.getCoursesByStudentId(studentID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("CourseID");
            tableModel.addColumn("CourseName");
            tableModel.addColumn("Category");

            for (Course course : courses) {
                Object[] row = {
                        course.getCourseID(),
                        course.getCourseName(),
                        course.getCategory()
                };
                tableModel.addRow(row);
            }

            JTable coursesTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(coursesTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Courses");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }

    // Seçili öğrenciye ait programları görüntüleme işlemi
    private void viewSchedulesOfSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            List<Schedule> schedules = scheduleController.getSchedulesByStudentId(studentID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ScheduleID");
            tableModel.addColumn("StartDate");
            tableModel.addColumn("EndDate");
            tableModel.addColumn("Description");

            for (Schedule schedule : schedules) {
                Object[] row = {
                        schedule.getScheduleID(),
                        schedule.getStartDate(),
                        schedule.getEndDate(),
                        schedule.getDescription()
                };
                tableModel.addRow(row);
            }

            JTable schedulesTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(schedulesTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Schedules");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }

    private void viewTasksOfSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            List<Task> tasks = taskController.getTasksByStudentId(studentID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("TaskID");
            tableModel.addColumn("StudentID");
            tableModel.addColumn("AdvisorID");
            tableModel.addColumn("Text");
            tableModel.addColumn("DueDate");

            for (Task task : tasks) {
                Object[] row = {
                        task.getTaskID(),
                        task.getStudentID(),
                        task.getAdvisorID(),
                        task.getText(),
                        task.getDueDate()
                };
                tableModel.addRow(row);
            }

            JTable schedulesTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(schedulesTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Tasks");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }
}
