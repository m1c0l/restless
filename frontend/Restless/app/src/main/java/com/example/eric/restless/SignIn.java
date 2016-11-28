package com.example.eric.restless;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignIn extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private HttpURLConnection connection;
    EditText userText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button signIn = (Button) findViewById(R.id.signIn);
        userText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    public void signIn(View v) throws InterruptedException {

        //make login request!
        final httpInterface requester = new httpInterface();
        final int[] a = new int[1];
        a[0] = -1;
        try {
            System.setProperty("http.keepAlive", "false");


            final String url = new String("http://159.203.243.194/api/login/");
            final JSONObject requestObj = new JSONObject();

            requestObj.put("username",userText.getText().toString());
            requestObj.put("password",passwordText.getText().toString());
            Log.i("Signin: ",requestObj.toString());
            //Toast.makeText(getApplicationContext(),requestObj.toString(),Toast.LENGTH_LONG).show();
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", requestObj, url);
                    try {
                        if(b!=null)
                            a[0] = (Integer)(b.get("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(a[0]!=-1){
            //setting global user object
            Log.i("user id: ", String.valueOf(a[0]));
            User.getUser().setId(a[0]);
            //final JSONObject requestObj = new JSONObject();

            Thread thread1=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, "http://159.203.243.194/api/get/user/"+User.getUser().getId());
                    try {
                        User.getUser().setName((String)((JSONObject)(((JSONArray)b.get("results")).get(0))).get("first_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread1.start();
            thread1.join();


            Thread thread2=new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL("http://159.203.243.194/api/img/get/user/"+String.valueOf((Integer) User.getUser().getId()));
                        InputStream stream = (InputStream) url.getContent();
                        Bitmap bmp = BitmapFactory.decodeStream(stream);

                        if (bmp != null)
                            User.getUser().setImage(bmp);
                        else
                            User.getUser().setImage(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.default_photo));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            );
            thread2.start();
            thread2.join();

            Intent transfer=new Intent(SignIn.this,DevPMSelectionActivity.class);
            startActivity(transfer);
        }
        else
            Toast.makeText(getApplicationContext(),"Invalid username/password combination",Toast.LENGTH_SHORT).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

}
