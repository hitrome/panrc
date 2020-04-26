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
package ru.hitrome.tools.panrc.modes;

import ru.hitrome.tools.panrc.ActionsUtil;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.ControlConstants;
import ru.hitrome.tools.panrc.RemoteControlCommands;
import ru.hitrome.tools.panrc.StateChecker;
import ru.hitrome.tools.panrc.forms.InitCameraForm;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class InitCameraMode implements AbstractApplicationMode, Runnable {
    
    private InitCameraForm form;
    private ApplicationContext applicationContext;
    private boolean result = false;

    @Override
    public void initMode(ApplicationContext applicationContext) {
        result = false;
        form = new InitCameraForm(applicationContext);
        ActionsUtil.disableAllExceptExit(applicationContext);
        applicationContext.setCameraInitialized(false);
        applicationContext.setFinishedTaskFlag(false);
        this.applicationContext = applicationContext;
        Thread thread = new Thread(this);
        thread.start();
        
    }

    @Override
    public void unInitMode(ApplicationContext applicationContext) {
        applicationContext.getMainGui().getFrame().getContentPane().remove(form.getPanel());
        applicationContext.getMainGui().getFrame().pack();
        applicationContext.setStateChecker(null);
        ActionsUtil.enableAll(applicationContext);
    }

    @Override
    public void run() {
        RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
        if (rcc.getCapability()) {
            applicationContext.setStateChecker(new StateChecker(applicationContext));
            applicationContext.getStateChecker().checkState();
            if (applicationContext.getStateChecker().getState() != null) {
                if (!applicationContext.getStateChecker().getState().getCammode().equals(ControlConstants.STATE_MODE_PLAYAVCHD)) {
                    result = rcc.switchToPlayMode();
                } else {
                    result = true;
                }
                applicationContext.setCameraInitialized(true);
            } else {
                result = false;
            }
        } else {
            applicationContext.setCameraInitialized(result);
        }
        applicationContext.setFinishedTaskFlag(true);
    }

    @Override
    public boolean getResult() {
        return result;
    }
    
}
