package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.SectionBasedResultController;
import forms.SectionBasedResultForm;
import models.SectionBasedResult;
import javax.swing.table.DefaultTableModel;

public class SectionBasedResultPanel extends JPanel {
    private JTable resultsTable;
    private SectionBasedResultController sectionBasedResultController;
    private JButton editButton;
    private JButton deleteButton;

    public SectionBasedResultPanel() {
        sectionBasedResultController = new SectionBasedResultController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Bölüm Bazlı Sonuçlar", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Sınav ID");
        tableModel.addColumn("Bölüm Adı");
        tableModel.addColumn("Doğru Sayısı");
        tableModel.addColumn("Yanlış Sayısı");
        tableModel.addColumn("Net");

        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateResultsTable();

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Yeni Sonuç Ekle");

        addButton.addActionListener(e -> {
            SectionBasedResultForm resultForm = new SectionBasedResultForm(result -> {
                sectionBasedResultController.addSectionBasedResult(result);
                updateResultsTable();
            });
            resultForm.setVisible(true);
        });

        editButton = new JButton("Düzenle");
        editButton.addActionListener(e -> editSelectedResult());
        editButton.setEnabled(false);

        deleteButton = new JButton("Sil");
        deleteButton.addActionListener(e -> deleteSelectedResult());
        deleteButton.setEnabled(false);

        resultsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = resultsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateResultsTable() {
        List<SectionBasedResult> results = sectionBasedResultController.getAllSectionBasedResults();
        DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
        tableModel.setRowCount(0);

        for (SectionBasedResult result : results) {
            Object[] row = new Object[5];
            row[0] = result.getExamID();
            row[1] = result.getSectionName();
            row[2] = result.getTrueNum();
            row[3] = result.getFalseNum();
            row[4] = result.getNet();

            tableModel.addRow(row);
        }
    }

    private void editSelectedResult() {
//        int selectedRow = resultsTable.getSelectedRow();
//        if (selectedRow != -1) {
//            int examID = (int) resultsTable.getValueAt(selectedRow, 0);
//            String sectionName = (String) resultsTable.getValueAt(selectedRow, 1);
//            SectionBasedResult result = sectionBasedResultController.getSectionBasedResultById(examID, sectionName);
//
//            SectionBasedResultForm resultForm = new SectionBasedResultForm(resultToEdit -> {
//                result.setTrueNum(resultToEdit.getTrueNum());
//                result.setFalseNum(resultToEdit.getFalseNum());
//                result.setNet(resultToEdit.getNet());
//
//                sectionBasedResultController.updateSectionBasedResult(result);
//                updateResultsTable();
//            });
//
//            resultForm.setVisible(true);
//        }
    }

    private void deleteSelectedResult() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu sonucu silmek istediğinizden emin misiniz?",
                    "Sonuç Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int examID = (int) resultsTable.getValueAt(selectedRow, 0);
                String sectionName = (String) resultsTable.getValueAt(selectedRow, 1);
                sectionBasedResultController.deleteSectionBasedResult(examID, sectionName);
                updateResultsTable();
            }
        }
    }
}
