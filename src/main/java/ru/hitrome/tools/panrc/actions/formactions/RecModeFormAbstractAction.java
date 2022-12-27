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
package ru.hitrome.tools.panrc.actions.formactions;

import java.util.function.Consumer;
import javax.swing.AbstractAction;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public abstract class RecModeFormAbstractAction extends AbstractAction {
    
    protected Consumer<Boolean> onSetEnabled;
    protected Consumer<Boolean> onExecuted;
    protected Runnable onExecutedAl;
    
    public RecModeFormAbstractAction(String name) {
        super(name);
    }
    
    @Override
    public void setEnabled(boolean newValue) {
        super.setEnabled(newValue);
        
        if (onSetEnabled != null) {
            onSetEnabled.accept(newValue);
        }
    }

    public void setOnSetEnabled(Consumer<Boolean> onSetEnabled) {
        this.onSetEnabled = onSetEnabled;
    }

    public void setOnExcecuted(Consumer<Boolean> onExcecuted) {
        this.onExecuted = onExcecuted;
    }
    
    public void setOnExcecuted(Runnable onExcecuted) {
        this.onExecutedAl = onExcecuted;
    }
    
}
