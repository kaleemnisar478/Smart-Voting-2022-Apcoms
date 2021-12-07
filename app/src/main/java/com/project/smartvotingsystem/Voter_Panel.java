package com.project.smartvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Voter_Panel extends AppCompatActivity {
   private String user;
   private Bundle bundle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter__panel);

        user=getIntent().getStringExtra("user");

        System.out.println("In voter panel user :"+user);
        System.out.println("In voter panel user :"+user);
        System.out.println("In voter panel user :"+user);

        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            Voter_Panel_Launcher_Fragment voter_panel_launcher_fragment= new Voter_Panel_Launcher_Fragment();
            bundle=new Bundle();
            bundle.putString("user",user);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, voter_panel_launcher_fragment).commit();
            voter_panel_launcher_fragment.setArguments(bundle);
        }
    }
}