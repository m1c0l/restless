package com.example.eric.restless;

import android.content.Intent;
import android.view.View;

/**
 * Created by minh on 11/25/16.
 */

public class enterSkillsNewAccount extends enterSkills {

    public void finishSkillsList(View v){
        //push data to server
        //push skills and rating array
        Intent transfer=new Intent(enterSkillsNewAccount.this,DevPMSelectionActivity.class);
        startActivity(transfer);
    }
}
