package com.example.rohit.practoauthenticator;

import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.rohit.practoauthenticator.auth.SntpClient;

import java.util.Date;

/**
 * Created by rohit on 09/08/16.
 */
public class ServerTime extends AsyncTask<String,Void,Long> {

    protected Long doInBackground(String  ... params) {
        String defaultServer = params[0];
        Date current =  null;
        SntpClient client = new SntpClient();
        if (client.requestTime(defaultServer,5000)) {
            long now = client.getNtpTime() + SystemClock.elapsedRealtime() -
                    client.getNtpTimeReference();
            current = new Date(now);
        }

        return current.getTime();
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        Log.d("AsyncTask","Current Time on Server " + Long.toString(result) + "ms");
        MainActivityFragment.currentTime = result;
    }
}
