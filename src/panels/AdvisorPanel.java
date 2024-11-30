package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import forms.AdvisorForm;
import models.Advisor;
import controllers.AdvisorController;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AdvisorPanel extends JPanel {
    private JTable advisorsTable;
    private AdvisorController advisorController;
    private JButton editButton;
    private JButton deleteButton;

    public AdvisorPanel() {
        advisorController = new AdvisorController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Advisors", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);


        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("AdvisorID");
        tableModel.addColumn("StudentID");
        tableModel.addColumn("FirstName");
        tableModel.addColumn("MidName");
        tableModel.addColumn("LastName");
        tableModel.addColumn("Email");
        tableModel.addColumn("PhoneNo");
        tableModel.addColumn("Major");

        advisorsTable = new JTable(tableModel);
        advisorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateAdvisorTable();

        JScrollPane scrollPane = new JScrollPane(advisorsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Advisor");

        addButton.addActionListener(e -> {
            AdvisorForm advisorForm = new AdvisorForm(advisor -> {
                advisorController.addAdvisor(advisor);
                updateAdvisorTable();
            });
            advisorForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedAdvisor());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedAdvisor());
        deleteButton.setEnabled(false);

        advisorsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = advisorsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateAdvisorTable() {
        List<Advisor> advisors = advisorController.getAllAdvisors();
        DefaultTableModel tableModel = (DefaultTableModel) advisorsTable.getModel();
        tableModel.setRowCount(0);

        for (Advisor advisor : advisors) {
            Object[] row = new Object[8];
            row[0] = advisor.getAdvisorID();
            row[1] = advisor.getStudentID();
            row[2] = advisor.getFirstName();
            row[3] = advisor.getMidName();
            row[4] = advisor.getLastName();
            row[5] = advisor.getEmail();
            row[6] = advisor.getPhoneNo();
            row[7] = advisor.getMajor();

            tableModel.addRow(row);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        advisorsTable.setRowSorter(sorter);
    }

    private void editSelectedAdvisor() {
        int selectedRow = advisorsTable.getSelectedRow();
        if (selectedRow != -1) {
            int advisorID = (int) advisorsTable.getValueAt(selectedRow, 0);
            Advisor advisor = advisorController.getAdvisorById(advisorID);

            AdvisorForm advisorForm = new AdvisorForm(advisorToEdit -> {
                advisor.setStudentID(advisorToEdit.getStudentID());
                advisor.setFirstName(advisorToEdit.getFirstName());
                advisor.setMidName(advisorToEdit.getMidName());
                advisor.setLastName(advisorToEdit.getLastName());
                advisor.setEmail(advisorToEdit.getEmail());
                advisor.setPhoneNo(advisorToEdit.getPhoneNo());
                advisor.setMajor(advisorToEdit.getMajor());

                advisorController.updateAdvisor(advisor);
                updateAdvisorTable();
            });

            advisorForm.setVisible(true);
        }
    }

    private void deleteSelectedAdvisor() {
        int selectedRow = advisorsTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu danışmanı silmek istediğinizden emin misiniz?",
                    "Danışman Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int advisorID = (int) advisorsTable.getValueAt(selectedRow, 0);
                advisorController.deleteAdvisor(advisorID);
                updateAdvisorTable();
            }
        }
    }
}
