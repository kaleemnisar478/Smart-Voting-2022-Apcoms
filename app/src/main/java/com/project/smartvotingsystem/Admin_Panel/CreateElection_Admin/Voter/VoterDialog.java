package com.project.smartvotingsystem.Admin_Panel.CreateElection_Admin.Voter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.helper_classes.Voter;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class VoterDialog extends AppCompatDialogFragment {

    private TextInputLayout voterName,email;

    private Voter voter;
    private String state;

    private AlertDialog.Builder builder=null;
    private AlertDialog mAlertDialog=null;

    private DatabaseReference databaseReference;

    private String ID;

    public VoterDialog(Voter voter, String state,String ID) {
        this.voter = voter;
        this.state = state;
        this.ID=ID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.fragment_dialog_add_voter,null);

        voterName=view.findViewById(R.id.dialog_voter_name);
        email=view.findViewById(R.id.email_address_dialog);


        databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Voters");;

        voterName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && voterName.getEditText().getText().toString().isEmpty())
                {
                    voterName.setError("Field cannot be empty");
                }
            }

        });

        email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && email.getEditText().getText().toString().isEmpty())
                {
                    email.setError("Field cannot be empty");
                }
            }

        });


        voterName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

                if(s.toString().trim().length()==0) {
                    voterName.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(name_checker)))
                {
                    voterName.setError("Invalid Name. Special Characters and numbers are not acceptable");
                }
                else if(s.length()>0 && s.length()<30) {
                    voterName.setErrorEnabled(false);
                }
                else if (s.toString().trim().length()>=30){
                    voterName.setError("Maximum length reached");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String email_checker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

                if(s.length()==0) {
                    email.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(email_checker)))
                {
                    email.setError("Invalid Email");
                }
                else
                {
                    email.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if(state.equals("add")) {
            builder.setView(view)
                    .setTitle("Add New Voters")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Add", null);
            mAlertDialog= builder.create();
            mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            mAlertDialog.show();


            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateEmail() | !validateName()) {
                        return;
                    }
                    addVoter();
                    mAlertDialog.dismiss();
                }
            });

        }
        else
        {
            voterName.getEditText().setText(voter.getName());
            email.getEditText().setText(voter.getEmail());

            builder.setView(view)
                    .setTitle("Edit Voter Info")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData();

                        }
                    }).setPositiveButton("Update", null)
                    .setNeutralButton("Cancel", null);

            mAlertDialog= builder.create();
            mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            mAlertDialog.show();



            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateEmail() | !validateName()) {
                        return;
                    }
                    updateData();
                    mAlertDialog.dismiss();
                }
            });


        }


        return mAlertDialog;

    }


    private boolean validateName(){
        String val=voterName.getEditText().getText().toString().trim();
        final String name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            voterName.setError("Field cannot be empty");
            voterName.requestFocus();
            return false;
        }
        else if(!val.matches(name_checker))
        {
            voterName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        final String emailchecker="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            email.requestFocus();
            return false;
        }
        else if(!val.matches(emailchecker))
        {
            email.requestFocus();
            return false;
        }
        return true;

    }



    void addVoter()
    {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Election").child(ID).child("Voters");;


        String name = voterName.getEditText().getText().toString();
        String mail = email.getEditText().getText().toString();


        String id = reference.push().getKey();


        voter = new Voter(name, mail,id);


        reference.child(id).setValue(voter);


        String emailID="abc";
        String emailPass="abc";

        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session= Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailID,emailPass);
            }
        });

//        try {
//            String recipients=mail;
//            Message message=new MimeMessage(session);
//            message.setFrom(new InternetAddress(emailID));
//            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abc@gmail.com"));
//            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
//            message.setSubject("Election");
//            message.setText("Hi "+name+" You are added as Voter in election. If you don't have smart voting system app then download and install this app from https://drive.google.com/drive/folders/1dYWSt1K7z8J2BJxYJIjDjXiDy_dwxg3l?usp=sharing <br> ");
//
//              String msg="Dear "+name+",<br>You are added as Voter in election. You can vote, coming elections and results through our 'Smart Voting System' app after login. If you don't have username and password, register yourself in smart voting system.<br>If you don't have smart voting system app then download and install this app from https://drive.google.com/drive/folders/1dYWSt1K7z8J2BJxYJIjDjXiDy_dwxg3l?usp=sharing <br>Regards,<br>Election Launcher";
//              message.setText(String.valueOf(Html.fromHtml(msg)));


//            new SendMail().execute(message);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

        Toast.makeText(getContext(),"New Voter Added Successfully", Toast.LENGTH_LONG).show();

    }



    void updateData()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Voters").child(voter.getVoterID());


        String name = voterName.getEditText().getText().toString();
        String mail = email.getEditText().getText().toString();
        Voter newVoter = new Voter(name, mail,voter.getVoterID());

        databaseReference.setValue(newVoter);

        Toast.makeText(getContext(),"Voter Updated Successfully", Toast.LENGTH_LONG).show();

    }


    void deleteData()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Voters").child(voter.getVoterID());
        databaseReference.removeValue();
        Toast.makeText(getContext(),voter.getName()+" Deleted Successfully", Toast.LENGTH_LONG).show();

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
