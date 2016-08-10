package com.kotsokrat.revizor;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.widget.SimpleCursorAdapter;
import android.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

public class RevizorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
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
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_revizor, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        dba = new DBA(context);
        dba.openDB();

        etInum = (EditText)getView().findViewById(R.id.etInum);
        tvInfo = (TextView)getView().findViewById(R.id.tvInfo);
        lvData = (ListView)getView().findViewById(R.id.tvData);
        btn = (Button)getView().findViewById(R.id.btnInum);

        httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth("ks", "1qaz3eDC");

        // листенеры
        btn.setOnClickListener(new BtnInumListener());
        etInum.setOnEditorActionListener(new EtInumListener());

        // листвью
        String[] scaSource = new String[] {DBA.NUM, DBA.ITEM, DBA.CONF, DBA.LAST_VERIFIED_ICON};
        int[] scaDest = new int[]{ R.id.tvListInum, R.id.tvListItem, R.id.tvListConf, R.id.ivListState};
        simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_item, null, scaSource, scaDest, 0);
        lvData.setAdapter(simpleCursorAdapter);
        lvData.setOnItemClickListener(new ListItemClick());
        registerForContextMenu(lvData);
        dba.flushDB();
        getActivity().getLoaderManager().initLoader(0, null, this);

        //getActivity().getActionBar();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dba.closeDB();
    }


    /**********************************************************************************************
     * Menu
     **********************************************************************************************/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_add_device:
                Toast.makeText(context, "Данная функция пока не реализована!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_preferences:
                /*Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);*/
                getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************
     * GENERATE DISPLAY INFO
     **********************************************************************************************/
    void printMetaData(){
        Cursor cursor = dba.getAllItemsFromDB();
        cursor.moveToFirst();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.aboutOwner))
                .append(" ")
                .append(cursor.getString(cursor.getColumnIndex(DBA.OWNER)))
                .append(getString(R.string.aboutSenior))
                .append(" ")
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
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long listPosition = info.id;
        int menuItemId = item.getItemId();
        String num = dba.getInumByTableId(listPosition);

        switch (menuItemId){
            case R.id.cm_about_id:
                Log.d(t, "selected cm_about_id");
                show_about(listPosition);
                break;
            case R.id.cm_change_user:
                Toast.makeText(context, "Данная функция пока не реализована!!!", Toast.LENGTH_SHORT).show();
                Log.d(t, "selected change_user");
                break;
            case R.id.cm_change_senior:
                Toast.makeText(context, "Данная функция пока не реализована!!!", Toast.LENGTH_SHORT).show();
                Log.d(t, "selected change_senior");
                break;
            case R.id.cm_set_as_notok:
                postNewStatus(num, STATUS_NOTOK);
                Log.d(t, "selected set_as_notok");
                break;
            case R.id.cm_mark_as_lost:
                postNewStatus(num, STATUS_LOST);
                Log.d(t, "selected mark_as_lost");
                break;
            default:
                Toast.makeText(context, "Unknown menu selected: " + item.toString() + " Item id: " + menuItemId + " listId: " + listPosition, Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }


    /**********************************************************************************************
     * LoaderCallbacks methods implementations
     **********************************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new RevizorCursorLoader(context, dba);
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
    static class RevizorCursorLoader extends CursorLoader {
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
    // Отслеживание нажатий кнопки "Поиск" на виртуальной клавиатуре
    private class EtInumListener implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                getSaveShowAll();
                Log.d("myTag", "SEARCH SOFTKEY PRESSED!!!!!!");
            }
            return false;
        }
    }


    // Отслеживание короткого клика на пункте меню (пометка ОК)
    private class ListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(t, "clicked position: " + position + ", id: " + id);
            String num = dba.getInumByTableId(id);
            postNewStatus(num, STATUS_OK);
            new BtnInumListener().onClick(btn);
        }
    }


    // Отслеживание нажания вьюва с кнопкой "Поиск"
    private class BtnInumListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnInum) {
                try {
                    Log.d(t, "onClick!");
                    getSaveShowAll();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.toast_if_input_not_number), Toast.LENGTH_SHORT).show();
                    clearListView();
                }
            }
        }
    }

    // Отображение подробной информации по выбранной позиции
    private void show_about(long id){
        Bundle bundle = dba.getAllByTableId(id);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.about_item_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setPositiveButton(getString(R.string.show_about_close_btn), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog ab = builder.create();
        ab.show();
        //ImageView iv = (ImageView)ab.findViewById(R.id.ivDialog);
        TextView tvDialogNum = (TextView)ab.findViewById(R.id.tvDialogNum);
        TextView tvDialogItem = (TextView)ab.findViewById(R.id.tvDialogItem);
        TextView tvDialogConf = (TextView)ab.findViewById(R.id.tvDialogConf);
        TextView tvDialogOwner = (TextView)ab.findViewById(R.id.tvDialogOwner);
        TextView tvDialogDepartment = (TextView)ab.findViewById(R.id.tvDialogDepartment);
        TextView tvDialogSenior = (TextView)ab.findViewById(R.id.tvDialogSenior);
        TextView tvDialogBarcode = (TextView)ab.findViewById(R.id.tvDialogBarcode);
        TextView tvDialogLastVerified = (TextView)ab.findViewById(R.id.tvDialogLastVerified);

        //iv.setImageResource(Integer.parseInt(bundle.getString(DBA.LAST_VERIFIED_ICON)));
        tvDialogNum.setText(bundle.getString(DBA.NUM));
        tvDialogItem.setText(bundle.getString(DBA.ITEM));
        tvDialogConf.setText(bundle.getString(DBA.CONF));
        tvDialogOwner.setText(bundle.getString(DBA.OWNER));
        tvDialogDepartment.setText(bundle.getString(DBA.DEPARTMENT));
        tvDialogSenior.setText(bundle.getString(DBA.SENIOR));
        tvDialogBarcode.setText(bundle.getString(DBA.BARCODE));
        tvDialogLastVerified.setText(bundle.getString(DBA.LAST_VERIFIED));
    }


    /**********************************************************************************************
     * Асинхронный обмен с сетью и обновление экрана
     **********************************************************************************************/
    // Загрузка всех данных с сервера по определенному инв. номеру
    public void getSaveShowAll(){
        int inum = Integer.parseInt(etInum.getText().toString());
        String url = httpAddr + "?num=" + inum;
        httpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200 && response.length() > 0) {
                    dba.flushDB();
                    dba.saveItemsToDB(response);
                    printMetaData();
                    getActivity().getLoaderManager().getLoader(0).forceLoad();
                }
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200 && response.length() > 0) {
                    clearListView();
                    Log.d(t, response.toString());
                    Toast.makeText(context, getString(R.string.no_inum), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show();
                Log.d(t, "FAILURE");
                super.onFailure(statusCode, headers, throwable, errorResponse);

                //TODO trowl network error

            }
        });
    }


    // Установка статуса инвентаризации для определенного номера
    void postNewStatus(String num, String status_type){
        String url = httpAddr;
        RequestParams params = new RequestParams();
        params.put("num", num);
        params.put("type", status_type);

        httpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(t, response.toString());
                getSaveShowAll();
                getActivity().getLoaderManager().getLoader(0).forceLoad();
                Toast.makeText(context, "SAVE OK " + response.toString(), Toast.LENGTH_SHORT).show();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, "ERROR WITH UPDATE ITEM STATUS", Toast.LENGTH_SHORT).show();
                Log.d(t, "FAILURE");
            }
        });
    }


}
