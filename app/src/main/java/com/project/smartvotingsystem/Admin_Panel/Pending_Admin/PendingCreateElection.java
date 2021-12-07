package com.project.smartvotingsystem.Admin_Panel.Pending_Admin;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Details.Election_Details;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Participant.Participants;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Voter.Voters;
import com.project.smartvotingsystem.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingCreateElection extends Fragment {

    private MaterialCardView launch_election,add_Details,add_participants,add_voters;
    private Bundle bundle;
    private static String id;
    private DatabaseReference databaseReference;


    public PendingCreateElection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_election, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Pendings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        launch_election=view.findViewById(R.id.launch_election);
        add_Details=view.findViewById(R.id.add_Details);
        add_participants=view.findViewById(R.id.add_participants);
        add_voters=view.findViewById(R.id.add_voters);

        databaseReference= FirebaseDatabase.getInstance().getReference("Elections");


        id =getArguments().getString("electionID");
        boolean participants =getArguments().getBoolean("participants");
        boolean voters =getArguments().getBoolean("voters");
        add_Details.setBackgroundDrawable(getResources().getDrawable(R.drawable.done));
        add_Details.setEnabled(false);

        if(participants) {
            add_participants.setBackgroundDrawable(getResources().getDrawable(R.drawable.done));
            add_participants.setEnabled(false);

        }
        if(voters) {
            add_voters.setBackgroundDrawable(getResources().getDrawable(R.drawable.done));
            add_voters.setEnabled(false);

        }
        bundle=new Bundle();
        bundle.putString("electionID",id);

        launch_election.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Election Launched Successfully", Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStackImmediate();
            }
        });

        add_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Election_Details election_details=new Election_Details();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, election_details).addToBackStack("Details").commit();
                election_details.setArguments(bundle);

            }
        });

        add_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Participants participants=new Participants();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, participants).addToBackStack("Participants").commit();
                participants.setArguments(bundle);

            }
        });

        add_voters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voters voters=new Voters();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, voters).addToBackStack("Voters").commit();
                voters.setArguments(bundle);

            }
        });

        return view;

    }

}
