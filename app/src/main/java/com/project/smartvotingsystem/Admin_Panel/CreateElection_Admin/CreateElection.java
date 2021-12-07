package com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Details.Election_Details;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Participant.Participants;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Voter.Voters;
import com.project.smartvotingsystem.helper_classes.Participant;
import com.project.smartvotingsystem.helper_classes.Voter;

import java.util.ArrayList;
import java.util.List;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateElection extends Fragment {

    private MaterialCardView launch_election,add_Details,add_participants,add_voters;
    private Bundle bundle;
    private static String id;
    private DatabaseReference databaseReference;
    private List<String> votersEmailList;
    private List<String> participantEmailList;


    private String electName;
    private String startD;
    private String endD;
    private String startT;
    private String endT;


    public CreateElection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_election, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Create Election");

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
        bundle=new Bundle();
        bundle.putString("electionID",id);

        votersEmailList=new ArrayList<>();
        participantEmailList=new ArrayList<>();


        launch_election.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Election");
                                                   Query checkAdmin = reference.orderByChild("detailId").equalTo(id);
                                                   checkAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                           if (dataSnapshot.exists()) {

                                                               if (dataSnapshot.child(id).hasChild("Participants") && dataSnapshot.child(id).hasChild("Voters")) {

                                                                   electName=dataSnapshot.child(id).child("title").getValue(String.class);
                                                                   startD=dataSnapshot.child(id).child("startDate").getValue(String.class);
                                                                   endD=dataSnapshot.child(id).child("endDate").getValue(String.class);
                                                                   startT=dataSnapshot.child(id).child("startTime").getValue(String.class);
                                                                   endT=dataSnapshot.child(id).child("endTime").getValue(String.class);

                                                                   String emailID=getString(R.string.email);
                                                                   String vto=getString(R.string.voting);

                                                                   Properties properties=new Properties();
                                                                   properties.put("mail.smtp.auth","true");
                                                                   properties.put("mail.smtp.starttls.enable","true");
                                                                   properties.put("mail.smtp.host","smtp.gmail.com");
                                                                   properties.put("mail.smtp.port","587");

                                                                   Session session= Session.getInstance(properties, new Authenticator() {
                                                                       @Override
                                                                       protected PasswordAuthentication getPasswordAuthentication() {
                                                                           return new PasswordAuthentication(emailID,vto);
                                                                       }
                                                                   });
                                                                   reference.child(id).child("Participants").addValueEventListener(new ValueEventListener() {
                                                                       @RequiresApi(api = Build.VERSION_CODES.O)
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                           participantEmailList.clear();
                                                                           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                               Participant c = postSnapshot.getValue(Participant.class);
                                                                               participantEmailList.add(c.getEmail());
                                                                           }
                                                                           System.out.println("ppppppppppppp");

                                                                           System.out.println(participantEmailList);

                                                                           String string = String.join(", ", participantEmailList);
                                                                           System.out.println(string);

                                                                           try {
                                                                               //String recipients="abc@gmail.com,abc@gmail.com";
                                                                               Message message=new MimeMessage(session);
                                                                               message.setFrom(new InternetAddress(emailID));
                                                                               //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abc@gmail.com"));
                                                                               message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(string));
                                                                               message.setSubject(electName);
                                                                               String msg="Dear Candidate,<br> Election Named '<b>"+electName+"</b>' is launched. This election will be open from <b>'"+startD+", "+startT+"'</b>  to  <b>'"+endD+", "+endT+"</b>'.<br>You are candidate in this election.<br> Regards,<br>Election Launcher";
                                                                               message.setText(String.valueOf(Html.fromHtml(msg)));

                                                                               new SendMail().execute(message);

                                                                           } catch (MessagingException e) {
                                                                               e.printStackTrace();
                                                                           }


                                                                       }

                                                                       @Override
                                                                       public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                           Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   });

                                                                   reference.child(id).child("Voters").addValueEventListener(new ValueEventListener() {
                                                                       @RequiresApi(api = Build.VERSION_CODES.O)
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                           votersEmailList.clear();
                                                                           for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                               Voter c = postSnapshot.getValue(Voter.class);
                                                                               votersEmailList.add(c.getEmail());
                                                                           }
                                                                           System.out.println("llllllllllllllllllll");

                                                                           System.out.println(votersEmailList);

                                                                           String string = String.join(", ", votersEmailList);
                                                                           System.out.println(string);

                                                                               try {
                                                                                   //String recipients="abc@gmail.com,abc@gmail.com";
                                                                                   Message message=new MimeMessage(session);
                                                                                   message.setFrom(new InternetAddress(emailID));
                                                                                   //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abc@gmail.com"));
                                                                                   message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(string));
                                                                                   message.setSubject(electName);
                                                                                   String msg="Dear Voter,<br> Election Named '<b>"+electName+"</b>' is launched. This election will be open from <b>'"+startD+", "+startT+"</b> to <b>"+endD+", "+endT+"</b>.<br>You are invited as voter to cast your precious vote in this election. You can vote through our 'Smart Voting System' app after login. If you don't have username and password, register yourself in smart voting system.<br>If you don't have smart voting system app then download and install this app from https://drive.google.com/drive/folders/18Wz_nvSy1X5UAEKf56T93LGfq-quuI6U?usp=SHARING <br>Regards,<br>Election Launcher";
                                                                                   message.setText(String.valueOf(Html.fromHtml(msg)));

                                                                                   new SendMail().execute(message);

                                                                               } catch (MessagingException e) {
                                                                                   e.printStackTrace();
                                                                               }


                                                                       }

                                                                       @Override
                                                                       public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                           Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   });


                                                                   Toast.makeText(getContext(), "Election Launched Successfully", Toast.LENGTH_LONG).show();

                                                                   System.out.println("llllllllllllllllllll");

                                                                   System.out.println(votersEmailList);
                                                                   getFragmentManager().popBackStackImmediate();

                                                               } else {
                                                                   Toast.makeText(getContext(), "Add some participants and voters first", Toast.LENGTH_LONG).show();
                                                               }
                                                           } else {
                                                               Toast.makeText(getContext(), "Complete all requirements first", Toast.LENGTH_LONG).show();

                                                           }
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError databaseError) {
                                                           Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                       }
                                                   });
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

    private class SendMail extends AsyncTask<Message,String,String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressDialog=ProgressDialog.show(getContext(),"Please Wait","Sending Mail...",true,false);
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
            //progressDialog.dismiss();
//            if(s.equals("Success"))
//            {
////                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
////                builder.setCancelable(false);
////                builder.setTitle("Success");
////                builder.setMessage("Mail sent successfully");
////                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.dismiss();
////                    }
////                });
////                builder.show();
//                Toast.makeText(getActivity(),"Mail sent",Toast.LENGTH_SHORT).show();
//
//            }
//            else {
//                Toast.makeText(getActivity(),"Something went wrong... in sending mail",Toast.LENGTH_SHORT).show();
//            }
        }
    }


}
