package com.rowem.passipadcloud.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rowem.passipadcloud.pref.Pref;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        // Get new FCM registration token
                        String token = task.getResult();

                        Pref.INSTANCE.load(SplashActivity.this).setPushToken(token);

                        startHome();
                    }else{
                    }
                });
    }

    private void startHome(){
        Intent i = new Intent(this, HomeActivity.class);

        startActivity(i);
        finish();
    }
}