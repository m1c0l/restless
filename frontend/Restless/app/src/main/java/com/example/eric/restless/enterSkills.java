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
import android.widget.Toast;

import java.util.ArrayList;

public abstract class enterSkills extends AppCompatActivity {
    ListView list;
    CustomSkillAdapter adapter;
    public enterSkills customListView = null;
    public ArrayList<skillUnit> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_skills);
        customListView = this;

        Resources res = getResources();
        list = (ListView) findViewById(R.id.skill_list);

        //adding data?
        adapter = new CustomSkillAdapter(customListView, CustomListViewValuesArr, res);
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

                //check for input?
                skillUnit temp = new skillUnit(edit.getText().toString(), ratingBar.getRating());
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


    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        skillUnit tempValues = ( skillUnit ) CustomListViewValuesArr.get(mPosition);
        final int mPos = mPosition;
        final Dialog dialog = new Dialog(enterSkills.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_skill_popup);
        Button confirmBtn = (Button)dialog.findViewById(R.id.confirm);
        Button cancelBtn = (Button)dialog.findViewById(R.id.cancel);
        Button deleteBtn = (Button)dialog.findViewById(R.id.delete);

        final EditText edit = (EditText) dialog.findViewById(R.id.skillText);
        edit.setText(tempValues.getName());
        final RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.skillRating);
        ratingBar.setRating(tempValues.getSkillRating());
        dialog.show();
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //check for input?
                (( skillUnit ) CustomListViewValuesArr.get(mPos)).setSkillRating(ratingBar.getRating());
                (( skillUnit ) CustomListViewValuesArr.get(mPos)).setName(edit.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                CustomListViewValuesArr.remove(mPos);
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
    public abstract void finishSkillsList(View v);
}
