package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class enterSkills extends AppCompatActivity {
    String[] skills={"","",""};
    int[] ratings={};
    private ListView skill_list;
    RowAdapter row_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_skills);
        row_adapter=new RowAdapter(this, 1);
        skill_list= (ListView) findViewById(R.id.skill_list);
        skill_list.setAdapter(row_adapter);

        skill_list.setOnItemClickListener(
                        new AdapterView.OnItemClickListener(){
                            public void onItemClick(AdapterView<?> Parent, View view, int position, long id){
                                skills[0]="fuck";
                            }
                        }


        );
    }

    public void returnToMain(View v){
        //push skills and rating array
        Intent transfer=new Intent(enterSkills.this,MainActivity.class);
        startActivity(transfer);
    }
}
