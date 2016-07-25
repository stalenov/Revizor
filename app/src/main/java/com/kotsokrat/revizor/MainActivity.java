package com.kotsokrat.revizor;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public String httpAddr = "http://84.204.30.84:56565/restInvent/index.php";
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();


        final TextView tvStatusCode = (TextView)findViewById(R.id.tvStatusCode);
        final TextView tvHeaderw = (TextView)findViewById(R.id.tvHeaders);
        final TextView tvMsgBody = (TextView)findViewById(R.id.tvMsgBody);

        new REST(httpAddr).getItemsByInum(db, 370);




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
