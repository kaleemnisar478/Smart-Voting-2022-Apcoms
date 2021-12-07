package com.project.smartvotingsystem.FaceRecognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.smartvotingsystem.FaceRecognition.camera.BaseFaceView;
import com.project.smartvotingsystem.FaceRecognition.camera.CameraSurfaceView;
import com.project.smartvotingsystem.FaceRecognition.imageutil.FaceRecognizerSingleton;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_imgproc;

import java.io.File;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvEqualizeHist;

import com.project.smartvotingsystem.FaceRecognition.service.RequestUserPermission;
import com.project.smartvotingsystem.Login;
import com.project.smartvotingsystem.R;


public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private BaseFaceView baseFaceView;
    private CameraSurfaceView preview;
    private final  static String TAG = "rainjay";
    private FaceRecognizer faceRecognizer = null;
    private MatVector trainImages = null;
    private Mat trainLabel = null;
    private Uri uriRecognizer;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userID;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userID=getIntent().getStringExtra("userID");
        userType=getIntent().getStringExtra("userType");

        setContentView(R.layout.activity_register);
        faceRecognizer = FaceRecognizerSingleton.getInstance(this);

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyCameraPermissions();

        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userType+"s").child(userID);



    }

    private int takeNum = 0;
    private IntBuffer labelsBuf = null;
    public void goRegisterCamera(View view) {
        EditText numberText = (EditText)findViewById(R.id.editText);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            takeNum = Integer.valueOf(numberText.getText().toString());
        } catch (NumberFormatException e) {
            takeNum = 5;
        }
        if( takeNum > 0)
            trainImages = new MatVector(takeNum);
            trainLabel = new Mat(takeNum,1, opencv_core.CV_32SC1);
            labelsBuf = trainLabel.createBuffer();
            counter = 0;
            createCameraView();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
//                INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        return true;
//    }


    private  int counter = 0;
    private Toast t = null;
    @SuppressWarnings("deprecation")
    public void takePhoto(View view) {
        IplImage facex = baseFaceView.captureFace();
        if( facex != null) {
            if(takeNum >= 1){
                takeNum--;
                if( takeNum > 0) {
                    toastFactory("Remaining " + takeNum + " Photo");
                }
                cvEqualizeHist(facex,facex);
                trainImages.put(counter,new Mat(facex));
                labelsBuf.put(counter, 1);
                counter++;
            }
            if( takeNum == 0){
                this.destoryCamereView();
                setContentView(R.layout.activity_register2);
                faceRecognizer.train(trainImages,trainLabel);

                // check train data is exist
                File tmp = new File(this.getFilesDir() + FaceRecognizerSingleton.getSaveFileName());
                if( tmp.exists() ){
                    tmp.delete();
                }
                faceRecognizer.save(this.getFilesDir() + FaceRecognizerSingleton.getSaveFileName());
                uriRecognizer=Uri.fromFile(new File(getFilesDir()+FaceRecognizerSingleton.getSaveFileName()));
                System.out.println("faceRecognizer register activity"+getFilesDir()+FaceRecognizerSingleton.getSaveFileName());

                final String id = databaseReference.push().getKey();


//                participant = new Participant(id,name,mail,pName,"pic","symb");


                if(uriRecognizer!=null )
                {

                    try {

                        storageReference= FirebaseStorage.getInstance().getReference("facialData");



                        storageReference.child(id).putFile(uriRecognizer).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                       // participant.setPic(uri.toString());
                                          databaseReference.child("imageData").setValue(uri.toString());
                                        Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();



                                    }
                                });
                            }
                        });


                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }



            }
        }

    }

    private void createCameraView(){
        ((Button)findViewById(R.id.takePhotoButton)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.startbutton)).setVisibility(View.GONE);
        layout = (RelativeLayout) findViewById(R.id.activity_register);
        baseFaceView = new BaseFaceView(this);
        preview = new CameraSurfaceView(this,baseFaceView);

        layout.addView(preview);
        layout.addView(baseFaceView);
    }

    private  void destoryCamereView(){
        layout.removeView(preview);
        layout.removeView(baseFaceView);
        ((Button)findViewById(R.id.takePhotoButton)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.startbutton)).setVisibility(View.VISIBLE);
    }

    private  void showTakePhoto(IplImage face){
        Bitmap bmp = Bitmap.createBitmap(face.width(), face.height(), Config.ARGB_8888);
        IplImage temp = opencv_core.cvCreateImage(opencv_core.cvGetSize(face), IPL_DEPTH_8U, 4);
        cvCvtColor(face, temp , opencv_imgproc.CV_GRAY2RGBA);
        bmp.copyPixelsFromBuffer(temp.createBuffer());
        ImageView image = (ImageView) findViewById(R.id.faceimage);
        image.setImageBitmap(bmp);
    }

    public void returnOnclick(View view) {
        //finish();
        Toast.makeText(getApplicationContext(),"Username and password sent to your registered mail",Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // send user(registerUser class object) to  otp activity
        startActivity(intent);

    }

    private void toastFactory( String str ){
        if (t != null)t.cancel();
        t = Toast.makeText(this,str, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
