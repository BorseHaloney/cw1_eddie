package com.example.cw1_s2328015.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;


public class TrafficRepository
{
    public static final String UNPLANNED_URL = "https://datex2.trafficscotland.org/rest/2.3/publications/UnplannedEvents/Content.xml";
    public static final String ROADWORKS_URL = "https://datex2.trafficscotland.org/rest/2.3/publications/CurrentRoadworks/Content.xml";


    public static final String UNPLANNED_FILE = "unplanned.xml";
    public static final String ROADWORKS_FILE = "roadworks.xml";

    private final String clientId;
    private final String clientKey;

    private final File filesDir;


    private boolean downloadFailed = false;
    private boolean usedSavedData = false;

    public TrafficRepository(String clientId, String clientKey, File filesDir)
    {
        this.clientId = clientId;
        this.clientKey = clientKey;
        this.filesDir = filesDir;
    }


    public ArrayList<Event> fetchEvents(String url, Event.Category category, String fileName)
    {
        downloadFailed = false;
        usedSavedData = false;
        String result = download(url);

        //Clean up any leading garbage characters
        int i = result.indexOf("<?"); //initial tag
        if (i >= 0) {
            result = result.substring(i);
            saveToFile(fileName, result); // keep it for when there's no connection
        } else {
            // No XML received (no connection, bad key, ...) - use the last saved copy instead
            Log.e("TrafficRepository", "No XML received from " + url);
            downloadFailed = true;
            result = loadFromFile(fileName);
            if (result == null) {
                return new ArrayList<>(); // nothing saved yet (e.g. first run offline)
            }
            usedSavedData = true;
            Log.d("TrafficRepository", "Using saved " + fileName);
        }

        // Now that you have the xml data into result, you can parse it
        return new EventParser().parseData(result, category);
    }


    public boolean downloadFailed() { return downloadFailed; }

    public boolean usedSavedData() { return usedSavedData; }

    //time data was saved at
    public String getSavedTimeText(String fileName)
    {
        File f = new File(filesDir, fileName);
        return new SimpleDateFormat("dd/MM HH:mm", Locale.UK).format(new Date(f.lastModified()));
    }

        //saves to file for offline use
    private void saveToFile(String fileName, String xml)
    {
        File f = new File(filesDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(xml.getBytes());
            fos.close();
            Log.d("TrafficRepository", "Saved " + fileName);
        } catch (IOException e) {
            Log.e("TrafficRepository", "Couldn't save " + fileName, e);
        }
    }


     // Reads saved daya from storage
    private String loadFromFile(String fileName)
    {
        File f = new File(filesDir, fileName);
        if (!f.exists()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        String inputLine;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            Log.e("TrafficRepository", "Couldn't read " + fileName, e);
            return null;
        }
        return result.toString();
    }



    //download the feed
    private String download(String myurl)
    {
        // StringBuilder instead of result = result + inputLine
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        String inputLine = "";
        Log.d("TrafficRepository","Downloading " + myurl);
        try {
            URL url = new URL(myurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization",Base64.getEncoder().encodeToString((clientId + ":" + clientKey).getBytes()));
            try {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((inputLine = in.readLine()) != null){
                    result.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                // Log the HTTP status (e.g. 401 = bad client ID / key) instead of crashing
                Log.e("urlConnection", "ioexception, HTTP " + urlConnection.getResponseCode(), e);
            }
            finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("URL", "ioexception", e);
        }
        return result.toString();
    }
}
