package com.vistula.voinov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnectionReader {

    public static String readFromUrl(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        StringBuilder inputLine = new StringBuilder();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = in.readLine()) != null)
                inputLine.append(line).append("\n");
        } finally {
            if (in != null) {
                in.close();
            }

            urlConnection.disconnect();
        }

        return inputLine.toString();
    }
}
