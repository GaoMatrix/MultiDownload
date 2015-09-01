
package com.gao.downloader;

import java.io.Serializable;

public class DownloadEntry implements Serializable {
    public String id;
    public String name;
    public String url;

    public enum DownloadStatus {
        waiting, downloading, pause, resume, cancel, completed
    }

    public DownloadStatus status;

    public int currentLength;
    public int totalLength;

    @Override
    public String toString() {
        return "DownloadEntry [id=" + id + ", name=" + name + ", url=" + url + ", status=" + status
                + ", currentLength=" + currentLength + ", totalLength=" + totalLength + "]";
    }

}
