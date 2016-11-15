package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DevPMSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_pmselection);
        Button pmButton = (Button) findViewById(R.id.pmButton);
        ImageButton editProfileButton = (ImageButton) findViewById(R.id.editProfile);
        Button devButton = (Button) findViewById(R.id.devButton);

    }

    public void pmMethod(View v){
        Intent transfer=new Intent(DevPMSelectionActivity.this,PMActivity.class);
        startActivity(transfer);
    }
    public void devMethod(View v){
        Intent transfer=new Intent(DevPMSelectionActivity.this,dev_main.class);
        startActivity(transfer);
    }
    public void editProfileMethod(View v){
        Intent transfer=new Intent(DevPMSelectionActivity.this,editProfileActivity.class);
        startActivity(transfer);
    }
}
