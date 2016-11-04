package com.example.eric.restless;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;

public class devSwipe extends AppCompatActivity {


    private GestureDetectorCompat gdetect;

    String[] a= {"Populate field with GET API Overview 0","Populate field with GET API Skills  1","Populate field with GET API background/past projects 2"};
    int a_pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_swipe);

        //populate stack with GET query

        ImageView profile_pic = (ImageView) findViewById(R.id.dev_profile_pic);
        //populate page with stack information
        TextView body = (TextView) findViewById(R.id.BioBody);
        gdetect = new GestureDetectorCompat(this, new GestureListener());
        body.setText(a[0]);
    }
    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private float minFling = 100;
        private float minVelocity = 100;
        @Override
        public boolean onDown(MotionEvent event){
            Context s=getApplicationContext();
            //Toast.makeText(s,"pushed",Toast.LENGTH_SHORT).show();

            return true;
        }
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float horizontal=event2.getX()-event1.getX();
            float vertical=event2.getY()-event1.getY();

            boolean horizontal_move = ((abs(horizontal) > minFling) && abs(velocityX)>minVelocity && abs(horizontal)>abs(vertical) && abs(velocityX)>abs(velocityY));
            boolean vertical_move = (abs(vertical)>minFling && abs(velocityY)>minVelocity && abs(vertical)>abs(horizontal) && abs(velocityY)>abs(velocityX));

            if(horizontal_move){
                a_pos = (horizontal > 0)? a_pos-1:a_pos+1;
                if(a_pos==-1)
                    a_pos = 2;
                a_pos=a_pos%3;
                ((TextView) findViewById(R.id.BioBody)).setText(a[a_pos]);
            }

            if(vertical_move){
                String match= (vertical < 0)? "I want you":"I will send you a rejection letter in 3 months";
                Toast.makeText(getApplicationContext(),match,Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gdetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}