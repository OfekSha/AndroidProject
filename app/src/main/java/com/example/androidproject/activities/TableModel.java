package com.example.androidproject.activities;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.androidproject.FireBaseListener;
import com.example.androidproject.IRespondDialog;
import com.example.androidproject.R;
import com.example.androidproject.RecyclerTableModelAdapter;
import com.example.androidproject.StorageData;
import com.example.androidproject.TableDialog;
import com.example.androidproject.controller.FireBaseConnection;
import com.example.androidproject.data.Restaurant;
import com.example.androidproject.data.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableModel extends BaseActivity implements IRespondDialog, FireBaseListener {
    private RecyclerTableModelAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.restuarant_model_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //getting the ordered table in order to make sure it's marked off
        Table orderedTable= StorageData.getSP(StorageData.SP_STRING_TABLE,this,Table.class);
        ArrayList<Table> data=new ArrayList<Table>();
        int index;
        if(orderedTable!=null) {
            index = data.indexOf(orderedTable);
            if (index > -1) {
                data.set(index, orderedTable);
            }
        }
        // setting up the recycler
        recyclerAdapter = new RecyclerTableModelAdapter(data);
        recyclerAdapter.setOrderedTable(orderedTable);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50000);
        recyclerView.setDrawingCacheEnabled(true);

        // connect database and read live data of tables.
        String restaurant_id=StorageData.getSP(StorageData.SP_STRING_REST,this, Restaurant.class).getId();
        FireBaseConnection.instance.changeListener(this);
        FireBaseConnection.instance.connectData(restaurant_id);
    }

    // response for when the user presses the order button
    public void orderBtnClicked(View v){
        // the function puts the appropriate dialog for each case [table is full -cant select  , no table selected - tale the user  , and an available table confirm  ]
        if (recyclerAdapter.getSelectedTable()==null) { // empty cell
            TableDialog.errorOrderDialog(this, "no table selected").show(getSupportFragmentManager(), "dialog");
        }
        else if (recyclerAdapter.getSelectedTable().isFull()) // table is full
            TableDialog.errorOrderDialog(this, "table number "+recyclerAdapter.getSelectedTable().getId()+ " is Full").show(getSupportFragmentManager(), "dialog");
        else { // good table
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
    public void responseYES() { // user accept the table order and send the request to the restaurant.
       //@@TODO: add data of table to request and metadata of user.
        String restaurant_id=StorageData.getSP(StorageData.SP_STRING_REST,this, Restaurant.class).getId();
        String client_id=StorageData.getSP("google_details",this ,String.class);
        HashMap data=new HashMap();
        data.put("table_id",recyclerAdapter.getSelectedTable().getId());
        data.put("time",StorageData.getRaw(StorageData.RAW_STRING,this));
        data.put("approved",false);
        data.put("client_id", client_id);
        FireBaseConnection.instance.connectCollectionInDocument(restaurant_id,"requests",client_id,data);
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

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> doc) {
        if (doc.get("table_id")!=null){ // table order request change
            if ((boolean)doc.get("approved")){ // if table approve by the restaurant do logic operations for show client his table.
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
            }
        }
        else { // restaurant data has change
            ArrayList<Table> tables = new ArrayList<Table>();
            ((ArrayList) doc.get("tables")).forEach(el -> {
                HashMap tableHashMap = (HashMap) ((HashMap) el).get("Table");
                Table table;
                table = new Table(fromLongToInt((Long) tableHashMap.get("id")), fromLongToInt((Long) tableHashMap.get("seats")), (Boolean) tableHashMap.get("isSmoke"));
                tables.add(table);

            });
            recyclerAdapter.setTables(tables);
        }
    }
    private int fromLongToInt(Long l){
        return l.intValue();
    }
    @Override
    public void onDataRemoved(Map<String, Object> doc) {

    }

    @Override
    public void onDataAdded(Map<String, Object> doc) {
        ArrayList<Table> tables=new ArrayList<Table>();
        ((ArrayList)doc.get("tables")).forEach(el->{
            HashMap tableHashMap = (HashMap) el;
            Table table;
            table=new Table(Integer.valueOf((Integer) tableHashMap.get("id")),Integer.valueOf((Integer) tableHashMap.get("seats")),Boolean.valueOf((boolean)tableHashMap.get("isSmoke")));
            tables.add(table);

        });
        recyclerAdapter.setTables(tables);
    }
}