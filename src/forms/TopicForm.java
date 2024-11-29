package forms;

import javax.swing.*;
import java.awt.*;
import models.Topic;

public class TopicForm extends JDialog {
    public interface TopicFormListener {
        void onTopicSubmitted(Topic topic);
    }

    public TopicForm(TopicFormListener listener) {
        setTitle("Create New Topic");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setModal(true);

        add(new JLabel("CourseID:"));
        JTextField courseIDField = new JTextField();
        add(courseIDField);

        add(new JLabel("TopicName:"));
        JTextField topicNameField = new JTextField();
        add(topicNameField);

        add(new JLabel("Difficulty:"));
        JTextField difficultyField = new JTextField();
        add(difficultyField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Topic topic = new Topic();
                topic.setCourseID(Integer.parseInt(courseIDField.getText()));
                topic.setTopicName(topicNameField.getText());
                topic.setDifficulty(difficultyField.getText());

                listener.onTopicSubmitted(topic);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public TopicForm(Topic topic, TopicFormListener listener) {
        this(listener);
        setTitle("Update the Topic");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(topic.getCourseID()));
        ((JTextField) getContentPane().getComponent(3)).setText(topic.getTopicName());
        ((JTextField) getContentPane().getComponent(5)).setText(topic.getDifficulty());
    }
}
