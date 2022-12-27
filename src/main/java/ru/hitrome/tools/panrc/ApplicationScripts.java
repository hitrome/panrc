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

import java.util.logging.Level;
import java.util.logging.Logger;
import ru.hitrome.tools.panrc.modes.ChangingCamIpManuallyMode;
import ru.hitrome.tools.panrc.modes.InitCameraMode;
import ru.hitrome.tools.panrc.modes.RecMode;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ApplicationScripts {
    
    private static final Logger LOGGER = Logger.getLogger(ApplicationScripts.class.getName());
    
    public static void startScript(ApplicationContext applicationContext) {
        applicationContext.setApplicationMode(new ChangingCamIpManuallyMode());
        
    }
    
    public static void firstInitScript(ApplicationContext applicationContext) {
        LOGGER.info("FirstInit script started...");
        applicationContext.setApplicationMode(new InitCameraMode());
        LOGGER.log(Level.INFO, "Mode {0} has been set.", applicationContext.getApplicationMode().getClass().getSimpleName());
        
        Thread thread = new Thread(() -> {
            Util.checkWithTimeout(() -> { return false; }, applicationContext::isFinishedTaskFlag, 4000);
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                //
            }
            
            if (applicationContext.getApplicationMode().getResult()) {
                LOGGER.info("Switching to RecMode...");
                applicationContext.setApplicationMode(new RecMode());
                
            } else {
                LOGGER.log(Level.INFO,
                        "Mode {0} has returned with false result. So, transferring to ChangingCamIpManually mode.",
                        applicationContext.getApplicationMode().getClass().getSimpleName());
                applicationContext.setApplicationMode(new ChangingCamIpManuallyMode());
            }
        });
        thread.start();
    }
    
    public static void recModeFailedScript(ApplicationContext applicationContext) {
        LOGGER.info("RecModeFailed script started - redirecting to ChangingCamIpManually mode...");
        applicationContext.setApplicationMode(new ChangingCamIpManuallyMode());
    }
    
}
