package com.example.eric.restless;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class devSwipe extends AppCompatActivity {
    private Bitmap src;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //populate stack with GET query
        ImageView profile_pic = (ImageView) findViewById(R.id.dev_profile_pic);
        //populate page with stack information
        setContentView(R.layout.activity_dev_swipe);
    }

}
