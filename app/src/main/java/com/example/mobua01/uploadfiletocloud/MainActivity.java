package com.example.mobua01.uploadfiletocloud;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.users.FullAccount;
import com.example.mobua01.uploadfiletocloud.utils.SharedPreferenceKey;
import com.example.mobua01.uploadfiletocloud.utils.SharedPreferenceWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 27;
    private String accessToken = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!tokenExists()) {
            //No token
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        //Get Token
        accessToken = retrieveAccessToken();

        //Get User Account Details
        getUserAccount();

        initListeners();


    }

    private void initListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFileToUpload();
            }
        });
    }

    private void selectFileToUpload() {
        if (accessToken == null) return;

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent();
        intent.setType("application/* | text/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Upload to Dropbox"), IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        if (requestCode == IMAGE_REQUEST_CODE) {
                //Image URI received
                File file = new File(URI_to_Path.getPath(getApplication(), data.getData()));
                if (file != null) {
                    //Initialize UploadTask
                    new UploadTask(DropboxClient.getClient(accessToken), file, getApplicationContext()).execute();
                }
        }
    }

    private void getUserAccount() {

        if (accessToken == null) return;

        new UserAccountTask(DropboxClient.getClient(accessToken), new UserAccountTask.AccountDetailListener() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Get account's detail
                Log.d("User", account.getEmail());
                Log.d("User", account.getName().getDisplayName());
                Log.d("User", account.getAccountType().name());
                updateUI(account);
            }

            @Override
            public void onError(Exception error) {
                Log.d("User", "Error receiving account details.");
            }
        }).execute();

    }

    private void updateUI(FullAccount account) {
        ImageView profile = (ImageView) findViewById(R.id.imageView);
        TextView name = (TextView) findViewById(R.id.name_textView);
        TextView email = (TextView) findViewById(R.id.email_textView);

        name.setText(account.getName().getDisplayName());
        email.setText(account.getEmail());


    }

    private String retrieveAccessToken() {
        String accessToken = SharedPreferenceWriter.getInstance(MainActivity.this).getString(SharedPreferenceKey.TOKEN);
        if (accessToken == null) {
            Log.d("AccessToken Status", "No token found");
            return null;
        } else {
            //accessToken already exists
            Log.d("AccessToken Status", "Token exists");
            return accessToken;
        }
    }

    private boolean tokenExists() {
        String accessToken = SharedPreferenceWriter.getInstance(MainActivity.this).getString(SharedPreferenceKey.TOKEN);
        return accessToken != null;

    }

}
