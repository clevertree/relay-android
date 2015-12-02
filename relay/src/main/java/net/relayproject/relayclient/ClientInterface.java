package net.relayproject.relayclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by ari on 11/27/2015.
 */
public class ClientInterface extends WebChromeClient {
    WebView mWebView;

    /** Instantiate the interface and set the context */
    @SuppressLint("AddJavascriptInterface")
    public ClientInterface(WebView webView) {
        mWebView = webView;
        mWebView.addJavascriptInterface(this, "Host");
        mWebView.setWebChromeClient(this);
    }

    private static final String TAG = ClientInterface.class.getSimpleName();

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                cm.lineNumber(), cm.sourceId()));
        return true;
    }


    @JavascriptInterface
    public void handleResponse(String responseString) {
        Log.i(TAG, "Response: " + responseString);
    }


    public void sendCommand(String command) {
        mWebView.loadUrl("javascript:ClientSocketWorker.sendCommand('" + command + "');");
        Log.v(TAG, "Command: " + command);
    }
}