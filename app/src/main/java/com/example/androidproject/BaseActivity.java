package com.example.androidproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BaseActivity  extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    protected void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        int tableOrderTime = (int) StorageData.getSP(StorageData.SP_STRING_TIME,this,int.class);
        serviceIntent.putExtra("inputExtra", getResources().getStringArray(R.array.time_array)[tableOrderTime]);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    protected void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.exit:
                ExitDialogFrag.newInstance().show(getSupportFragmentManager(), "dialog");
                return true;
            case R.id.lastSMS:
                String smsMessage=StorageData.getSP(StorageData.SP_SMS,this,String.class);
                if (smsMessage!=null)
                    Toast.makeText(this, smsMessage, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "No SMS Found", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
