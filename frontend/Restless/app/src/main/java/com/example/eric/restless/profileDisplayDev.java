package com.example.eric.restless;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by minh on 11/27/16.
 */

public class profileDisplayDev extends profileDisplay{
    public void back(View v){
        Intent transfer = new Intent(profileDisplayDev.this, PMActivity.class);
        startActivity(transfer);
    }
    public void setText(){
        developerUnit d;
        Bundle b = getIntent().getExtras();
        d = b.getParcelable("TEMP_USER");
        final Integer a= d.getId();
        if (d != null) {
            title.setText(d.getName());
            body1.setText(d.getBody1());
            body2.setText(d.getBody2());
            body3.setText(d.getBody3());
            body4.setText(d.getBody4());
        }
        Thread thread=new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://159.203.243.194/api/img/get/user/" + String.valueOf(a));
                    Log.i("help", url.toString());
                    final Bitmap bmp[] = new Bitmap[1];
                    bmp[0] = null;
                    bmp[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override

                        public void run() {
                            if (bmp[0] != null && bmp[0].getByteCount() > 10000)
                                profile_pic.setImageBitmap(bmp[0]);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
