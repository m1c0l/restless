package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button next = (Button) findViewById(R.id.Continue);
        setContentView(R.layout.activity_sign_up);


    }
    public void next(View v){
        EditText pass1= (EditText) findViewById(R.id.password);
        EditText pass2= (EditText) findViewById(R.id.passwordconfirm);
        if(pass1.getText().toString()!=pass2.getText().toString()) {
            // tell user that passwords don't match!
            return;
        }
        boolean successfulReq= true;
        if(!successfulReq){
            // tell user the error code from the server!
            return;
        }
        Intent transfer=new Intent(signUp.this,enterSkills.class);
        startActivity(transfer);
    }
}
