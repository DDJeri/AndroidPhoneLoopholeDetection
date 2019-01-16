package com.example.ddd.detection;

import org.litepal.crud.LitePalSupport;

public class Movie extends LitePalSupport {
    //运用注解来为字段添加index标签 //name是唯一的，且默认值为unknown

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


