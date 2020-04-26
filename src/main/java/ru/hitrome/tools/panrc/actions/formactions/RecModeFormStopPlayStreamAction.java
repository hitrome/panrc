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

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import ru.hitrome.tools.panrc.ActionConstants;
import ru.hitrome.tools.panrc.Constants;
import ru.hitrome.tools.panrc.LanguageUtil;
import ru.hitrome.tools.panrc.forms.RecModeForm;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class RecModeFormStopPlayStreamAction extends RecModeFormAbstractAction {
    
    private final RecModeForm form;
    
    
    public RecModeFormStopPlayStreamAction(RecModeForm form) {
        super(LanguageUtil.get(ActionConstants.RECMODEFORM_STOPPLAYSTREAM));
        this.putValue(Action.SHORT_DESCRIPTION, LanguageUtil.get(ActionConstants.RECMODEFORM_STOPPLAYSTREAM));
        ImageIcon actionImage = null;
        try {
            actionImage = new ImageIcon(ImageIO.read(getClass()
                    .getResourceAsStream(ru.hitrome.tools.panrc.Constants.IMG_PREVIEW_OFF)).getScaledInstance(-1, Constants.RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT, Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            Logger.getLogger(RecModeFormPlayStreamAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (actionImage != null) {
            this.putValue(Action.SMALL_ICON, actionImage);
        }
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.enabled) {
            unconditionalExecute();
        }
    }
    
    public synchronized void unconditionalExecute() {
        form.getViewer().stopViewer();
        if (onExecutedAl != null) {
            onExecutedAl.run();
        }
    }
    
}
