package com.example.androidproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StorageData {
    public static final String SP_STRING_TABLE="table";
    public static final String SP_STRING_TIME="time";
    public static final String RAW_STRING="choose_time";
    public static final String SP_SMS="sms";

    public static int getRaw(String filename , Context ctx) {
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
    public static void saveRaw(String filename, int data , Context ctx) {
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
    public static <T> void saveSP(String filename, T table , Context context) {
        SharedPreferences mPrefs  = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(table);
        prefsEditor.putString("sp_table", json);
        prefsEditor.commit();
    }
    public static <T> T getSP(String filename , Context ctx, Class<T> type) {
        T data;
        SharedPreferences mPrefs  = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = mPrefs.getString("sp_table", "");

        data  = gson.fromJson(json,type);
        if(data==null)
        {
            return null;
        }
        return data;
    }
    public static void clearSP(String filename, Context context){
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
