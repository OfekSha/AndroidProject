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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidproject.FireBaseListener;
import com.example.androidproject.R;
import com.example.androidproject.StorageData;
import com.example.androidproject.controller.FireBaseConnection;
import com.example.androidproject.data.Restaurant;
import com.example.androidproject.data.Table;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Map;

public class RestMain extends BaseActivity implements View.OnClickListener, FireBaseListener {
    Spinner timeChoose;
    Context mainContext;
    Restaurant rest;
    int first_index; // for spinner to know what time to show.
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
        else if (v.getId()== R.id.btn_clear_order){
            StorageData.clearSP(StorageData.SP_SMS,this);
            StorageData.clearSP(StorageData.SP_STRING_TIME,this);
            StorageData.clearSP(StorageData.SP_STRING_TABLE,this);
            getOrderDetails();
            stopService();
        }
        else if(v.getId()== R.id.btn_chat){
            Intent intent = new Intent(this,ChatActivity.class);
            startActivity(intent);
        }
    }
    // information on the last order made by user (if there is not one - shows nothing )
    private void getOrderDetails(){
        FireBaseConnection.instance.changeListener(this);
        String clientId=StorageData.getSP("google_details",this ,String.class);
        FireBaseConnection.instance.connectCollectionInDocument(rest.getId(),"requests",clientId,null);
        Table order=StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        if (order!= null){
            View view_order=findViewById(R.id.order);
            view_order.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.table)).setText("#"+order.getId());
            ((TextView)findViewById(R.id.seats)).setText("Seats: "+order.getSeats());
            ((TextView)findViewById(R.id.smoke)).setVisibility(order.isSmoke()?View.VISIBLE:View.INVISIBLE);
            int tableOrderTime = (int) StorageData.getSP(StorageData.SP_STRING_TIME,this,int.class);
            ((EditText) findViewById(R.id.et_time_order)).setText("" + getResources().getStringArray(R.array.time_array)[tableOrderTime]);
            ((Button)findViewById(R.id.btn_clear_order)).setEnabled(true);

        }
        else { // no order hide buttons and order details.
            findViewById(R.id.order).setVisibility(View.INVISIBLE);
            //((Button)findViewById(R.id.btn_clear_order)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.btn_clear_order)).setEnabled(false);
        }
    }
    @Override
    protected void onResume() {
     super.onResume();
        int loadLastTimeChoose=StorageData.getRaw(StorageData.RAW_STRING,mainContext);
        if (loadLastTimeChoose-first_index>=0)
            timeChoose.setSelection(loadLastTimeChoose-first_index,false);
        getOrderDetails();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturaunt_menu);
        findViewById(R.id.btn_choose_table).setOnClickListener(this);
        findViewById(R.id.btn_clear_order).setOnClickListener(this);
        findViewById(R.id.btn_chat).setOnClickListener(this);
        timeChoose=findViewById(R.id.time_choose);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                 android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       LocalTime now_time = LocalTime.now();
       int hour=now_time.getHour();
       int minute=now_time.getMinute();
       first_index=((hour-7)*2+(minute<30 ? 0:1))-2;
       if (first_index<0)first_index=0;
        String[] arr = getResources().getStringArray(R.array.time_array);
        for (int i=first_index;i<arr.length ;i++){
            adapter.add(arr[i]);
       }
        timeChoose.setAdapter(adapter);
        mainContext=this;
        timeChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StorageData.saveRaw(StorageData.RAW_STRING,position+ first_index,mainContext);
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
        //get data of restaurant
        rest=StorageData.getSP(StorageData.SP_STRING_REST,this,Restaurant.class);
        if (!rest.isAvailable())  findViewById(R.id.btn_choose_table).setEnabled(false);
        ((TextView)findViewById(R.id.rest_name)).setText(rest.getName());
        ((TextView)findViewById(R.id.tv_address)).setText(rest.getAddress());
        ((TextView)findViewById(R.id.tv_owner)).setText(rest.getOwner_name());
        ((TextView)findViewById(R.id.tv_phone)).setText(rest.getPhone());
        ((TextView)findViewById(R.id.tv_status)).setText(rest.isAvailable()? "Available":"Unavailable");
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

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> doc) {
        Table order;
        try {
             order = new Table(((Long) doc.get("table_id")).intValue(), ((Long) doc.get("table_seats")).intValue(), (Boolean) doc.get("table_smoke"));
        }catch (NullPointerException e){
            order=null;
        }
        if (order!= null){
            View view_order=findViewById(R.id.order);
            view_order.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.table)).setText("#"+order.getId());
            ((TextView)findViewById(R.id.seats)).setText("Seats: "+order.getSeats());
            ((TextView)findViewById(R.id.smoke)).setVisibility(order.isSmoke()?View.VISIBLE:View.INVISIBLE);
            int tableOrderTime = (int) StorageData.getSP(StorageData.SP_STRING_TIME,this,int.class);
            ((EditText) findViewById(R.id.et_time_order)).setText("" + getResources().getStringArray(R.array.time_array)[tableOrderTime]);
            ((Button)findViewById(R.id.btn_clear_order)).setEnabled(true);

        }
        else { // no order hide buttons and order details.
            findViewById(R.id.order).setVisibility(View.INVISIBLE);
            //((Button)findViewById(R.id.btn_clear_order)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.btn_clear_order)).setEnabled(false);
        }
    }

    @Override
    public void onDataRemoved(Map<String, Object> doc) {

    }

    @Override
    public void onDataAdded(Map<String, Object> doc) {

    }
}