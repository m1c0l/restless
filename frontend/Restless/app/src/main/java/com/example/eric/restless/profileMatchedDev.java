package com.example.eric.restless;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Math.abs;

/**
 * Created by Eric on 11/25/2016.
 */

public class profileMatchedDev extends AppCompatActivity {
    protected TextView body1,body2,body3,body4, title;
    private ImageView profile_pic;
    private GestureDetectorCompat gdetect;
    private ViewFlipper textflipper;

    private projectUnit p;
    private developerUnit d;
    public profileMatchedDev() {
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //getting data
        Bundle b = getIntent().getExtras();
        d = b.getParcelable("TEMP_USER");
        p = b.getParcelable("TEMP_PROJECT");


        setContentView(R.layout.profile_display_manage);

        body1=(TextView )findViewById(R.id.Text1);
        body2=(TextView )findViewById(R.id.Text2);
        body3=(TextView )findViewById(R.id.Text3);
        body4=(TextView) findViewById(R.id.Text4);
        title=(TextView )findViewById(R.id.TextFieldTitle1);
        profile_pic = (ImageView) findViewById(R.id.dev_profile_pic1);
        Thread thread= new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://159.203.243.194/api/img/get/user/" + String.valueOf(d.getId()));
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

        textflipper = (ViewFlipper) findViewById(R.id.textFlipper1);
        //populate by polling website ?
        gdetect = new GestureDetectorCompat(this, new GestureListener());
        setText();
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private float minFling = 50;
        private float minVelocity = 50;
        @Override
        public boolean onDown(MotionEvent event){

            return true;
        }
        @Override
        public void onLongPress(MotionEvent event){
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            return false;
        }
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float horizontal = event2.getX() - event1.getX();
            float vertical=event2.getY()-event1.getY();

            boolean horizontal_move = ((abs(horizontal) > minFling) && abs(velocityX)>minVelocity && abs(horizontal)>abs(vertical)*1.5 && abs(velocityX)>abs(velocityY)*1.5);

            if(horizontal_move) {

                if (horizontal > 0) {
                    textflipper.setInAnimation(profileMatchedDev.this, R.anim.in_from_left);
                    textflipper.setOutAnimation(profileMatchedDev.this, R.anim.out_to_right);
                    textflipper.showPrevious();
                } else {

                    textflipper.setInAnimation(profileMatchedDev.this, R.anim.in_from_right);
                    textflipper.setOutAnimation(profileMatchedDev.this, R.anim.out_to_left);
                    textflipper.showNext();
                }
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent event){
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            return true;
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gdetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void setText(){

        title.setText(d.getName());
        body1.setText(d.getBody1());
        body2.setText(d.getBody2());
        body3.setText(d.getBody3());
        body4.setText(d.getBody4());

    }
    public void onConfirm(View v){
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/matches/accept/"
            + d.getId() + "/" + p.getId());

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                }
            });
            thread.start();
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent transfer=new Intent(profileMatchedDev.this, manageMatchesPM.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);
    }
    public void onDelete(View v){
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/matches/decline/"
                    + d.getId() + "/" + p.getId());

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                }
            });
            thread.start();
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent transfer=new Intent(profileMatchedDev.this, manageMatchesPM.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);
    }
    public void back(View v){
        Intent transfer=new Intent(profileMatchedDev.this, manageMatchesPM.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);
    }

}