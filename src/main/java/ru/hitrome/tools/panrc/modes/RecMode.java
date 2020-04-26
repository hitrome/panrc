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

import ru.hitrome.tools.panrc.ActionConstants;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.ApplicationScripts;
import ru.hitrome.tools.panrc.RemoteControlCommands;
import ru.hitrome.tools.panrc.StateChecker;
import ru.hitrome.tools.panrc.actions.AbstractActionWithCallback;
import ru.hitrome.tools.panrc.forms.RecModeForm;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class RecMode implements AbstractApplicationMode, Runnable {
    
    private RecModeForm form;
    private ApplicationContext applicationContext;
    private boolean result = false;

    @Override
    public void initMode(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Thread thread = new Thread(this);
        applicationContext.setFinishedTaskFlag(false);
        thread.start();
    }

    @Override
    public void unInitMode(ApplicationContext applicationContext) {
        if (applicationContext.getStateChecker() != null) {
            applicationContext.getStateChecker().stopCheckingContinously();
            applicationContext.getStateChecker().setOnCheckState(null);
        }
        if (form != null) {
            form.getViewer().stopViewer();
        }
        if (applicationContext.getCamQueue() != null) {
                applicationContext.getCamQueue().stop();
        }
        RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
        rcc.switchToPlayMode();
        if (form != null) {
            applicationContext.getMainGui().getFrame().getContentPane().remove(form.getPanel());
            applicationContext.getMainGui().getFrame().pack();
            applicationContext.setStateChecker(null);
        }
        ((AbstractActionWithCallback)applicationContext.getMainActions().getAction(ActionConstants.START_RECORD_ACTION)).setOnExecuted(null);
        ((AbstractActionWithCallback)applicationContext.getMainActions().getAction(ActionConstants.STOP_RECORD_ACTION)).setOnExecuted(null);
    }

    @Override
    public void run() {
        RemoteControlCommands rcc = new RemoteControlCommands(applicationContext.getCameraIpAddress());
        if (rcc.switchToRecMode()) {
            if (applicationContext.getCamQueue() != null) {
                applicationContext.getCamQueue().start();
            }
            applicationContext.setStateChecker(new StateChecker(applicationContext));
            form = new RecModeForm(applicationContext);
            result = true;
        } else {
            ApplicationScripts.recModeFailedScript(applicationContext);
        }
        applicationContext.setFinishedTaskFlag(true);
    }

    @Override
    public boolean getResult() {
        return result;
    }
    
}
