package com.gao.downloader;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gao.multidownload.R;

public class MainActivity extends ActionBarActivity implements OnClickListener{

    private Button mDownloadBtn;
    private DownloadManager mDownloadManager;
    private DataWatcher mDataWatcher = new DataWatcher() {
        
        @Override
        public void notifyUpdate(DownloadEntry data) {
            Trace.d(data.toString());
        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadBtn = (Button) findViewById(R.id.mDownloadBtn);
        mDownloadBtn.setOnClickListener(this);
        mDownloadManager  = DownloadManager.getInstance(this);
    }
    
    @Override
    public void onClick(View v) {
        DownloadEntry entry = new DownloadEntry();
        entry.name = "test.jpg";
        entry.url = "http://api.stay4it.com/uploads/test.jpg";
        entry.id = "1";
        mDownloadManager.add(entry);
        mDownloadManager.cancel(entry);
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
