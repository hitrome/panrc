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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.ApplicationScripts;
import static ru.hitrome.tools.panrc.Constants.DEFAULT_LABEL_COLOR;
import static ru.hitrome.tools.panrc.Constants.DEFAULT_MANUALCAMERAIP_FIELD_SIZE;
import ru.hitrome.tools.panrc.LanguageUtil;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ManualCameraIpForm {
    
    private final ApplicationContext applicationContext;
    private final JPanel panel;
    private final JTextField camIpFld;
    private final JTextField camIpMaskFld;
    
    public ManualCameraIpForm(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Container container = this.applicationContext.getMainGui().getFrame().getContentPane();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new Box.Filler(new Dimension(0,5), new Dimension(0,30), new Dimension(0,200)));
        
        // IP address fields
        JLabel camIpAddressLabel = new JLabel(LanguageUtil.get("manualcameraipform.cameraipaddress"));
        Font font = camIpAddressLabel.getFont().deriveFont(Font.ITALIC, 18);
        camIpAddressLabel.setFont(font);
        camIpAddressLabel.setForeground(Color.decode(DEFAULT_LABEL_COLOR));
        camIpAddressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(camIpAddressLabel);
        camIpFld = new JTextField(DEFAULT_MANUALCAMERAIP_FIELD_SIZE);
        camIpFld.setFont(font);
        camIpFld.setMaximumSize(camIpFld.getPreferredSize());
        camIpFld.setText(applicationContext.getCameraIpAddress());
        panel.add(camIpFld);
        panel.add(new Box.Filler(new Dimension(0,5), new Dimension(0,20), new Dimension(0,30)));
        JLabel camIpMaskLabel = new JLabel(LanguageUtil.get("manualcameraipform.cameraipmask"));
        camIpMaskLabel.setFont(font);
        camIpMaskLabel.setForeground(Color.decode(DEFAULT_LABEL_COLOR));
        camIpMaskLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(camIpMaskLabel);
        camIpMaskFld = new JTextField(DEFAULT_MANUALCAMERAIP_FIELD_SIZE);
        camIpMaskFld.setFont(font);
        camIpMaskFld.setMaximumSize(camIpMaskFld.getPreferredSize());
        camIpMaskFld.setText(applicationContext.getCameraIpMask());
        panel.add(camIpMaskFld);
        
        panel.add(new Box.Filler(new Dimension(0,5), new Dimension(0,20), new Dimension(0,30)));
        
        // Connect to camera button
        JButton connectButton = new JButton(LanguageUtil.get("manualcameraipform.connectbtn"));
        connectButton.setFont(font);
        connectButton.setForeground(Color.decode(DEFAULT_LABEL_COLOR));
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(connectButton);
        
        panel.add(new Box.Filler(new Dimension(0,5), new Dimension(0,300), new Dimension(0,500)));
        container.add(panel, BorderLayout.CENTER);
        this.applicationContext.getMainGui().getFrame().pack();
        
        // Listeners
        connectButton.addActionListener((ActionEvent e) -> {
            applicationContext.setCameraIpAddress(camIpFld.getText());
            applicationContext.setCameraIpMask(camIpMaskFld.getText());
            ApplicationScripts.firstInitScript(applicationContext);
        });
    }

    public JPanel getPanel() {
        return panel;
    }
    
    
    
}
