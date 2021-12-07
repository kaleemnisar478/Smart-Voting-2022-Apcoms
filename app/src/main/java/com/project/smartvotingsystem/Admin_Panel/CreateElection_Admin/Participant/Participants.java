package com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Participant;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Participant;
import com.project.smartvotingsystem.helper_classes.ParticipantAdapter;
import com.project.smartvotingsystem.helper_classes.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;


/**
 * A simple {@link Fragment} subclass.
 */
public class Participants extends Fragment {

    private TextView noParticipant;
    private RecyclerView recyclerView;
    private List<Participant> participantList;
    private ParticipantAdapter participantAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;
    private String ID;

    private ParticipantsDialog dialog=null;
    public Participants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_participants, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Add Participants");

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddParticipantDialog(null,"add");
            }
        });
        ID=getArguments().getString("electionID");
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
                openAddParticipantDialog(c,"edit");

            }

            @Override
            public void onLongClick(View view, int position) {
                Participant c=participantList.get(position);
                openAddParticipantDialog(c,"edit");

            }
        }));

        return view;

    }


    private void openAddParticipantDialog(Participant c, String state) {
        dialog=new ParticipantsDialog(c,state, ID);
        dialog.show(getActivity().getSupportFragmentManager(),"emergency dialog");// use getChild... see from a link
        dialog.setCancelable(false);     // dialog should not close on touches outside the dialog (and using this another case by default on back press dialog not closes)
        //  dialog.setCanceledOnTouchOutside(false); not work in fragment
       // dialog.getDialog().getButton(AlertDialog.BUTTON_POSITIVE)
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

    private class SendMail extends AsyncTask<Message,String,String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(getContext(),"Please Wait","Sending Mail...",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("Success");
                builder.setMessage("Mail sent successfully");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
            else {
                Toast.makeText(getContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
