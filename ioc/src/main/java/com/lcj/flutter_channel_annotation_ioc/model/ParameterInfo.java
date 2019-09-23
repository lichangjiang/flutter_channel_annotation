package com.lcj.flutter_channel_annotation_ioc.model;

public class ParameterInfo {
    private String key;
    private String type;

    public ParameterInfo(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }
}
