package com.example.eric.restless;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

/**
 * Created by minh on 11/25/16.
 */

public class enterSkillsNewProject extends enterSkills {
    private projectUnit project = null;
    public void finishSkillsList(View v){
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");
        //adding skills for this project
        for ( skillUnit s : CustomListViewValuesArr){
            s.setProject();
            s.setId(project.getId());
            s.pushToServer();
        }
        //new project
        if(!checkPermission())
            requestPermission();
        project.newProjectToServer();
        Intent transfer=new Intent(enterSkillsNewProject.this,PMActivity.class);
        startActivity(transfer);
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(enterSkillsNewProject.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(enterSkillsNewProject.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(enterSkillsNewProject.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(enterSkillsNewProject.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
