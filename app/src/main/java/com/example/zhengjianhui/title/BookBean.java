package com.example.zhengjianhui.title;

import java.io.Serializable;

/**
 * Created by zhengjianhui on 17/6/9.
 */

public class BookBean implements Serializable {

    private Integer key;

    private String name;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
