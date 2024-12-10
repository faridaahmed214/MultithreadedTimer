package com.timerapp;

import javax.swing.*;
import java.awt.*;

public class TimerAppGUI extends JFrame {
    private final JPanel timersPanel;
    private final TimerManager manager;

    public TimerAppGUI() {
        setTitle("Gradient Timer App");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        manager = new TimerManager();

        JPanel headerPanel = new GradientPanel(new Color(58, 123, 213), new Color(34, 193, 195));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("⏱️ Timer App");
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel addTimerPanel = new JPanel();
        addTimerPanel.setBackground(new Color(44, 62, 80));
        addTimerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel durationLabel = new JLabel("⏳ Duration (sec): ");
        durationLabel.setForeground(Color.WHITE);
        durationLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));

        JTextField durationField = new JTextField(10);
        durationField.setFont(new Font("Sans Serif", Font.PLAIN, 16));

        JButton addButton = createButton("➕ Add Timer");
        addButton.setBackground(new Color(39, 174, 96));
        addButton.setForeground(Color.WHITE);

        addTimerPanel.add(durationLabel);
        addTimerPanel.add(durationField);
        addTimerPanel.add(addButton);
        add(addTimerPanel, BorderLayout.SOUTH);

        timersPanel = new JPanel();
        timersPanel.setLayout(new GridLayout(0, 2, 15, 15)); 
        timersPanel.setBackground(new Color(52, 73, 94));

        JScrollPane scrollPane = new JScrollPane(timersPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

    addButton.addActionListener(e -> {
    String input = durationField.getText();
    try {
        long duration = Long.parseLong(input) * 1000;
        if (duration <= 0 || duration > 86400000) {
            JOptionPane.showMessageDialog(this, "Duration must be between 1 and 86400 seconds.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        TimerPanel timerPanel = new TimerPanel(duration, manager);
        timersPanel.add(timerPanel);
        timersPanel.revalidate();
        timersPanel.repaint();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
    }
});

    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimerAppGUI app = new TimerAppGUI();
            app.setVisible(true);
        });
    }
}
