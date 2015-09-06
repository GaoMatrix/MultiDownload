
package com.gao.downloader.core;

import android.os.Handler;
import android.os.Message;

import com.gao.downloader.entities.DownloadEntry;
import com.gao.downloader.entities.DownloadEntry.DownloadStatus;
import com.gao.downloader.utilities.Trace;

public class DownloadTask implements Runnable {

    private DownloadEntry mEntry;
    private volatile boolean mIsPaused;
    private volatile boolean mIsCancelled;
    private Handler mHandler;

    public DownloadTask(DownloadEntry entry, Handler handler) {
        this.mEntry = entry;
        this.mHandler = handler;
    }

    public void pause() {
        Trace.d("Download pause");
        mIsPaused = true;
    }

    public void cancel() {
        Trace.d("Download cancel");
        mIsCancelled = true;
    }

    public void start() {
        mEntry.status = DownloadStatus.downloading;
        // DataChanger.getInstance().postStatus(mEntry);
        notifyUpdate(mEntry, DownloadService.NOTIFY_DOWNLOADING);

        mEntry.totalLength = 1024 * 100;
        for (int i = mEntry.currentLength; i < mEntry.totalLength;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mIsCancelled || mIsPaused) {
                mEntry.status = mIsCancelled ? DownloadStatus.cancelled : DownloadStatus.paused;
                notifyUpdate(mEntry, DownloadService.NOTIFY_PAUSED_OR_CANCELLED);
                // TODO if cancelled, delete related file.
                return;
            }

            i += 1024;
            mEntry.currentLength += 1024;
            mEntry.status = DownloadEntry.DownloadStatus.downloading;
            notifyUpdate(mEntry, DownloadService.NOTIFY_UPDATING);
        }

        mEntry.status = DownloadEntry.DownloadStatus.completed;
        notifyUpdate(mEntry, DownloadService.NOTIFY_COMPLETED);
    }

    private void notifyUpdate(DownloadEntry entry, int what) {
        Message message = mHandler.obtainMessage();
        message.what = what;
        message.obj = entry;
        mHandler.sendMessage(message);
    }

    @Override
    public void run() {
        start();
    }

}
