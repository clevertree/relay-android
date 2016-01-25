package net.relayproject.relayclient.proximity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import net.relayproject.relayclient.ManagedWebView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ari on 12/1/2015.
 */
public class ClientWIFIListener {
    private static final String TAG = ClientWIFIListener.class.getSimpleName();

    public ClientWIFIListener(ManagedWebView webView) {
        WifiManager wifiMgr = (WifiManager) webView.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String SSID = wifiInfo.getSSID();
        String BSSID = wifiInfo.getBSSID();
        String MAC = wifiInfo.getMacAddress();

//        int IP_ADDRESS = wifiInfo.getIpAddress();
        int NETWORK_ID = wifiInfo.getNetworkId();
//        https://freegeoip.net/json/
//        if(IP_ADDRESS > 0) mWebViewHostService.execute("CHANNEL.SEARCH.SUGGEST /ip/" + String.format("%d.%d.%d.%d",
//                (IP_ADDRESS & 0xff),
//                (IP_ADDRESS >> 8 & 0xff),
//                (IP_ADDRESS >> 16 & 0xff),
//                (IP_ADDRESS >> 24 & 0xff)));
        if(NETWORK_ID > 0) webView.execute("CHANNEL.SEARCH.SUGGEST /ip/" + String.format("%d.%d.%d.%d",
                (NETWORK_ID & 0xff),
                (NETWORK_ID >> 8 & 0xff),
                (NETWORK_ID >> 16 & 0xff),
                (NETWORK_ID >> 24 & 0xff)));
//        if(BSSID != null) mWebViewHostService.execute("CHANNEL.SEARCH.SUGGEST /bssid/" + BSSID);
//        if(MAC != null) mWebViewHostService.execute("CHANNEL.SEARCH.SUGGEST /mac/" + MAC);
        if(SSID != null) webView.execute("CHANNEL.SEARCH.SUGGEST /ssid/" + SSID.replace("\"", "").replace(" ", "_"));
//        mWebViewHostService.execute("CHANNEL.SEARCH.SUGGEST /wifi/" + BSSID + "/" + SSID);
        Log.v(TAG, wifiInfo.toString());

        new GetIPAddress(webView).execute();


        List<ScanResult> results = wifiMgr.getScanResults();
        int size = results.size();

        for (int i = 0; i < size; i++) {
            ScanResult result = results.get(i);
            if (!result.SSID.isEmpty())
                webView.execute("CHANNEL.SEARCH.SUGGEST /ssid/" + result.SSID.replace("\"", "").replace(" ", "_"));

        }
    }

    class GetIPAddress extends AsyncTask<String, Void, String> {

        private final ManagedWebView mWebView;

        public GetIPAddress(ManagedWebView webView) {
            mWebView = webView;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL("http://ifcfg.me/ip");
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String ipAddress) {
            super.onPostExecute(ipAddress);

            if(ipAddress.length() > 0)
                mWebView.execute("CHANNEL.SEARCH.SUGGEST /ip/" + ipAddress);
        }
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
