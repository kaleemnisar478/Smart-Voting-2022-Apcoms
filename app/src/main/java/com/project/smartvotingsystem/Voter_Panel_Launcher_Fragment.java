package com.project.smartvotingsystem;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.project.smartvotingsystem.Voters_Panel.Coming_Voter.Coming_Voter;
import com.project.smartvotingsystem.Voters_Panel.InProgress_Voter.InProgress_Voter;
import com.project.smartvotingsystem.Voters_Panel.Results_Voter.Results_Voter;

public class Voter_Panel_Launcher_Fragment extends Fragment {


    private MaterialCardView inProgress,results,settings,coming;
    private Bundle bundle;

    public Voter_Panel_Launcher_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_voter_panel, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Voter Panel");

        inProgress=view.findViewById(R.id.inProgress);

        coming=view.findViewById(R.id.coming);

        results=view.findViewById(R.id.results);
        settings=view.findViewById(R.id.settings);

        String user=getArguments().getString("user");
        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("In voter fragment user :"+user);
                System.out.println("In voter fragment user :"+user);
                System.out.println("In voter fragment user :"+user);


                bundle=new Bundle();
                bundle.putString("user",user);
                InProgress_Voter inProgress_voter= new InProgress_Voter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, inProgress_voter).addToBackStack("In Progress").commit();
                inProgress_voter.setArguments(bundle);
            }
        });

        coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle=new Bundle();
                bundle.putString("user",user);
                Coming_Voter coming_voter= new Coming_Voter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, coming_voter).addToBackStack("Coming").commit();
                coming_voter.setArguments(bundle);
            }
        });


        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Results_Voter()).addToBackStack("Results").commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Settings()).addToBackStack("Settings").commit();
//                Intent intent=new Intent(getActivity(), Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

            }
        });


        return view;

    }

}
