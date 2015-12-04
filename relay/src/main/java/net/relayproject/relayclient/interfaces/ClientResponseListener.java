package net.relayproject.relayclient.interfaces;

import android.webkit.ConsoleMessage;
import android.webkit.WebView;

/**
 * Created by ari on 11/27/2015.
 */


public interface ClientResponseListener {
    public void handleResponse(String responseString);

    public boolean onConsoleMessage(ConsoleMessage cm);

    public void onClientPageFinished(WebView view, String url);
}
