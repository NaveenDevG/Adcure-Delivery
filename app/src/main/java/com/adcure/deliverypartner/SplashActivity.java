package com.adcure.deliverypartner;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            FirebaseApp.initializeApp(this);

            rootRef = FirebaseDatabase.getInstance().getReference();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

                        if (firebaseUser != null) {
                            String currentUserid = mAuth.getCurrentUser().getUid();
                        }
                        currentUser = mAuth.getCurrentUser();
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            sendUserToMainActivity();
                        } else {
                            sendUserToLoginActivity();
                        }
                    } else {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(SplashActivity.this);

                        builder.setMessage("Please check your internet connection")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Toast.makeText(getApplicationContext(), "closing your app",
                                                Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                        finish();
                                    }
                                });

                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("ALERT");
                        alert.show();
                    }

                }
            }, 3000);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyUserExistance() {
        mAuth = FirebaseAuth.getInstance();

        String useruid = mAuth.getCurrentUser().getUid();
        String useruid1 = mAuth.getCurrentUser().getUid();

        rootRef.child("Users").child(useruid1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    Toast.makeText(SplashActivity.this, "Welcome..", Toast.LENGTH_SHORT).show();
                    sendUserToMainActivity();

//                    onStop();
                } else {
                    sendUserToLoginActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, OtpActivity.class);
        startActivity(intent);
        finish();
    }
}