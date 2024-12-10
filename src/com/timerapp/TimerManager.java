package com.timerapp;

import java.util.ArrayList;
import java.util.List;

public class TimerManager {
    private final List<TimerTask> timers;
    
    public TimerManager() {
        timers = new ArrayList<>();
    }
    
    public void startTimer(TimerTask timer) {
        // Ensure the timer is not already running
        if (!timer.isRunning()) {
            timers.add(timer);  
            timer.startTimer(); 
        }
    }
    
    public void pauseTimer(TimerTask timer) {
        if (timer != null) {
            timer.pauseTimer();
        }
    }
    
    public void resumeTimer(TimerTask timer) {
        if (timer != null) {
            timer.resumeTimer();
        }
    }
    
    public void resetTimer(TimerTask timer, long newDuration) {
        if (timer != null) {
            timer.resetTimer(newDuration); 
        }
    }
    
    public void stopTimer(TimerTask timer) {
        if (timer != null) {
            timer.stopTimer();
        }
    }
}