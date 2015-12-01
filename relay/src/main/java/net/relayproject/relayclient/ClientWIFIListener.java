package net.relayproject.relayclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by ari on 12/1/2015.
 */
public class ClientWIFIListener {
    private static final String TAG = ClientWIFIListener.class.getName();

    public ClientWIFIListener(ClientHostActivity clientHostActivity) {
        WifiManager wifiMgr = (WifiManager) clientHostActivity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String SSID = wifiInfo.getSSID().replace("\"", "").replace(" ", "_");
        String BSSID = wifiInfo.getBSSID();
        String MAC = wifiInfo.getMacAddress();

        int ipAddress = wifiInfo.getIpAddress();
        String IP_ADDRESS = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));

        clientHostActivity.addSuggestedCommand("JOIN /ssid/" + SSID, SSID);
        clientHostActivity.addSuggestedCommand("JOIN /bssid/" + BSSID, SSID);
        clientHostActivity.addSuggestedCommand("JOIN /ip/" + IP_ADDRESS, SSID);
        clientHostActivity.addSuggestedCommand("JOIN /mac/" + MAC, SSID);
//        clientHostActivity.addSuggestedCommand("JOIN /wifi/" + BSSID + "/" + SSID);
        Log.v(TAG, wifiInfo.toString());
    }

//    public String getCurrentSSID(Context context) {
//
//        String ssid = null;
//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (networkInfo.isConnected()) {
//            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//            if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
//                //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
//                ssid = connectionInfo.getSSID();
//            }
//            // Get WiFi status MARAKANA
//            WifiInfo info = wifiManager.getConnectionInfo();
//            String textStatus = "";
//            textStatus += "\n\nWiFi Status: " + info.toString();
//            String BSSID = info.getBSSID();
//            String MAC = info.getMacAddress();
//
//            List<ScanResult> results = wifiManager.getScanResults();
//            ScanResult bestSignal = null;
//            int count = 1;
//            String etWifiList = "";
//            for (ScanResult result : results) {
//                etWifiList += count++ + ". " + result.SSID + " : " + result.level + "\n" +
//                        result.BSSID + "\n" + result.capabilities +"\n" +
//                        "\n=======================\n";
//            }
//            Log.v(TAG, "from SO: \n" + etWifiList);
//
//            // List stored networks
//            List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
//            for (WifiConfiguration config : configs) {
//                textStatus+= "\n\n" + config.toString();
//            }
//            Log.v(TAG,"from marakana: \n"+textStatus);
//        }
//        return ssid;
//    }

}
