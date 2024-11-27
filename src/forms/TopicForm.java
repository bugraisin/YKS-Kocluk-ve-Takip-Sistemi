package forms;

import javax.swing.*;
import java.awt.*;
import models.Topic;

public class TopicForm extends JDialog {
    public interface TopicFormListener {
        void onTopicSubmitted(Topic topic);
    }

    public TopicForm(TopicFormListener listener) {
        setTitle("Yeni Konu Ekle");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setModal(true);

        add(new JLabel("Ders ID:"));
        JTextField courseIDField = new JTextField();
        add(courseIDField);

        add(new JLabel("Konu Adı:"));
        JTextField topicNameField = new JTextField();
        add(topicNameField);

        add(new JLabel("Zorluk Seviyesi:"));
        JTextField difficultyField = new JTextField();
        add(difficultyField);

        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                Topic topic = new Topic();
                topic.setCourseID(Integer.parseInt(courseIDField.getText()));
                topic.setTopicName(topicNameField.getText());
                topic.setDifficulty(difficultyField.getText());

                listener.onTopicSubmitted(topic);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doğru girin!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public TopicForm(Topic topic, TopicFormListener listener) {
        this(listener);
        setTitle("Konuyu Düzenle");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(topic.getCourseID()));
        ((JTextField) getContentPane().getComponent(3)).setText(topic.getTopicName());
        ((JTextField) getContentPane().getComponent(5)).setText(topic.getDifficulty());
    }
}
