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

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ru.hitrome.tools.panrc.Constants;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class RecModeIndicator extends JPanel {
    
    private final Icon previewIndicatorOn;
    private final Icon previewIndicatorOff;
    private final Icon camIndicatorOn;
    private final Icon camIndicatorOff;
    private final Icon camIndicatorRec;
    private final Icon batteryIndicatorGood;
    private final Icon batteryIndicatorBad;
    private final JLabel previewIndicator;
    private final JLabel camIndicator;
    private final JLabel batteryIndicator;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public RecModeIndicator() throws IOException {
        super();
        previewIndicatorOn = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_CAM_PREVIEW_INDICATOR_ON))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        previewIndicatorOff = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_CAM_PREVIEW_INDICATOR_OFF))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        camIndicatorOn = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_CAM_INDICATOR_ON))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        camIndicatorOff = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_CAM_INDICATOR_OFF))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        camIndicatorRec = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_CAM_INDICATOR_REC))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        batteryIndicatorGood = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_BATTERY_INDICATOR_GOOD))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        batteryIndicatorBad = new ImageIcon(ImageIO.read(getClass()
                .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_BATTERY_INDICATOR_BAD))
                .getScaledInstance(-1, Constants.RECMODEINDICATOR_HEIGHT, Image.SCALE_SMOOTH));
        previewIndicator = new JLabel();
        previewIndicator.setIcon(previewIndicatorOff);
        camIndicator = new JLabel();
        camIndicator.setIcon(camIndicatorOff);
        batteryIndicator = new JLabel();
        batteryIndicator.setIcon(batteryIndicatorBad);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createHorizontalGlue());
        this.add(previewIndicator);
        this.add(new Box.Filler(new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH,
                Constants.RECMODEINDICATOR_HEIGHT),
                new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH, Constants.RECMODEINDICATOR_HEIGHT),
                new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH, Constants.RECMODEINDICATOR_HEIGHT)));
        this.add(camIndicator);
        this.add(new Box.Filler(new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH,
                Constants.RECMODEINDICATOR_HEIGHT),
                new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH, Constants.RECMODEINDICATOR_HEIGHT),
                new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH, Constants.RECMODEINDICATOR_HEIGHT)));
        this.add(batteryIndicator);
        Dimension indicatorSize = new Dimension(Constants.RECMODEINDICATOR_SPACER_WIDTH * 4
                + previewIndicatorOff.getIconWidth() + camIndicatorOff.getIconWidth()
                + batteryIndicatorBad.getIconWidth(),
                Constants.RECMODEINDICATOR_HEIGHT + 8);
        this.add(Box.createHorizontalGlue());
        this.setPreferredSize(indicatorSize);
        this.setMinimumSize(indicatorSize);
        this.setMaximumSize(indicatorSize);
        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    }
    
    public void setPreviewIndicator(boolean state) {
        if (state) {
            previewIndicator.setIcon(previewIndicatorOn);
            
        } else {
            previewIndicator.setIcon(previewIndicatorOff);
        }
    }
    
    public void setCamIndicator(int state) {
        switch (state) {
            case CAM_INDICATOR_OFF  :
                camIndicator.setIcon(camIndicatorOff);
                break;
            case CAM_INDICATOR_ON   :
                camIndicator.setIcon(camIndicatorOn);
                break;
            case CAM_INDICATOR_REC  :
                camIndicator.setIcon(camIndicatorRec);
                break;
            default :
                camIndicator.setIcon(camIndicatorOff);
                break;
        }
    }
    
    public void setBatteryIndicator(int state) {
        switch (state) {
            case BATTERY_INDICATOR_BAD  :
                batteryIndicator.setIcon(batteryIndicatorBad);
                break;
            case BATTERY_INDICATOR_GOOD :
                batteryIndicator.setIcon(batteryIndicatorGood);
                break;
            default :
                batteryIndicator.setIcon(batteryIndicatorBad);
                break;
        }
    }
    
    public void setBatteryIndicator(int state, String batteryMessage) {
        batteryIndicator.setToolTipText(batteryMessage);
        setBatteryIndicator(state);
    }
    
    public void resetIndicators() {
        previewIndicator.setIcon(previewIndicatorOff);
        camIndicator.setIcon(camIndicatorOff);
        batteryIndicator.setIcon(batteryIndicatorBad);
        batteryIndicator.setToolTipText(null);
    }
    
    public static final int CAM_INDICATOR_OFF = 0;
    public static final int CAM_INDICATOR_ON = 1;
    public static final int CAM_INDICATOR_REC = 2;
    
    public static final int BATTERY_INDICATOR_BAD = 0;
    public static final int BATTERY_INDICATOR_GOOD = 1;
    
}
