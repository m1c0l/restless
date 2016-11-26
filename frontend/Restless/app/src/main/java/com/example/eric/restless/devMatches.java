package com.example.eric.restless;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import static java.lang.Math.abs;

public class devMatches extends AppCompatActivity {

    private ViewFlipper page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        page= (ViewFlipper) findViewById(R.id.view_flipper_matches);
    }

    public void switchPage(View v){
        page.setInAnimation(devMatches.this, R.anim.in_from_right);
        page.setOutAnimation(devMatches.this, R.anim.out_to_left);
        page.showNext();
    }
}
