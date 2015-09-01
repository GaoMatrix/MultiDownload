
package com.gao.downloader;

import java.util.Observable;

public class DataChanger extends Observable {
    private static DataChanger mInstance;

    private DataChanger() {
    }

    // Set DataChander to SingleInstance, convenient for register/unregister
    // DataWatcher
    public synchronized static DataChanger getInstance() {
        if (null == mInstance) {
            mInstance = new DataChanger();
        }

        return mInstance;
    }

    public void postStatus(DownloadEntry entry) {
        setChanged();
        notifyObservers(entry);
    }

}
