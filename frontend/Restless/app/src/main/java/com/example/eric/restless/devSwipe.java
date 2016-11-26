package com.example.eric.restless;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.w3c.dom.Text;

import static java.lang.Math.abs;

public class devSwipe extends AppCompatActivity {


    private GestureDetectorCompat gdetect;
    private PopupWindow match_popup;
    private LayoutInflater inflater;
    private ImageView profile_pic, profile_pic_reserve, match_pic;
    private TextView body1,body2,body3, body1_reserve, body2_reserve, body3_reserve;
    private RelativeLayout relativeLayout;
    private ViewFlipper textflip, profileflip, textflip_reserve;
    private TextView primary_text, reserve_text, match_text;
    private ViewAssociation first_page, second_page;
    private ViewGroup container;
    private Point dimensions;
    String[] a= {"Populate field with GET API Overview 0","Populate field with GET API Skills  1","Populate field with GET API background/past projects 2"};
    String[] b={"reserve 1", "reserve 2", "reserve 3"};
    int a_pos = 0;


    public class Container{
        public Container(ViewAssociation a, ViewAssociation b, Boolean swipe_up){
            curr=a;
            following=b;
            swipeDir=swipe_up;
        }
        public ViewAssociation curr;
        public ViewAssociation following;
        public boolean swipeDir;
    }
    public class ViewAssociation{
        public ViewAssociation(ViewFlipper text, TextView body_1, TextView body_2, TextView body_3, ImageView picture, TextView name){
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
        public TextView one,two,three, name;
        public ViewFlipper textswitcher;
        public ImageView picture;
    }

    public class HTTPThread implements Runnable {

        public HTTPThread(Container a) {
            // store parameter for later user
            first = a.curr;
            second = a.following;
            swipe_up = a.swipeDir;
        }
        @Override
        public void run() {
            //post request
            //if match, set match_pic and match_text
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String one, two, three;
                    one = (String) first.one.getText();
                    two = (String) first.two.getText();
                    three = (String) first.three.getText();
                    first.update((String) second.one.getText(), (String) second.two.getText(), (String) second.three.getText());
                    second.update(one, two, three);
                    Boolean matched = swipe_up;
                    if(matched){
                        //set up and display popup
                        match_text = (TextView)container.findViewById(R.id.with);
                        match_text.setText("boogers");
                        match_popup.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,0,0);
                    }
                }
            });

            //fetch next guy right here
        }
        private ViewAssociation first;
        private ViewAssociation second;
        private Boolean swipe_up;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_swipe);
        Display display = getWindowManager().getDefaultDisplay();
        dimensions = new Point();
        display.getSize(dimensions);
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
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_dev_swipe);
        first_page= new ViewAssociation(textflip,body1,body2,body3,profile_pic, primary_text);
        second_page = new ViewAssociation(textflip_reserve,body1_reserve,body2_reserve,body3_reserve,profile_pic_reserve, reserve_text);
        inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        container = (ViewGroup) inflater.inflate(R.layout.match_popup,null);

        match_popup = new PopupWindow(container, dimensions.x,dimensions.y,true);
        gdetect = new GestureDetectorCompat(this, new GestureListener());
        container.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                match_popup.dismiss();
                return true;
            }
        });

    }
    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
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

            else if(vertical_move){
                //switch to next guy!
                Container a;
                if(profileflip.getDisplayedChild()==0)
                    a=new Container(first_page,second_page,vertical < 0);
                else
                    a=new Container(second_page,first_page, vertical < 0);
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

                //reset text boxes to initial
                if(profileflip.getDisplayedChild()==0)
                    first_page.textswitcher.setDisplayedChild(0);
                else
                    second_page.textswitcher.setDisplayedChild(0);
                textflip.clearAnimation();

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

    public void edit_transfer(View v){
        Intent transfer=new Intent(devSwipe.this,editProfileMainScreen.class);
        startActivity(transfer);
    }
    public void match_transfer(View v){
        Intent transfer=new Intent(devSwipe.this,devMatches.class);
        startActivity(transfer);
    }
}