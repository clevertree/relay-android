package net.relayproject.relayclient;

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

import java.util.List;
import java.util.Locale;

/**
 * Created by ari on 11/30/2015.
 */
public class ClientLocationListener implements LocationListener {
    private static final String TAG = ClientLocationListener.class.getName();

    private final ClientHostActivity mClientHostActivity;

    public ClientLocationListener(ClientHostActivity clientHostActivity) {

        LocationManager locationManager = (LocationManager)
                clientHostActivity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(clientHostActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(clientHostActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);

            locationManager.requestLocationUpdates(provider, 5000, 10, this);
        }

        mClientHostActivity = clientHostActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geoCoder = new Geocoder(mClientHostActivity, Locale.getDefault());
        StringBuilder builder = new StringBuilder();

        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

            int maxLines = addresses.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = addresses.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");

                String finalAddress = builder.toString(); //This is the complete address.
                mClientHostActivity.addSuggestedCommand("JOIN " + finalAddress, "Address");
            }
        } catch (Exception e) {
            mClientHostActivity.handleException(e);
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
