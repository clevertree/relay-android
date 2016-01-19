package net.relayproject.relayclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.relayproject.relayclient.client.HostInterface;

import java.util.ArrayList;

public class RelayService extends Service {
    private static final String TAG = HostInterface.class.getSimpleName();

    public static final int MSG_RESPONSE = 1;

    /** For showing and hiding our notification. */
    NotificationManager mNM;
    /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();

    private WebView mWebView;
    private boolean mPageLoaded = false;
    private ArrayList<String> mQueuedCommands = new ArrayList<>();

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(!mClients.contains(msg.replyTo))
                mClients.add(msg.replyTo);

            String messageString = (String) msg.obj;
            execute(messageString);
        }
    }

    void execute(String commandString) {

        if(!mPageLoaded) {
            mQueuedCommands.add(commandString);

        } else {
            mWebView.loadUrl("javascript:Client.execute('" + commandString + "');");
            Log.v(TAG, "Command: " + commandString);
        }
    }


    @JavascriptInterface
    void processResponse(String responseString) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i).send(Message.obtain(null,
                        MSG_RESPONSE, 0, 0, responseString));

            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                // we are going through the list from back to front
                // so this is safe to do inside the loop.
                mClients.remove(i);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        mWebView = new WebView(this);
        mWebView.loadUrl("file:///android_asset/service-android.html");
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                showNotification();

                mPageLoaded = true;
                String[] queue = mQueuedCommands.toArray(new String[mQueuedCommands.size()]);
                mQueuedCommands.clear();
                for (String queuedCommand : queue) {
                    mWebView.loadUrl("javascript:Client.execute('" + queuedCommand + "');");
                    Log.v(TAG, "Queued Command: " + queuedCommand);
                }
            }
        });


        mWebView.addJavascriptInterface(this, "Host");

    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HostInterface.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_send)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.service_started, notification);
    }
}
