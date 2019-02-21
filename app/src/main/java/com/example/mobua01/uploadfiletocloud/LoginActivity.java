package com.example.mobua01.uploadfiletocloud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.android.Auth;
import com.example.mobua01.uploadfiletocloud.utils.SharedPreferenceKey;
import com.example.mobua01.uploadfiletocloud.utils.SharedPreferenceWriter;

/**
 * Created by mobua01 on 23/10/18.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initListeners();
    }

    private void initListeners() {
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token(); //generate Access Token
        if (accessToken != null) {
            //Store accessToken in SharedPreferences
            SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN,accessToken);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
