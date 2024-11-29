package panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import forms.ResourceForm;
import models.Resource;
import controllers.ResourceController;
import javax.swing.table.DefaultTableModel;

public class ResourcesPanel extends JPanel {
    private JTable resourcesTable;
    private ResourceController resourceController;
    private JButton editButton;
    private JButton deleteButton;

    public ResourcesPanel() {
        resourceController = new ResourceController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Resources", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ResourceID");
        tableModel.addColumn("TopicID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Title");
        tableModel.addColumn("URL");

        resourcesTable = new JTable(tableModel);
        resourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateResourcesTable();

        JScrollPane scrollPane = new JScrollPane(resourcesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Create New Resource");

        addButton.addActionListener(e -> {
            ResourceForm resourceForm = new ResourceForm(resource -> {
                resourceController.addResource(resource);
                updateResourcesTable();
            });
            resourceForm.setVisible(true);
        });

        editButton = new JButton("Update");
        editButton.addActionListener(e -> editSelectedResource());
        editButton.setEnabled(false);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedResource());
        deleteButton.setEnabled(false);

        resourcesTable.getSelectionModel().addListSelectionListener(event -> {
            boolean isRowSelected = resourcesTable.getSelectedRow() != -1;
            editButton.setEnabled(isRowSelected);
            deleteButton.setEnabled(isRowSelected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateResourcesTable() {
        List<Resource> resources = resourceController.getAllResources();
        DefaultTableModel tableModel = (DefaultTableModel) resourcesTable.getModel();
        tableModel.setRowCount(0);

        for (Resource resource : resources) {
            Object[] row = new Object[5];
            row[0] = resource.getResourceID();
            row[1] = resource.getTopicID();
            row[2] = resource.getType();
            row[3] = resource.getTitle();
            row[4] = resource.getURL();

            tableModel.addRow(row);
        }
    }

    private void editSelectedResource() {
        int selectedRow = resourcesTable.getSelectedRow();
        if (selectedRow != -1) {
            int resourceID = (int) resourcesTable.getValueAt(selectedRow, 0);
            Resource resource = resourceController.getResourceById(resourceID);

            ResourceForm resourceForm = new ResourceForm(resourceToEdit -> {
                resource.setTopicID(resourceToEdit.getTopicID());
                resource.setType(resourceToEdit.getType());
                resource.setTitle(resourceToEdit.getTitle());
                resource.setURL(resourceToEdit.getURL());

                resourceController.updateResource(resource);
                updateResourcesTable();
            });

            resourceForm.setVisible(true);
        }
    }

    private void deleteSelectedResource() {
        int selectedRow = resourcesTable.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(this, "Bu kaynağı silmek istediğinizden emin misiniz?", "Kaynak Sil", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int resourceID = (int) resourcesTable.getValueAt(selectedRow, 0);
                resourceController.deleteResource(resourceID);
                updateResourcesTable();
            }
        }
    }
}
