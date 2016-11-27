package com.example.eric.restless;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class createProjectActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private projectUnit project = null;
    static final String PROJECT = "PROJECT_DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
    }

    public void selectImage(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.projectImage);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

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

        //transfering to next project
        transfer.putExtra("TEMP_PROJECT", project);
        startActivity(transfer);
    }
}
