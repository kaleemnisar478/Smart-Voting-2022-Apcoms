package com.project.smartvotingsystem.FaceRecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.smartvotingsystem.FaceRecognition.camera.CameraSurfaceView;
import com.project.smartvotingsystem.FaceRecognition.camera.FaceRecogntionView;
import com.project.smartvotingsystem.FaceRecognition.imageutil.FaceRecognition;
import com.project.smartvotingsystem.FaceRecognition.imageutil.FaceRecognizerSingleton;
import com.project.smartvotingsystem.Login;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.Voter_Panel;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_imgproc;

import java.io.File;


public class RecognizerActivityForVoteCast extends Activity implements FaceRecognition {

    private FaceRecognizer faceRecognizer =null;
    private RelativeLayout layout;
    private FaceRecogntionView baseFaceView;
    private CameraSurfaceView preview;
    private boolean flag = true;
    private  final  static String TAG = "FaceRecognizer";
    private String ID;
    private String participantID;
    private String user;

    private int votes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        ID=getIntent().getStringExtra("id");
        participantID=getIntent().getStringExtra("getParticipantId");
        user=getIntent().getStringExtra("user");

        votes=getIntent().getIntExtra("votes",-1);


        if( new File(getFilesDir()+ FaceRecognizerSingleton.getSaveFileName()).exists()){
            layout = (RelativeLayout) findViewById(R.id.activity_recongizer);
            baseFaceView = new FaceRecogntionView(this);
            baseFaceView.setActivity(this);
            preview = new CameraSurfaceView(this,baseFaceView);
            layout.addView(preview);
            layout.addView(baseFaceView);
            faceRecognizer = FaceRecognizerSingleton.getInstance(this);
            faceRecognizer.load(getFilesDir()+FaceRecognizerSingleton.getSaveFileName());
        }



    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: recognition");
        flag = true;
        baseFaceView.setVisibility(View.VISIBLE);
        super.onStart();
    }




    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: recognition");
        preview.startPreview();
        super.onResume();
    }

    @Override
    protected void onPause(){
        Log.d(TAG, "onPause: recognition");
        preview.stopPreview();
        flag =false;
        super.onPause();

    }

    private Toast toast = null;
    private int count = 0;

    @Override
    public void execute(IplImage face) {
        if (flag) {
            opencv_imgproc.cvEqualizeHist(face,face);
            int predict = faceRecognizer.predict_label(new Mat(face));
            Log.d(TAG, "faceRecognizer: predict:" + predict);
            if (predict == 1) {
                flag = false;
                if (toast != null) {
                    toast.cancel();
                }
                if (count != 0) {
                    Toast.makeText(RecognizerActivityForVoteCast.this, "Face recognize!! Try: " + count, Toast.LENGTH_SHORT).show();
                    count = 0;

                    DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("ParticipatedVoters");
                    String id = databaseReference1.push().getKey();
                    databaseReference1.child(id).setValue(user);
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Participants");
                    votes++;
                    databaseReference.child(participantID).child("votes").setValue(votes);


                    Intent intent = new Intent(getApplicationContext(), Voter_Panel.class);
                    intent.putExtra("user",user);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                } else {
                    Toast.makeText(RecognizerActivityForVoteCast.this, "Vote casted", Toast.LENGTH_SHORT).show();

                    DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("ParticipatedVoters");
                    String id = databaseReference1.push().getKey();
                    databaseReference1.child(id).setValue(user);

                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Election").child(ID).child("Participants");
                    votes++;
                    databaseReference.child(participantID).child("votes").setValue(votes);


                    Intent intent = new Intent(getApplicationContext(), Voter_Panel.class);
                    intent.putExtra("user",user);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                }

            } else {
                baseFaceView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(RecognizerActivityForVoteCast.this, "Face recognition failed" + count++, Toast.LENGTH_SHORT);
                        toast.show();
                        if(count>=33)
                        {
                            toast = Toast.makeText(RecognizerActivityForVoteCast.this, "Face does not match" + count++, Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent=new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }
                });
            }
        }
    }
}
