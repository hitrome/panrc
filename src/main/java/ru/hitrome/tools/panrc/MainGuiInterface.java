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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class MainGuiInterface {
    
    private JFrame frame;
    private final ApplicationContext applicationContext;
    
    MainGuiInterface(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.applicationContext.setMainGui(this);
        initGui();
    }

    public JFrame getFrame() {
        return frame;
    }
    
    
    private void initGui() {
        frame = new JFrame(LanguageUtil.get("application.title"));
        try {
            frame.setIconImage(ImageIO.read(getClass().getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_APPLICATION_ICON)));
        } catch (IOException ex) {
            Logger.getLogger(MainGuiInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        WindowListener wndCloser = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                applicationContext.getMainActions().getAction(ActionConstants.EXIT_ACTION).actionPerformed(null);
            }
        };
        frame.addWindowListener(wndCloser);
        buildMainMenu();
        
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void buildMainMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu(LanguageUtil.get("application.mainmenu.file"));
        
        JMenuItem changeCameraIp = new JMenuItem();
        changeCameraIp.setAction(applicationContext.getMainActions().getAction(ActionConstants.CHANGE_CAMERA_IP_ACTION));
        fileMenu.add(changeCameraIp);
        
        JMenuItem initCameraItem = new JMenuItem();
        initCameraItem.setAction(applicationContext.getMainActions().getAction(ActionConstants.INIT_CAMERA_ACTION));
        fileMenu.add(initCameraItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem();
        exitItem.setAction(applicationContext.getMainActions().getAction(ActionConstants.EXIT_ACTION));
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        
        frame.setJMenuBar(menuBar);
    }
    
}
