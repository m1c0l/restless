package com.example.eric.restless;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

public class enterSkills extends AppCompatActivity {
    ListView list;
    CustomAdapter adapter;
    public enterSkills customListView = null;
    public ArrayList<SkillModel> CustomListViewValuesArr = new ArrayList<SkillModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_skills);
        customListView = this;

        Resources res = getResources();
        list = (ListView) findViewById(R.id.skill_list);

        //adding data?

        adapter = new CustomAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }

    public void addNewSkill(View v){
        final Dialog dialog = new Dialog(enterSkills.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_skill_popup);
        Button submitBtn = (Button)dialog.findViewById(R.id.submit);
        Button cancelBtn = (Button)dialog.findViewById(R.id.cancel);
        final EditText edit = (EditText) dialog.findViewById(R.id.skillText);
        final RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.skillRating);
        dialog.show();
        submitBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SkillModel temp = new SkillModel(edit.getText().toString(), ratingBar.getRating());
                CustomListViewValuesArr.add(temp);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.dismiss();
            }
        });
    }


    public void returnToMain(View v){
        //push data to server

        //push skills and rating array

        Intent transfer=new Intent(enterSkills.this,MainActivity.class);
        startActivity(transfer);
    }

}
