package net.relayproject.relayclient.proximity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import net.relayproject.relayclient.ClientHostActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;

/**
 * Created by ari on 12/1/2015.
 */
public class ClientGeoIPListener {
    private static final String TAG = ClientGeoIPListener.class.getSimpleName();

    private final ClientHostActivity mClientHostActivity;

    public ClientGeoIPListener(ClientHostActivity clientHostActivity) {
        clientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /timezone/" + TimeZone.getDefault().getID().toLowerCase());

        mClientHostActivity = clientHostActivity;
        new GetIPAddress().execute();
    }

    class GetIPAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL("https://freegeoip.net/json/");
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
        protected void onPostExecute(String responseText) {
            super.onPostExecute(responseText);

            JSONObject obj = null;
            try {
                obj = new JSONObject(responseText);

                if(obj.has("time_zone"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /timezone/" + obj.getString("time_zone").toLowerCase());

                if(obj.has("ip"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /ip/" + obj.getString("ip"));

                if(obj.has("country_code"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /country/" + obj.getString("country_code"));

//                if(obj.has("country_name"))
//                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /country/" + obj.getString("country_name"));

                if(obj.has("region_code"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /state/" + obj.getString("region_code").toLowerCase());

//                if(obj.has("region_name"))
//                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /state/" + obj.getString("region_name"));

                if(obj.has("city"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /city/" + obj.getString("city").toLowerCase().replace(' ', '_'));

                if(obj.has("zip_code"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /zipcode/" + obj.getString("zip_code"));

                if(obj.has("longitude"))
                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST " +
                            "/gps" +
                            "/" + Math.round(obj.getDouble("longitude")) +
                            "/" + Math.round(obj.getDouble("latitude"))
                    );

//                if(obj.has("metro_code"))
//                    mClientHostActivity.execute("CHANNEL.SEARCH.SUGGEST /metro/" + obj.getString("metro_code"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
