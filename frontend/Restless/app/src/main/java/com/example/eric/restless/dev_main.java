package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class dev_main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button swipe=(Button) findViewById(R.id.swipe_button);
        Button edit=(Button) findViewById(R.id.edit_button);
        Button matches=(Button) findViewById(R.id.matches_button);
        setContentView(R.layout.activity_dev_main);
    }

    public void swipe_transition(View v){
        Intent transfer=new Intent(dev_main.this,devSwipe.class);
        startActivity(transfer);
    }
    public void matches_transition(View v){
        Intent transfer=new Intent(dev_main.this,devMatches.class);
        startActivity(transfer);
    }
    public void profile_transition(View v){
        Intent transfer=new Intent(dev_main.this, devEdit.class);
        startActivity(transfer);
    }
}
