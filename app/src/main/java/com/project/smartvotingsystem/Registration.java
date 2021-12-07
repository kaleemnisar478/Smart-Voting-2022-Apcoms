package com.project.smartvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.smartvotingsystem.FaceRecognition.RegisterActivity;
import com.project.smartvotingsystem.helper_classes.User;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Registration extends AppCompatActivity {
    TextInputLayout fullname,phone,email,cnic,dob;
    Button login,registerFace;
    RadioGroup radioGroup;
    String type;
    DatabaseReference reference;
    int count;
    User newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login=findViewById(R.id.signup_login_button);
        String text="Already have an account? <font color=#48C9B0>Login</font>";
        login.setText(Html.fromHtml(text));

        fullname=findViewById(R.id.full_name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        cnic=findViewById(R.id.cnic);
        dob=findViewById(R.id.doj);

        radioGroup=findViewById(R.id.radiogroup);

        registerFace=findViewById(R.id.registerFaceButton);
        registerFace.setVisibility(View.GONE);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker mdp = builder.build();
        dob.getEditText().setKeyListener(null);
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.getEditText().setText(mdp.getHeaderText());
            }
        });
        dob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp.show(getSupportFragmentManager(),"DOB_PICKER");
            }
        });


        // error message for first time

        fullname.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && fullname.getEditText().getText().toString().isEmpty())
                {
                    fullname.setError("Field cannot be empty");
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

        phone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && phone.getEditText().getText().toString().isEmpty())
                {
                    phone.setError("Field cannot be empty");
                }
            }

        });

        cnic.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && cnic.getEditText().getText().toString().isEmpty())
                {
                    cnic.setError("Field cannot be empty");
                }
            }

        });



        //after first letter errors
        fullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

                if(s.length()==0) {
                    fullname.setError("Field cannot be empty");
                }
                else if (!(s.toString().matches(full_name_checker)))
                {
                    fullname.setError("Invalid Name. Special Characters and numbers are not acceptable");
                }
                else if(s.length()>0 && s.length()<30) {
                    fullname.setErrorEnabled(false);
                }
                else if (s.length()>=30){
                    fullname.setError("Maximum length reached");
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

        phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    phone.setError("Field cannot be empty");
                }
                else if (s.length()<7){
                    phone.setError("Invalid Phone number");
                }
                else
                {
                    phone.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnic.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0) {
                    cnic.setError("Field cannot be empty");
                }
                else if (s.length()<13){
                    cnic.setError("Invalid/incomplete CNIC. CNIC must be 3500312433123 format");
                }
                else
                {
                    cnic.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }




    private boolean validateFullname(){
        String val=fullname.getEditText().getText().toString();
        final String full_name_checker="^[a-zA-Z. ]+((['. ][a-zA-Z ])?[a-zA-Z. ]*)*$";

        if(val.isEmpty()){
            fullname.setError("Field cannot be empty");
            fullname.requestFocus();
            return false;
        }
        else if(!val.matches(full_name_checker))
        {
            fullname.requestFocus();
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

    private boolean validatePhone(){
        String val=phone.getEditText().getText().toString();
        if(val.isEmpty()){
            phone.setError("Field cannot be empty");
            phone.requestFocus();
            return false;
        }
        else if(val.length()<7)
        {
            phone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateCnic(){
        String val=cnic.getEditText().getText().toString();
        if(val.isEmpty()){
            cnic.setError("Field cannot be empty");
            cnic.requestFocus();
            return false;
        }
        else if(val.length()<13)
        {
            cnic.requestFocus();
            return false;
        }
        return true;
    }




    public void registerUser(View view) {
        if(
                //!validateRegiteredFace() |
                !validateCnic() | !validatePhone() | !validateEmail()   | !validateFullname()){

            Toast.makeText(getApplicationContext(),"Fill all Fields with no errors",Toast.LENGTH_LONG).show();

            return;
        }

       int userType=radioGroup.getCheckedRadioButtonId();

        type="";
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

        if(userType==R.id.voter)
        {   reference = rootNode.getReference("Users").child("Voters");
            type="Voter";
        }
        else if(userType==R.id.admin)
        {
            reference = rootNode.getReference("Users").child("Admins");
            type="Admin";
        }
        else
        {
            type="None";

        }


        String name=fullname.getEditText().getText().toString();
        String mail=email.getEditText().getText().toString();
        String number=phone.getEditText().getText().toString();
        String CNIC=cnic.getEditText().getText().toString();
        String DOB=dob.getEditText().getText().toString();

        String id = reference.push().getKey();

        String password=GetPassword(7);

        System.out.println("password is");

        System.out.println(password);


        User newuser=new User(name,mail,number,CNIC,DOB,type,id,password);


        count=0;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    if(u.getEmail().equals(mail))
                    {
                        count++;
                        email.setError("Account already exists. If you don't know your password go for forget password");
                        email.setErrorEnabled(true);
                        Toast.makeText(getApplicationContext(),"Account already exists. If you don't know your password go for forget password",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if(count==0){
                    register(newuser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void register(User user)
    {

        reference.child(user.getId()).setValue(user);

        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra("userID",user.getId());
        intent.putExtra("userType",user.getUserType());
        startActivity(intent);

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

        try {
            String recipients=user.getEmail();
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(emailID));
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abc@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
            message.setSubject("Smart Voting System");

            String msg="Dear "+user.getFullname()+",<br>You are successfully registered in 'SMART VOTING SYSTEM'. Your username is ' "+user.getEmail()+" ' and password is ' "+user.getPassword()+" '";
            message.setText(String.valueOf(Html.fromHtml(msg)));


            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String GetPassword(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@&*".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        Random rand = new Random();

        for(int i = 0; i < length; i++){
            char c = chars[rand.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    public void back_to_login(View view) {
        Intent intent=new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void faceRegister(View view) {
        //Intent intent=new Intent(getApplicationContext(), FaceMainActivity.class);
        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
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
           // progressDialog.dismiss();
            if(s.equals("Success"))
            {
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getApplicationContext());
//                builder.setCancelable(false);
//                builder.setTitle("Success");
//                builder.setMessage("Mail sent successfully");
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//
//
//                    }
//                });
//                builder.show();



            }
            else {
                Toast.makeText(getApplicationContext(),"Something went wrong... in sending mail",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
