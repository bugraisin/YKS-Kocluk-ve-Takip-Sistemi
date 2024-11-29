package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import forms.StudentForm;
import forms.AdvisorForm;
import models.*;
import controllers.StudentController;
import controllers.ExamController;
import controllers.CourseController;
import controllers.ScheduleController;

import javax.swing.table.DefaultTableModel;

public class StudentsPanel extends JPanel {
    private JTable studentsTable;
    private StudentController studentController;
    private ExamController examController;
    private CourseController courseController;
    private ScheduleController scheduleController;
    private JButton editButton;
    private JButton deleteButton;
    private JButton addAdvisorButton;
    private JButton viewExamsButton;
    private JButton viewCoursesButton;
    private JButton viewAdvisorsButton;
    private JButton viewSchedulesButton;

    public StudentsPanel() {
        studentController = new StudentController();
        examController = new ExamController();
        courseController = new CourseController();
        scheduleController = new ScheduleController();
        setLayout(new BorderLayout());

        // Başlık
        JLabel title = new JLabel("Öğrenciler", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // JTable için model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("SectionBasedResult ID");
        tableModel.addColumn("Ad");
        tableModel.addColumn("Ortanca İsim");
        tableModel.addColumn("Soyad");
        tableModel.addColumn("Email");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Sınıf");
        tableModel.addColumn("Hedef Üniversite");
        tableModel.addColumn("Hedef Bölüm");

        studentsTable = new JTable(tableModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateStudentTable();

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Yeni Öğrenci Ekle");

        addButton.addActionListener(e -> {
            StudentForm studentForm = new StudentForm(student -> {
                studentController.addStudent(student);
                updateStudentTable();
            });
            studentForm.setVisible(true);
        });

        // Düzenle ve Sil butonları
        editButton = new JButton("Düzenle");
        editButton.addActionListener(e -> editSelectedStudent());
        editButton.setEnabled(false);  // Başlangıçta devre dışı

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        deleteButton.setEnabled(false);  // Başlangıçta devre dışı

        // Öğrenciye ait sınavları, dersleri, danışmanı ve programları getirme butonları
        viewExamsButton = new JButton("Sınavları Görüntüle");
        viewExamsButton.addActionListener(e -> viewExamsOfSelectedStudent());
        viewExamsButton.setEnabled(false);

        viewCoursesButton = new JButton("Dersleri Görüntüle");
        viewCoursesButton.addActionListener(e -> viewCoursesOfSelectedStudent());
        viewCoursesButton.setEnabled(false);

        viewAdvisorsButton = new JButton("Danışmanları Görüntüle");
        viewAdvisorsButton.addActionListener(e -> viewAdvisorsOfSelectedStudent());
        viewAdvisorsButton.setEnabled(false);

        viewSchedulesButton = new JButton("Programları Görüntüle");
        viewSchedulesButton.addActionListener(e -> viewSchedulesOfSelectedStudent());
        viewSchedulesButton.setEnabled(false);

        // Seçili satır değiştiğinde butonların durumunu güncelleme
        studentsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = studentsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
            viewExamsButton.setEnabled(isRowSelected);
            viewCoursesButton.setEnabled(isRowSelected);
            viewAdvisorsButton.setEnabled(isRowSelected);
            viewSchedulesButton.setEnabled(isRowSelected);
        });

        // Butonları panelde düzenle
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewExamsButton);
        buttonPanel.add(viewCoursesButton);
        buttonPanel.add(viewAdvisorsButton);
        buttonPanel.add(viewSchedulesButton);
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
            tableModel.addColumn("Danışman ID");
            tableModel.addColumn("Ad");
            tableModel.addColumn("Ortanca İsim");
            tableModel.addColumn("Soyad");
            tableModel.addColumn("Telefon");
            tableModel.addColumn("Email");
            tableModel.addColumn("Bölüm");

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
            dialog.setTitle("Danışmanlar");
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
            tableModel.addColumn("Sınav ID");
            tableModel.addColumn("Sınav Tarihi");
            tableModel.addColumn("Sınav Süresi");

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
            dialog.setTitle("Sınavlar");
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
            tableModel.addColumn("Ders ID");
            tableModel.addColumn("Ders Adı");
            tableModel.addColumn("Türü");

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
            dialog.setTitle("Dersler");
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
            tableModel.addColumn("Program ID");
            tableModel.addColumn("Başlangıç Tarihi");
            tableModel.addColumn("Bitiş Tarihi");
            tableModel.addColumn("Açıklama");

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
            dialog.setTitle("Programlar");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
    }
}
