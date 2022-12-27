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
package ru.hitrome.tools.panrc.forms.components;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import javax.swing.DefaultButtonModel;
import static javax.swing.DefaultButtonModel.PRESSED;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class PushButtonModel extends DefaultButtonModel {
    
    @Override
        public void setPressed(boolean b) {
            if (!isEnabled()) {
                return;
            }
            
            if (b) {
                stateMask |= PRESSED;
                
            } else {
                stateMask &= ~PRESSED;
            }

            if (isArmed()) {
                int modifiers = 0;
                AWTEvent currentEvent = EventQueue.getCurrentEvent();
                
                if (currentEvent instanceof InputEvent) {
                    modifiers = ((InputEvent) currentEvent).getModifiers();
                    
                } else if (currentEvent instanceof ActionEvent) {
                    modifiers = ((ActionEvent) currentEvent).getModifiers();
                }
                fireActionPerformed(
                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                getActionCommand(),
                                EventQueue.getMostRecentEventTime(),
                                modifiers));
            }

            fireStateChanged();
        }
    
}
