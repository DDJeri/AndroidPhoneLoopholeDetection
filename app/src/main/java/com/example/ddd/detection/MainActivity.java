package com.example.ddd.detection;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddd.detection.db.Picture;
import com.example.ddd.detection.service.pictureOcr;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private pictureOcr pictureocr = null; //new pictureOcr();   //图片Ocr
    private TextView text;
    private static final int OCR_FINISHED = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OCR_FINISHED:
                    text.setText("Ocr finished");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        RequestPermission();

        text = (TextView) findViewById(R.id.text);
        
        Button button = (Button) findViewById(R.id.databaseCreate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new Thread(pictureocr).start();
                //pictureocr.pictureDatabaseCreate();
            }
        });

        Button button2 = (Button) findViewById(R.id.query);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                pictureocr.pictureQuery();
                text.setText("query finish");
            }
        });

        Button button3 = (Button) findViewById(R.id.delete);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LitePal.deleteAll(Picture.class);
                text.setText("delete finish");
            }
        });
    }

    private void Init(){

        //图片ocr的初始化
        //SQLiteDatabase db = LitePal.getDatabase();
        pictureocr = new pictureOcr(getContentResolver(),handler);

    }

    private void RequestPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }else{
                    Toast.makeText(this,"You denied READ_EXTERNAL_STORAGE",Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }
}
