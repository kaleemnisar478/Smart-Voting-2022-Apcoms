package com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Voter;


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

import java.util.ArrayList;
import java.util.List;

import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Voter;
import com.project.smartvotingsystem.helper_classes.VoterAdapter;
import com.project.smartvotingsystem.helper_classes.RecyclerTouchListener;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;


/**
 * A simple {@link Fragment} subclass.
 */
public class Voters extends Fragment {

    private TextView noVoter;
    private RecyclerView recyclerView;
    private List<Voter> voterList;
    private VoterAdapter voterAdapter;
    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    private VoterDialog dialog=null;
    private String ID;
    public Voters() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_voters, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Add Voters");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        noVoter=view.findViewById(R.id.empty_voter_view);
        recyclerView=view.findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        voterList=new ArrayList<>();
        voterAdapter = new VoterAdapter(getContext(), voterList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(voterAdapter);
        toggleEmptyVoters();



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddVoterDialog(null,"add");
//                String emailID="abc";
//                String emailPass="abc";
//
//                Properties properties=new Properties();
//                properties.put("mail.smtp.auth","true");
//                properties.put("mail.smtp.starttls.enable","true");
//                properties.put("mail.smtp.host","smtp.gmail.com");
//                properties.put("mail.smtp.port","587");
//
//                Session session= Session.getInstance(properties, new Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(emailID,emailPass);
//                    }
//                });
//
//                try {
//                    String recipients="abc@gmail.com,abc@gmail.com";
//                    Message message=new MimeMessage(session);
//                    message.setFrom(new InternetAddress(emailID));
//                    //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abc@gmail.com"));
//                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
//                    message.setSubject("test");
//                    message.setText("Testing mail api");
//
//                    new SendMail().execute(message);
//
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }

            }
        });
        ID=getArguments().getString("electionID");

        databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Voters");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                voterList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Voter c = postSnapshot.getValue(Voter.class);
                    voterList.add(c);
                }
                voterAdapter.notifyDataSetChanged();
                toggleEmptyVoters();
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
                Voter c=voterList.get(position);
                openAddVoterDialog(c,"edit");

            }

            @Override
            public void onLongClick(View view, int position) {
                Voter c=voterList.get(position);
                openAddVoterDialog(c,"edit");

            }
        }));






        return view;

    }


    private void openAddVoterDialog(Voter c, String state) {
        dialog=new VoterDialog(c,state,ID);
        dialog.show(getActivity().getSupportFragmentManager(),"emergency dialog");// use getChild... see from a link
        dialog.setCancelable(false);     // dialog should not close on touches outside the dialog (and using this another case by default on back press dialog not closes)
        //  dialog.setCanceledOnTouchOutside(false); not work in fragment
       // dialog.getDialog().getButton(AlertDialog.BUTTON_POSITIVE)
    }


    private void toggleEmptyVoters() {
        // you can check notesList.size() > 0

        if (voterAdapter.getItemCount() > 0) {
            noVoter.setVisibility(View.GONE);
        } else {
            noVoter.setVisibility(View.VISIBLE);
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
