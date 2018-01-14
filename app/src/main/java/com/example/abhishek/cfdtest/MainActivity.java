package com.example.abhishek.cfdtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private RelativeLayout layout;
    private ImageView img;
    private TextView name, phone, email;
    private Button btnLogOut;
    private SignInButton btnLogIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE =9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout=(RelativeLayout) findViewById(R.id.layout1);
        name=(TextView) findViewById(R.id.text1);
        phone=(TextView) findViewById(R.id.text2);
        email=(TextView) findViewById(R.id.text3);
        img =(ImageView) findViewById(R.id.img);
        btnLogIn=(SignInButton) findViewById(R.id.btnLogIn);
        btnLogOut=(Button) findViewById(R.id.btnLogOut);
        btnLogIn.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        layout.setVisibility(GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient =new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogIn:
                signIn();
                break;
            case R.id.btnLogOut:
                signout();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signIn(){
        Intent intent =Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }
    private void signout(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }
    private void handleResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String usrname=account.getDisplayName();
            String usremail=account.getEmail();
            //String usrimgurl=account.getPhotoUrl().toString();
            name.setText(usrname);
            email.setText(usrname);
            //Glide.with(this).load(usrimgurl).into(img);
            updateUI(true);
        }
        else{
            updateUI(false);
        }
    }
    private void updateUI(boolean isLogin){
        if (isLogin){
            layout.setVisibility(View.VISIBLE);
            btnLogIn.setVisibility(View.GONE);
        }
        else{
            layout.setVisibility(View.GONE);
            btnLogIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
