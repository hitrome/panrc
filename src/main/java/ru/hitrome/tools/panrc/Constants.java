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

import java.util.logging.Level;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class Constants {
    
    public static final Level DEFAULT_LOGGING_LEVEL = Level.SEVERE;
    
    public static final String DEFAULT_LABEL_COLOR = "0x77ee";
    
    public static final int DEFAULT_MANUALCAMERAIP_FIELD_SIZE = 30;
    
    public static final int HTTP_CONNECT_TIMEOUT = 3000;
    public static final int HTTP_SOCKET_READ_TIMEOUT = 6000;
    
    public static final String VIEWER_BACKGROUND = "/images/background.png";
    public static final int VIEWER_UDP_PORT = 49199;
    public static final int VIEWER_UDP_SOCKET_TIMEOUT = 2000; // 2 sec
    public static final int VIEWER_MAX_UDP_SOCKET_TIMEOUTS = 10;
    
    public static final int RECMODEFORM_VIEWERCONTROLPANEL_BTNIMG_HEIGHT = 40;
    public static final int RECMODEINDICATOR_HEIGHT = 30;
    public static final int RECMODEINDICATOR_SPACER_WIDTH = 15;
    
    public static final int RECMODEFORM_STATECHECK_INTERVAL = 3000;
    
    // Images and Icons
    public static final String IMG_APPLICATION_ICON = "/images/panrc-img.png";
    public static final String IMG_PREVIEW_ON = "/images/cam-preview-on.png";
    public static final String IMG_PREVIEW_OFF = "/images/cam-preview-off.png";
    public static final String IMG_BATTERY_INDICATOR_GOOD = "/images/cam-battery-indicator-good.png";
    public static final String IMG_BATTERY_INDICATOR_BAD = "/images/cam-battery-indicator-bad.png";
    public static final String IMG_CAM_PREVIEW_INDICATOR_ON = "/images/cam-preview-indicator-on.png";
    public static final String IMG_CAM_PREVIEW_INDICATOR_OFF = "/images/cam-preview-indicator-off.png";
    public static final String IMG_CAM_INDICATOR_ON = "/images/video-camera-indicator-on.png";
    public static final String IMG_CAM_INDICATOR_OFF = "/images/video-camera-indicator-off.png";
    public static final String IMG_CAM_INDICATOR_REC = "/images/video-camera-indicator-rec.png";
    public static final String IMG_CAM_START_RECORD = "/images/cam-start-record.png";
    public static final String IMG_CAM_STOP_RECORD = "/images/cam-stop-record.png";
    public static final String IMG_CAM_ZOOM_IN_FAST = "/images/cam-zoom-in-fast.png";
    public static final String IMG_CAM_ZOOM_IN = "/images/cam-zoom-in.png";
    public static final String IMG_CAM_ZOOM_OUT_FAST = "/images/cam-zoom-out-fast.png";
    public static final String IMG_CAM_ZOOM_OUT = "/images/cam-zoom-out.png";
    public static final String IMG_CAM_STOP_ZOOM = "/images/cam-stop-zoom.png";
    public static final String IMG_CAM_ZOOMAUTOSTOP_ON = "/images/cam-zoom-autostop-on.png";
    public static final String IMG_CAM_ZOOMAUTOSTOP_OFF = "/images/cam-zoom-autostop-off.png";
    
}
