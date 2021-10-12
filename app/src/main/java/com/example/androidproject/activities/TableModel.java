package com.example.androidproject.activities;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.androidproject.IRespondDialog;
import com.example.androidproject.R;
import com.example.androidproject.RecyclerTableModelAdapter;
import com.example.androidproject.StorageData;
import com.example.androidproject.TableDialog;
import com.example.androidproject.data.Table;

import java.util.ArrayList;

public class TableModel extends BaseActivity implements IRespondDialog {
    private RecyclerTableModelAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.restuarant_model_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //getting the ordered table in order to make sure it's marked off
        Table orderedTable= StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        ArrayList<Table> data=testTables();
        int index=data.indexOf(orderedTable);
        if (index>-1){
            data.set(index,orderedTable);
        }
        // setting up the recycler
        recyclerAdapter = new RecyclerTableModelAdapter(data);
        recyclerAdapter.setOrderedTable(orderedTable);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50000);
        recyclerView.setDrawingCacheEnabled(true);
    }

    // response for when the user presses the order button
    public void orderBtnClicked(View v){
        // the function puts the appropriate dialog for each case [table is full -cant select  , no table selected - tale the user  , and an available table confirm  ]
        if (recyclerAdapter.getSelectedTable()==null) {
            TableDialog.errorOrderDialog(this, "no table selected").show(getSupportFragmentManager(), "dialog");
        }
        else if (recyclerAdapter.getSelectedTable().isFull())
            TableDialog.errorOrderDialog(this, "table number "+recyclerAdapter.getSelectedTable().getId()+ " is Full").show(getSupportFragmentManager(), "dialog");
        else {

            TableDialog.goodOrderDialog(this).show(getSupportFragmentManager(),"dialog");


        }
    }
    // creates  the tables
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
        // save data about table

            //check old order
        if (recyclerAdapter.getOrderedTable()!=null){
            recyclerAdapter.getOrderedTable().setFull(false);
            int indexPrevOrder=recyclerAdapter.getTables().indexOf( recyclerAdapter.getOrderedTable());
            recyclerAdapter.notifyItemChanged(indexPrevOrder);
        }
            //selected table is the new ordered table
        recyclerAdapter.getSelectedTable().setFull(true);
        recyclerAdapter.setOrderedTable(recyclerAdapter.getSelectedTable());
        StorageData.saveSP(StorageData.SP_STRING_TABLE, recyclerAdapter.getSelectedTable() ,this);
        StorageData.saveSP(StorageData.SP_STRING_TIME, StorageData.getRaw(StorageData.RAW_STRING,this) ,this);
        toastMsg("order committed for table number "+ recyclerAdapter.getSelectedTable().getId());
        //updating recycler of data change
        int indexOrder=recyclerAdapter.getTables().indexOf( recyclerAdapter.getSelectedTable());
        recyclerAdapter.notifyItemChanged(indexOrder);
        stopService();
        startService();

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