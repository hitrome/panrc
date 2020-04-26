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

import java.util.Map;
import javax.swing.AbstractAction;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ActionsUtil {
    
    public static void enableAll(ApplicationContext applicationContext) {
        applicationContext.getMainActions().getEntySet().forEach((Map.Entry<String, AbstractAction> _item) -> {
            _item.getValue().setEnabled(true);
        });
    }
    
    public static void disableAll(ApplicationContext applicationContext) {
        applicationContext.getMainActions().getEntySet().forEach((Map.Entry<String, AbstractAction> _item) -> {
            _item.getValue().setEnabled(false);
        });
    }
    
    public static void disableAllExceptExit(ApplicationContext applicationContext) {
        applicationContext.getMainActions().getEntySet().forEach((Map.Entry<String, AbstractAction> _item) -> {
            if (!_item.getKey().equals(ActionConstants.EXIT_ACTION)) {
                _item.getValue().setEnabled(false);
            }
        });
    }
    
}
