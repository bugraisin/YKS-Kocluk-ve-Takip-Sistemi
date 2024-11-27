package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import forms.ExamForm;
import models.Exam;
import controllers.ExamController;
import javax.swing.table.DefaultTableModel;

public class ExamsPanel extends JPanel {
    private JTable examsTable;
    private ExamController examController;
    private JButton editButton;
    private JButton deleteButton;

    public ExamsPanel() {
        examController = new ExamController();
        setLayout(new BorderLayout());

        // Başlık
        JLabel title = new JLabel("Sınavlar", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // JTable için model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Exam ID");
        tableModel.addColumn("Öğrenci ID");
        tableModel.addColumn("Sınav Tarihi");
        tableModel.addColumn("Sınav Süresi");

        examsTable = new JTable(tableModel);
        examsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateExamTable();

        JScrollPane scrollPane = new JScrollPane(examsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Yeni Sınav Ekle");

        addButton.addActionListener(e -> {
            ExamForm examForm = new ExamForm(exam -> {
                examController.addExam(exam);
                updateExamTable();
            });
            examForm.setVisible(true);
        });

        // Düzenle ve Sil butonları
        editButton = new JButton("Düzenle");
        editButton.addActionListener(e -> editSelectedExam());
        editButton.setEnabled(false);

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> deleteSelectedExam());
        deleteButton.setEnabled(false);

        // Seçili satır değiştiğinde düzenle ve sil butonlarının durumunu güncelleme
        examsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = examsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        // Butonları panelde düzenle
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Sınav tablosunu güncelleme
    private void updateExamTable() {
        List<Exam> exams = examController.getAllExams();
        DefaultTableModel tableModel = (DefaultTableModel) examsTable.getModel();
        tableModel.setRowCount(0);

        for (Exam exam : exams) {
            Object[] row = new Object[4];
            row[0] = exam.getExamID();
            row[1] = exam.getStudentID();
            row[2] = exam.getExamDate();
            row[3] = exam.getExamTime();

            tableModel.addRow(row);
        }
    }

    // Seçili sınavı düzenleme işlemi
    private void editSelectedExam() {
        int selectedRow = examsTable.getSelectedRow();
        if (selectedRow != -1) {
            int examID = (int) examsTable.getValueAt(selectedRow, 0);
            Exam exam = examController.getExamById(examID);

            ExamForm examForm = new ExamForm(examToEdit -> {
                exam.setStudentID(examToEdit.getStudentID());
                exam.setExamDate(examToEdit.getExamDate());
                exam.setExamTime(examToEdit.getExamTime());

                examController.updateExam(exam);
                updateExamTable();
            });

            examForm.setVisible(true);
        }
    }

    // Seçili sınavı silme işlemi
    private void deleteSelectedExam() {
        int selectedRow = examsTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu sınavı silmek istediğinizden emin misiniz?",
                    "Sınav Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int examID = (int) examsTable.getValueAt(selectedRow, 0);
                examController.deleteExam(examID);
                updateExamTable();
            }
        }
    }
}