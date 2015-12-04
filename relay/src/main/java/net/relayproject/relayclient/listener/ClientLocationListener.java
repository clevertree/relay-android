package net.relayproject.relayclient.listener;

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

import net.relayproject.relayclient.ClientHostActivity;

import java.util.Locale;

/**
 * Created by ari on 11/30/2015.
 */
public class ClientLocationListener implements LocationListener {
    private static final String TAG = ClientLocationListener.class.getSimpleName();

    private final ClientHostActivity mClientHostActivity;

    public ClientLocationListener(ClientHostActivity clientHostActivity) {

        LocationManager locationManager = (LocationManager)
                clientHostActivity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(clientHostActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(clientHostActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);

            locationManager.requestLocationUpdates(provider, 5000, 10, this);
        }

        mClientHostActivity = clientHostActivity;
    }

    @Override
    public void onLocationChanged(Location location) {

        mClientHostActivity.addSuggestedCommand(
            "JOIN /gps" +
            "/" + Math.round(location.getLongitude()) +
            "/" + Math.round(location.getLatitude())
        );

        try {
            Geocoder geoCoder = new Geocoder(mClientHostActivity, Locale.getDefault());
            StringBuilder builder = new StringBuilder();

            Address address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);

            mClientHostActivity.addSuggestedCommand("JOIN /city/" + address.getSubAdminArea().replace(" ", "_"));
            mClientHostActivity.addSuggestedCommand("JOIN /state/" + address.getAdminArea().replace(" ", "_"));
            mClientHostActivity.addSuggestedCommand("JOIN /co/" + address.getCountryCode().replace(" ", "_"));
            mClientHostActivity.addSuggestedCommand("JOIN /zip/" + address.getPostalCode().replace(" ", "_"));


        } catch (Exception e) {
            mClientHostActivity.handleException(e);
//            e.printStackTrace();
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
