package com.kotsokrat.revizor;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public final String t = "myTag";
    public final String STATUS_OK = "OK";
    public final String STATUS_NOTOK = "NOTOK";
    public final String STATUS_LOST = "LOST";

    String httpAddr = "http://84.204.30.84:56565/restInvent/index.php";
    DBA dba;
    EditText etInum;
    ListView lvData;
    TextView tvInfo;
    Button btn;
    SimpleCursorAdapter simpleCursorAdapter;
    AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dba = new DBA(this);
        dba.openDB();

        etInum = (EditText)findViewById(R.id.etInum);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
        lvData = (ListView)findViewById(R.id.tvData);

        httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth("ks", "1qaz3eDC");

        btn = (Button)findViewById(R.id.btnInum);
        btn.setOnClickListener(new BtnInumListener());

        String[] scaSource = new String[] {DBA.NUM, DBA.ITEM, DBA.CONF, DBA.LAST_VERIFIED};
        int[] scaDest = new int[]{ R.id.tvListInum, R.id.tvListItem, R.id.tvListConf, R.id.ivListState};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null, scaSource, scaDest, 0);
        lvData.setAdapter(simpleCursorAdapter);
        lvData.setOnItemClickListener(new ListItemClick());
        registerForContextMenu(lvData);
        dba.flushDB();
        getSupportLoaderManager().initLoader(0, null, this);
   }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dba.closeDB();

    }

    /**********************************************************************************************
     * GENERATE DISPLAY INFO AND LISTVIEW
     **********************************************************************************************/
    void printMetaData(){
        //Log.d(t, "create items list");
        Cursor cursor = dba.getAllItemsFromDB();
        /*startManagingCursor(cursor);*/

        cursor.moveToFirst();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.aboutOwner))
                .append(cursor.getString(cursor.getColumnIndex(DBA.OWNER)))
                .append(getString(R.string.aboutSenior))
                .append(cursor.getString(cursor.getColumnIndex(DBA.SENIOR)));
        tvInfo.setText(sb.toString());
    }

    void clearListView(){
       // TODO make clear
    }

    /**********************************************************************************************
     * CONTEXT MENU
     **********************************************************************************************/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int intListPostition = info.position;
        long listPosition = info.id;
        int menuItemId = item.getItemId();
        String num = dba.getInumByTableId(listPosition);

        switch (menuItemId){
            case R.id.cm_about_id:
                Log.d(t, "selected cm_about_id");
                break;
            case R.id.cm_change_user:
                Log.d(t, "selected change_user");
                break;
            case R.id.cm_change_senior:
                Log.d(t, "selected change_senior");
                break;
            case R.id.cm_set_as_notok:
                insertInumStatus(num, STATUS_NOTOK);
                updateBd();
                //getSupportLoaderManager().getLoader(0).forceLoad();
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                Log.d(t, "selected set_as_notok");
                break;
            case R.id.cm_mark_as_lost:
                insertInumStatus(num, STATUS_LOST);
                updateBd();
                //getSupportLoaderManager().getLoader(0).forceLoad();
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                Log.d(t, "selected mark_as_lost");
                break;
            default:
                Toast.makeText(this, "Unknown menu selected: " + item.toString() + " Item id: " + menuItemId + " listId: " + listPosition, Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }

    /**********************************************************************************************
     * ACTIONS
     **********************************************************************************************/

    // Установка статуса инвентаризации для определенного номера
    void insertInumStatus(String num, String status_type){
        String url = httpAddr;
        RequestParams params = new RequestParams();
        params.put("num", num);
        params.put("type", status_type);

        httpClient.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                updateBd();
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                Log.d(t, response.toString());
                Toast.makeText(MainActivity.this, "SAVE OK " + response.toString(), Toast.LENGTH_SHORT).show();

            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "SAVE FAILURE", Toast.LENGTH_SHORT).show();
                Log.d(t,"FAILURE");
            }
        });

    }

    /**********************************************************************************************
     * LoaderCallbacks methods implementations
     **********************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new RevizorCursorLoader(this, dba);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**********************************************************************************************
     * Cursor adapter
     ***********************************************************************************************/
    static class RevizorCursorLoader extends CursorLoader{
        DBA dba;

        public RevizorCursorLoader(Context context, DBA dba) {
            super(context);
            this.dba = dba;
        }

        @Override
        public Cursor loadInBackground() {
            Log.d("myTag", "revizorCursorLoader loadInBackground");
            return dba.getAllItemsFromDB();
        }
    }


    /**********************************************************************************************
     * LISTENERS
     **********************************************************************************************/

    // check position as revized
    private class ListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d(t, "clicked position: " + position + ", id: " + id);
            String num = dba.getInumByTableId(id);
            insertInumStatus(num, STATUS_OK);
            new BtnInumListener().onClick(btn);
            getSupportLoaderManager().getLoader(0).forceLoad();
            //getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }

    // get data list after button pressed
    private class BtnInumListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnInum) {
                try {
                    Log.d(t, "onClick!");
                    int inum = Integer.parseInt(etInum.getText().toString());
                    loadALLtoDbByInum(inum);
                    //getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                    printMetaData();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.toast_if_input_not_number), Toast.LENGTH_SHORT).show();
                    clearListView();
                }
            }
        }
    }

    public void updateBd(){
        int inum = Integer.parseInt(etInum.getText().toString());
        loadALLtoDbByInum(inum);
        //simpleCursorAdapter.notifyDataSetChanged();
    }


    public void loadALLtoDbByInum(int inum){

        String url = httpAddr + "?num=" + inum;

        httpClient.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200 && response.length() > 0){
                    //clearListView();
                    dba.flushDB();
                    dba.saveItemsToDB(response);

                    getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                }
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200 && response.length() > 0){
                    //new DBA(context).saveItemsToDB(response);
                    clearListView();
                    Log.d(t, response.toString());
                    Toast.makeText(MainActivity.this, "NO DATA IN DB", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d(t,"FAILURE");
            }
        });
    }
}
