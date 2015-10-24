package net.gongmingqm10.markethacker.model;

import java.io.Serializable;

public class Avatar implements Serializable {
    private String url;

    public Avatar(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
