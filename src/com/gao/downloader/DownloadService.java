
package com.gao.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gao.downloader.DownloadEntry.DownloadStatus;

public class DownloadService extends Service {

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
        if (action == Constants.KEY_DOWNLOAD_ACTION_ADD) {
            entry.status = DownloadStatus.downloading;
        }
    }

}
