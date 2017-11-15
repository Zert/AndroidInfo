package com.meow.ops.androidpropsinfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.meow.ops.androidpropsinfo.root.RootUtil1;
import com.meow.ops.androidpropsinfo.root.RootUtil2;
import com.meow.ops.androidpropsinfo.root.RootUtil3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 12;

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Item> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.params = new ArrayList<>();
        params.add(new SectionItem("General"));
        Log.d(TAG, "GAID check: " + GooglePlayServicesUtil.isGooglePlayServicesAvailable(this));
        step_1();
    }

    private void step_1() {
        // Google Ads ID
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String gaid = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this).getId();
                    if (gaid != null) {
                        Log.d(TAG, "GAID: " + gaid);
                        params.add(new ParamItem("Google Ads ID", gaid));
                    } else {
                        params.add(new ParamItem("Google Ads ID", "-- empty --"));
                    }
                } catch (IllegalStateException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException | IOException e) {
                    e.printStackTrace();
                    params.add(new ParamItem("Google Ads ID", "error occurred"));
                }
                step_2();
            }
        }).start();
    }

    private void step_2() {
        // ANDROID_ID
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        params.add(new ParamItem("Settings.Secure.ANDROID_ID (1)", android_id));

        android_id = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        params.add(new ParamItem("Settings.Secure.ANDROID_ID (2)", android_id));

        android_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        params.add(new ParamItem("Settings.Secure.ANDROID_ID (3)", android_id));

        // -----------------------------------------------------------------------------------------
        //                  _  __    __                  __
        //                 / |/ /__ / /__    _____  ____/ /__
        //                /    / -_) __/ |/|/ / _ \/ __/  '_/
        //               /_/|_/\__/\__/|__,__/\___/_/ /_/\_\
        //
        // -----------------------------------------------------------------------------------------

        // NetworkInterface.getNetworkInterfaces()

//        params.add(new SectionItem("Network"));
//
//        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wm.getConnectionInfo();
//        params.add(new ParamItem("WIFI MAC (< 6): ", info.getMacAddress()));
//
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        params.add(new ParamItem("Bluetooth MAC (< 6): ", adapter.getAddress()));

        params.add(new ParamItem("wlan0 MAC: ", Utils.getMacAddr()));


        // -----------------------------------------------------------------------------------------
        //      ____         __              ___                        __  _
        //     / __/_ _____ / /____ __ _    / _ \_______  ___  ___ ____/ /_(_)__ ___
        //    _\ \/ // (_-</ __/ -_)  ' \  / ___/ __/ _ \/ _ \/ -_) __/ __/ / -_|_-<
        //   /___/\_, /___/\__/\__/_/_/_/ /_/  /_/  \___/ .__/\__/_/  \__/_/\__/___/
        //       /___/                                 /_/
        // -----------------------------------------------------------------------------------------
        params.add(new SectionItem("System.getProperties"));

        Properties properties = System.getProperties();
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = System.getProperty(key);
            params.add(new ParamItem(key, value));
        }

        // -----------------------------------------------------------------------------------------
        //                           ___            __
        //                          / _ \___  ___  / /_
        //                         / , _/ _ \/ _ \/ __/
        //                        /_/|_|\___/\___/\__/
        //
        // -----------------------------------------------------------------------------------------
        params.add(new SectionItem("is rooted?"));

        params.add(new ParamItem("check 1, rooted: ", RootUtil1.isRooted()));
        params.add(new ParamItem("check 2, rooted: ", RootUtil2.isRooted()));
        params.add(new ParamItem("check 3, rooted: ", RootUtil3.isRooted()));

        // -----------------------------------------------------------------------------------------
        //                        ___       _ __   __
        //                       / _ )__ __(_) /__/ /
        //                      / _  / // / / / _  /
        //                     /____/\_,_/_/_/\_,_/
        //
        // -----------------------------------------------------------------------------------------
        params.add(new SectionItem("android.os.Build"));

        params.add(new ParamItem("Build.getRadioVersion()", Build.getRadioVersion()));
        params.add(new ParamItem("Build.BOARD", Build.BOARD));
        params.add(new ParamItem("Build.BOOTLOADER", Build.BOOTLOADER));
        params.add(new ParamItem("Build.BRAND", Build.BRAND));
        params.add(new ParamItem("Build.CPU_ABI", Build.CPU_ABI));
        params.add(new ParamItem("Build.CPU_ABI2", Build.CPU_ABI2));
        params.add(new ParamItem("Build.DEVICE", Build.DEVICE));
        params.add(new ParamItem("Build.DISPLAY", Build.DISPLAY));
        params.add(new ParamItem("Build.FINGERPRINT", Build.FINGERPRINT));
        params.add(new ParamItem("Build.HARDWARE", Build.HARDWARE));
        params.add(new ParamItem("Build.HOST", Build.HOST));
        params.add(new ParamItem("Build.ID", Build.ID));
        params.add(new ParamItem("Build.MANUFACTURER", Build.MANUFACTURER));
        params.add(new ParamItem("Build.MODEL", Build.MODEL));
        params.add(new ParamItem("Build.PRODUCT", Build.PRODUCT));
        params.add(new ParamItem("Build.RADIO", Build.RADIO));
        params.add(new ParamItem("Build.SERIAL", Build.SERIAL));
        params.add(new ParamItem("Build.SUPPORTED_32_BIT_ABIS", Build.SUPPORTED_32_BIT_ABIS));
        params.add(new ParamItem("Build.SUPPORTED_64_BIT_ABIS", Build.SUPPORTED_64_BIT_ABIS));
        params.add(new ParamItem("Build.SUPPORTED_ABIS", Build.SUPPORTED_ABIS));
        params.add(new ParamItem("Build.TAGS", Build.TAGS));
        params.add(new ParamItem("Build.TIME", Build.TIME));
        params.add(new ParamItem("Build.TYPE", Build.TYPE));
        params.add(new ParamItem("Build.USER", Build.USER));
        params.add(new ParamItem("Build.VERSION.BASE_OS", Build.VERSION.BASE_OS));
        params.add(new ParamItem("Build.VERSION.CODENAME", Build.VERSION.CODENAME));
        params.add(new ParamItem("Build.VERSION.INCREMENTAL", Build.VERSION.INCREMENTAL));
        params.add(new ParamItem("Build.VERSION.PREVIEW_SDK_INT", Build.VERSION.PREVIEW_SDK_INT));
        params.add(new ParamItem("Build.VERSION.RELEASE", Build.VERSION.RELEASE));
        params.add(new ParamItem("Build.VERSION.SDK", Build.VERSION.SDK));
        params.add(new ParamItem("Build.VERSION.SDK_INT", Build.VERSION.SDK_INT));
        params.add(new ParamItem("Build.VERSION.SECURITY_PATCH", Build.VERSION.SECURITY_PATCH));


        // -----------------------------------------------------------------------------------------
        //    ______    __         __                  __  ___
        //   /_  __/__ / /__ ___  / /  ___  ___  __ __/  |/  /__ ____  ___ ____ ____ ____
        //    / / / -_) / -_) _ \/ _ \/ _ \/ _ \/ // / /|_/ / _ `/ _ \/ _ `/ _ `/ -_) __/
        //   /_/  \__/_/\__/ .__/_//_/\___/_//_/\_, /_/  /_/\_,_/_//_/\_,_/\_, /\__/_/
        //                /_/                  /___/                      /___/
        // -----------------------------------------------------------------------------------------
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            step_3();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    step_3();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showUI();
                }
            }
        }
    }

    private void step_3() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        params.add(new SectionItem("TelephonyManager"));

        params.add(new ParamItem("getCallState", Integer.toString(tm.getCallState())));
        params.add(new ParamItem("getDataActivity", Integer.toString(tm.getDataActivity())));
        params.add(new ParamItem("getDataState", Integer.toString(tm.getDataState())));
        params.add(new ParamItem("IMEI/getDeviceId", tm.getDeviceId()));
        params.add(new ParamItem("getDeviceSoftwareVersion", tm.getDeviceSoftwareVersion()));
        params.add(new ParamItem("getGroupIdLevel1", tm.getGroupIdLevel1()));
        params.add(new ParamItem("getLine1Number", tm.getLine1Number()));
        params.add(new ParamItem("getMmsUAProfUrl", tm.getMmsUAProfUrl()));
        params.add(new ParamItem("getMmsUserAgent", tm.getMmsUserAgent()));
        params.add(new ParamItem("getNetworkCountryIso", tm.getNetworkCountryIso()));
        params.add(new ParamItem("getNetworkOperatorName", tm.getNetworkOperatorName()));
        params.add(new ParamItem("getNetworkType", tm.getNetworkType()));
        params.add(new ParamItem("getPhoneCount", tm.getPhoneCount()));
        params.add(new ParamItem("getPhoneType", tm.getPhoneType()));
        params.add(new ParamItem("getSimCountryIso", tm.getSimCountryIso()));
        params.add(new ParamItem("getSimOperator", tm.getSimOperator()));
        params.add(new ParamItem("getSimOperatorName", tm.getSimOperatorName()));
        params.add(new ParamItem("getSimSerialNumber", tm.getSimSerialNumber())); // for CDMA it is null
        params.add(new ParamItem("getSimState", tm.getSimState()));
        params.add(new ParamItem("getSubscriberId", tm.getSubscriberId()));
        params.add(new ParamItem("getVoiceMailAlphaTag", tm.getVoiceMailAlphaTag()));
        params.add(new ParamItem("getVoiceMailNumber", tm.getVoiceMailNumber()));
        params.add(new ParamItem("isNetworkRoaming", tm.isNetworkRoaming()));
        params.add(new ParamItem("isSmsCapable", tm.isSmsCapable()));
        params.add(new ParamItem("isTtyModeSupported", tm.isTtyModeSupported()));
        params.add(new ParamItem("isVoiceCapable", tm.isVoiceCapable()));
        params.add(new ParamItem("isWorldPhone", tm.isWorldPhone()));

        showUI();
    }

    private void showUI() {

        try {
//            Process process = Runtime.getRuntime().exec("top -n 1 -d 1");
            Process process = Runtime.getRuntime().exec("cat /proc/uptime");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Log.d(TAG, "----------------------------------------------------");
            String aux;
            while ((aux = in.readLine()) != null) {
                Log.d(TAG, aux);
            }
            Log.d(TAG, "----------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            public void run() {
                ParamAdapter adapter = new ParamAdapter(getApplicationContext(), R.layout.listview_item_row, params);
                Log.d(TAG, "test3");
                ListView listView = (ListView) findViewById(R.id.listView);
                assert listView != null;
                listView.setAdapter(adapter);
            }
        });


    }
}
