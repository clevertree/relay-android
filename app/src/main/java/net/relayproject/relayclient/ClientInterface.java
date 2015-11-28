package net.relayproject.relayclient;

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
    Context mContext;

    /** Instantiate the interface and set the context */
    public ClientInterface(WebView webView) {
        mContext = webView.getContext();
        webView.addJavascriptInterface(this, "Host");
        webView.setWebChromeClient(this);
    }

    private static final String TAG = ClientInterface.class.getName();

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                cm.lineNumber(), cm.sourceId()));
        return true;
    }


    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }


}