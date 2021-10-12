package com.example.androidproject.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidproject.R;
import com.example.androidproject.StorageData;
import com.example.androidproject.data.Table;

public class RestMain extends BaseActivity implements View.OnClickListener {
    Spinner timeChoose;
    Context mainContext;

    private ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;
    final String[] PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS

    };
    static public String lastSMS="" ;
    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_choose_table){
            Intent intent = new Intent(this,TableModel.class);
            startActivity(intent);
        }
        if (v.getId()== R.id.btn_clear_order){
            StorageData.clearSP(StorageData.SP_SMS,this);
            StorageData.clearSP(StorageData.SP_STRING_TIME,this);
            StorageData.clearSP(StorageData.SP_STRING_TABLE,this);
            getOrderDetails();
            stopService();
        }
    }
    // information on the last order made by user (if there is not one - shows nothing )
    private void getOrderDetails(){
        Table order=StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        if (order!= null){
            View view_order=findViewById(R.id.order);
            view_order.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.table)).setText("#"+order.getId());
            ((TextView)findViewById(R.id.seats)).setText("Seats: "+order.getSeats());
            ((TextView)findViewById(R.id.smoke)).setVisibility(order.isSmoke()?View.VISIBLE:View.INVISIBLE);
            int tableOrderTime = (int) StorageData.getSP(StorageData.SP_STRING_TIME,this,int.class);
            ((EditText) findViewById(R.id.et_time_order)).setText("" + getResources().getStringArray(R.array.time_array)[tableOrderTime]);
            findViewById(R.id.btn_clear_order).setVisibility(View.VISIBLE);

        }
        else {
            findViewById(R.id.order).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_clear_order).setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onResume() {
     super.onResume();
        int loadLastTimeChoose=StorageData.getRaw(StorageData.RAW_STRING,mainContext);
        if (loadLastTimeChoose>=0)
            timeChoose.setSelection(loadLastTimeChoose,false);
        getOrderDetails();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturaunt_menu);
        findViewById(R.id.btn_choose_table).setOnClickListener(this);
        findViewById(R.id.btn_clear_order).setOnClickListener(this);
        timeChoose=findViewById(R.id.time_choose);
        mainContext=this;
        timeChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StorageData.saveRaw(StorageData.RAW_STRING,position,mainContext);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        //requesting premisions for sms
        requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionActivityResultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
            if (isGranted.containsValue(false)) {
                multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            }
        });

        askPermissions();
    }





    private void askPermissions() {
        if (!hasPermissions(PERMISSIONS)) {

            multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}