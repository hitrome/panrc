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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class Actions {
    
    private Map<String, AbstractAction> actions;
    
    public Actions() {
        actions = new HashMap<String, AbstractAction>();
    }
    
    public AbstractAction getAction(String name) {
        return actions.get(name);
    }
    
    public void putAction(String name, AbstractAction action) {
        actions.put(name, action);
    }
    
    public Set<Map.Entry<String, AbstractAction>> getEntySet() {
        return actions.entrySet();
    }
    
}
