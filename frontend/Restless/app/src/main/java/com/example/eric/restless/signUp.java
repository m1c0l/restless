package com.example.eric.restless;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class signUp extends AppCompatActivity {
    EditText email,username, phone, wage,city,name,github;
    ImageButton image;
    String path;
    Uri currImageURI= null;
    File image_file = null;
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
        image = (ImageButton) findViewById(R.id.imageButton);
        image.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
// To open up a gallery browser
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            }

        });
    }
    public void next(View v) throws JSONException {
        EditText pass1= (EditText) findViewById(R.id.password);
        EditText pass2= (EditText) findViewById(R.id.passwordconfirm);
        if(!pass1.getText().toString().equals(pass2.getText().toString()) && pass1.getText().length()!=0) {
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
        if(email.getText().length()!=0)
            obj.put("email",email.getText().toString());
        if(phone.getText().length()!=0)
            obj.put("phone", phone.getText().toString());
        if(wage.getText().length()!=0)
            obj.put("desired_salary",Integer.valueOf(wage.getText().toString()));
        if(github.getText().length()!=0)
            obj.put("github_link", github.getText().toString());
        if(city.getText().length()!=0)
            obj.put("city",city.getText().toString());
        if(name.getText().length()!=0)
            obj.put("first_name",name.getText().toString());
        Thread thread=new Thread(new Runnable() {
            public void run() {
                JSONObject b=requester.request("POST", obj, url);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e)
        {e.printStackTrace();}
        if(image_file!=null) {
            //final httpInterface requester2 = new httpInterface();

            final String url2 = new String("http://159.203.243.194/api/img/upload/user/" + User.getUser().getId());
            final httpInterface requester1 = new httpInterface();

            Thread thread1 = new Thread(new Runnable() {
                public void run() {
                    requestPermission();
                    requester1.post_image(path,url2);
                }
            });
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //post


        Intent transfer=new Intent(signUp.this,enterSkillsNewAccount.class);
        startActivity(transfer);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // currImageURI is the global variable I’m using to hold the content:
                currImageURI = data.getData();
                path = (getRealPathFromURI(currImageURI));
                Log.i("path!",path);
                image_file = new File(path);
                Log.i("image file",String.valueOf(image_file.getTotalSpace()));



            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.getApplicationContext(), contentUri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(signUp.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(signUp.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(signUp.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(signUp.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
