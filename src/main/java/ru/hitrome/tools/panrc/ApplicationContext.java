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

import java.awt.image.BufferedImage;
import ru.hitrome.tools.panrc.camqueue.CamQueue;
import ru.hitrome.tools.panrc.modes.AbstractApplicationMode;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ApplicationContext {
    
    private final Actions mainActions;
    
    private AbstractApplicationMode applicationMode;
    private MainGuiInterface mainGui;
    private String cameraIpAddress;
    private String cameraIpMask;
    private BufferedImage viewerBackground;
    private StateChecker stateChecker;
    
    private boolean cameraInitialized = false;
    private boolean finishedTaskFlag = false;
    private boolean zoomAutoStop = true;
    private int viewerUdpPort = Constants.VIEWER_UDP_PORT;
    private CamQueue camQueue;
    
    public ApplicationContext() {
        mainActions = new Actions();
    }
    
    public Actions getMainActions() {
        return mainActions;
    }

    public AbstractApplicationMode getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(AbstractApplicationMode applicationMode) {
        if (this.applicationMode != null) {
            this.applicationMode.unInitMode(this);
        }
        this.applicationMode = applicationMode;
        this.applicationMode.initMode(this);
    }

    
    
    public String getCameraIpAddress() {
        return cameraIpAddress;
    }

    public void setCameraIpAddress(String cameraIpAddress) {
        this.cameraIpAddress = cameraIpAddress;
    }

    public String getCameraIpMask() {
        return cameraIpMask;
    }

    public void setCameraIpMask(String cameraIpMask) {
        this.cameraIpMask = cameraIpMask;
    }

    public CamQueue getCamQueue() {
        return camQueue;
    }

    public void setCamQueue(CamQueue camQueue) {
        this.camQueue = camQueue;
    }

    public MainGuiInterface getMainGui() {
        return mainGui;
    }

    public void setMainGui(MainGuiInterface mainGui) {
        this.mainGui = mainGui;
    }
    
    public boolean hasGui() {
        return mainGui != null;
    }

    public boolean isCameraInitialized() {
        return cameraInitialized;
    }

    public synchronized void setCameraInitialized(boolean cameraInitialized) {
        this.cameraInitialized = cameraInitialized;
    }

    public boolean isFinishedTaskFlag() {
        return finishedTaskFlag;
    }

    public synchronized void setFinishedTaskFlag(boolean finishedTaskFlag) {
        this.finishedTaskFlag = finishedTaskFlag;
    }

    public BufferedImage getViewerBackground() {
        return viewerBackground;
    }

    public void setViewerBackground(BufferedImage viewerBackground) {
        this.viewerBackground = viewerBackground;
    }

    public int getViewerUdpPort() {
        return viewerUdpPort;
    }

    public void setViewerUdpPort(int viewerUdpPort) {
        this.viewerUdpPort = viewerUdpPort;
    }

    public StateChecker getStateChecker() {
        return stateChecker;
    }

    public void setStateChecker(StateChecker stateChecker) {
        this.stateChecker = stateChecker;
    }

    public boolean isZoomAutoStop() {
        return zoomAutoStop;
    }

    public void setZoomAutoStop(boolean zoomAutoStop) {
        this.zoomAutoStop = zoomAutoStop;
    }
    
}
