package com.project.smartvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Admin_Election_Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__election__launcher);


        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Admin_Election_Launcher_Fragment()).commit();
        }

    }
}