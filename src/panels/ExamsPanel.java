package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.SectionBasedResultController;
import forms.ExamForm;
import models.Course;
import models.Exam;
import controllers.ExamController;
import models.SectionBasedResult;

import javax.swing.table.DefaultTableModel;

public class ExamsPanel extends JPanel {
    private JTable examsTable;
    private ExamController examController;
    private SectionBasedResultController sectionBasedResultController; // Kontrolcü değişkeni
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewSectionBasedButton;

    public ExamsPanel() {
        examController = new ExamController();
        sectionBasedResultController = new SectionBasedResultController(); // NESNE OLUŞTURULDU
        setLayout(new BorderLayout());

        // Başlık
        JLabel title = new JLabel("Exams", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // JTable için model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ExamID");
        tableModel.addColumn("StudentID");
        tableModel.addColumn("ExamDate");
        tableModel.addColumn("ExamTime");

        examsTable = new JTable(tableModel);
        examsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateExamTable();

        JScrollPane scrollPane = new JScrollPane(examsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Exam");

        addButton.addActionListener(e -> {
            ExamForm examForm = new ExamForm(exam -> {
                examController.addExam(exam);
                updateExamTable();
            });
            examForm.setVisible(true);
        });

        // Düzenle ve Sil butonları
        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedExam());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedExam());
        deleteButton.setEnabled(false);

        // Bölüm Bazlı Sonuçları Görüntüleme Butonu
        viewSectionBasedButton = new JButton("See SectionBasedResults");
        viewSectionBasedButton.addActionListener(e -> viewSectionBasedResultSelectedExam());
        viewSectionBasedButton.setEnabled(false);

        // Seçili satır değiştiğinde butonların durumunu güncelleme
        examsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = examsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
            viewSectionBasedButton.setEnabled(isRowSelected);
        });

        // Butonları panelde düzenle
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewSectionBasedButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Bölüm Bazlı Sonuçları Görüntüleme
    private void viewSectionBasedResultSelectedExam() {
        int selectedRow = examsTable.getSelectedRow();
        if (selectedRow != -1) {
            int examID = (int) examsTable.getValueAt(selectedRow, 0);
            List<SectionBasedResult> sectionBasedResults = sectionBasedResultController.getSectionBasedResultsByExamId(examID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Bölüm Adı");
            tableModel.addColumn("Doğru Sayısı");
            tableModel.addColumn("Yanlış Sayısı");
            tableModel.addColumn("Net");

            for (SectionBasedResult sectionBasedResult : sectionBasedResults) {
                Object[] row = {
                        sectionBasedResult.getSectionName(),
                        sectionBasedResult.getTrueNum(),
                        sectionBasedResult.getFalseNum(),
                        sectionBasedResult.getNet()
                };
                tableModel.addRow(row);
            }

            JTable resultsTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(resultsTable);
            JDialog dialog = new JDialog();
            dialog.setTitle("Bölüm Bazlı Sonuçlar");
            dialog.setSize(600, 400);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        }
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
