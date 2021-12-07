package com.project.smartvotingsystem.FaceRecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.project.smartvotingsystem.Admin_Election_Launcher;
import com.project.smartvotingsystem.FaceRecognition.camera.CameraSurfaceView;
import com.project.smartvotingsystem.FaceRecognition.camera.FaceRecogntionView;
import com.project.smartvotingsystem.FaceRecognition.imageutil.FaceRecognition;
import com.project.smartvotingsystem.FaceRecognition.imageutil.FaceRecognizerSingleton;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_imgproc;

import java.io.File;

import com.project.smartvotingsystem.Login;
import com.project.smartvotingsystem.R;
import com.project.smartvotingsystem.Voter_Panel;


public class RecognizerActivity extends Activity implements FaceRecognition {

    private FaceRecognizer faceRecognizer =null;
    private RelativeLayout layout;
    private FaceRecogntionView baseFaceView;
    private CameraSurfaceView preview;
    private boolean flag = true;
    private  final  static String TAG = "FaceRecognizer";
    private String userType;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        userType=getIntent().getStringExtra("userType");
        user=getIntent().getStringExtra("user");

        if( new File(getFilesDir()+ FaceRecognizerSingleton.getSaveFileName()).exists()){
            layout = (RelativeLayout) findViewById(R.id.activity_recongizer);
            baseFaceView = new FaceRecogntionView(this);
            baseFaceView.setActivity(this);
            preview = new CameraSurfaceView(this,baseFaceView);
            layout.addView(preview);
            layout.addView(baseFaceView);
            faceRecognizer = FaceRecognizerSingleton.getInstance(this);
            faceRecognizer.load(getFilesDir()+FaceRecognizerSingleton.getSaveFileName());
            System.out.println("faceRecognizer recognizer activity"+getFilesDir()+FaceRecognizerSingleton.getSaveFileName());
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
                    Toast.makeText(RecognizerActivity.this, "Login Success!! Try: " + count, Toast.LENGTH_SHORT).show();
                    count = 0;
                    if(userType.equals("Admin")) {
                        Intent intent = new Intent(getApplicationContext(), Admin_Election_Launcher.class);
                        intent.putExtra("user",user);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), Voter_Panel.class);
                        intent.putExtra("user",user);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(RecognizerActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                    if(userType.equals("Admin")) {
                        Intent intent = new Intent(getApplicationContext(), Admin_Election_Launcher.class);
                        intent.putExtra("user",user);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), Voter_Panel.class);
                        intent.putExtra("user",user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

            } else {
                baseFaceView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(RecognizerActivity.this, "Login Fail!! " + count++, Toast.LENGTH_SHORT);
                        toast.show();
                        if(count>=33)
                        {
                            toast = Toast.makeText(RecognizerActivity.this, "Login Failed! Face does not match" + count++, Toast.LENGTH_LONG);
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
