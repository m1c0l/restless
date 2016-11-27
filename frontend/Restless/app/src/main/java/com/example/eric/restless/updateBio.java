package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class updateBio extends AppCompatActivity {
    private EditText bio, name, email, phone, city, wage, github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bio);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        wage = (EditText) findViewById(R.id.wage);
        name= (EditText) findViewById(R.id.name);
        wage = (EditText) findViewById(R.id.wage);
        city = (EditText) findViewById(R.id.city);
        github = (EditText) findViewById(R.id.github);
        bio = (EditText) findViewById(R.id.bio);
    }

    public void update(View v) throws JSONException {
        final String url = new String("http://159.203.243.194/api/update/user/" + String.valueOf(User.getUser().getId()));
        final JSONObject obj = new JSONObject();
        final httpInterface requester = new httpInterface();
        if(email.getText().toString()!=null)
            obj.put("email",email.getText().toString());
        if(phone.getText().toString()!=null)
            obj.put("phone", phone.getText().toString());
        if(wage.getText().toString()!=null)
            obj.put("desired_salary",Integer.valueOf(wage.getText().toString()));
        if(github.getText().toString()!=null)
            obj.put("github_link", github.getText().toString());
        if(city.getText().toString()!=null)
            obj.put("city",city.getText().toString());
        if(name.getText().toString()!=null)
            obj.put("first_name",name.getText().toString());
        if(bio.getText().toString()!=null)
            obj.put("bio",bio.getText().toString());
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
        moveBack(v);
    }
    public void moveBack(View v){
        Intent transfer= new Intent(updateBio.this,editProfileMainScreen.class);
        startActivity(transfer);
    }
}
