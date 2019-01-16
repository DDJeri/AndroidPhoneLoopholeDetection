package com.example.ddd.detection.service;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ddd.detection.db.Picture;

import org.litepal.LitePal;
import java.util.List;

public class pictureOcr extends AppCompatActivity {

    private ContentResolver cr = null;

    public void setCr(ContentResolver a){
        cr = a;
    }

    public void pictureQuery(){

        List<Picture> pictures = LitePal.findAll(Picture.class);
        for(Picture picture: pictures){
            Log.e("photos.details", "imageId is " + picture.getImageId());
            Log.e("photos.details", "path is " + picture.getPath());
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
            picture.save();

            Log.e("photos", imageId + " -- " + fileName + " -- " + filePath);
        }
        cursor.close();
        cursor = null;
    }

}
