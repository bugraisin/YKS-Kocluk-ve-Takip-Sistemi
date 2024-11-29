package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import controllers.TopicController;
import models.Topic;
import forms.TopicForm;
import javax.swing.table.DefaultTableModel;

public class TopicsPanel extends JPanel {
    private JTable topicsTable;
    private TopicController topicController;
    private JButton editButton;
    private JButton deleteButton;

    public TopicsPanel() {
        topicController = new TopicController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Topics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("TopicID");
        tableModel.addColumn("CourseID");
        tableModel.addColumn("TopicName");
        tableModel.addColumn("Difficulty");

        topicsTable = new JTable(tableModel);
        topicsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTopicsTable();

        JScrollPane scrollPane = new JScrollPane(topicsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Topic");

        addButton.addActionListener(e -> {
            TopicForm topicForm = new TopicForm(topic -> {
                topicController.addTopic(topic);
                updateTopicsTable();
            });
            topicForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedTopic());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedTopic());
        deleteButton.setEnabled(false);

        topicsTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = topicsTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTopicsTable() {
        List<Topic> topics = topicController.getAllTopics();
        DefaultTableModel tableModel = (DefaultTableModel) topicsTable.getModel();
        tableModel.setRowCount(0);

        for (Topic topic : topics) {
            Object[] row = new Object[4];
            row[0] = topic.getTopicID();
            row[1] = topic.getCourseID();
            row[2] = topic.getTopicName();
            row[3] = topic.getDifficulty();

            tableModel.addRow(row);
        }
    }

    private void editSelectedTopic() {
        int selectedRow = topicsTable.getSelectedRow();
        if (selectedRow != -1) {
            int topicID = (int) topicsTable.getValueAt(selectedRow, 0);
            Topic topic = topicController.getTopicById(topicID);

            TopicForm topicForm = new TopicForm(topicToEdit -> {
                topic.setCourseID(topicToEdit.getCourseID());
                topic.setTopicName(topicToEdit.getTopicName());
                topic.setDifficulty(topicToEdit.getDifficulty());

                topicController.updateTopic(topic);
                updateTopicsTable();
            });

            topicForm.setVisible(true);
        }
    }

    private void deleteSelectedTopic() {
        int selectedRow = topicsTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu konuyu silmek istediğinizden emin misiniz?",
                    "Delete the Topic", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int topicID = (int) topicsTable.getValueAt(selectedRow, 0);
                topicController.deleteTopic(topicID);
                updateTopicsTable();
            }
        }
    }
}
