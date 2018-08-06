package com.example.alice.demo_linelogin;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import static com.linecorp.linesdk.LineApiResponseCode.CANCEL;
import static com.linecorp.linesdk.LineApiResponseCode.SUCCESS;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // App to App Login
        final TextView loginButton = (TextView) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    Intent loginIntent = LineLoginApi.getLoginIntent(v.getContext(), Constants.CHANNEL_ID);
                    startActivityForResult(loginIntent, REQUEST_CODE);
                }
                catch(Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });
        //Browser Login
        final TextView browserLoginButton = (TextView) findViewById(R.id.btn_browser_login);
        browserLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent LoginIntent = LineLoginApi.getLoginIntentWithoutLineAppAuth(v.getContext(), Constants.CHANNEL_ID);
                    startActivityForResult(LoginIntent, REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful

//                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();
                Log.v("getDisplayName:", result.getLineProfile().getDisplayName());
                if(result.getLineProfile().getStatusMessage()!=null) {
                    Log.v("getStatus_message:", result.getLineProfile().getStatusMessage());
                }
                if(result.getLineProfile().getUserId()!=null) {
                    Log.v("getUserID:", result.getLineProfile().getUserId());
                }
                if(result.getLineProfile().getPictureUrl().toString()!=null) {
                    Log.v("getPicture_Url:", result.getLineProfile().getPictureUrl().toString());
                }
                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user!!");
                break;

            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }
    }
}
