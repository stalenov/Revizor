package com.kotsokrat.revizor;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar();
        getFragmentManager().beginTransaction().add(R.id.frame, new RevizorFragment()).commit();

   }
}
