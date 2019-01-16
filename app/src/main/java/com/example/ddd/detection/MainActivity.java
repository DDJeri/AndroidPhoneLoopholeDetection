package com.example.ddd.detection;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ddd.detection.db.Picture;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteDatabase db = LitePal.getDatabase();

        Picture picture = new Picture();
        picture.setOcrResult("中国");
        picture.setHasWord(true);
        picture.save();
    }
}
