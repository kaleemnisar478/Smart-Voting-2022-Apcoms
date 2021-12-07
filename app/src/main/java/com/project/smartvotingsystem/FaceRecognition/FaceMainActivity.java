package com.project.smartvotingsystem.FaceRecognition;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.project.smartvotingsystem.FaceRecognition.service.RequestUserPermission;
import com.project.smartvotingsystem.R;


public class FaceMainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private boolean isServiceRunFlag = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("rainjay", "onDestroy: Main");
        //android.os.Process.killProcess(android.os.Process.myPid());
    }



    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_main);
        Log.d("rainjay", "onCreate: Main");

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyCameraPermissions();

        settings = getSharedPreferences("threshold_data",0);
//        TinyDancer.create()
//                .redFlagPercentage(.1f) // set red indicator for 10%
//                .startingGravity(Gravity.TOP)
//                .startingXPosition(15)
//                .startingYPosition(15)
//                .show(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {


            final Dialog d = new Dialog(FaceMainActivity.this);
            d.setTitle("NumberPicker");
            d.setContentView(R.layout.mydialog);
            Button b1 = (Button) d.findViewById(R.id.button1);
            //Button b2 = (Button) d.findViewById(R.id.button2);
            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
            np.setMaxValue(105);
            np.setMinValue(75);
            np.setValue(settings.getInt("thrd",getResources().getInteger(R.integer.def_thrd)));
            np.setWrapSelectorWheel(false);
            b1.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v) {
//                    Log.d("rainjay", "onClick: " + String.valueOf(np.getValue()));
                    settings.edit().putInt("thrd",np.getValue()).commit();
                    d.dismiss();
                }
            });
//            b2.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v) {
//                    d.dismiss();
//                }
//            });
            d.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void registerOnClick(View view) {
        Log.d("rainjay","start register");
        startActivity(new Intent().setClass(FaceMainActivity.this,RegisterActivity.class));

    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//    public void testOnClick(View view) {
//        startActivity(new Intent().setClass(FaceMainActivity.this, RecognizerActivity.class));
//    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}