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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import ru.hitrome.tools.panrc.ActionConstants;
import ru.hitrome.tools.panrc.Actions;
import ru.hitrome.tools.panrc.ApplicationContext;
import ru.hitrome.tools.panrc.Constants;
import ru.hitrome.tools.panrc.ControlConstants;
import ru.hitrome.tools.panrc.Util;
import ru.hitrome.tools.panrc.actions.AbstractActionWithCallback;
import ru.hitrome.tools.panrc.actions.formactions.RecModeFormPlayStreamAction;
import ru.hitrome.tools.panrc.actions.formactions.RecModeFormStopPlayStreamAction;
import ru.hitrome.tools.panrc.actions.formactions.RecModeFormZoomAutoStopOffAction;
import ru.hitrome.tools.panrc.actions.formactions.RecModeFormZoomAutoStopOnAction;
import ru.hitrome.tools.panrc.forms.components.PushButtonModel;
import ru.hitrome.tools.panrc.forms.components.RecModeIndicator;
import ru.hitrome.tools.panrc.model.StateRec;
import ru.hitrome.tools.panrc.viewer.Viewer;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class RecModeForm {
    
    private final ApplicationContext applicationContext;
    private final Actions formActions;
    private final JPanel panel;
    private final Viewer viewer;
    private final JButton previewOnOfBtn;
    private final JButton recordButton;
    private final JButton zoomInFastButton;
    private final JButton zoomInButton;
    private final JButton zoomOutFastButton;
    private final JButton zoomOutButton;
    private final JButton stopZoomButton;
    private final JButton zoomAutoStopOnOffBtn;
    private RecModeIndicator recModeIndicator;
    
    public RecModeForm(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        formActions = new Actions();
        
        // Preview button
        previewOnOfBtn = new JButton();
        previewOnOfBtn.setHideActionText(true);
        previewOnOfBtn.setMargin(new Insets(0, 0, 0, 0));
        
        // Start/stop record button
        recordButton = new JButton();
        recordButton.setHideActionText(true);
        recordButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Zoom in fast button
        zoomInFastButton = new JButton();
        zoomInFastButton.setModel(new PushButtonModel());
        zoomInFastButton.setHideActionText(true);
        zoomInFastButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Zoom in normal button
        zoomInButton = new JButton();
        zoomInButton.setModel(new PushButtonModel());
        zoomInButton.setHideActionText(true);
        zoomInButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Zoom out fast button
        zoomOutFastButton = new JButton();
        zoomOutFastButton.setModel(new PushButtonModel());
        zoomOutFastButton.setHideActionText(true);
        zoomOutFastButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Zoom out normal button
        zoomOutButton = new JButton();
        zoomOutButton.setModel(new PushButtonModel());
        zoomOutButton.setHideActionText(true);
        zoomOutButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Stop zoom button
        stopZoomButton = new JButton();
        stopZoomButton.setModel(new PushButtonModel());
        stopZoomButton.setHideActionText(true);
        stopZoomButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Autostop zoom on/off button
        zoomAutoStopOnOffBtn = new JButton();
        zoomAutoStopOnOffBtn.setHideActionText(true);
        zoomAutoStopOnOffBtn.setMargin(new Insets(0, 0, 0, 0));
        
        
        // Add form actions
        RecModeFormPlayStreamAction recModeFormPlayStreamAction = new RecModeFormPlayStreamAction(this);
        formActions.putAction(ActionConstants.RECMODEFORM_PLAYSTREAM, recModeFormPlayStreamAction);
        previewOnOfBtn.setAction(recModeFormPlayStreamAction);
        RecModeFormStopPlayStreamAction recModeFormStopPlayStreamAction = new RecModeFormStopPlayStreamAction(this);
        formActions.putAction(ActionConstants.RECMODEFORM_STOPPLAYSTREAM, recModeFormStopPlayStreamAction);
        
        recModeFormPlayStreamAction.setOnExcecuted(res -> {
            
            if (res) {
                previewOnOfBtn.setAction(recModeFormStopPlayStreamAction);
            }
        });
        
        recModeFormStopPlayStreamAction.setOnExcecuted(() -> {
            previewOnOfBtn.setAction(recModeFormPlayStreamAction);
        });
        
        Action startRecordAction = applicationContext.getMainActions().getAction(ActionConstants.START_RECORD_ACTION);
        Action stopRecordAction = applicationContext.getMainActions().getAction(ActionConstants.STOP_RECORD_ACTION);
        recordButton.setAction(startRecordAction);
        
        ((AbstractActionWithCallback)startRecordAction)
                .setOnExecuted((b) -> {
                    
                    if (b) {
                        recordButton.setAction(stopRecordAction);
                    }
                });
        
        ((AbstractActionWithCallback)stopRecordAction)
                .setOnExecuted((b) -> {
                    
                    if (b) {
                        recordButton.setAction(startRecordAction);
                    }
                });
        
        zoomInFastButton.setAction(applicationContext.getMainActions().getAction(ActionConstants.ZOOM_IN_FAST_ACTION));
        zoomInButton.setAction(applicationContext.getMainActions().getAction(ActionConstants.ZOOM_IN_ACTION));
        zoomOutFastButton.setAction(applicationContext.getMainActions()
                .getAction(ActionConstants.ZOOM_OUT_FAST_ACTION));
        zoomOutButton.setAction(applicationContext.getMainActions().getAction(ActionConstants.ZOOM_OUT_ACTION));
        stopZoomButton.setAction(applicationContext.getMainActions().getAction(ActionConstants.STOP_ZOOM_ACTION));
        
        RecModeFormZoomAutoStopOnAction recModeFormZoomAutoStopOnAction = new RecModeFormZoomAutoStopOnAction(this);
        formActions.putAction(ActionConstants.RECMODEFORM_ZOOM_AUTO_STOP_ON, recModeFormZoomAutoStopOnAction);
        RecModeFormZoomAutoStopOffAction recModeFormZoomAutoStopOffAction = new RecModeFormZoomAutoStopOffAction(this);
        formActions.putAction(ActionConstants.RECMODEFORM_ZOOM_AUTO_STOP_OFF, recModeFormZoomAutoStopOffAction);
        zoomAutoStopOnOffBtn.setAction(recModeFormZoomAutoStopOffAction);
        recModeFormZoomAutoStopOnAction.setOnExcecuted(()
                -> this.zoomAutoStopOnOffBtn.setAction(recModeFormZoomAutoStopOffAction));
        recModeFormZoomAutoStopOffAction.setOnExcecuted(()
                -> this.zoomAutoStopOnOffBtn.setAction(recModeFormZoomAutoStopOnAction));
        
        
        viewer = new Viewer(this.applicationContext);
        viewer.externalRestarter(true);
        viewer.setSocketTimeOutCallback(() -> recModeFormStopPlayStreamAction.unconditionalExecute());
        Container container = this.applicationContext.getMainGui().getFrame().getContentPane();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new Box.Filler(new Dimension(0,10), new Dimension(0,15), new Dimension(0,20)));
        
        if (viewer.getSurface() != null) {
            viewer.getSurface().setBackground(Color.blue);
            panel.add(viewer.getSurface());
        }
        
        try {
            recModeIndicator = new RecModeIndicator();
            panel.add(recModeIndicator);
            
            if (applicationContext.getStateChecker() != null) {
                applicationContext.getStateChecker().setTimeInterval(Constants.RECMODEFORM_STATECHECK_INTERVAL);
                applicationContext.getStateChecker().setOnCheckState(() -> this.updateRecModeIndicator());
                applicationContext.getStateChecker().checkState();
                applicationContext.getStateChecker().startCheckingContinously();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(RecModeForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        JPanel viewerControlPanel = new JPanel();
        viewerControlPanel.setLayout(new BoxLayout(viewerControlPanel, BoxLayout.X_AXIS));
        viewerControlPanel.add(Box.createHorizontalGlue());
        viewerControlPanel.add(previewOnOfBtn);
        viewerControlPanel.add(new Box.Filler(new Dimension(15,0), new Dimension(20,0), new Dimension(25,0)));
        viewerControlPanel.add(recordButton);
        viewerControlPanel.add(new Box.Filler(new Dimension(30,0), new Dimension(40,0), new Dimension(50,0)));
        viewerControlPanel.add(zoomInFastButton);
        viewerControlPanel.add(zoomInButton);
        viewerControlPanel.add(new Box.Filler(new Dimension(5,0), new Dimension(10,0), new Dimension(15,0)));
        viewerControlPanel.add(stopZoomButton);
        viewerControlPanel.add(new Box.Filler(new Dimension(5,0), new Dimension(10,0), new Dimension(15,0)));
        viewerControlPanel.add(zoomOutButton);
        viewerControlPanel.add(zoomOutFastButton);
        viewerControlPanel.add(new Box.Filler(new Dimension(15,0), new Dimension(20,0), new Dimension(25,0)));
        viewerControlPanel.add(zoomAutoStopOnOffBtn);
        viewerControlPanel.add(Box.createHorizontalGlue());
        viewerControlPanel.setPreferredSize(
                new Dimension(490, Constants.RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT + 12));
        viewerControlPanel.setMaximumSize(
                new Dimension(490, Constants.RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT + 12));
        viewerControlPanel.setMinimumSize(
                new Dimension(490, Constants.RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT + 12));
        panel.add(viewerControlPanel);
        
        panel.add(new Box.Filler(new Dimension(0,10), new Dimension(0,15), new Dimension(0,20)));
        
        container.add(panel, BorderLayout.CENTER);
        
        this.applicationContext.getMainGui().getFrame().pack();
        formActions.getAction(ActionConstants.RECMODEFORM_PLAYSTREAM).actionPerformed(null);
    }

    public JPanel getPanel() {
        return panel;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    private void updateRecModeIndicator() {
        if (applicationContext.getStateChecker() != null && applicationContext.getStateChecker().getState() != null
                && recModeIndicator != null && applicationContext.getStateChecker().getState() instanceof StateRec) {
            StateRec tmpState = (StateRec)applicationContext.getStateChecker().getState();
            recModeIndicator.setPreviewIndicator(tmpState.getLivestream() != null
                    ? tmpState.getLivestream().equals(ControlConstants.ON) : false);
            
            if (tmpState.getOperation() != null && tmpState.getOperation().equals(ControlConstants.OPERATE_ENABLE)) {
                
                if (tmpState.getRec() != null && tmpState.getRec().equals(ControlConstants.ON)) {
                   recModeIndicator.setCamIndicator(RecModeIndicator.CAM_INDICATOR_REC);
                   
                } else {
                    recModeIndicator.setCamIndicator(RecModeIndicator.CAM_INDICATOR_ON);
                }
                
            } else {
                recModeIndicator.setCamIndicator(RecModeIndicator.CAM_INDICATOR_OFF);
            }
            int batteryPercentage = Util.getBatteryPercentage(tmpState.getBatt());
            
            if (batteryPercentage > 20) {
                recModeIndicator.setBatteryIndicator(RecModeIndicator.BATTERY_INDICATOR_GOOD, tmpState.getBatt());
                
            } else {
                recModeIndicator.setBatteryIndicator(RecModeIndicator.BATTERY_INDICATOR_BAD, tmpState.getBatt());
            }
        } else {
            
            if (recModeIndicator != null) {
                recModeIndicator.resetIndicators();
            }
        }
    }
    
}
