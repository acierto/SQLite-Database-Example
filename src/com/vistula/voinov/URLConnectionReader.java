package com.vistula.voinov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionReader {

    public String readFromUrl(URL url) throws Exception {
        URLConnection yc = url.openConnection();
        BufferedReader in = null;
        StringBuilder inputLine = new StringBuilder();

        try {
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String line;
            while ((line = in.readLine()) != null)
                inputLine.append(line);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return inputLine.toString();
    }
}
