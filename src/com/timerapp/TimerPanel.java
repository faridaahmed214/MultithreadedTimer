package com.timerapp;

import javax.swing.*;
import java.awt.*;

public class TimerPanel extends JPanel {
    private final JLabel timerLabel;
    private final TimerManager timerManager;
    private TimerTask timerTask;
    private final long initialDuration;

    public TimerPanel(long duration, TimerManager timerManager) {
        // Store the initial duration for reset functionality
        this.initialDuration = duration;
        this.timerManager = timerManager;
        
        // Setup panel layout and appearance
        setLayout(new BorderLayout());
        setBackground(new Color(44, 62, 80));
        setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        // Create timer label
        timerLabel = new JLabel(formatTime(duration), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Sans Serif", Font.BOLD, 22));
        timerLabel.setForeground(Color.WHITE);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(44, 62, 80));
        
        // Create buttons
        JButton startButton = createButton("▶ Start");
        JButton pauseButton = createButton("⏸ Pause");
        JButton resumeButton = createButton("⏯ Resume");
        JButton resetButton = createButton("⟳ Reset");
        
        // Add buttons to panel
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(resetButton);
        
        // Start button action
        startButton.addActionListener(e -> {
            // Check if timer is not running using our new method
            if (timerTask == null || !timerTask.isRunning()) {
                // Create new timer task and start it
                timerTask = new TimerTask(initialDuration, this);
                timerManager.startTimer(timerTask);
            }
        });
        
        // Pause button action
        pauseButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.pauseTimer(timerTask);
            }
        });
        
        // Resume button action
        resumeButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.resumeTimer(timerTask);
            }
        });
        
        // Reset button action
        resetButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.resetTimer(timerTask, initialDuration);
            }
        });
        
        // Add components to panel
        add(timerLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Update timer label with remaining time
    public void updateTime(long timeRemaining) {
        timerLabel.setText(formatTime(timeRemaining));
    }
    
    // Show time up message
    public void onTimeUp() {
        JOptionPane.showMessageDialog(this, "Time is up!");
    }
    
    // Getter for timer task
    public TimerTask getTimerTask() {
        return timerTask;
    }
    
    // Format time to HH:MM:SS
    private String formatTime(long time) {
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60));
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    // Create styled button
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }}