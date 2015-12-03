package net.relayproject.relayclient.listener;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import net.relayproject.relayclient.ClientHostActivity;

/**
 * Created by ari on 12/1/2015.
 */
public class ClientWIFIListener {
    private static final String TAG = ClientWIFIListener.class.getSimpleName();

    public ClientWIFIListener(ClientHostActivity clientHostActivity) {
        WifiManager wifiMgr = (WifiManager) clientHostActivity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String SSID = wifiInfo.getSSID();
        String BSSID = wifiInfo.getBSSID();
        String MAC = wifiInfo.getMacAddress();

        int IP_ADDRESS = wifiInfo.getIpAddress();
        int NETWORK_ID = wifiInfo.getNetworkId();

        if(IP_ADDRESS > 0) clientHostActivity.addSuggestedCommand("JOIN /ip/" + String.format("%d.%d.%d.%d",
                (IP_ADDRESS & 0xff),
                (IP_ADDRESS >> 8 & 0xff),
                (IP_ADDRESS >> 16 & 0xff),
                (IP_ADDRESS >> 24 & 0xff)));
        if(NETWORK_ID > 0) clientHostActivity.addSuggestedCommand("JOIN /ip/" + String.format("%d.%d.%d.%d",
                (NETWORK_ID & 0xff),
                (NETWORK_ID >> 8 & 0xff),
                (NETWORK_ID >> 16 & 0xff),
                (NETWORK_ID >> 24 & 0xff)));
        if(BSSID != null) clientHostActivity.addSuggestedCommand("JOIN /bssid/" + BSSID);
//        if(MAC != null) clientHostActivity.addSuggestedCommand("JOIN /mac/" + MAC);
        if(SSID != null) clientHostActivity.addSuggestedCommand("JOIN /ssid/" + SSID.replace("\"", "").replace(" ", "_"));
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
