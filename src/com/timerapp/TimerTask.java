package com.timerapp;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TimerTask {
    private final AtomicLong duration;
    private final AtomicLong timeRemaining;
    private final AtomicBoolean isPaused;
    private final AtomicBoolean isRunning;
    private final TimerPanel timerPanel;
    
    private Thread timerThread;
    private long lastUpdateTime;

    public TimerTask(long duration, TimerPanel timerPanel) {
        this.duration = new AtomicLong(duration);
        this.timeRemaining = new AtomicLong(duration);
        this.timerPanel = timerPanel;
        
        this.isPaused = new AtomicBoolean(false);
        this.isRunning = new AtomicBoolean(false);
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void startTimer() {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }

        timerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() && timeRemaining.get() > 0) {
                    // Handle pause state
                    while (isPaused.get()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // Calculate precise elapsed time
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - lastUpdateTime;
                    
                    try {
                        Thread.sleep(Math.max(0, 1000 - elapsedTime));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    // Update last update time
                    lastUpdateTime = System.currentTimeMillis();

                    // Decrement time safely
                    long newTimeRemaining = timeRemaining.addAndGet(-1000);

                    // Update UI on Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> {
                        if (newTimeRemaining > 0) {
                            timerPanel.updateTime(newTimeRemaining);
                        } else {
                            // Ensure we stop exactly at 0
                            timerPanel.updateTime(0);
                            timerPanel.onTimeUp();
                            stopTimer();
                        }
                    });

                    // Break if time is up
                    if (newTimeRemaining <= 0) {
                        break;
                    }
                }
            } finally {
                // Ensure running state is reset
                isRunning.set(false);
            }
        });

        timerThread.start();
    }

    public void pauseTimer() {
        isPaused.set(true);
    }

    public void resumeTimer() {
        lastUpdateTime = System.currentTimeMillis();
        isPaused.set(false);
    }

    public void resetTimer(long newDuration) {
        // Stop the current timer
        stopTimer();

        // Reset duration and time remaining
        duration.set(newDuration);
        timeRemaining.set(newDuration);
        lastUpdateTime = System.currentTimeMillis();

        // Update the UI to show the new duration
        SwingUtilities.invokeLater(() -> {
            timerPanel.updateTime(newDuration);
        });
    }

    public void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
        
        isRunning.set(false);
        isPaused.set(false);
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}