package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.ScheduleController;
import models.Schedule;
import forms.ScheduleForm;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SchedulesPanel extends JPanel {
    private JTable schedulesTable;
    private ScheduleController scheduleController;
    private JButton editButton;
    private JButton deleteButton;

    public SchedulesPanel() {
        scheduleController = new ScheduleController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Schedules", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ScheduleID");
        tableModel.addColumn("StudentID");
        tableModel.addColumn("StartDate");
        tableModel.addColumn("EndDate");
        tableModel.addColumn("Description");
        tableModel.addColumn("Title");

        schedulesTable = new JTable(tableModel);
        schedulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateSchedulesTable();

        JScrollPane scrollPane = new JScrollPane(schedulesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Schedule");

        addButton.addActionListener(e -> {
            ScheduleForm scheduleForm = new ScheduleForm(schedule -> {
                scheduleController.addSchedule(schedule);
                updateSchedulesTable();
            });
            scheduleForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedSchedule());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedSchedule());
        deleteButton.setEnabled(false);

        schedulesTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = schedulesTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateSchedulesTable() {
        List<Schedule> schedules = scheduleController.getAllSchedules();
        DefaultTableModel tableModel = (DefaultTableModel) schedulesTable.getModel();
        tableModel.setRowCount(0);

        for (Schedule schedule : schedules) {
            Object[] row = new Object[6];
            row[0] = schedule.getScheduleID();
            row[1] = schedule.getStudentID();
            row[2] = schedule.getStartDate();
            row[3] = schedule.getEndDate();
            row[4] = schedule.getDescription();
            row[5] = schedule.getTitle();

            tableModel.addRow(row);
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        schedulesTable.setRowSorter(sorter);
    }

    private void editSelectedSchedule() {
        int selectedRow = schedulesTable.getSelectedRow();
        if (selectedRow != -1) {
            int scheduleID = (int) schedulesTable.getValueAt(selectedRow, 0);
            Schedule schedule = scheduleController.getScheduleById(scheduleID);

            ScheduleForm scheduleForm = new ScheduleForm(scheduleToEdit -> {
                schedule.setStudentID(scheduleToEdit.getStudentID());
                schedule.setStartDate(scheduleToEdit.getStartDate());
                schedule.setEndDate(scheduleToEdit.getEndDate());
                schedule.setDescription(scheduleToEdit.getDescription());
                schedule.setTitle(scheduleToEdit.getTitle());

                scheduleController.updateSchedule(schedule);
                updateSchedulesTable();
            });

            scheduleForm.setVisible(true);
        }
    }

    private void deleteSelectedSchedule() {
        int selectedRow = schedulesTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu programı silmek istediğinizden emin misiniz?",
                    "Program Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int scheduleID = (int) schedulesTable.getValueAt(selectedRow, 0);
                scheduleController.deleteSchedule(scheduleID);
                updateSchedulesTable();
            }
        }
    }
}
