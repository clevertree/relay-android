package net.relayproject.relayclient.client;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.relayproject.relayclient.interfaces.ClientResponseListener;

import java.util.ArrayList;


public class HostInterface {
    private static final String TAG = HostInterface.class.getSimpleName();

    private ClientResponseListener mResponseListener;
    private WebView mWebView;

    private boolean mPageLoaded = false;
    private ArrayList<String> mQueuedCommands = new ArrayList<>();

    /** Instantiate the interface and set the context */
    @SuppressLint("AddJavascriptInterface")
    public HostInterface(ClientResponseListener hostClientResponseListener, WebView hostWebView) {
        mWebView = hostWebView;
        mWebView.addJavascriptInterface(this, "Host");

        mResponseListener = hostClientResponseListener;

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                switch(cm.messageLevel()) {
                    default:
                    case DEBUG:
                        Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                                cm.lineNumber(), cm.sourceId()));
                        break;
                    case ERROR:
                        Log.e(TAG, String.format("%s @ %d: %s", cm.message(),
                                cm.lineNumber(), cm.sourceId()));
                        break;
                    case LOG:
                        Log.v(TAG, String.format("%s @ %d: %s", cm.message(),
                                cm.lineNumber(), cm.sourceId()));
                        break;
                    case TIP:
                        Log.i(TAG, String.format("%s @ %d: %s", cm.message(),
                                cm.lineNumber(), cm.sourceId()));
                        break;
                    case WARNING:
                        Log.w(TAG, String.format("%s @ %d: %s", cm.message(),
                                cm.lineNumber(), cm.sourceId()));
                        break;
                }
                mResponseListener.onConsoleMessage(cm);
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                mPageLoaded = true;
                mResponseListener.onClientPageFinished(view, url);
                String[] queue = mQueuedCommands.toArray(new String[mQueuedCommands.size()]);
                mQueuedCommands.clear();
                for(String queuedCommand: queue) {
                    mWebView.loadUrl("javascript:Client.execute('" + queuedCommand + "');");
                    Log.v(TAG, "Queued Command: " + queuedCommand);
                }

            }
        });

    }

    @JavascriptInterface
    public void processResponse(String responseString) {
        mResponseListener.processResponse(responseString);
//        Log.i(TAG, "Response: " + responseString);
    }

    public void sendCommand(String command) {
        if(!mPageLoaded) {
            mQueuedCommands.add(command);

        } else {
            mWebView.loadUrl("javascript:Client.execute('" + command + "');");
            Log.v(TAG, "Command: " + command);
        }
    }
}