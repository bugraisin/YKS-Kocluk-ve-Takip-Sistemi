package views;

import panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("YKS Koçluk Sistemi");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem studentsMenuItem = new JMenuItem("Students");
        JMenuItem tasksMenuItem = new JMenuItem("Tasks");
        JMenuItem examsMenuItem = new JMenuItem("Exams");
        JMenuItem sectionBasedResultMenuItem = new JMenuItem("SectionBasedResults");
        JMenuItem advisorsMenuItem = new JMenuItem("Advisors");
        JMenuItem coursesMenuItem = new JMenuItem("Courses");
        JMenuItem topicsMenuItem = new JMenuItem("Topics");
        JMenuItem schedulesMenuItem = new JMenuItem("Schedules");
        JMenuItem resourcesMenuItem = new JMenuItem("Resources");

        studentsMenuItem.addActionListener(e -> showStudentsPanel());
        tasksMenuItem.addActionListener(e -> showTasksPanel());
        examsMenuItem.addActionListener(e -> showExamsPanel());
        sectionBasedResultMenuItem.addActionListener(e -> showSectionBasedResultPanel());
        advisorsMenuItem.addActionListener(e -> showAdvisorsPanel());
        coursesMenuItem.addActionListener(e -> showCoursesPanel());
        topicsMenuItem.addActionListener(e -> showTopicsPanel());
        schedulesMenuItem.addActionListener(e -> showSchedulesPanel());
        resourcesMenuItem.addActionListener(e -> showResourcesPanel());

        menu.add(studentsMenuItem);
        menu.add(tasksMenuItem);
        menu.add(examsMenuItem);
        menu.add(sectionBasedResultMenuItem);
        menu.add(advisorsMenuItem);
        menu.add(coursesMenuItem);
        menu.add(topicsMenuItem);
        menu.add(schedulesMenuItem);
        menu.add(resourcesMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        showStudentsPanel();
    }

    private void showStudentsPanel() {
        setContentPane(new StudentsPanel());
        validate();
    }

    private void showTasksPanel() {
        setContentPane(new TasksPanel());
        validate();
    }

    private void showExamsPanel() {
        setContentPane(new ExamsPanel());
        validate();
    }

    private void showAdvisorsPanel() {
        setContentPane(new AdvisorPanel());
        validate();
    }

    private void showCoursesPanel() {
        setContentPane(new CoursesPanel());
        validate();
    }

    private void showTopicsPanel() {
        setContentPane(new TopicsPanel());
        validate();
    }

    private void showSchedulesPanel() {
        setContentPane(new SchedulesPanel());
        validate();
    }

    private void showResourcesPanel() {
        setContentPane(new ResourcesPanel());
        validate();
    }

    private void showSectionBasedResultPanel() {
        setContentPane(new SectionBasedResultPanel());
        validate();
    }
}
