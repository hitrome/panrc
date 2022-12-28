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
package ru.hitrome.tools.panrc.model;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class State {
    
    private String batt;
    private String batttype;
    private String livestream;
    private String cammode;
    private String sdcardstatus;
    private String operation;
    private String sd_memory;
    private String version;
    private String temperature;
    
    
    @SuppressWarnings("LeakingThisInConstructor")
    public State(Map data) {
        Field[] fields = this.getClass().getDeclaredFields();
        
        for (Field field:fields) {
            
            try {
                field.setAccessible(true);
                field.set(this, data.get(field.getName())) ;
                
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(State.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    public String getBatt() {
        return batt;
    }

    public String getBatttype() {
        return batttype;
    }

    public String getLivestream() {
        return livestream;
    }

    public String getCammode() {
        return cammode;
    }

    public String getSdcardstatus() {
        return sdcardstatus;
    }

    public String getOperation() {
        return operation;
    }

    public String getSd_memory() {
        return sd_memory;
    }

    public String getVersion() {
        return version;
    }

    public String getTemperature() {
        return temperature;
    }
    
    
    
}
