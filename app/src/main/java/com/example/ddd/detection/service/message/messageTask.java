package com.example.ddd.detection.service.message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ddd.detection.db.Message;

import org.litepal.LitePal;

import java.util.List;

public class messageTask extends AsyncTask<Void, Integer, Integer> {

    private messageListener listener;
    private int progressNum;
    private Context context;

    public messageTask(Context context, messageListener listener){
        this.context = context;
        this.listener = listener;
        /* 同步数据库 */
        syncDatabase();
    }

    @Override
    protected Integer doInBackground(Void... params) {

        List<Message> messages = LitePal.findAll(Message.class);
        progressNum = messages.size();
        int num=0;
        for(Message message: messages){
            if(isSafe(message.getBody())){
                message.setDetectionResult(true);
                message.save();
                publishProgress(num++);
            }
        }
        return num;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        listener.onProgress(progress,progressNum);
    }

    @Override
    protected void onPostExecute(Integer a) {
        listener.onSuccess();
    }

    private boolean isSafe(String body){
        return true;
    }
    private void syncDatabase() {
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type",};
        Cursor cur = context.getContentResolver().query(uri, projection, null,
                null, "date desc"); // 获取手机内部短信

        String strAddress;
        int intPerson;
        String strbody;
        int intType;

        while (cur.moveToNext()) {

            strAddress = cur.getString(cur.getColumnIndex("address"));
            intPerson = cur.getInt(cur.getColumnIndex("person"));
            strbody = cur.getString(cur.getColumnIndex("body"));
            intType = cur.getInt(cur.getColumnIndex("type"));

            List<Message> messages = LitePal.where("body=?", strbody).find(Message.class);
            if (messages.isEmpty()) {
                Message message = new Message();
                message.setAddress(strAddress);
                message.setBody(strbody);
                message.setPerson(intPerson);
                message.setType(intType);
                message.save();
                Log.e("messages", strAddress + " -- " + intPerson + " -- " + strbody + " -- " + intType);
            }
        }
        cur.close();
    }
}
