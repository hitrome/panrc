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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.hitrome.tools.panrc.actions.ChangeCameraIpAction;
import ru.hitrome.tools.panrc.actions.ExitAction;
import ru.hitrome.tools.panrc.actions.InitCameraAction;
import ru.hitrome.tools.panrc.actions.RecModeAction;
import ru.hitrome.tools.panrc.actions.StartRecordAction;
import ru.hitrome.tools.panrc.actions.StopRecordAction;
import ru.hitrome.tools.panrc.actions.StopZoomAction;
import ru.hitrome.tools.panrc.actions.ZoomInAction;
import ru.hitrome.tools.panrc.actions.ZoomInFastAction;
import ru.hitrome.tools.panrc.actions.ZoomOutAction;
import ru.hitrome.tools.panrc.actions.ZoomOutFastAction;
import ru.hitrome.tools.panrc.camqueue.CamQueue;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class PanRc {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        setLoggerLevel(Constants.DEFAULT_LOGGING_LEVEL);
        
        List<String> keyArgs = new ArrayList<>();
        String parserMode = null;
        int argumentCount = 0;
        for (String arg:args) {
            if (argumentCount == 0) {
                switch (arg) {
                    case CmdLineKeys.LOGGING_LEVEL  :
                        parserMode = CmdLineKeys.LOGGING_LEVEL;
                        argumentCount = 1;
                        break;
                    case CmdLineKeys.LOGGING_LEVEL_EX  :
                        parserMode = CmdLineKeys.LOGGING_LEVEL;
                        argumentCount = 1;
                        break;
                    case CmdLineKeys.VERBOSE  :
                        parserMode = CmdLineKeys.VERBOSE;
                        argumentCount = 0;
                        break;
                    case CmdLineKeys.VERBOSE_EX  :
                        parserMode = CmdLineKeys.VERBOSE;
                        argumentCount = 0;
                        break;
                }
            } else {
                keyArgs.add(arg);
                argumentCount--;
            }
            if (parserMode != null && argumentCount == 0) {
                switch (parserMode) {
                    case CmdLineKeys.LOGGING_LEVEL  :
                        setLoggerLevel(keyArgs.get(0), parserMode);
                        break;
                    case CmdLineKeys.VERBOSE    :
                        setLoggerLevel(Level.INFO);
                        break;
                }
                keyArgs.clear();
                parserMode = null;
            }
        }
        
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setCamQueue(new CamQueue());
        
        applicationContext.getMainActions().putAction(ActionConstants.EXIT_ACTION, new ExitAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.INIT_CAMERA_ACTION, new InitCameraAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.CHANGE_CAMERA_IP_ACTION, new ChangeCameraIpAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.REC_MODE_ACTION, new RecModeAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.START_RECORD_ACTION, new StartRecordAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.STOP_RECORD_ACTION, new StopRecordAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.ZOOM_IN_FAST_ACTION, new ZoomInFastAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.ZOOM_IN_ACTION, new ZoomInAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.ZOOM_OUT_FAST_ACTION, new ZoomOutFastAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.ZOOM_OUT_ACTION, new ZoomOutAction(applicationContext));
        applicationContext.getMainActions().putAction(ActionConstants.STOP_ZOOM_ACTION, new StopZoomAction(applicationContext));
        
        MainGuiInterface mainWindow = new MainGuiInterface(applicationContext);
        ApplicationScripts.startScript(applicationContext);
        
    }
    
    public static void setLoggerLevel(Level targetLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
    }
    
    private static void setLoggerLevel(String targetLevel, String mode) {
        try {
            Level level = Level.parse(targetLevel);
            setLoggerLevel(level);
            LOGGER.log(level, "{0} {1}", new Object[]{LanguageUtil.get("message.loggersettolevel"), level.getName()});
        } catch (IllegalArgumentException e) {
            System.out.println(LanguageUtil.get("error.wrongargument") + " " + mode);
        }
    }
    
    private static final Logger LOGGER = Logger.getLogger(PanRc.class.getName());
    
}
