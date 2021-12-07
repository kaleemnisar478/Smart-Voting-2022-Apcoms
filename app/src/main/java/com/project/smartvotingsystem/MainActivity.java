package com.project.smartvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Details.Election_Details;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Election_Details()).commit();
        }
    }
}