package com.example.androidproject;

import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity  extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
