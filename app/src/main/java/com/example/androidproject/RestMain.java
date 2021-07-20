package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RestMain extends AppCompatActivity implements View.OnClickListener {
    Spinner timeChoose;
    Context mainContext;
    Menu menu;
    static public String lastSMS="" ;
    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_choose_table){
            Intent intent = new Intent(this,TableModel.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
     super.onResume();
        Table order=StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        //TODO: check int null value
        int loadLastTimeChoose=StorageData.getRaw(StorageData.RAW_STRING,mainContext);
        if (loadLastTimeChoose>=0)
            timeChoose.setSelection(loadLastTimeChoose,false);
        if (order!= null){
            View view_order=findViewById(R.id.order);
            view_order.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.table)).setText("#"+order.getId());
            ((TextView)findViewById(R.id.seats)).setText("Seats: "+order.getSeats());
            ((TextView)findViewById(R.id.smoke)).setVisibility(order.isSmoke()?View.VISIBLE:View.INVISIBLE);
            int tableOrderTime = (int) StorageData.getSP(StorageData.SP_STRING_TIME,this,int.class);
            ((EditText) findViewById(R.id.et_time_order)).setText("" + getResources().getStringArray(R.array.time_array)[tableOrderTime]);

        }
        else findViewById(R.id.order).setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturaunt_menu);
        findViewById(R.id.btn_choose_table).setOnClickListener(this);
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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu,menu);
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT)
        {
            menu.getItem(1).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.exit:
                ExitDialogFrag.newInstance().show(getSupportFragmentManager(), "dialog");
                return true;
            case R.id.lastSMS:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}