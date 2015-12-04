package net.relayproject.relayclient.client;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.relayproject.relayclient.interfaces.ClientResponseListener;


public class HostInterface {
    private static final String TAG = HostInterface.class.getSimpleName();

    private ClientResponseListener mResponseListener;
    private WebView mWebView;


    /** Instantiate the interface and set the context */
    @SuppressLint("AddJavascriptInterface")
    public HostInterface(ClientResponseListener hostClientResponseListener, WebView hostWebView) {
        mWebView = hostWebView;
        mWebView.addJavascriptInterface(this, "Host");

        mResponseListener = hostClientResponseListener;

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                        cm.lineNumber(), cm.sourceId()));
                mResponseListener.onConsoleMessage(cm);
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mResponseListener.onClientPageFinished(view, url);
            }
        });

    }



    public void sendCommand(String command) {
        mWebView.loadUrl("javascript:ClientSocketWorker.sendCommand('" + command + "');");
        Log.v(TAG, "Command: " + command);
    }
}