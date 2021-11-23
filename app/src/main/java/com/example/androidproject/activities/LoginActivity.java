package com.example.androidproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.androidproject.IRespondDialog;
import com.example.androidproject.R;
import com.example.androidproject.StorageData;
import com.example.androidproject.TableDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends BaseActivity implements View.OnClickListener , IRespondDialog {

    private static final int RC_SIGN_IN = 152;
    private static final String TAG = "google_login";
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(this);


        // set result from sign in google account:
        //startActivityForResult(signInIntent, RC_SIGN_IN);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //if (result.getResultCode() ==  RC_SIGN_IN) {
                            if (result.getResultCode() == Activity.RESULT_OK ) {
                            // There are no request codes
                            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                            // The Task returned from this call is always completed, no need to attach
                            // a listener.
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignInResult(task);

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }
    private void signIn() {


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            TableDialog.errorOrderDialog(this,"Wellcome "+account.getDisplayName()).show(getSupportFragmentManager(), "dialog");
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            StorageData.saveSP("google_details",account.getId(),this );
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    @Override
    public void responseYES() {
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void responseNOT() {
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }
}