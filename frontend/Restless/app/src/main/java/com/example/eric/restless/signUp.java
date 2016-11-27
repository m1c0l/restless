package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class signUp extends AppCompatActivity {
    EditText email,username, phone, wage;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button next = (Button) findViewById(R.id.Continue);
        setContentView(R.layout.activity_sign_up);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.Phone);
        wage = (EditText) findViewById(R.id.wageVal);
    }
    public void next(View v){
        EditText pass1= (EditText) findViewById(R.id.password);
        EditText pass2= (EditText) findViewById(R.id.passwordconfirm);
        if(!pass1.getText().toString().equals(pass2.getText().toString())) {
            // tell user that passwords don't match!
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean successfulNewAcc= true;
        final int[] a =new int[1];
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

        //post


        Intent transfer=new Intent(signUp.this,enterSkillsNewAccount.class);
        startActivity(transfer);
    }
}
