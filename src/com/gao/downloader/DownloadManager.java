
package com.gao.downloader;

import android.content.Context;
import android.content.Intent;

import com.gao.downloader.core.DownloadService;
import com.gao.downloader.entities.DownloadEntry;
import com.gao.downloader.notify.DataChanger;
import com.gao.downloader.notify.DataWatcher;
import com.gao.downloader.utilities.Constants;

public class DownloadManager {

    private static DownloadManager mInstance;
    private Context mContext;
    private static final int MIN_OPERATE_INTERVAL = 1000 * 1;
    private long mLastOperatedTime = 0;

    private DownloadManager(Context context) {
        this.mContext = context;
    }

    public static synchronized DownloadManager getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new DownloadManager(context);
        }

        return mInstance;
    }

    public void add(DownloadEntry entry) {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(intent);
    }

    private boolean checkIfExecutable() {
        long temp = System.currentTimeMillis();
        if (temp - mLastOperatedTime > MIN_OPERATE_INTERVAL) {
            mLastOperatedTime = temp;
            return true;
        }
        return false;
    }

    public void pause(DownloadEntry entry) {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(intent);
    }

    public void resume(DownloadEntry entry) {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(intent);
    }

    public void cancel(DownloadEntry entry) {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_CANCEL);
        mContext.startService(intent);
    }

    public void addObserver(DataWatcher watcher) {
        DataChanger.getInstance().addObserver(watcher);
    }

    public void removeObserver(DataWatcher watcher) {
        DataChanger.getInstance().deleteObserver(watcher);
    }

    public void pauseAll() {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL);
        mContext.startService(intent);
    }

    public void recoverAll() {
        if (!checkIfExecutable()) {
            return;
        }
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RECOVER_ALL);
        mContext.startService(intent);
    }
}
