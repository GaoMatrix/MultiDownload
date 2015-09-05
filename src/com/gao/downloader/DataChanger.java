
package com.gao.downloader;

import com.gao.downloader.DownloadEntry.DownloadStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

public class DataChanger extends Observable {
    private static DataChanger mInstance;
    private LinkedHashMap<String, DownloadEntry> mOperatedEntries;

    private DataChanger() {
        mOperatedEntries = new LinkedHashMap<String, DownloadEntry>();
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
        // Save all operated DownloadEntry .
        mOperatedEntries.put(entry.id, entry);
        setChanged();
        notifyObservers(entry);
    }

    public ArrayList<DownloadEntry> queryAllRecoverableEntries() {
        ArrayList<DownloadEntry> mRecoverableEntries = null;
        for (Map.Entry<String, DownloadEntry> entry : mOperatedEntries.entrySet()) {
            if (entry.getValue().status == DownloadStatus.paused) {
                if (null == mRecoverableEntries) {
                    mRecoverableEntries = new ArrayList<DownloadEntry>();
                }
                mRecoverableEntries.add(entry.getValue());
            }
        }
        return mRecoverableEntries;
    }
}
