
package com.gao.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gao.downloader.DownloadEntry.DownloadStatus;

import java.util.HashMap;

public class DownloadService extends Service {
    private HashMap<String, DownloadTask> mDownloadingTasks = new HashMap<String, DownloadTask>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownloadEntry entry = (DownloadEntry) intent
                .getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ACTION, -1);
        doAction(action, entry);

        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action, DownloadEntry entry) {
        // check action , do related action
        switch (action) {
            case Constants.KEY_DOWNLOAD_ACTION_ADD:
                startDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_CANCEL:
                cancelDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE:
                pauseDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RESUME:
                resumeDownload(entry);
                break;
            default:
                break;
        }
    }

    private void resumeDownload(DownloadEntry entry) {
        startDownload(entry);
    }

    private void cancelDownload(DownloadEntry entry) {
        // remove DownloadTask from mDownloadingTasks because it contains
        // downloading task.
        DownloadTask task = mDownloadingTasks.remove(entry.id);
        if (null != task) {
            task.cancel();
        }
    }

    private void pauseDownload(DownloadEntry entry) {
        // remove DownloadTask from mDownloadingTasks because it contains
        // downloading task.
        DownloadTask task = mDownloadingTasks.remove(entry.id);
        if (null != task) {
            task.pause();
        }
    }

    private void startDownload(DownloadEntry entry) {
        DownloadTask task = new DownloadTask(entry);
        task.start();
        // entery.id as key
        mDownloadingTasks.put(entry.id, task);

        
    }

}
