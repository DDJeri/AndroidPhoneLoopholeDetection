package com.example.ddd.detection.service.message;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.example.ddd.detection.MainActivity;
import com.example.ddd.detection.service.picture.pictureService;

public class messageService extends Service {

    private messageTask task;

    private messageListener listener  = new messageListener() {
        @Override
        public void onProgress(Integer... params) {
            int i = params[0],len = params[1];
            MainActivity.messageProgressBar.setMax(len);
            MainActivity.messageProgressBar.setProgress(i);
        }

        @Override
        public void onSuccess() {
            task = null;
            MainActivity.messageProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(messageService.this, "Message Success", Toast.LENGTH_SHORT).show();
        }
    };

    private messageService.messageBinder mBinder = new messageService.messageBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    public class messageBinder extends Binder {
        public void DetectionProcess(){
            MainActivity.messageProgressBar.setVisibility(View.VISIBLE);
            if(task == null){
                task = new messageTask(messageService.this,listener);
                task.execute();
            }
        }
    }


}
