package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import forms.StudentForm;
import models.Student;
import controllers.StudentController;
import javax.swing.table.DefaultTableModel;

public class StudentsPanel extends JPanel {
    private JTable studentsTable;
    private StudentController studentController;
    private JButton editButton;
    private JButton deleteButton;

    public StudentsPanel() {
        studentController = new StudentController();
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

        // Seçili satır değiştiğinde düzenle ve sil butonlarının durumunu güncelleme
        studentsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = studentsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        // Butonları panelde düzenle
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
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
            // Seçili satırdan öğrenci bilgilerini al
            int studentID = (int) studentsTable.getValueAt(selectedRow, 0);
            Student student = studentController.getStudentById(studentID);

            // Öğrenci bilgilerini düzenleme formu
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

            // Formu doldurmak için mevcut öğrenci bilgilerini göster
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
}
