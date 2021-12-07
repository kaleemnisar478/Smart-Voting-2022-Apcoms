package com.project.smartvotingsystem.Voters_Panel.Cast_Vote;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Participant.ParticipantsDialog;
import com.project.smartvotingsystem.FaceRecognition.RecognizerActivityForVoteCast;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Participant;
import com.project.smartvotingsystem.helper_classes.ParticipantAdapter;
import com.project.smartvotingsystem.helper_classes.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cast_Vote extends Fragment {

    private TextView noParticipant;
    private RecyclerView recyclerView;
    private List<Participant> participantList;
    private ParticipantAdapter participantAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String ID;
    private String user;

    Bundle bundle;

    private ParticipantsDialog dialog=null;
    public Cast_Vote() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_participants, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Cast Vote");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        noParticipant=view.findViewById(R.id.empty_participant_view);
        recyclerView=view.findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        participantList=new ArrayList<>();
        participantAdapter = new ParticipantAdapter(getContext(), participantList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(participantAdapter);
        toggleEmptyParticipants();



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        ID=getArguments().getString("electionID");
        user=getArguments().getString("user");

        databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Participants");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Participant c = postSnapshot.getValue(Participant.class);
                    participantList.add(c);
                }
                participantAdapter.notifyDataSetChanged();
                toggleEmptyParticipants();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Participant c=participantList.get(position);
         //       openAddParticipantDialog(c,"edit");
                startDialog(c);

            }

            @Override
            public void onLongClick(View view, int position) {
                Participant c=participantList.get(position);
                startDialog(c);

            }
        }));






        return view;

    }

    private void startDialog(Participant p) {
        android.app.AlertDialog.Builder myAlertDialog = new android.app.AlertDialog.Builder(getContext());
        myAlertDialog.setTitle("Cast Vote");
        myAlertDialog.setMessage(Html.fromHtml("Are you sure you want to cast vote to "+p.getName()+" of "+p.getParty()+"<br><font color=#FF0000><b>Note:</b></font><br>Once you voted it can never be undo."));
        myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int votes=p.getVotes();

                Intent intent=new Intent(getContext(), RecognizerActivityForVoteCast.class);
                intent.putExtra("id",ID);
                intent.putExtra("user",user);

                intent.putExtra("votes",votes);
                intent.putExtra("getParticipantId",p.getParticipantId());
                startActivity(intent);


            }
        });
        myAlertDialog.setNegativeButton("No",null);
        myAlertDialog.show();
    }



    private void toggleEmptyParticipants() {
        // you can check notesList.size() > 0

        if (participantAdapter.getItemCount() > 0) {
            noParticipant.setVisibility(View.GONE);
        } else {
            noParticipant.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

}
