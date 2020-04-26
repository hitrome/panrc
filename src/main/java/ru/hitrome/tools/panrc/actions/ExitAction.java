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
package ru.hitrome.tools.panrc.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import ru.hitrome.tools.panrc.ActionConstants;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.LanguageUtil;
import ru.hitrome.tools.panrc.modes.TerminateMode;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ExitAction extends AbstractAction {
    
    private final ApplicationContext applicationContext;
    
    public ExitAction(ApplicationContext applicationContext) {
        super(LanguageUtil.get(ActionConstants.EXIT_ACTION));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("X"));
        this.applicationContext = applicationContext;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        applicationContext.setApplicationMode(new TerminateMode());
    }
    
}
