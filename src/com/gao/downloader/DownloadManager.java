
package com.gao.downloader;

import android.content.Context;
import android.content.Intent;

public class DownloadManager {

    private static DownloadManager mInstance;
    private Context mContext;

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
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(intent);
    }

    public void pause(DownloadEntry entry) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(intent);
    }

    public void resume(DownloadEntry entry) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(intent);
    }

    public void cancel(DownloadEntry entry) {
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
}
