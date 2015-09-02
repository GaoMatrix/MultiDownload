
package com.gao.downloader;

import com.gao.downloader.DownloadEntry.DownloadStatus;

public class DownloadTask {

    private DownloadEntry mEntry;
    private boolean mIsPaused;
    private boolean mIsCancelled;

    public DownloadTask(DownloadEntry entry) {
        this.mEntry = entry;
    }

    public void pause() {
        mIsPaused = true;
    }

    public void cancel() {
        mIsCancelled = true;
    }

    public void start() {
        mEntry.status = DownloadStatus.downloading;
        DataChanger.getInstance().postStatus(mEntry);

        mEntry.totalLength = 1024 * 100;
        for (int i = 0; i < mEntry.totalLength;) {
            if (mIsCancelled || mIsPaused) {
                mEntry.status = mIsCancelled ? DownloadStatus.cancel : DownloadStatus.pause;
                DataChanger.getInstance().postStatus(mEntry);
                //TODO if cancelled, delete related file.
                return;
            }

            i += 1024;
            mEntry.currentLength += 1024;
            DataChanger.getInstance().postStatus(mEntry);
        }

        mEntry.status = DownloadStatus.completed;
        DataChanger.getInstance().postStatus(mEntry);
    }

}
