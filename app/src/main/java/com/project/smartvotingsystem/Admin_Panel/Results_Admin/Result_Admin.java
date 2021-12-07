package com.project.smartvotingsystem.Admin_Panel.Results_Admin;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Participant;
import com.project.smartvotingsystem.helper_classes.ResultAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Result_Admin extends Fragment {

    private TextView title,startDate,endDate,startTime,endTime,winner;
    private RecyclerView recyclerView;
    private List<Participant> participantList;
    private ResultAdapter resultAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String ID;
    private String winnerName;
    private String winnerParty;
    private int winnerVotes;

    public Result_Admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_result, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Result");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        title=view.findViewById(R.id.title);
        startDate=view.findViewById(R.id.startDate);
        endDate=view.findViewById(R.id.endDate);
        startTime=view.findViewById(R.id.startTime);
        endTime=view.findViewById(R.id.endTime);
        winner=view.findViewById(R.id.winner);

        title.setText(getArguments().getString("title"));
        startTime.setText("Start Time: "+getArguments().getString("startTime"));
        endTime.setText("End Time: "+getArguments().getString("endTime"));
        startDate.setText("Start Date: "+getArguments().getString("startDate"));
        endDate.setText(" End Date: "+getArguments().getString("endDate"));

        winnerVotes=0;
        winnerName="";
        winnerParty="";

        recyclerView=view.findViewById(R.id.recyclerviewParticipantsResult);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        participantList=new ArrayList<>();
        resultAdapter = new ResultAdapter(getContext(), participantList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(resultAdapter);

        ID=getArguments().getString("electionID");
        databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Participants");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Participant c = postSnapshot.getValue(Participant.class);
                    if(c.getVotes()>winnerVotes)
                    {
                        winnerName=c.getName();
                        winnerParty=c.getParty();
                        winnerVotes=c.getVotes();
                    }
                    else if(c.getVotes()==winnerVotes)
                    {
                        winnerName="Draw";
                        winnerParty="between parties";
                    }
                    participantList.add(c);
                }
                winner.setText("Winner: "+winnerName+" of "+winnerParty);
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
