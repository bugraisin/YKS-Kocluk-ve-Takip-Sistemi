package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.TaskController;
import models.Task;
import forms.TaskForm;
import javax.swing.table.DefaultTableModel;

public class TasksPanel extends JPanel {
    private JTable tasksTable;
    private TaskController taskController;
    private JButton editButton;
    private JButton deleteButton;

    public TasksPanel() {
        taskController = new TaskController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Tasks", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("TaskID");
        tableModel.addColumn("StudentID");
        tableModel.addColumn("AdvisorID");
        tableModel.addColumn("Text");
        tableModel.addColumn("DueDate");

        tasksTable = new JTable(tableModel);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTasksTable();

        JScrollPane scrollPane = new JScrollPane(tasksTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Task");

        addButton.addActionListener(e -> {
            TaskForm taskForm = new TaskForm(task -> {
                taskController.addTask(task);
                updateTasksTable();
            });
            taskForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedTask());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedTask());
        deleteButton.setEnabled(false);

        tasksTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = tasksTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTasksTable() {
        List<Task> tasks = taskController.getAllTasks();
        DefaultTableModel tableModel = (DefaultTableModel) tasksTable.getModel();
        tableModel.setRowCount(0);

        for (Task task : tasks) {
            Object[] row = new Object[5];
            row[0] = task.getTaskID();
            row[1] = task.getStudentID();
            row[2] = task.getAdvisorID();
            row[3] = task.getText();
            row[4] = task.getDueDate();

            tableModel.addRow(row);
        }
    }

    private void editSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow != -1) {
            int taskID = (int) tasksTable.getValueAt(selectedRow, 0);
            Task task = taskController.getTaskById(taskID);

            TaskForm taskForm = new TaskForm(taskToEdit -> {
                task.setStudentID(taskToEdit.getStudentID());
                task.setAdvisorID(taskToEdit.getAdvisorID());
                task.setText(taskToEdit.getText());
                task.setDueDate(taskToEdit.getDueDate());

                taskController.updateTask(task);
                updateTasksTable();
            });

            taskForm.setVisible(true);
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu görevi silmek istediğinizden emin misiniz?",
                    "Delete the Task", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int taskID = (int) tasksTable.getValueAt(selectedRow, 0);
                taskController.deleteTask(taskID);
                updateTasksTable();
            }
        }
    }
}
