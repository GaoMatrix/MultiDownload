
package com.gao.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.gao.downloader.DownloadEntry.DownloadStatus;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class DownloadService extends Service {
    private HashMap<String, DownloadTask> mDownloadingTasks = new HashMap<String, DownloadTask>();
    private ExecutorService mExecutors;
    private LinkedBlockingDeque<DownloadEntry> mWaitingQueue = new LinkedBlockingDeque<DownloadEntry>();

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            DownloadEntry entry = (DownloadEntry) msg.obj;
            switch (entry.status) {
                case cancelled:
                case paused:
                case completed:
                    checkNext(entry);
                    break;

                default:
                    break;
            }
            // put this callback to UI thread.
            DataChanger.getInstance().postStatus((DownloadEntry) msg.obj);
        };
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    protected void checkNext(DownloadEntry entry) {
        DownloadEntry newEntry = mWaitingQueue.poll();
        if (null != newEntry) {
            startDownload(newEntry);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutors = Executors.newCachedThreadPool();
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
                // startDownload(entry);
                addDownload(entry);
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

    private void addDownload(DownloadEntry entry) {
        if (mDownloadingTasks.size() >= Constants.MAX_DOWNLOAD_TASKS) {
            entry.status = DownloadStatus.waiting;
            mWaitingQueue.offer(entry);
            // waiting is also a state, need to notify the UI.
            DataChanger.getInstance().postStatus(entry);
        } else {
            startDownload(entry);
        }
    }

    private void resumeDownload(DownloadEntry entry) {
        addDownload(entry);
    }

    private void cancelDownload(DownloadEntry entry) {
        // remove DownloadTask from mDownloadingTasks because it contains
        // downloading task.
        DownloadTask task = mDownloadingTasks.remove(entry.id);
        if (null != task) {
            task.cancel();
        } else {
            // When addDownload, the entry may in mDownloadingTasks, or in
            // mWaitingQueue. If the entry is not in mDownloadingTasks, it will
            // in mWaitingQueue, so we should remove it from mWaitingQueue.
            mWaitingQueue.remove(entry);
            entry.status = DownloadStatus.cancelled;
            DataChanger.getInstance().postStatus(entry);
        }
    }

    private void pauseDownload(DownloadEntry entry) {
        // remove DownloadTask from mDownloadingTasks because it contains
        // downloading task.
        DownloadTask task = mDownloadingTasks.remove(entry.id);
        if (null != task) {
            task.pause();
        } else {
            // When addDownload, the entry may in mDownloadingTasks, or in
            // mWaitingQueue. If the entry is not in mDownloadingTasks, it will
            // in mWaitingQueue, so we should remove it from mWaitingQueue.
            mWaitingQueue.remove(entry);
            entry.status = DownloadStatus.paused;
            DataChanger.getInstance().postStatus(entry);
        }
    }

    private void startDownload(DownloadEntry entry) {
        DownloadTask task = new DownloadTask(entry, mHandler);
        // task.start();
        // entery.id as key
        mDownloadingTasks.put(entry.id, task);
        mExecutors.execute(task);
    }

}
