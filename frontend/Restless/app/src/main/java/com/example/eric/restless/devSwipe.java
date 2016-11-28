package com.example.eric.restless;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Stack;

import static java.lang.Math.abs;

public class devSwipe extends AppCompatActivity {


    protected GestureDetectorCompat gdetect;
    protected PopupWindow match_popup;
    protected LayoutInflater inflater;
    protected ImageView profile_pic, profile_pic_reserve, matcher_pic, matchee_pic;
    protected TextView body1,body2,body3, body1_reserve, body2_reserve, body3_reserve;
    protected RelativeLayout relativeLayout;
    protected ViewFlipper textflip, profileflip, textflip_reserve;
    protected TextView primary_text, reserve_text, matchee_text, matcher_text;
    protected ViewAssociation first_page, second_page;
    protected ViewGroup container;
    protected Point dimensions;
    String[] a= {"Populate field with GET API Overview 0","Populate field with GET API Skills  1","Populate field with GET API background/past projects 2"};
    String[] b={"reserve 1", "reserve 2", "reserve 3"};
    Stack<Integer> user_stack=new Stack<Integer>();
    int a_pos = 0;


    protected class Container{
        public Container(ViewAssociation a, ViewAssociation b, Boolean swipe_up){
            curr=a;
            following=b;
            swipeDir=swipe_up;
        }
        public ViewAssociation curr;
        public ViewAssociation following;
        public boolean swipeDir;
    }
    protected class ViewAssociation{
        public ViewAssociation(ViewFlipper text, TextView body_1, TextView body_2, TextView body_3, ImageView picture, TextView name, Integer id){
            textswitcher = text;
            one=body_1;
            two=body_2;
            three=body_3;
            this.picture=picture;
            this.name = name;
        }
        public void update(String body_1, String body_2, String body_3){

            one.setText(body_1);
            two.setText(body_2);
            three.setText(body_3);
        }
        public TextView one,two,three, name;
        public int id;
        public ViewFlipper textswitcher;
        public ImageView picture;
    }
    protected void runSwipe(Container a){
        final ViewAssociation first=a.curr;
        Boolean swipe_up=a.swipeDir;
        final httpInterface requester=new httpInterface();
        final Boolean matched[] = new Boolean[1];
        matched[0] = false;
        Thread thread;
        int i = swipe_up ? 1 : 0;
        final String url = new String("http://159.203.243.194/api/swipe/user/"+String.valueOf(User.getUser().getId())+"/"+String.valueOf(first.id)+"/"+String.valueOf(i));
        thread = new Thread (new Runnable() {
            public void run() {
                JSONObject b=requester.request("GET", null, url);
                try {
                    if(b!=null && b.get("id")!=null)
                        matched[0] = (Integer)b.get("id") > 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (matched[0]) {
                            //set up and display popup
                            matchee_text = (TextView) container.findViewById(R.id.with);
                            matcher_text = (TextView) container.findViewById(R.id.matcher);
                            matchee_text.setText(first.name.getText().toString());
                            matcher_text.setText("boogers");
                            matchee_pic = (ImageView) container.findViewById(R.id.matcheePic);
                            matchee_pic.setImageDrawable(first.picture.getDrawable());
                            match_popup.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 0, 0);
                        }
                    }
                });
            }
        });
        thread.start();


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!user_stack.empty())
            user_stack.pop();
        if(fetch_and_update(first)==null){
            Toast.makeText(this, "No profiles found", Toast.LENGTH_SHORT).show();
            Intent transfer = new Intent(devSwipe.this,DevPMSelectionActivity.class);
            startActivity(transfer);
        }
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
        body1 = (TextView) findViewById(R.id.Text1);
        body2= (TextView) findViewById(R.id.Text2);
        body3= (TextView) findViewById(R.id.Text3);
        body1_reserve= (TextView) findViewById(R.id.Text4);
        body2_reserve= (TextView) findViewById(R.id.Text5);
        body3_reserve= (TextView) findViewById(R.id.Text6);


        populate_stack();
        //fetch profile
        if(user_stack.empty()) {
            Toast.makeText(this, "No profiles found", Toast.LENGTH_SHORT).show();
            Intent transfer = new Intent(devSwipe.this,DevPMSelectionActivity.class);
            startActivity(transfer);
            return;
        }
        first_page= new ViewAssociation(textflip,body1,body2,body3,profile_pic, primary_text,-1);
        second_page = new ViewAssociation(textflip_reserve,body1_reserve,body2_reserve,body3_reserve,profile_pic_reserve, reserve_text,-1);

        Log.i("hello", "reached");
        Thread first = fetch_and_update(first_page);
        user_stack.pop();
        Thread second = fetch_and_update(second_page);

        try {
            first.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(second!=null)
                second.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        body1.setText(a[0]);
        body2.setText(a[1]);
        body3.setText(a[2]);
        //fetch profile
        body1_reserve.setText(b[0]);
        body2_reserve.setText(b[1]);
        body3_reserve.setText(b[2]);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_dev_swipe);

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
    protected void populate_stack(){
        try{
            final String url = new String("http://159.203.243.194/api/stack/user/"+User.getUser().getId());
            //final JSONObject obj = new JSONObject();
            final httpInterface requester = new httpInterface();
            //populate obj
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {

                        if(b!=null) {
                            JSONArray stack_vals = (JSONArray)b.get("stack");

                            for (int i = 0; i < stack_vals.length(); i++) {
                                user_stack.push(stack_vals.getInt(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } 

                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    protected Thread fetch_and_update(final ViewAssociation viewer){
        Integer top;


        synchronized(user_stack){
            if(user_stack.empty()) {
                Toast.makeText(this, "No profiles found", Toast.LENGTH_SHORT).show();

                return null;
            }
            //top=user_stack.pop();
            top = user_stack.peek();
            Log.i("popped object: ", String.valueOf(top));
        }
        viewer.id = top;

        JSONObject obj[] = new JSONObject[1];
        final httpInterface requester = new httpInterface();
        Thread thread;

        final String url = new String("http://159.203.243.194/api/get/project/"+top.toString());


        //Log.i("Signin: ",requestObj.toString());
            //Toast.makeText(getApplicationContext(),requestObj.toString(),Toast.LENGTH_LONG).show();
        thread=new Thread(new Runnable() {
            public void run() {

                try {
                    final JSONObject b=(JSONObject)((JSONArray)requester.request("GET", null, url).get("results")).get(0);
                    String skill_desc=new String();
                    JSONArray skills = (JSONArray) b.get("skills_needed");


                    for(int i=0; i < skills.length(); i++){
                        skill_desc+=skills.getString(i);
                        skill_desc+=((skills.length()!=i) ? " " : ".");
                    }
                    final String skills_desc = skill_desc;
                    final JSONObject pm = (JSONObject) ((JSONArray) requester.request("GET",null,"http://159.203.243.194/api/get/user/"+String.valueOf((Integer)b.get("pm_id"))).get("results")).get(0);

                    runOnUiThread(new Runnable() {
                        @Override

                        public void run() {
                            try {

                                viewer.update((String)b.get("description"),skills_desc, (String)pm.get("bio"));

                                viewer.name.setText((String)b.get("title"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    URL url = new URL("http://159.203.243.194/api/img/get/project/"+String.valueOf((Integer)b.get("id")));
                    final Bitmap bmp;
                    try {
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        if(bmp!=null)
                            viewer.picture.setImageBitmap(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();


        return thread;
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
                //Thread updater;
                runSwipe(a);
                if(vertical < 0) {


                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_bot);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_top);
                    //(updater = new Thread(thread_demo)).start();

                    profileflip.showNext();

                }
                else{

                    profileflip.setInAnimation(devSwipe.this,R.anim.in_from_top);
                    profileflip.setOutAnimation(devSwipe.this,R.anim.out_to_bot);
                    //(updater = new Thread(thread_demo)).start();

                    profileflip.showNext();

                }



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
        /*
        Intent transfer=new Intent(devSwipe.this,profileDisplay.class);
        startActivity(transfer);
        */
    }
}