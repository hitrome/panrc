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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ru.hitrome.tools.panrc.Constants;
import ru.hitrome.tools.panrc.exceptions.ImageReceiverTimeOutException;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class ImageReceiver implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ImageReceiver.class.getName());
    
    private final int serverPort;
    private final Runnable streamRestarter;
    private Runnable fireSocketTimeOut;
    private DatagramSocket socket;
    private DatagramPacket udpPacket;
    private byte[] outBuffer = new byte[ViewerConstants.RECEIVER_BUFFER];
    private final AtomicReference<BufferedImage> receivedImage;
    private Thread receiverThread;
    
    private long referenceTime = System.currentTimeMillis();
    private int frameCounter = 0;
    private int startFrameCounter = 0;
    private int filteredFrameCounter = 0;
    private final AtomicInteger realFrameCounter = new AtomicInteger(0);
    private boolean interrupt = false;
    private int socketTimeOutCount = 0;
    private boolean externalRestarter = false;
    

    
    public ImageReceiver(int port, Runnable streamRestarter) {
        this.serverPort = port;
        this.streamRestarter = streamRestarter;
        this.receivedImage = new AtomicReference<>();
    }

    public BufferedImage getReceivedImage() {
        return receivedImage.get();
    }
    
    public void startReceiver() {
        this.interrupt = false;
        receiverThread = new Thread(this);
        receiverThread.setPriority(Thread.MAX_PRIORITY);
        receiverThread.start();
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    public boolean isExternalRestarter() {
        return externalRestarter;
    }

    public void setExternalRestarter(boolean externalRestarter) {
        this.externalRestarter = externalRestarter;
    }

    public void setFireSocketTimeOut(Runnable fireSocketTimeOut) {
        this.fireSocketTimeOut = fireSocketTimeOut;
    }
    
    class ProcessImageData extends Thread {
        
        private final byte[] buffer;
        
        ProcessImageData(byte[] buffer) {
            this.buffer = buffer;
        }
        
        @Override
        @SuppressWarnings("UseSpecificCatch")
        public void run() {
            
            try {
                BufferedImage tempImg = ImageIO.read(new ByteArrayInputStream(buffer));
                receivedImage.set(tempImg);
                realFrameCounter.incrementAndGet();
                
            } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }

        }
    
    }
    
    private void receiveImage() {
        try {
            startFrameCounter++;
            
            // This is here to anticipate a timeout.
            // It seems that the stream should be restarted periodically,
            // something like every 12 seconds.
            if (System.currentTimeMillis() - referenceTime > ViewerConstants.STREAM_RESTART_TIME) {
                throw new ImageReceiverTimeOutException();
            }
            socket.receive(udpPacket);
            
            if (udpPacket.getLength() < ViewerConstants.MINIMAL_VALID_FRAME_SIZE) {
                
                return;
            }
            filteredFrameCounter++;
            
            outBuffer = udpPacket.getData();
            socketTimeOutCount = 0;
            int offset = -1;
            
            // Seeking to the beginning of the image
            for (
                    int i = udpPacket.getOffset(); 
                    i < (udpPacket.getLength() < udpPacket.getOffset() + ViewerConstants.MARKER_SEARCH_RANGE 
                    ? udpPacket.getLength() : udpPacket.getOffset() + ViewerConstants.MARKER_SEARCH_RANGE);
                    i++) {
                if (i + 1 < udpPacket.getLength() && outBuffer[i] == -1 && outBuffer[i + 1] == -40) {
                    offset = i;
                    break;
                }
            }
            
            if (offset != -1) {
                Thread thread = new ProcessImageData(Arrays.copyOfRange(outBuffer, offset,
                        udpPacket.getLength() < outBuffer.length ?
                                udpPacket.getLength() - offset : outBuffer.length - offset));
                thread.start();
                frameCounter++;
            }
            
            
        } catch (ImageReceiverTimeOutException e) {
            
            if (!externalRestarter) {
                Thread thread = new Thread(() -> {
                    streamRestarter.run();
                });
                thread.start();
            }
            referenceTime = System.currentTimeMillis();
            LOGGER.log(Level.INFO, "{0}\t{1}\t{2}\t{3}\t{4}", new Object[]{String.valueOf(referenceTime),
                startFrameCounter, filteredFrameCounter, frameCounter, realFrameCounter.get()});
            startFrameCounter = 0;
            filteredFrameCounter = 0;
            frameCounter = 0;
            realFrameCounter.set(0);
            System.gc();
            
        } catch (SocketTimeoutException ste) {
            socketTimeOutCount++;

            if (fireSocketTimeOut != null && socketTimeOutCount > Constants.VIEWER_MAX_UDP_SOCKET_TIMEOUTS) {
                fireSocketTimeOut.run();
            }
            LOGGER.info(ste.getMessage());
            
        } catch (IOException ioe) { 
            LOGGER.log(Level.INFO, ioe.getMessage(), ioe);
        }
        
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(serverPort);
            socket.setSoTimeout(Constants.VIEWER_UDP_SOCKET_TIMEOUT);
            
        } catch (SocketException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return;
        }
        
        try {
            udpPacket = new DatagramPacket(outBuffer, outBuffer.length, InetAddress.getByName("127.0.1.1"), serverPort);
            
        } catch (UnknownHostException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            socket.close();
            return;
        }
        
        while (!interrupt) {
            receiveImage(); 
        }
        
        socket.close();
    }
    
}
