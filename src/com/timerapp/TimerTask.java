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
                lastUpdateTime = System.currentTimeMillis();

                while (!Thread.currentThread().isInterrupted() && timeRemaining.get() > 0) {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - lastUpdateTime;

                    if (isPaused.get()) {
                        lastUpdateTime = currentTime; 
                        Thread.sleep(100); 
                        continue;
                    }

                    if (elapsedTime >= 1000) {
                        timeRemaining.addAndGet(-1000); 
                        lastUpdateTime += 1000; 

                        SwingUtilities.invokeLater(() -> {
                            long remaining = timeRemaining.get();
                            if (remaining > 0) {
                                timerPanel.updateTime(remaining);
                            } else {
                                timerPanel.updateTime(0);
                                timerPanel.onTimeUp();
                                stopTimer();
                            }
                        });
                    }

                    long sleepTime = Math.max(1, 1000 - elapsedTime);
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            } finally {
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
        stopTimer();

        duration.set(newDuration);
        timeRemaining.set(newDuration);
        lastUpdateTime = System.currentTimeMillis();

        SwingUtilities.invokeLater(() -> timerPanel.updateTime(newDuration));
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