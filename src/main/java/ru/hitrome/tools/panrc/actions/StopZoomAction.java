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
package ru.hitrome.tools.panrc.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import ru.hitrome.tools.panrc.ActionConstants;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.Constants;
import ru.hitrome.tools.panrc.LanguageUtil;
import ru.hitrome.tools.panrc.RemoteControlCommands;
import ru.hitrome.tools.panrc.camqueue.QueueSimpleElement;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class StopZoomAction extends AbstractActionWithCallback {
    
    private final ApplicationContext applicationContext;
    
    public StopZoomAction(ApplicationContext applicationContext) {
        super(LanguageUtil.get(ActionConstants.STOP_ZOOM_ACTION));
        this.applicationContext = applicationContext;
        ImageIcon actionImage = null;
        
        try {
            actionImage = new ImageIcon(ImageIO.read(getClass()
                    .getResourceAsStream(Constants.IMG_CAM_STOP_ZOOM))
                    .getScaledInstance(-1, Constants.RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT, Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            Logger.getLogger(StopZoomAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (actionImage != null) {
            this.putValue(Action.SMALL_ICON, actionImage);
        }
    }
    
    @Override
    public void action(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (((JButton)e.getSource()).getModel().isPressed()) {
                sendStopZoomCommand();
            }
        } else {
            sendStopZoomCommand();
        }
    }
    
    private void sendStopZoomCommand() {
        RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
        if (applicationContext.getCamQueue() != null) {
            this.delayedResult = true;
            applicationContext.getCamQueue().put(new QueueSimpleElement(() -> rcc.stopZoom(), null));
        } else {
            this.actionResult = rcc.stopZoom();
        }
    }
    
}