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

import java.util.function.Supplier;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class Util {
    
    public static void checkWithTimeout(Supplier<Boolean> terminated, Supplier<Boolean> checker, int timeOut) {
        for (int time = 0; time < timeOut; time++) {
            
            if (terminated.get() || checker.get()) {
                break;
            }
            
            try {
                Thread.sleep(1);
                
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
    
    public static int getBatteryPercentage(String batteryState) {
        int result = 0;
        
        if (batteryState != null) {
            String[] tmpArr = batteryState.split("/");
            
            if (tmpArr.length == 2) {
                result = (int)((((float)(Integer.parseInt(tmpArr[0]))) / Integer.parseInt(tmpArr[1])) * 100);
            }
        }
        
        return result;
    }
    
}
