package com.kotsokrat.revizor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class REST {
    private String serverUrl;
    AsyncHttpClient httpClient;
    JSONArray data;

    REST(String serverUrl){
        this.serverUrl = serverUrl;
        httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth("ks", "1qaz3eDC");
    }

    public void getItemsByInum(SQLiteDatabase db, int inum){
        final SQLiteDatabase thisDb = db;

        RequestParams params = new RequestParams();
        params.put("num", inum);

        httpClient.get(serverUrl + "?num=" + inum, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                log(Integer.toString(statusCode));
                log(response.toString());
                if (statusCode == 200 && response.length() > 0){
                    DB bla = new DB();
                    bla.loadItemsFromREST(response, thisDb);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                log(Integer.toString(statusCode));
            }

            void log(String str) {
                Log.d("myTag", str);
            }
        });
    }
}