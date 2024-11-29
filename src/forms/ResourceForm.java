package forms;

import javax.swing.*;
import java.awt.*;
import models.Resource;

public class ResourceForm extends JDialog {
    public interface ResourceFormListener {
        void onResourceSubmitted(Resource resource);
    }

    public ResourceForm(ResourceFormListener listener) {
        setTitle("Create New Resource");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setModal(true);

        add(new JLabel("TopicID:"));
        JTextField topicIDField = new JTextField();
        add(topicIDField);

        add(new JLabel("Type"));
        JTextField typeField = new JTextField();
        add(typeField);

        add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        add(titleField);

        add(new JLabel("URL:"));
        JTextField urlField = new JTextField();
        add(urlField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Resource resource = new Resource();
                resource.setTopicID(Integer.parseInt(topicIDField.getText()));
                resource.setType(typeField.getText());
                resource.setTitle(titleField.getText());
                resource.setURL(urlField.getText());

                listener.onResourceSubmitted(resource);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public ResourceForm(Resource resource, ResourceFormListener listener) {
        this(listener);
        setTitle("Update the Resource");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(resource.getTopicID()));
        ((JTextField) getContentPane().getComponent(3)).setText(resource.getType());
        ((JTextField) getContentPane().getComponent(5)).setText(resource.getTitle());
        ((JTextField) getContentPane().getComponent(7)).setText(resource.getURL());
    }
}
