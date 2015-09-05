
package com.gao.downloader;

import java.io.Serializable;

public class DownloadEntry implements Serializable {
    public String id;
    public String name;
    public String url;

    public enum DownloadStatus {
        idle, waiting, downloading, paused, resume, cancel, completed
    }

    public DownloadStatus status = DownloadStatus.idle;

    public int currentLength;
    public int totalLength;

    public DownloadEntry(String url) {
        this.url = url;
        this.id = url;
        this.name = url.substring(url.lastIndexOf("/") + 1);
    } 

    public DownloadEntry() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "DownloadEntry: " + url + " is " + status.name() + " with " + currentLength + "/"
                + totalLength;
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
