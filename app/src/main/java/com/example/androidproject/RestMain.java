package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

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
    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_choose_table){
            Intent intent = new Intent(this,TableModel.class);
            startActivity(intent);
            //startActivityForResult(intent,123);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturaunt_menu);
        findViewById(R.id.btn_choose_table).setOnClickListener(this);
        timeChoose=findViewById(R.id.time_choose);
        mainContext=this;
        int loadLastTimeChoose=getRaw("choose_time",mainContext);
        if (loadLastTimeChoose>=0)
        timeChoose.setSelection(loadLastTimeChoose,false);
        timeChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                saveRaw("choose_time",position,mainContext);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }
    public  void saveRaw(String filename, int data , Context ctx) {
        FileOutputStream fos;
        try {
            fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public int getRaw(String filename , Context ctx) {
        FileInputStream fos;
        int data=0;
        try {
            fos = ctx.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fos);
            data=(int) ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }


}