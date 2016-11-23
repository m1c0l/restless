package com.example.eric.restless;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import static java.lang.Math.abs;

public class devSwipe extends AppCompatActivity {


    private GestureDetectorCompat gdetect;
    private ImageView profile_pic;
    private TextView body1,body2,body3;
    private ViewFlipper textflip, profileflip;
    String[] a= {"Populate field with GET API Overview 0","Populate field with GET API Skills  1","Populate field with GET API background/past projects 2"};
    int a_pos = 0;
    String output;
    public class HTTPThread implements Runnable {

        public HTTPThread(int a) {
            // store parameter for later user
            id=a;
        }

        public void run() {
            output= (id == 0)? "I want you":"I will send you a rejection letter in 3 months";

        }
        private int id;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_swipe);

        //populate stack with GET query
        profileflip= (ViewFlipper) findViewById(R.id.view_flipper_main);
        profile_pic = (ImageView) findViewById(R.id.dev_profile_pic);
        textflip= (ViewFlipper) findViewById(R.id.textFlipper);
        //populate page with stack information
        body1 = (TextView) findViewById(R.id.Text1);
        body2= (TextView) findViewById(R.id.Text2);
        body3= (TextView) findViewById(R.id.Text3);
        gdetect = new GestureDetectorCompat(this, new GestureListener());
        body1.setText(a[0]);
        body2.setText(a[1]);
        body3.setText(a[2]);

    }
    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private float minFling = 50;
        private float minVelocity = 50;
        @Override
        public boolean onDown(MotionEvent event){
            Context s=getApplicationContext();
            //Toast.makeText(s,"pushed",Toast.LENGTH_SHORT).show();

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
            float horizontal=event2.getX()-event1.getX();
            float vertical=event2.getY()-event1.getY();
            if(!onSingleTapConfirmed(event1))
                return false;
            boolean horizontal_move = ((abs(horizontal) > minFling) && abs(velocityX)>minVelocity && abs(horizontal)>abs(vertical)*1.5 && abs(velocityX)>abs(velocityY)*1.5);
            boolean vertical_move = (abs(vertical)>minFling && abs(velocityY)>minVelocity && abs(vertical)>abs(horizontal)*1.5 && abs(velocityY)>abs(velocityX)*1.5);

            if(horizontal_move){

                if(horizontal > 0){
                    textflip.setInAnimation(devSwipe.this, R.anim.in_from_left);
                    textflip.setOutAnimation(devSwipe.this, R.anim.out_to_right);
                    textflip.showPrevious();
                }
                else {

                    textflip.setInAnimation(devSwipe.this, R.anim.in_from_right);
                    textflip.setOutAnimation(devSwipe.this, R.anim.out_to_left);
                    textflip.showNext();
                }
            }

            if(vertical_move){

                //switch to next guy!
                if(vertical < 0) {
                    HTTPThread thread_demo= new HTTPThread(0);
                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_bot);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_top);
                    new Thread(thread_demo).start();
                    Toast.makeText(getApplicationContext(),output, Toast.LENGTH_SHORT).show();
                    profileflip.showNext();
                }
                else{
                    HTTPThread thread_demo = new HTTPThread(1);
                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_top);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_bot);
                    new Thread(thread_demo).start();
                    Toast.makeText(getApplicationContext(),output, Toast.LENGTH_SHORT).show();
                    profileflip.showNext();
                }
                textflip.clearAnimation();
                textflip.setDisplayedChild(0);
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


}