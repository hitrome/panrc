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
package ru.hitrome.tools.panrc.forms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import ru.hitrome.tools.panrc.ApplicationContext;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class InitCameraForm {
    
    private final ApplicationContext applicationContext;
    private final JPanel panel;
    
    public InitCameraForm(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Container container = this.applicationContext.getMainGui().getFrame().getContentPane();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMinimumSize(new Dimension(100, 20));
        progressBar.setPreferredSize(new Dimension(200, 30));
        progressBar.setMaximumSize(new Dimension(400, 40));
        panel.add(Box.createVerticalGlue());
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(Box.createVerticalGlue());
        container.add(panel, BorderLayout.CENTER);
        this.applicationContext.getMainGui().getFrame().pack();
        
        
    }

    public JPanel getPanel() {
        return panel;
    }
    
    
    
}
