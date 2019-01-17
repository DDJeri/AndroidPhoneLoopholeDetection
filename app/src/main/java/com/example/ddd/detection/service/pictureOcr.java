package com.example.ddd.detection.service;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ddd.detection.db.Picture;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class pictureOcr extends AppCompatActivity {

    private ContentResolver cr = null;
    private TestOCR ocr = null;

    public pictureOcr(ContentResolver a){
        cr = a;    // ContentResolver 初始化
        ocr = new TestOCR();
    }


    public void pictureQuery(){

        List<Picture> pictures = LitePal.findAll(Picture.class);
        for(Picture picture: pictures){
            Log.e("photos.details", "imageId is " + picture.getImageId());
            Log.e("photos.details", "path is " + picture.getPath());
            Log.e("photos.details", "ocrResult is " + picture.getOcrResult());
        }
    }

    public void pictureDatabaseCreate() {

        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME};

        //asc 按升序排列
        //    desc 按降序排列
        //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");
        String imageId;
        String fileName;
        String filePath;

        while (cursor.moveToNext()) {
            imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            
            Picture picture = new Picture();
            picture.setImageId(imageId);
            picture.setPath(filePath);
            Bitmap bitmap = GetBitmap(filePath);
            if(bitmap != null){
                picture.setOcrResult(ocr.getOCRResult(bitmap));    //加入OCR结果
            }
            picture.save();

            Log.e("photos", imageId + " -- " + fileName + " -- " + filePath);
        }
        cursor.close();
        cursor = null;
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
}
