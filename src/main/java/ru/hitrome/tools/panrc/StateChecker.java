/*
 * Copyright (C) 2019 
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package ru.hitrome.tools.panrc;

import ru.hitrome.tools.panrc.model.State;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class StateChecker implements Runnable {
    
    private final ApplicationContext applicationContext;
    private State state;
    private Runnable onCheckState;
    private int timeInterval = 1000;
    
    private Thread checkerThread;
    private boolean interrupt = false;
    
    public StateChecker(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public State getState() {
        return state;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setOnCheckState(Runnable onCheckState) {
        this.onCheckState = onCheckState;
    }
    
    public void checkState() {
        if (applicationContext.getCamQueue() != null && !applicationContext.getCamQueue().isInterrupt()) {
            applicationContext.getCamQueue().put(() -> checkStateEx(applicationContext.getCameraIpAddress()));
        } else {
            checkStateEx(applicationContext.getCameraIpAddress());
        }
    }
    
    public void checkStateEx(String camAddr) {
        RemoteControlCommands rcc = new RemoteControlCommands(camAddr);
        state = rcc.getStateEx();
        if (onCheckState != null) {
            onCheckState.run();
        }
    }
    
    public void startCheckingContinously() {
        interrupt = false;
        checkerThread = new Thread(this);
        checkerThread.start();
    }
    
    public void stopCheckingContinously() {
        interrupt = true;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (!interrupt) {
            synchronized (this) {
                checkState();
            }
            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException ex) {
                // Do nothing
            }
        }
    }
    
    
    
}
