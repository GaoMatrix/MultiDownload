
package com.gao.downloader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gao.downloader.DownloadEntry.DownloadStatus;

public class MainActivity extends ActionBarActivity implements OnClickListener {

    private Button mDownloadStartBtn;
    private Button mDownloadPauseBtn;
    private Button mDownloadCancelBtn;
    private DownloadManager mDownloadManager;
    private DownloadEntry mEntry;
    private DataWatcher mDataWatcher = new DataWatcher() {

        @Override
        public void notifyUpdate(DownloadEntry data) {
            mEntry = data;
            if (mEntry.status == DownloadStatus.cancel) {
                mEntry = null;
            }
            Trace.d(data.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadStartBtn = (Button) findViewById(R.id.mDownloadStartBtn);
        mDownloadStartBtn.setOnClickListener(this);
        mDownloadPauseBtn = (Button) findViewById(R.id.mDownloadPauseBtn);
        mDownloadPauseBtn.setOnClickListener(this);
        mDownloadCancelBtn = (Button) findViewById(R.id.mDownloadCancelBtn);
        mDownloadCancelBtn.setOnClickListener(this);
        mDownloadManager = DownloadManager.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        if (null == mEntry) {
            mEntry = new DownloadEntry();
            mEntry.name = "test.jpg";
            mEntry.url = "http://api.stay4it.com/uploads/test.jpg";
            mEntry.id = "1";
        }
        switch (v.getId()) {
            case R.id.mDownloadStartBtn:
                mDownloadManager.add(mEntry);
                break;
            case R.id.mDownloadPauseBtn:
                if (mEntry.status == DownloadStatus.downloading) {
                    mDownloadManager.pause(mEntry);
                } else if (mEntry.status == DownloadStatus.paused) {
                    mDownloadManager.resume(mEntry);
                }
                break;
            case R.id.mDownloadCancelBtn:
                mDownloadManager.cancel(mEntry);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadManager.addObserver(mDataWatcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownloadManager.removeObserver(mDataWatcher);
    }

}
