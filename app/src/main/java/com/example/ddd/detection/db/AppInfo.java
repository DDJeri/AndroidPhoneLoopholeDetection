package com.example.ddd.detection.db;

import org.litepal.crud.LitePalSupport;

public class AppInfo extends LitePalSupport {

    private String appName;
    private String packageName;
    private String publickey;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }
}
