package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class signUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button next = (Button) findViewById(R.id.Continue);
        setContentView(R.layout.activity_sign_up);


    }
    public void next(View v){
        Intent transfer=new Intent(signUp.this,enterSkills.class);
        startActivity(transfer);
    }
}
