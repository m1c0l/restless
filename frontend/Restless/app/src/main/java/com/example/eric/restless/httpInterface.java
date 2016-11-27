package com.example.eric.restless;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eric on 11/26/2016.
 */

public class httpInterface {

    JSONObject request(String method, JSONObject json, String url){
        try {
            URL url1=new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod(method);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            if(json!=null) {

                //connection.setRequestProperty("Content-length", json.toString().getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //connection.setUseCaches(false);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.toString().getBytes("UTF-8"));
                //Log.i("Content: ", connection.getContent().toString());
                outputStream.close();
            }
            connection.connect();
            Log.i("Connection String: ",connection.toString());

            int status = connection.getResponseCode();

            Log.i("HTTP Client", "HTTP status code : " + status);
            switch (status) {
                case 200:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    Log.i("HTTP Client", "Received String : " + sb.toString());
                    //return received string
                    JSONObject obj =  new JSONObject(sb.toString());
                    return obj;
            }
        }

            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


