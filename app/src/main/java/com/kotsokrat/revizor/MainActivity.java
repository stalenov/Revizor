package com.kotsokrat.revizor;

import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    public String httpAddr = "http://84.204.30.84:56565/restInvent/index.php";

    JSONArray jsonArray;
    DB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DBHelper dbHelper = new DBHelper(this);
//        db = dbHelper.getWritableDatabase();


        final TextView tvStatusCode = (TextView)findViewById(R.id.tvStatusCode);
        final TextView tvHeaderw = (TextView)findViewById(R.id.tvHeaders);
        final TextView tvMsgBody = (TextView)findViewById(R.id.tvMsgBody);

        //new REST(httpAddr).getItemsByInum(db, 370);

        DB db = new DB(this);
        db.openDb();



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

        loadItemsToDB(370);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDb();
    }

    void showToast(int statusCode){
        Toast.makeText(MainActivity.this, Integer.toString(statusCode), Toast.LENGTH_SHORT).show();
        TextView tvMsgBody = (TextView)findViewById(R.id.tvMsgBody);
        tvMsgBody.setText(jsonArray.toString());
    }

    void loadItemsToDB(int iNum){
        getItemsFromHTTP(iNum);

        db.saveItemsToDB;

    }


    void getItemsFromHTTP(int num) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("ks", "1qaz3eDC");

        httpAddr = httpAddr + "?num=" + Integer.toString(num);

        client.get(httpAddr, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                //tvStatusCode.setText(Integer.toString(statusCode));
                jsonArray = response;
                showToast(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // TODO обрабатывать ошибки
            }
        });


    }


}
