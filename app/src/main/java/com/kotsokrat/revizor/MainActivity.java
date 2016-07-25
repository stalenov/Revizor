package com.kotsokrat.revizor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class MainActivity extends AppCompatActivity {
    public String httpAddr = "http://84.204.30.84:56565/restInvent/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tvStatusCode = (TextView)findViewById(R.id.tvStatusCode);
        final TextView tvHeaderw = (TextView)findViewById(R.id.tvHeaders);
        final TextView tvMsgBody = (TextView)findViewById(R.id.tvMsgBody);

        REST rest = new REST(httpAddr);
        rest.getItemsByInum(370);




/*        RequestParams params = new RequestParams();
        params.put("num", "9000");
        String str = "";
        client.post(httpAddr, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("myTag", response.toString());

                }
        });*/

 /*       client.get(httpAddr + num, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                tvStatusCode.setText(Integer.toString(statusCode));
                tvMsgBody.setText(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "ERROR" + Integer.toString(statusCode), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
