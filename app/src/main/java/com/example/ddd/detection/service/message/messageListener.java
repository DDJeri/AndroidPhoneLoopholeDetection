package com.example.ddd.detection.service.message;

public interface messageListener {

    void onProgress(Integer... params);

    void onSuccess();

}
