package com.kotsokrat.revizor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import cz.msebera.android.httpclient.Header;

public class REST {
    Context context;

    private final String[] data = new String[1];

    REST(Context context){
        this.context = context;
       // httpClient = new AsyncHttpClient();
        //httpClient.setBasicAuth("ks", "1qaz3eDC");
    }



}