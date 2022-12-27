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
package ru.hitrome.tools.panrc.modes;

import ru.hitrome.tools.panrc.ApplicationContext;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class TerminateMode implements AbstractApplicationMode {

    @Override
    public void initMode(ApplicationContext applicationContext) {
        System.exit(0);
    }

    @Override
    public void unInitMode(ApplicationContext applicationContext) {
        // Not used
    }

    @Override
    public boolean getResult() {
        
        return true;
    }
    
}
