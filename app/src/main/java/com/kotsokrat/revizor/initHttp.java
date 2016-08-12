package com.kotsokrat.revizor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

public class InitHttp {
    String httpAddr, login, password;
    Context context;
    public InitHttp(Context context) {
        this.context = context;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        httpAddr = sp.getString("url", "");
        login = sp.getString("login", "");
        password = sp.getString("password", "");

        if (httpAddr.equals("") || login.equals("") || password.equals("")) {
            Toast.makeText(context, context.getString(R.string.no_prefs_msg), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, PrefActivity.class);
            context.startActivity(intent);
        }
    }

    public AsyncHttpClient init(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth(login, password);
        return httpClient;
    }
    public String getAddr(){
        return httpAddr;
    }


}
