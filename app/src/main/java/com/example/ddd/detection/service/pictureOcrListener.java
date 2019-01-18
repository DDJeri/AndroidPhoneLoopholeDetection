package com.example.ddd.detection.service;

public interface pictureOcrListener {

    void onProgress(Integer... params);

    void onSuccess();

    void onCancel();

}
