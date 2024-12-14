package com.timerapp;

import javax.swing.*;
import java.awt.*;

public class TimerPanel extends JPanel {
    private final JLabel timerLabel;
    private final TimerManager timerManager;
    private TimerTask timerTask;
    
    public TimerPanel(long duration, TimerManager timerManager) {
        this.timerManager = timerManager;
        setLayout(new BorderLayout());
        
        setBackground(new Color(102, 51, 153));
        setBorder(BorderFactory.createLineBorder(new Color(153, 102, 204), 2));
        
        timerLabel = new JLabel(formatTime(duration), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Sans Serif", Font.BOLD, 36));
        timerLabel.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(102, 51, 153));
        
        JButton startButton = createButton("▶ Start", new Color(0, 200, 0));
        JButton pauseButton = createButton("⏸ Pause", new Color(255, 165, 0));
        JButton resumeButton = createButton("⏯ Resume", new Color(30, 144, 255));
        JButton resetButton = createButton("⟳ Reset", new Color(220, 20, 60));
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(resetButton);
        
        startButton.addActionListener(e -> {
            if (timerTask == null || !timerTask.isRunning()) {
                timerTask = new TimerTask(duration, this);
                timerManager.startTimer(timerTask);
            }
        });
        
        pauseButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.pauseTimer(timerTask);
            }
        });
        
        resumeButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.resumeTimer(timerTask);
            }
        });
        
        resetButton.addActionListener(e -> {
            if (timerTask != null) {
                timerManager.resetTimer(timerTask, duration);
            }
        });
        
        add(timerLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void updateTime(long timeRemaining) {
        timerLabel.setText(formatTime(timeRemaining));
    }
    
    public void onTimeUp() {
        JOptionPane optionPane = new JOptionPane(
            "Time is up!", 
            JOptionPane.INFORMATION_MESSAGE, 
            JOptionPane.DEFAULT_OPTION
        );
        JDialog dialog = optionPane.createDialog("Timer Finished");
        dialog.getContentPane().setBackground(new Color(102, 51, 153));
        dialog.setVisible(true);
    }
    
    public TimerTask getTimerTask() {
        return timerTask;
    }
    
    private String formatTime(long time) {
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60));
        
        return time > 0 
            ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
            : "00:00:00";
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}