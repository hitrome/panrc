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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.hitrome.tools.panrc.model.State;
import ru.hitrome.tools.panrc.model.StateRec;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class RemoteControlCommands {
    
    private static final Logger LOGGER = Logger.getLogger(RemoteControlCommands.class.getName());
    
    private final HttpUtil httpUtil = new HttpUtil();
    private final String cameraAddress;
    private String contentBuffer;
    private int httpStatus;
    
    
    public RemoteControlCommands(String camAddr) {
        cameraAddress = camAddr;
    }
    
    public String getHttpResponseContent() {
        return contentBuffer;
    }
    
    public int getLastHttpStatus() {
        return httpStatus;
    }
    
    public boolean switchToPlayMode() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_PLAYMODE);
        
        return sendCommand(params);
    }
    
    public boolean switchToRecMode() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_RECMODE);
        
        return sendCommand(params);
    }
    
    public boolean startStream(int port) {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_START_STREAM);
        params.put(ControlConstants.CAM_VAL, String.valueOf(port));
        
        return sendCommand(params);
    }
    
    public boolean stopStream(int port) {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_STOP_STREAM);
        
        return sendCommand(params);
    }
    
    public boolean getState() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_GET_STATE);
        
        return sendCommand(params);
    }
    
    public State getStateEx() {
        State result = null;
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_GET_STATE);
        
        try {
            sendCommandEx(params);
            result = parseState();
            
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
        }
        
        return result;
    }
    
    public boolean getCapability() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_GET_INFO);
        params.put(ControlConstants.CAM_TYPE, ControlConstants.CAM_CAPABILITY);
        
        return sendCommand(params);
    }
    
    public boolean startRecord() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_START_REC);
        
        return sendCommand(params);
    }
    
    public boolean stopRecord() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_STOP_REC);
        
        return sendCommand(params);
    }
    
    public boolean zoomInFast() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_ZOOM_IN_FAST);
        
        return sendCommand(params);
    }
    
    public boolean zoomIn() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
        params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_ZOOM_IN);
        
        return sendCommand(params);
    }
    
    public boolean zoomOutFast() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
            params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_ZOOM_OUT_FAST);
            
        return sendCommand(params);
    }
    
    public boolean zoomOut() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
            params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_ZOOM_OUT);
            
        return sendCommand(params);
    }
    
    public boolean stopZoom() {
        Map params = new HashMap<String, String>();
        params.put(ControlConstants.CAM_MODE, ControlConstants.CAM_CMD);
            params.put(ControlConstants.CAM_VAL, ControlConstants.CAM_STOP_ZOOM);
            
        return sendCommand(params);
    }
    
    private void sendCommandEx(Map params) throws IOException {
        contentBuffer = httpUtil.doHttpGetRequest(cameraAddress, params);
        httpStatus = httpUtil.getHttpStatus();
    }
    
    private boolean sendCommand(Map params) {
        
        try {
            sendCommandEx(params);
            
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            
            return false;
        }
        
        return parseResult();
    }
    
    @SuppressWarnings("UseSpecificCatch")
    private boolean parseResult() {
        boolean result = false;
        
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(contentBuffer.getBytes()));
            Node root = document.getDocumentElement();
            NodeList elements = root.getChildNodes();
            
            for (int i = 0; i < elements.getLength(); i++) {
                if (elements.item(i).getNodeName().equals("result") && elements.item(i).getTextContent().equals("ok")) {
                    result = true;
                }
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return result;
    }
    
    private State parseState() {
        State result = null;
        
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(contentBuffer.getBytes()));
            Node root = document.getDocumentElement();
            NodeList elements = root.getChildNodes();
            
            for (int iii = 0; iii < elements.getLength(); iii++) {
                
                switch (elements.item(iii).getNodeName()) {
                    case "result"   :
                        
                        if (!elements.item(iii).getTextContent().equals("ok")) {
                            return null;
                        }
                        break;
                    case "state": {
                        NodeList stateElements = elements.item(iii).getChildNodes();
                        Map params = new HashMap();
                        String mode = "";
                        
                        for (int i = 0; i < stateElements.getLength(); i++) {
                            
                            if (stateElements.item(i).getNodeName().equals("recremaincapacity")
                                    || stateElements.item(i).getNodeName().equals("rectime")) {
                                NodeList subElements = stateElements.item(i).getChildNodes();
                                
                                for (int ii = 0; ii < subElements.getLength(); ii++) {
                                    params.put(stateElements.item(i).getNodeName()
                                            + subElements.item(ii).getNodeName().substring(0, 1).toUpperCase()
                                            + subElements.item(ii).getNodeName().substring(1),
                                            subElements.item(ii).getTextContent());
                                }
                                
                            } else {
                                
                                if (stateElements.item(i).getNodeName().equals("cammode")) {
                                    mode = stateElements.item(i).getTextContent();
                                }
                                params.put(stateElements.item(i).getNodeName(), stateElements.item(i).getTextContent());
                            }
                        }
                        switch (mode) {
                            case ControlConstants.STATE_MODE_REC:
                                result = new StateRec(params);
                                break;

                            default:
                                result = new State(params);
                                break;
                        }
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | DOMException | SAXException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
        }
        return result;
    }
    
}
