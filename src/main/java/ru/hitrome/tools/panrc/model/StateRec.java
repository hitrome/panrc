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
public class StateRec extends State {
    
    private String rec;
    private String recremaincapacityHours;
    private String recremaincapacityMin;
    private String remaincapacity;
    private String rectimeHours;
    private String rectimeMin;
    private String rectimeSec;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public StateRec(Map data) {
        super(data);
        Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
        for (Field field:fields) {
            try {
                field.setAccessible(true);
                field.set(this, data.get(field.getName())) ;
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(State.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getRec() {
        return rec;
    }

    public String getRecremaincapacityHours() {
        return recremaincapacityHours;
    }

    public String getRecremaincapacityMin() {
        return recremaincapacityMin;
    }

    public String getRemaincapacity() {
        return remaincapacity;
    }

    public String getRectimeHours() {
        return rectimeHours;
    }

    public String getRectimeMin() {
        return rectimeMin;
    }

    public String getRectimeSec() {
        return rectimeSec;
    }
    
}
