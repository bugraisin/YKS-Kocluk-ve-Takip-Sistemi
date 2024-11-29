package forms;

import javax.swing.*;
import java.awt.*;
import models.SectionBasedResult;

public class SectionBasedResultForm extends JDialog {
    public interface SectionBasedResultFormListener {
        void onSectionBasedResultSubmitted(SectionBasedResult result);
    }

    public SectionBasedResultForm(SectionBasedResultFormListener listener) {
        setTitle("Create New SectionBasedResult");
        setSize(400, 400);
        setLayout(new GridLayout(6, 2));
        setModal(true);

        add(new JLabel("ExamID:"));
        JTextField examIDField = new JTextField();
        add(examIDField);

        add(new JLabel("SectionName:"));
        JTextField sectionNameField = new JTextField();
        add(sectionNameField);

        add(new JLabel("TrueNum:"));
        JTextField trueNumField = new JTextField();
        add(trueNumField);

        add(new JLabel("FalseNum:"));
        JTextField falseNumField = new JTextField();
        add(falseNumField);

        add(new JLabel("Net:"));
        JTextField netField = new JTextField();
        add(netField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                SectionBasedResult result = new SectionBasedResult();
                result.setExamID(Integer.parseInt(examIDField.getText()));
                result.setSectionName(sectionNameField.getText());
                result.setTrueNum(Integer.parseInt(trueNumField.getText()));
                result.setFalseNum(Integer.parseInt(falseNumField.getText()));
                result.setNet(Float.parseFloat(netField.getText()));

                listener.onSectionBasedResultSubmitted(result);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please Fill the Boxes Correctly!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
    }

    public SectionBasedResultForm(SectionBasedResult result, SectionBasedResultFormListener listener) {
        this(listener);
        setTitle("Update the CourseBaseResult");

        ((JTextField) getContentPane().getComponent(1)).setText(String.valueOf(result.getExamID()));
        ((JTextField) getContentPane().getComponent(3)).setText(result.getSectionName());
        ((JTextField) getContentPane().getComponent(5)).setText(String.valueOf(result.getTrueNum()));
        ((JTextField) getContentPane().getComponent(7)).setText(String.valueOf(result.getFalseNum()));
        ((JTextField) getContentPane().getComponent(9)).setText(String.valueOf(result.getNet()));
    }
}
