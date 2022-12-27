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
package ru.hitrome.tools.panrc.viewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ViewerSurface extends JPanel {
    
    private BufferedImage bufferedImage = null;
    private BufferedImage defaultImage = null;
    
    public ViewerSurface(BufferedImage defaultImage) {
        super();
        this.defaultImage = defaultImage;
    }
    
    public void displayFrame(BufferedImage frame) {
        bufferedImage = frame;
        SwingUtilities.invokeLater(this::repaint);
    }
    
    private void doDrawing(Graphics2D g) {
        BufferedImage tmpImage;
        
        if (bufferedImage != null) {
            tmpImage = bufferedImage;
            
        } else {
            tmpImage = defaultImage;
        }
        double sF = (double) getWidth() / tmpImage.getWidth();
        double dH = tmpImage.getHeight() * sF;
        
        if (dH > getHeight()) {
            sF = (double) getHeight() / tmpImage.getHeight();
        }
        g.scale(sF, sF);
        g.drawImage(tmpImage, (int)((((double)getWidth() - (double)tmpImage.getWidth() * sF) / 2) / sF),
                (int)(((double)(getHeight() - (double)tmpImage.getHeight() * sF) / 2) / sF), null);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing((Graphics2D) g);
    }
    
}
