package com.kotsokrat.revizor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class AddDeviceActivity extends AppCompatActivity{
    String httpAddr;
    AsyncHttpClient httpClient;
    DBA dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        dba = new DBA(this);
        dba.openDB();

        InitHttp ih = new InitHttp(this);
        httpClient = ih.init();
        httpAddr = ih.getAddr();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadDevicesData();

    }

    private void loadDevicesData() {
        String url = httpAddr + "?deviceconfsandnames=1";

        httpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                dba.flushDevicesDB();
                dba.saveDevicesToDB(response);
                Toast.makeText(AddDeviceActivity.this, "All devices loaded", Toast.LENGTH_SHORT).show();
                Log.d("myLog", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AddDeviceActivity.this, "loadDeviceData error, status " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
