package com.kotsokrat.revizor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public String httpAddr = "http://84.204.30.84:56565/restInvent/index.php?num=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String num = "256";

        final TextView tvStatusCode = (TextView)findViewById(R.id.tvStatusCode);
        final TextView tvHeaderw = (TextView)findViewById(R.id.tvHeaders);
        final TextView tvMsgBody = (TextView)findViewById(R.id.tvMsgBody);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(httpAddr + num, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                tvStatusCode.setText(Integer.toString(statusCode));
                StringBuilder sb = new StringBuilder();
                //sb.append(headers);
                for (int i = 0; i < headers.length; i++) {
                    sb.append(headers[i]);
                }
                tvHeaderw.setText(sb.toString());

                StringBuilder sbResp = new StringBuilder();
                for (int i = 0; i < responseBody.length; i++){
                    sbResp.append(responseBody[i]);
                }


                tvMsgBody.setText(sbResp);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }




}
