package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class TableModel extends AppCompatActivity implements IRespondDialog {
    private RecyclerTableModelAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.restuarant_model_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        Table orderedTable=StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        ArrayList<Table> data=testTables();
        int index=data.indexOf(orderedTable);
        if (index>-1){
            data.set(index,orderedTable);
        }
        recyclerAdapter = new RecyclerTableModelAdapter(data);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50000);
        recyclerView.setDrawingCacheEnabled(true);
    }

    public void orderBtnClicked(View v){
        if (recyclerAdapter.getSelectedTable()==null) {
            TableDialog.errorOrderDialog(this, "no table selected").show(getSupportFragmentManager(), "dialog");
        }
        else if (recyclerAdapter.getSelectedTable().isFull())
            TableDialog.errorOrderDialog(this, "table number "+recyclerAdapter.getSelectedTable().getId()+ " is Full").show(getSupportFragmentManager(), "dialog");
        else {

            TableDialog.goodOrderDialog(this).show(getSupportFragmentManager(),"dialog");
        }
    }
    private ArrayList<Table> testTables(){
        ArrayList<Table> test= new ArrayList<Table>();
        for (int i=0;i<50;i++){
            if (i%6== 5) test.add(null);
            else {
                test.add(new Table(i, i % 5, i % 3 == 1 ? true : false));
                if (i%4==2)test.get(i).setFull(true);
            }
        }
            return test;
    }
    // response yes to order accept ordering table
    @Override
    public void responseYES() {
        // need to save data about table
        recyclerAdapter.getSelectedTable().setFull(true);
        recyclerAdapter.notifyDataSetChanged();
        StorageData.saveSP(StorageData.SP_STRING_TABLE, recyclerAdapter.getSelectedTable() ,this);
        StorageData.saveSP(StorageData.SP_STRING_TIME, StorageData.getRaw(StorageData.RAW_STRING,this) ,this);
        toastMsg("order committed for table number "+ recyclerAdapter.getSelectedTable().getId());
    }
    // response not to order accept ordering table
    @Override
    public void responseNOT() {
        toastMsg("order canceled");
    }
    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}