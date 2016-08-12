package com.kotsokrat.revizor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

public class InitHttpClient {
    String httpAddr, login, password;
    AsyncHttpClient httpClient;
    Context context;
    public InitHttpClient(Context context) {
        this.context = context;
    }

    public AsyncHttpClient init(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        httpAddr = sp.getString("url", "");
        login = sp.getString("login", "");
        password = sp.getString("password", "");

        if (httpAddr.equals("") || login.equals("") || password.equals("")) {
            Toast.makeText(context, context.getString(R.string.no_prefs_msg), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, PrefActivity.class);
            //startActivity(intent);
        }

        httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth(login, password);
        return httpClient;

    }


}
