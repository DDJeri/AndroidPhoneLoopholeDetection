package com.example.ddd.detection;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ddd.detection.service.message.messageService;
import com.example.ddd.detection.service.picture.pictureService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private pictureService.pictureBinder pictureBinder;
    private messageService.messageBinder messageBinder;
    public static ProgressBar progressBar;
    public static ProgressBar messageProgressBar;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pictureBinder = (pictureService.pictureBinder) service;
        }
    };
    private ServiceConnection messageConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageBinder = (messageService.messageBinder) service;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button PictureDetection = (Button) findViewById(R.id.PictureDetection);
        Button MessageDetection = (Button) findViewById(R.id.ShortMessageDetection);
        Button Result = (Button) findViewById(R.id.Result);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);progressBar.setVisibility(View.INVISIBLE);
        messageProgressBar = (ProgressBar) findViewById(R.id.message_progress_bar);messageProgressBar.setVisibility(View.INVISIBLE);

        PictureDetection.setOnClickListener(this);
        MessageDetection.setOnClickListener(this);
        Result.setOnClickListener(this);

        Intent intent = new Intent(this,pictureService.class);
        startService(intent); // 启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        intent = new Intent(this,messageService.class);
        startService(intent);
        bindService(intent,messageConnection,BIND_AUTO_CREATE);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},2);
        }
    }

    @Override
    public void onClick(View v) {
        if(pictureBinder == null){
            return;
        }
        switch (v.getId()) {
            case R.id.PictureDetection:
                pictureBinder.OcrProcess();
                break;
            case R.id.ShortMessageDetection:
                messageBinder.DetectionProcess();
                break;
            case R.id.Result:
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                startActivity(intent);
            default:
                break;
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
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }else{
                    Toast.makeText(this,"You denied READ_SMS",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
