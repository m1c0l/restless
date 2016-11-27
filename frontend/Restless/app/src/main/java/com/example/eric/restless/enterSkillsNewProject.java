package com.example.eric.restless;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        project.newProjectToServer();
        Intent transfer=new Intent(enterSkillsNewProject.this,PMActivity.class);
        startActivity(transfer);
    }
}
