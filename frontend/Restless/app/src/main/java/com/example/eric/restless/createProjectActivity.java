package com.example.eric.restless;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class createProjectActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    ImageButton image;
    String path;
    Uri currImageURI= null;
    File image_file = null;
    private projectUnit project = null;
    static final String PROJECT = "PROJECT_DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        image=(ImageButton) findViewById(R.id.imageButton3);
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

    @Override
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




    public void next(View v){
        Intent transfer=new Intent(createProjectActivity.this,enterSkillsNewProject.class);
        //creating project object and passing it on
        //get data from text field
        EditText title = (EditText) findViewById(R.id.projectName);
        EditText description = (EditText) findViewById(R.id.projectDescription);
        EditText income = (EditText) findViewById(R.id.projectIncome);
        project.setTitle(title.getText().toString());
        project.setDescription(description.getText().toString());
        project.setPayRange(Integer.parseInt(income.getText().toString()));
        if(path!=null)
            project.setImage_path(path);
        //transfering to next project
        transfer.putExtra("TEMP_PROJECT", project);
        startActivity(transfer);
    }
}
