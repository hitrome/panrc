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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 * @author Roman Novikov <rrl-software@mail.ru>
 */
public class HttpUtil {
    
    private int status;
    
    public int getHttpStatus() {
        
        return status;
    }
    
    public String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }
    
    public String doHttpGetRequest(String address, Map<String, String> params) throws IOException {
        status = 0;
        URL url = new URL("http://" + address + "/cam.cgi?" + getParamsString(params));
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(Constants.HTTP_CONNECT_TIMEOUT);
        con.setReadTimeout(Constants.HTTP_SOCKET_READ_TIMEOUT);
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
        con.connect();
        StringBuilder content;
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        status = con.getResponseCode();
        con.disconnect();
        
        return content.toString();
    }
    
}
