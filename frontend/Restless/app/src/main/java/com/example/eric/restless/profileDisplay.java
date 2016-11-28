package com.example.eric.restless;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import static java.lang.Math.abs;

/**
 * Created by Eric on 11/25/2016.
 */

public abstract class profileDisplay extends AppCompatActivity {
    protected TextView body1,body2,body3,body4, title;
    private ImageView profile_pic;
    private GestureDetectorCompat gdetect;
    private ViewFlipper textflipper;

    public profileDisplay() {
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_display_view);

        body1=(TextView )findViewById(R.id.Text1);
        body2=(TextView )findViewById(R.id.Text2);
        body3=(TextView )findViewById(R.id.Text3);
        body4=(TextView) findViewById(R.id.Text4);
        title=(TextView )findViewById(R.id.TextFieldTitle1);
        profile_pic = (ImageView) findViewById(R.id.dev_profile_pic1);
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
                    textflipper.setInAnimation(profileDisplay.this, R.anim.in_from_left);
                    textflipper.setOutAnimation(profileDisplay.this, R.anim.out_to_right);
                    textflipper.showPrevious();
                } else {

                    textflipper.setInAnimation(profileDisplay.this, R.anim.in_from_right);
                    textflipper.setOutAnimation(profileDisplay.this, R.anim.out_to_left);
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
        public abstract void setText();

}
