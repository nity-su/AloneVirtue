package com.anlyn.alonevirtue.contents.ui.video;

import java.util.ArrayList;
import java.util.List;

public class VideoContentsInfo {

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUriType() {
        return uriType;
    }

    public void setUriType(String uriType) {
        this.uriType = uriType;
    }

    private String title;
    private String url;
    private String uriType;
    private List<String> tag ;
}
