package net.relayproject.relayclient;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by ari on 11/27/2015.
 */
public class JSInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    JSInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}