package net.relayproject.relayclient;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ari on 11/30/2015.
 */
public class ClientLocationListener implements LocationListener {

    private final ClientHostActivity mClientHostActivity;

    public ClientLocationListener(ClientHostActivity clientHostActivity) {
        mClientHostActivity = clientHostActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geoCoder = new Geocoder(mClientHostActivity, Locale.getDefault());
        StringBuilder builder = new StringBuilder();

        List<Address> address = null;
        try {
            address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");

                String finalAddress = builder.toString(); //This is the complete address.
                mClientHostActivity.addSuggestedCommand("JOIN " + finalAddress);
            }
        } catch (IOException e) {
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
