package com.example.rohit.practoauthenticator;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rohit.practoauthenticator.auth.SntpClient;
import com.example.rohit.practoauthenticator.auth.TOTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    static final String OTP = "saved_otp";
    private String otpPin = "- - - - -";
    static Long currentTime = null;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        final TextView otpTextView = (TextView)rootView.findViewById(R.id.pinTextView);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            otpPin = savedInstanceState.getString(OTP);
            otpTextView.setText(otpPin);
        }
        Button button = (Button) rootView.findViewById(R.id.opt_gen_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                otpPin = generateTOTP();
                if( otpPin != null ){
                    otpTextView.setText(otpPin);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(OTP, otpPin);
        super.onSaveInstanceState(savedInstanceState);
    }

    public String generateTOTP() {
    new ServerTime().execute("2.android.pool.ntp.org");


//        // Seed for HMAC-SHA1 - 20 bytes
//        String seed = "3132333435363738393031323334353637383930";
//        // Seed for HMAC-SHA256 - 32 bytes
//        String seed32 = "3132333435363738393031323334353637383930" +
//                "313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "31323334";
        long T0 = 0;
        long X = 60 * 1000;
        if(currentTime == null)
            currentTime = System.currentTimeMillis();
        Log.d(LOG_TAG,"Current Time on System is : "+ Long.toString(currentTime) +"ms");
        String steps = "0";
        try {
                long T = (currentTime - T0)/X;
                steps = Long.toHexString(T).toUpperCase();
                while (steps.length() < 16) steps = "0" + steps;

                return TOTP.generateTOTP(seed64, steps, "5","HmacSHA512");
        }catch (final Exception e){
            Log.e(LOG_TAG,"Error : " + e);
            return null;
        }
    }

}
