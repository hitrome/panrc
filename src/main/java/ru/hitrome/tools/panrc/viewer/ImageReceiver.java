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
    
    private final int serverPort;
    private final Runnable streamRestarter;
    private Runnable fireSocketTimeOut;
    private DatagramSocket socket;
    private DatagramPacket udpPacket;
    private byte[] outBuffer = new byte[35840];
    private BufferedImage receivedImage;
    private Thread receiverThread;
    
    private long referenceTime = System.currentTimeMillis();
    private boolean interrupt = false;
    private int socketTimeOutCount = 0;
    private boolean externalRestarter = false;
    

    
    public ImageReceiver(int port, Runnable streamRestarter) {
        serverPort = port;
        this.streamRestarter = streamRestarter;
    }

    public BufferedImage getReceivedImage() {
        return receivedImage;
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
                receivedImage = tempImg;
            } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }

        }
    
    }
    
    private void receiveImage() {
        try {
            // this is here to anticipate the timeout.
            // It seems that the stream has to be restarted periodically,
            // something like every 12 seconds. We take a little margin here.
            if (System.currentTimeMillis() - referenceTime > 11000) {
                throw new ImageReceiverTimeOutException();
            }
            socket.receive(udpPacket);
            outBuffer = udpPacket.getData();
            socketTimeOutCount = 0;
            int offset = -1;
            // find beginning of the image
            for (int i = 0; i < 500; i++) {
                if (outBuffer[i] == -1 && outBuffer[i + 1] == -40) {
                    offset = i;
                    break;
                }
            }
            if (offset != -1) {
                Thread thread = new ProcessImageData(Arrays.copyOfRange(outBuffer, offset, outBuffer.length - offset));
                thread.start();
            }
            
            
        } catch (ImageReceiverTimeOutException e) {
            if (!externalRestarter) {
                Thread thread = new Thread(() -> {
                    streamRestarter.run();
                });
                thread.start();
            }
            referenceTime = System.currentTimeMillis();
            System.out.println(referenceTime);
        }  catch (IOException ioe) {
            if (ioe.getClass().getName().equals(SocketTimeoutException.class.getName())) {
                socketTimeOutCount++;
                if (fireSocketTimeOut != null && socketTimeOutCount > Constants.VIEWER_MAX_UDP_SOCKET_TIMEOUTS) {
                    fireSocketTimeOut.run();
                }
            } 
            LOGGER.log(Level.INFO, ioe.getMessage(), ioe);
        }
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(serverPort);
            socket.setSoTimeout(Constants.VIEWER_UDP_SOCKET_TIMEOUT);
        } catch (SocketException ex) {
            Logger.getLogger(ImageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            udpPacket = new DatagramPacket(outBuffer, outBuffer.length, InetAddress.getByName("127.0.1.1"), serverPort);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ImageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
            return;
        }
        while (!interrupt) {
            receiveImage(); 
        }
        socket.close();
    }
    
    private static final Logger LOGGER = Logger.getLogger(ImageReceiver.class.getName());
    
}
