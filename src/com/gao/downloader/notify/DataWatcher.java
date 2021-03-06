
package com.gao.downloader.notify;

import com.gao.downloader.entities.DownloadEntry;

import java.util.Observable;
import java.util.Observer;

public abstract class DataWatcher implements Observer {

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadEntry) {
            notifyUpdate((DownloadEntry) data);
        }
    }

    public abstract void notifyUpdate(DownloadEntry data) ;
}
