package com.kotsokrat.revizor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

public class AddDeviceActivity extends AppCompatActivity{
    String httpAddr, login, password;
    DBA dba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        httpAddr = sp.getString("url", "");
        login = sp.getString("login", "");
        password = sp.getString("password", "");

        dba = new DBA(this);
        dba.openDB();

        //loadDevicesData();

    }

/*    private void loadDevicesData() {
        String url = httpAddr + "?num=" + inum;

        httpClient.get(url, new JsonHttpResponseHandler() {

    }*/
}
