package com.timerapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TimerAppGUI extends JFrame {
    public TimerAppGUI() {
        TimerManager manager = new TimerManager();
        
        GradientPanel mainPanel = new GradientPanel(
            new Color(75, 0, 130),    
            new Color(138, 43, 226)   
        );
        mainPanel.setLayout(new GridLayout(0, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Monospaced", Font.BOLD, 16);

        JLabel hoursLabel = new JLabel("Hours:");
        JLabel minutesLabel = new JLabel("Minutes:");
        JLabel secondsLabel = new JLabel("Seconds:");
        
        hoursLabel.setForeground(Color.WHITE);
        minutesLabel.setForeground(Color.WHITE);
        secondsLabel.setForeground(Color.WHITE);
        
        hoursLabel.setFont(labelFont);
        minutesLabel.setFont(labelFont);
        secondsLabel.setFont(labelFont);

        JTextField hoursField = new JTextField("00", 2);
        JTextField minutesField = new JTextField("00", 2);
        JTextField secondsField = new JTextField("00", 2);

        hoursField.setFont(fieldFont);
        minutesField.setFont(fieldFont);
        secondsField.setFont(fieldFont);
        
        hoursField.setHorizontalAlignment(JTextField.CENTER);
        minutesField.setHorizontalAlignment(JTextField.CENTER);
        secondsField.setHorizontalAlignment(JTextField.CENTER);

        addNumericValidation(hoursField, 23);
        addNumericValidation(minutesField, 59);
        addNumericValidation(secondsField, 59);

        JButton addTimerButton = new JButton("Create Timer");
        addTimerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addTimerButton.setBackground(new Color(106, 13, 173));
        addTimerButton.setForeground(Color.black);
        addTimerButton.setFocusPainted(false);
        addTimerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(75, 0, 130), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(hoursLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(hoursField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(minutesLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(minutesField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(secondsLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(secondsField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(addTimerButton, gbc);

        addTimerButton.addActionListener(e -> {
            try {
                int hours = Integer.parseInt(hoursField.getText());
                int minutes = Integer.parseInt(minutesField.getText());
                int seconds = Integer.parseInt(secondsField.getText());
                
                if (hours == 0 && minutes == 0 && seconds == 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a non-zero duration in at least one field", 
                        "Invalid Duration", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                long duration = (hours * 3600L + minutes * 60L + seconds) * 1000;
                
                TimerPanel panel = new TimerPanel(duration, manager);
                mainPanel.add(panel);
                mainPanel.revalidate();
                mainPanel.repaint();
                
                hoursField.setText("00");
                minutesField.setText("00");
                secondsField.setText("00");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter valid numbers", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout(15, 15));
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        
        setTitle("Quantum Timer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        getContentPane().setBackground(new Color(75, 0, 130));
    }

    private void addNumericValidation(JTextField field, int maxValue) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                
                if (!((c >= '0') && (c <= '9') || 
                      (c == KeyEvent.VK_BACK_SPACE) || 
                      (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                    return;
                }
                
                if (field.getText().length() >= 2 && 
                    c != KeyEvent.VK_BACK_SPACE && 
                    c != KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }
                
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (!field.getText().isEmpty()) {
                            int value = Integer.parseInt(field.getText());
                            if (value > maxValue) {
                                field.setText(String.format("%02d", maxValue));
                            }
                        }
                    } catch (NumberFormatException ex) {
                        field.setText("00");
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new TimerAppGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}