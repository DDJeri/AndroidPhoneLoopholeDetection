package com.example.ddd.detection.service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ddd.detection.MainActivity;
import com.example.ddd.detection.db.Picture;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class OcrTask extends AsyncTask<Void, Integer, Integer> {

    private TessBaseAPI mTess;
    private pictureOcrListener listener;
    private int progressNum;
    private Context context;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private boolean isCanceled = false;
    private boolean isPaused = false;

    public OcrTask(Context context, pictureOcrListener listener){
        this.context = context;
        this.listener = listener;
        /* 同步数据库 */
        syncDatabase();
    }

    @Override
    protected  void onPreExecute(){

        /* Ocr 初始化 */
        mTess = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
        //String language = "eng";
        String language = "chi_sim";
        File dir = new File(datapath + "tessdata/");
        if (!dir.exists())
            dir.mkdirs();
        mTess.init(datapath, language);

    }

    @Override
    protected Integer doInBackground(Void... params) {

        int num = 1;
        /* 读取数据库 */
        List<Picture> pictures = LitePal.findAll(Picture.class);
        progressNum = pictures.size()+1;

        /* 做ocr */
        for(Picture picture: pictures){
            if(isCanceled){
                LitePal.deleteAll(Picture.class);
                return TYPE_CANCELED;
            }
            publishProgress(++num);
            if(picture.getOcrResult() != null){    // 已经做过ocr
            } else{    // 未做过ocr
                Bitmap bitmap = GetBitmap(picture.getPath());
                if(bitmap != null){
                    picture.setOcrResult(getOCRResult(bitmap));    //加入OCR结果
                    picture.save();
                }else{
                                //删除
                }
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
    protected void onPostExecute(Integer status) {
        if(status == TYPE_CANCELED){
            listener.onCancel();
        }else{
            listener.onSuccess();
        }
    }

    public void cancelOcr(){
        isCanceled = true;
    }

    private Bitmap GetBitmap(String path){
        FileInputStream in = null;
        Bitmap bm,bitmap;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File file = new File(path);

        if(file.exists()) {
            bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        }
        return null;
    }

    private String getOCRResult(Bitmap bitmap) {

        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        return result;
    }

    private void syncDatabase(){
        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME};

        //asc 按升序排列
        //    desc 按降序排列
        //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");
        String imageId;
        String fileName;
        String filePath;

        while (cursor.moveToNext()) {
            imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

            List<Picture> pictures = LitePal.where("path=?",filePath).find(Picture.class);
            if(pictures.isEmpty()){
                Picture picture = new Picture();
                picture.setImageId(imageId);
                picture.setPath(filePath);
                picture.save();
                Log.e("photos", imageId + " -- " + fileName + " -- " + filePath);
            }
        }
        cursor.close();
    }
}
