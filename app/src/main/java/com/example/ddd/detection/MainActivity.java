package com.example.ddd.detection;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ddd.detection.db.Picture;
import com.example.ddd.detection.service.pictureOcr;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private ContentResolver cr = null;
    private pictureOcr pictureocr = new pictureOcr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cr = getContentResolver();
        pictureocr.setCr(cr);
        //SQLiteDatabase db = LitePal.getDatabase();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
        }

        Button button = (Button) findViewById(R.id.databaseCreate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                pictureocr.pictureDatabaseCreate();
            }
        });

        Button button2 = (Button) findViewById(R.id.query);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                pictureocr.pictureQuery();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this,"You denied Permission",Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }
}
