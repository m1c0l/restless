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

import org.w3c.dom.Text;

import static java.lang.Math.abs;

public class devSwipe extends AppCompatActivity {


    private GestureDetectorCompat gdetect;
    private ImageView profile_pic, profile_pic_reserve;
    private TextView body1,body2,body3, body1_reserve, body2_reserve, body3_reserve;
    private ViewFlipper textflip, profileflip, textflip_reserve;
    private TextView primary_text, reserve_text;
    private ViewAssociation first_page, second_page;
    String[] a= {"Populate field with GET API Overview 0","Populate field with GET API Skills  1","Populate field with GET API background/past projects 2"};
    String[] b={"reserve 1", "reserve 2", "reserve 3"};
    int a_pos = 0;
    String output;
    public class Container{
        public Container(ViewAssociation a, ViewAssociation b){
            curr=a;
            following=b;
        }
        public ViewAssociation curr;
        public ViewAssociation following;
    }
    public class ViewAssociation{
        public ViewAssociation(ViewFlipper text, TextView body_1, TextView body_2, TextView body_3, ImageView picture){
            textswitcher = text;
            one=body_1;
            two=body_2;
            three=body_3;
            this.picture=picture;
        }
        public void update(String body_1, String body_2, String body_3){
            one.setText(body_1);
            two.setText(body_2);
            three.setText(body_3);
        }
        public TextView one,two,three;
        public ViewFlipper textswitcher;
        public ImageView picture;
    }
    public class HTTPThread implements Runnable {

        public HTTPThread(Container a) {
            // store parameter for later user
            first = a.curr;
            second = a.following;
        }

        public void run() {
            String one,two,three;


            one=(String)first.one.getText();
            two=(String) first.two.getText();
            three=(String) first.three.getText();
            first.update((String)second.one.getText(),(String)second.two.getText(),(String)second.three.getText());
            second.update(one,two,three);

        }
        private ViewAssociation first;
        private ViewAssociation second;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_swipe);

        //populate stack with GET query
        profileflip= (ViewFlipper) findViewById(R.id.view_flipper_main);
        profile_pic = (ImageView) findViewById(R.id.dev_profile_pic1);
        profile_pic_reserve = (ImageView) findViewById(R.id.dev_profile_pic2);
        textflip= (ViewFlipper) findViewById(R.id.textFlipper1);
        textflip_reserve = (ViewFlipper) findViewById(R.id.textFlipper2);
        primary_text = (TextView) findViewById(R.id.TextFieldTitle1);
        reserve_text = (TextView) findViewById(R.id.TextFieldTitle2);
        //populate page with stack information
        body1 = (TextView) findViewById(R.id.Text1);
        body2= (TextView) findViewById(R.id.Text2);
        body3= (TextView) findViewById(R.id.Text3);
        body1_reserve= (TextView) findViewById(R.id.Text4);
        body2_reserve= (TextView) findViewById(R.id.Text5);
        body3_reserve= (TextView) findViewById(R.id.Text6);
        body1.setText(a[0]);
        body2.setText(a[1]);
        body3.setText(a[2]);
        body1_reserve.setText(b[0]);
        body2_reserve.setText(b[1]);
        body3_reserve.setText(b[2]);

        first_page= new ViewAssociation(textflip,body1,body2,body3,profile_pic);
        second_page = new ViewAssociation(textflip_reserve,body1_reserve,body2_reserve,body3_reserve,profile_pic_reserve);




        gdetect = new GestureDetectorCompat(this, new GestureListener());

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
                ViewFlipper a = (profileflip.getDisplayedChild()==0) ? textflip : textflip_reserve;
                if(horizontal > 0){
                    a.setInAnimation(devSwipe.this, R.anim.in_from_left);
                    a.setOutAnimation(devSwipe.this, R.anim.out_to_right);
                    a.showPrevious();
                }
                else {

                    a.setInAnimation(devSwipe.this, R.anim.in_from_right);
                    a.setOutAnimation(devSwipe.this, R.anim.out_to_left);
                    a.showNext();
                }
            }

            if(vertical_move){
                output= (profileflip.getDisplayedChild()== 0)? "I want you":"I will send you a rejection letter in 3 months";
                //switch to next guy!
                Container a;
                if(profileflip.getDisplayedChild()==0)
                    a=new Container(first_page,second_page);
                else
                    a=new Container(second_page,first_page);
                HTTPThread thread_demo = new HTTPThread(a);
                Thread updater;
                if(vertical < 0) {


                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_bot);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_top);
                    (updater = new Thread(thread_demo)).start();

                    profileflip.showNext();

                }
                else{

                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_top);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_bot);
                    (updater = new Thread(thread_demo)).start();

                    profileflip.showNext();

                }
                while(updater.isAlive())
                    continue;

                Toast.makeText(getApplicationContext(),output, Toast.LENGTH_SHORT).show();
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