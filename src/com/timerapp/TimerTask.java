package com.timerapp;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TimerTask implements Runnable {
    // Use AtomicLong and AtomicBoolean for thread-safe state management
    private final AtomicLong duration;
    private final AtomicLong timeRemaining;
    private final AtomicBoolean isPaused;
    private final AtomicBoolean isRunning;
    private final TimerPanel timerPanel;
    
    // Use a volatile Thread to allow safe interruption
    private volatile Thread currentThread;

    public TimerTask(long duration, TimerPanel timerPanel) {
        this.duration = new AtomicLong(duration);
        this.timeRemaining = new AtomicLong(duration);
        this.timerPanel = timerPanel;
        
        // Initialize atomic state flags
        this.isPaused = new AtomicBoolean(false);
        this.isRunning = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        // Ensure we're not already running
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }

        currentThread = Thread.currentThread();

        while (!Thread.currentThread().isInterrupted() && timeRemaining.get() > 0) {
            // Check if paused
            while (isPaused.get()) {
                try {
                    // Use a small sleep to prevent busy waiting
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore interrupted status and break the loop
                Thread.currentThread().interrupt();
                break;
            }

            // Decrement time safely
            long newTimeRemaining = timeRemaining.addAndGet(-1000);

            // Update UI on Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                if (newTimeRemaining > 0) {
                    timerPanel.updateTime(newTimeRemaining);
                } else {
                    timerPanel.onTimeUp();
                    stopTimer();
                }
            });

            // Break if time is up
            if (newTimeRemaining <= 0) {
                break;
            }
        }

        // Reset running state
        isRunning.set(false);
    }

    public void pauseTimer() {
        isPaused.set(true);
    }

    public void resumeTimer() {
        isPaused.set(false);
    }

    public synchronized void resetTimer(long newDuration) {
        // Stop the current timer if running
        stopTimer();

        // Reset duration and time remaining
        duration.set(newDuration);
        timeRemaining.set(newDuration);

        // Start a new timer
//        startTimer();
    }

    public void stopTimer() {
        // Interrupt the current thread if running
        if (currentThread != null) {
            currentThread.interrupt();
        }
        
        // Reset states
        isRunning.set(false);
        isPaused.set(false);
    }

    public void startTimer() {
        // Ensure we're not already running
        if (!isRunning.get()) {
            // Create and start a new thread
            Thread timerThread = new Thread(this);
            timerThread.start();
        }
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}