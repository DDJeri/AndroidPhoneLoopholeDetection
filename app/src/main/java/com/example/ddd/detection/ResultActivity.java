package com.example.ddd.detection;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
            TextView text = (TextView) findViewById(R.id.title_text);
            text.setText("Result");
        }
    }
}
