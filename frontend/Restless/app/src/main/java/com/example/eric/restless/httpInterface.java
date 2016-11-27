package com.example.eric.restless;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.test.espresso.core.deps.guava.io.Files;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
            connection.setRequestProperty("Connection","close");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);
            Log.i("here","GOT");
            if(json!=null) {
                connection.setRequestProperty("Content-Type", "application/json");
                //connection.setRequestProperty("Content-length", json.toString().getBytes().length + "");
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.toString().getBytes("UTF-8"));
                //Log.i("Content: ", connection.getContent().toString());
                outputStream.close();
            }
            Log.i("here","GOT");
            //connection.connect();
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
            connection.disconnect();

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
    void post_image(String path,String url2){

        File file = new File(path);
        File temp = file;
        file.setReadable(true);
        file.setWritable(true);
        Log.i("file path: ",file.getAbsolutePath());


        try {
            URL url = new URL(url2);

            byte[] array = Files.toByteArray(temp);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection","close");
            connection.setRequestProperty("Content-Type","application/octet-stream");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            OutputStream outputStream = connection.getOutputStream();


            outputStream.write(array);
            outputStream.close();
            int status = connection.getResponseCode();

            Log.i("HTTP Client", "HTTP status code : " + status);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


