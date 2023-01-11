package com.example.model;

public class Tags {
    private String tag;
    private String id;

    public Tags(String tag, String id) {
        this.tag = tag;
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
