package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class editProfileMainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_main_screen);
    }


    public void changePassword(View v){
        Intent transfer = new Intent(editProfileMainScreen.this, DevPMSelectionActivity.class);
        startActivity(transfer);
    }

    public void updateBio(View v){
        Intent transfer = new Intent(editProfileMainScreen.this, DevPMSelectionActivity.class);
        startActivity(transfer);
    }

    public void updateSkills(View v){
        Intent transfer = new Intent(editProfileMainScreen.this, DevPMSelectionActivity.class);
        startActivity(transfer);
    }
}


