package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class changePassword extends AppCompatActivity {
    EditText curr,newPass,confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        curr = (EditText) findViewById(R.id.currentPass);
        newPass = (EditText) findViewById(R.id.newPass);
        confirm = (EditText) findViewById(R.id.confirmPass);
    }

    public void moveBack(View v){
        Intent transfer = new Intent(changePassword.this,editProfileMainScreen.class);
        startActivity(transfer);
    }
    public void checkValid(View v) throws JSONException {
        //ask server if curr and user name gives user's id.

        if(!newPass.getText().toString().equals(confirm.getText().toString())){
            Toast.makeText(getApplicationContext(),"New passwords do not match",Toast.LENGTH_SHORT).show();
            return;
        }
        // post to server password change
        final String url = new String("http://159.203.243.194/api/update/user/" + String.valueOf(User.getUser().getId()));
        final JSONObject obj = new JSONObject();
        final httpInterface requester = new httpInterface();
        obj.put("password",newPass.getText().toString());
        Thread thread=new Thread(new Runnable() {
            public void run() {
                JSONObject b=requester.request("POST", obj, url);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
        moveBack(v);
    }
}
