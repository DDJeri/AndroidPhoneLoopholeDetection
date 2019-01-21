package com.example.ddd.detection.service.picture;

public interface pictureOcrListener {

    void onProgress(Integer... params);

    void onSuccess();

    void onCancel();

}
