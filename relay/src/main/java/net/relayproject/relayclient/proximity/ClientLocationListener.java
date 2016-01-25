package net.relayproject.relayclient.proximity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import net.relayproject.relayclient.view.ManagedWebView;

import java.util.Locale;

/**
 * Created by ari on 11/30/2015.
 */
public class ClientLocationListener implements LocationListener {
    private static final String TAG = ClientLocationListener.class.getSimpleName();

    private final ManagedWebView mWebView;

    public ClientLocationListener(ManagedWebView webView) {

        LocationManager locationManager = (LocationManager)
                webView.getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(webView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(webView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);

            if(provider != null)
                locationManager.requestLocationUpdates(provider, 5000, 10, this);
        }

        mWebView = webView;
    }

    @Override
    public void onLocationChanged(Location location) {

        mWebView.execute("CHANNEL.SEARCH.SUGGEST /gps" +
                        "/" + Math.round(location.getLongitude()) +
                        "/" + Math.round(location.getLatitude())
        );

        try {
            Geocoder geoCoder = new Geocoder(mWebView.getContext(), Locale.getDefault());

            Address address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);

            mWebView.execute("CHANNEL.SEARCH.SUGGEST /city/" + address.getSubAdminArea().replace(" ", "_"));
            mWebView.execute("CHANNEL.SEARCH.SUGGEST /state/" + address.getAdminArea().replace(" ", "_"));
            mWebView.execute("CHANNEL.SEARCH.SUGGEST /co/" + address.getCountryCode().replace(" ", "_"));
            mWebView.execute("CHANNEL.SEARCH.SUGGEST /zip/" + address.getPostalCode().replace(" ", "_"));


        } catch (Exception e) {
//            mWebView.handleException(e);
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
