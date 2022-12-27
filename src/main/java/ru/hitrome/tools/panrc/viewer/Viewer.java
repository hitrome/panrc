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
package ru.hitrome.tools.panrc.viewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.RemoteControlCommands;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class Viewer implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(Viewer.class.getName());
    
    private final ApplicationContext applicationContext;
    private ViewerSurface surface;
    private ImageReceiver receiver;
    private Thread viewerThread;
    private volatile boolean interrupt = false;
    private Runnable viewerStartCallback;
    
    public Viewer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        
        if (applicationContext.getViewerBackground() == null) {
            BufferedImage background = null;
            
            try {
                background = ImageIO.read(
                        getClass().getResourceAsStream(ru.hitrome.tools.panrc.Constants.VIEWER_BACKGROUND));
                
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex); 
            }
            
            if (background != null) {
                surface = new ViewerSurface(background);
            }  
            
        } else {
            surface = new ViewerSurface(applicationContext.getViewerBackground());
        }
        
        if (surface != null) {
            receiver = new ImageReceiver(applicationContext.getViewerUdpPort(),
                    (new RemoteControlCommands(applicationContext.getCameraIpAddress()))::getState);
        }
        
    }
    
    public boolean startViewer() {
        boolean result = false;
        
        if (surface != null) {
            RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
            result = rcc.startStream(applicationContext.getViewerUdpPort());
            
            if (result) {
                receiver.startReceiver();
                interrupt = false;
                viewerThread = new Thread(this);
                viewerThread.start();
                
                if (viewerStartCallback != null) {
                    viewerStartCallback.run();
                }
            }
        }
        return result;
    }
    
    public void stopViewer() {
        interrupt = true;
        receiver.setInterrupt(true);
        RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
        rcc.stopStream(applicationContext.getViewerUdpPort());
    }

    public ViewerSurface getSurface() {
        return surface;
    }

    public void setSocketTimeOutCallback(Runnable socketTimeOutCallback) {
        receiver.setFireSocketTimeOut(socketTimeOutCallback);
    }

    public void setViewerStartCallback(Runnable viewerStartCallback) {
        this.viewerStartCallback = viewerStartCallback;
    }
    
    public void externalRestarter(boolean state) {
        receiver.setExternalRestarter(state);
    }

    @Override
    public void run() {
        while (!interrupt) {
            surface.displayFrame(receiver.getReceivedImage());
            
            try {
                Thread.sleep(40);
                
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                interrupt = true;
            }
            
        }
        surface.displayFrame(null);
    }
    
}
