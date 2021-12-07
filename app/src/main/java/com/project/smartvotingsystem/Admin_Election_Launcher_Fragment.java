package com.project.smartvotingsystem;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.smartvotingsystem.Admin_Panel.Coming_Admin.Coming;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.CreateElection;
import com.project.smartvotingsystem.Admin_Panel.InProgress_Admin.InProgress;
import com.project.smartvotingsystem.Admin_Panel.Pending_Admin.Pending;
import com.project.smartvotingsystem.Admin_Panel.Results_Admin.Results_Admin;

public class Admin_Election_Launcher_Fragment extends Fragment {


    private MaterialCardView createElection,inProgress,results,settings,pending,coming;
    private String id;
    private Bundle bundle;
    private DatabaseReference databaseReference;

    public Admin_Election_Launcher_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_election_launcher, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Election Launcher");



        createElection=view.findViewById(R.id.createElection);
        inProgress=view.findViewById(R.id.inProgress);
        pending=view.findViewById(R.id.pending);

        coming=view.findViewById(R.id.coming);

        results=view.findViewById(R.id.results);
        settings=view.findViewById(R.id.settings);

        createElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("Elections");
                id = databaseReference.push().getKey();
                bundle=new Bundle();
                bundle.putString("electionID",id);
                CreateElection createElection=new CreateElection();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createElection).addToBackStack("CreateElection").commit();
                createElection.setArguments(bundle);

            }
        });

        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InProgress()).addToBackStack("In Progress").commit();
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pending()).addToBackStack("Pending").commit();
            }
        });
        coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Coming()).addToBackStack("Coming").commit();
            }
        });


        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Results_Admin()).addToBackStack("Results").commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Settings()).addToBackStack("Settings").commit();


            }
        });


        return view;

    }

}
