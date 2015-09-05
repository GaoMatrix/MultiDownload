
package com.gao.downloader;

import android.os.Handler;
import android.os.Message;

import com.gao.downloader.DownloadEntry.DownloadStatus;

public class DownloadTask implements Runnable {

    private DownloadEntry mEntry;
    private boolean mIsPaused;
    private boolean mIsCancelled;
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
        Message message = mHandler.obtainMessage();
        message.obj = mEntry;
        mHandler.sendMessage(message);

        mEntry.totalLength = 1024 * 100;
        for (int i = mEntry.currentLength; i < mEntry.totalLength;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mIsCancelled || mIsPaused) {
                mEntry.status = mIsCancelled ? DownloadStatus.cancelled : DownloadStatus.paused;
                // DataChanger.getInstance().postStatus(mEntry);
                message = mHandler.obtainMessage();
                message.obj = mEntry;
                mHandler.sendMessage(message);
                // TODO if cancelled, delete related file.
                return;
            }

            i += 1024;
            mEntry.currentLength += 1024;
            // DataChanger.getInstance().postStatus(mEntry);
            message = mHandler.obtainMessage();
            message.obj = mEntry;
            mHandler.sendMessage(message);
        }

        mEntry.status = DownloadStatus.completed;
        // DataChanger.getInstance().postStatus(mEntry);
        message = mHandler.obtainMessage();
        message.obj = mEntry;
        mHandler.sendMessage(message);
    }

    @Override
    public void run() {
        start();
    }

}
