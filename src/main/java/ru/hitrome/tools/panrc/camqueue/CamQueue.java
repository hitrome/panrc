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
package ru.hitrome.tools.panrc.camqueue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class CamQueue implements Runnable {
    
    private final LinkedBlockingQueue<QueueElement> queue;
    private boolean interrupt = true;
    private int delay = 10;
    private Thread thread;
    
    public CamQueue() {
        queue = new LinkedBlockingQueue<>();
    }
    
    public void put(QueueElement element) {
        queue.add(element);
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    public void stop() {
        this.interrupt = true;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public void start() {
        if (thread != null) {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
        thread = new Thread(this);
        interrupt = false;
        thread.start();
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (!interrupt || !Thread.interrupted()) {
            QueueElement task = null;
            try {
                task = queue.take();
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
            if (task != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.WARNING, "Camera control queue delay interrupted", ex);
            }
        }
        queue.clear();
    }
    
    private static final Logger LOGGER = Logger.getLogger(CamQueue.class.getName());
    
}
