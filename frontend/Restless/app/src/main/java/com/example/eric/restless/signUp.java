package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class signUp extends AppCompatActivity {
    EditText email,username, phone, wage,city,name,github;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button next = (Button) findViewById(R.id.Continue);
        setContentView(R.layout.activity_sign_up);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        wage = (EditText) findViewById(R.id.wageVal);
        name= (EditText) findViewById(R.id.firstlastname);
        wage = (EditText) findViewById(R.id.wageVal);
        city = (EditText) findViewById(R.id.city);
        github = (EditText) findViewById(R.id.github);
    }
    public void next(View v) throws JSONException {
        EditText pass1= (EditText) findViewById(R.id.password);
        EditText pass2= (EditText) findViewById(R.id.passwordconfirm);
        if(!pass1.getText().toString().equals(pass2.getText().toString())) {
            // tell user that passwords don't match!
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean successfulNewAcc= true;
        final int[] a =new int[1];
        a[0] = -1;
        try{
            final String url = new String("http://159.203.243.194/api/new_user/");
            final JSONObject obj = new JSONObject();
            final httpInterface requester = new httpInterface();

            obj.put("username", username.getText().toString());
            obj.put("password",pass1.getText().toString());
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", obj, url);
                    try {
                        if(b!=null)
                         a[0] = (Integer)(b.get("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(a[0]==-1){
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            return;
        }
        User.getUser().setId(a[0]);

        final String url = new String("http://159.203.243.194/api/update/user/" + String.valueOf(a[0]));
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


        //post


        Intent transfer=new Intent(signUp.this,enterSkillsNewAccount.class);
        startActivity(transfer);
    }
}
