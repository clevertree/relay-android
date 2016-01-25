package net.relayproject.relayclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ManagedWebView extends WebView {
    private static final String TAG = ManagedWebView.class.getSimpleName();

    private boolean mPageLoaded = false;
    private ArrayList<String> mQueuedCommands = new ArrayList<>();
    private ManagedWebView mWebViewClient = null;
    private ManagedWebView mWebViewService = null;

    public ManagedWebView(Context context) {
        super(context);
        init();
    }

    public ManagedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManagedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        addJavascriptInterface(this, "Host");
//        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
        getSettings().setLoadsImagesAutomatically(true);

        setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mPageLoaded = true;

                Log.v(TAG, "Page Loaded: " + url);
                loadUrl("javascript:console.log('Host Loaded: " + url + "');");

                String[] queue = mQueuedCommands.toArray(new String[mQueuedCommands.size()]);
                mQueuedCommands.clear();
                for (String queuedCommand : queue) {
                    loadUrl("javascript:Client.execute('" + queuedCommand.replace("'", "\\'") + "');");
                    Log.v(TAG, "Queued Command: " + queuedCommand);
                }
            }
        });


        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                switch (cm.messageLevel()) {
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
                // mResponseListener.onConsoleMessage(cm);
                return true;
            }
        });
    }


    @JavascriptInterface
    public void execute(final String commandString) {

        if(mWebViewService != null) {
            // Execute via Service Instance
//            Log.v(TAG, "Passing Execution to Service Instance: " + commandString);
            mWebViewService.execute(commandString);

        } else {
            // Execute via Client
            if(!mPageLoaded) {
                mQueuedCommands.add(commandString);

            } else {

                post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Command: " + commandString);
                        loadUrl("javascript:Client.execute('" + commandString.replace("'", "\\'") + "');");
                    }
                });
            }
        }

    }


    @JavascriptInterface
    public void processResponse(final String responseString) {

        if(mWebViewClient != null) {
            // Send response to Client Instance
//            Log.v(TAG, "Passing Response to Client Instance: " + responseString);
            mWebViewClient.processResponse(responseString);

        } else {


            post(new Runnable() {
                @Override
                public void run() {
                    // Process response locally
//                    Log.v(TAG, "Response: " + responseString);
                    loadUrl("javascript:Client.processResponse('" + responseString.replace("'", "\\'") + "');");
                }
            });
        }
    }


    public void setWebViewClientInstance(ManagedWebView webViewClient) {
        mWebViewClient = webViewClient;
    }

    public void setWebViewServiceInstance(ManagedWebView webViewService) {
        mWebViewService = webViewService;
    }
}


