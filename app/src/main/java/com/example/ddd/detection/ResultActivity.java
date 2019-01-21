package com.example.ddd.detection;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ddd.detection.db.AppInfo;
import com.example.ddd.detection.db.Message;
import com.example.ddd.detection.db.Picture;
import com.example.ddd.detection.util.OcrAdapter;
import com.example.ddd.detection.util.OcrResult;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ResultActivity extends AppCompatActivity {

    private List<OcrResult> ocrShow = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Picture> pictures = LitePal.findAll(Picture.class);
        for(Picture picture:pictures){
            ocrShow.add(new OcrResult(picture.getOcrResult(),picture.getPath()));
        }

        List<Message> messages = LitePal.findAll(Message.class);
        for(Message message:messages){
            ocrShow.add(new OcrResult(message.getBody(),""));
        }

        List<AppInfo> apps = LitePal.findAll(AppInfo.class);
        for(AppInfo appInfo:apps){
            ocrShow.add(new OcrResult(appInfo.getAppName() + "\n" + appInfo.getPublickey(),""));
        }

        OcrAdapter adapter = new OcrAdapter(ResultActivity.this,R.layout.image_item,ocrShow);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
