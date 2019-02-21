package com.example.mobua01.uploadfiletocloud.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mobua01.uploadfiletocloud.MainActivity;
import com.example.mobua01.uploadfiletocloud.R;

/**
 * Created by mobua01 on 24/10/18.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SharedPreferenceKey.TOKEN,null);

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
