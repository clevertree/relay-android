package net.relayproject.relayclient;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import net.relayproject.relayclient.client.HostInterface;
import net.relayproject.relayclient.interfaces.ClientResponseListener;
import net.relayproject.relayclient.proximity.ClientGeoIPListener;
import net.relayproject.relayclient.proximity.ClientLocationListener;
import net.relayproject.relayclient.proximity.ClientWIFIListener;

import java.util.Arrays;
import java.util.List;

public class ClientHostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClientResponseListener {

    private HostInterface mHostInterface;
    private NavigationView mNavigationView;

    private static final String TAG = ClientHostActivity.class.getSimpleName();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doBindService();

        setContentView(R.layout.activity_client_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClick(view);
            }
        });
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


        WebView webView = (WebView) findViewById(R.id.web_view_host);

        // TODO: preserve instance?
//        if (savedInstanceState != null) {
//            webView.restoreState(savedInstanceState);

//        } else {
//            if(webView.getUrl() == null) {

//            }
//        }

        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mHostInterface = new HostInterface(this, webView);

//        if(webView.getUrl() == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                webView.loadUrl("file:///androiclient-android-portrait.htmlone.html");

            } else {
                webView.loadUrl("file:///androiclient-android-browser.html");

            }
//        }


//        Menu navigationMenu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
//        navigationMenu.findItem(R.id.nav_recent_commands_menu).setVisible(false);
//        navigationMenu.findItem(R.id.nav_suggested_commands_menu).setVisible(false);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        WebView webView = (WebView) findViewById(R.id.web_view_host);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        WebView webView = (WebView) findViewById(R.id.web_view_host);
        webView.restoreState(savedInstanceState);
    }

    private void onFABClick(View view) {
//        mHostInterface.sendCommand();

        mHostInterface.sendCommand("UI.CONTACTS");
//        Snackbar.make(view, "Private Messaging Coming Soon", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_about) {
//            return true;
//        }

        return onNavigationItemSelected(item);
    }


    public void rebuildNavigationViewMenu(List<String> menuLines) { // String showNavGroup
        Menu menu = mNavigationView.getMenu(); // findViewById(R.id.nav_suggested_commands_menu);
        menu.clear();

        Menu currentMenu = menu;
        for(String menuLine: menuLines) {
            if(menuLine.length() == 0)
                continue;

            String[] args = menuLine.split(";");

            if(args[0].charAt(0) == '#') {
                // Section
                currentMenu = menu.addSubMenu(args[1]);

            } else {
                MenuItem menuItem = currentMenu
                        .add(args[1])
                        .setIcon(R.drawable.ic_menu_send);
                menuItem.setTitleCondensed(args[0]);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        Menu navigationMenu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
//        final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
//        final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
//        wrapped.notifyDataSetChanged();

        switch(item.getItemId()) {
//            case R.id.nav_command_join:
//                sendCommandJoinChannel();
//                break;

            case R.id.action_pgp_keygen:
                mHostInterface.sendCommand("PGP.KEYGEN");
                break;

            case R.id.action_pgp_import:
                mHostInterface.sendCommand("PGP.IMPORT");
                break;
            case R.id.action_pgp_export:
                mHostInterface.sendCommand("PGP.IMPORT");
                break;

            case R.id.action_pgp_manage:
                mHostInterface.sendCommand("PGP.MANAGE");
                break;

            case R.id.action_about:
            case R.id.nav_command_about:
                mHostInterface.sendCommand("ABOUT");
                break;

            case R.id.action_about_alpha:
                String url = "https://www.facebook.com/relay.dev/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            case R.id.action_reload_client:
                mHostInterface.sendCommand("RELOAD");
                break;

            case R.id.action_render_menu:
                mHostInterface.sendCommand("RENDER {nav:menu}");
                break;

            default:
                String commandString = (String) item.getTitleCondensed();
                if(commandString == null || commandString.length() == 0)
                    throw new IllegalArgumentException("Unhandled Nav ID: " + item.getItemId());

                mHostInterface.sendCommand(commandString);


                if(commandString.substring(0, 7).equalsIgnoreCase("UI.MENU")) {
                    // Don't close drawer

                } else {
                    // Refresh Menu
                    mHostInterface.sendCommand("UI.MENU.TEXT");
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//
//    private void sendCommandJoinChannel() {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                this);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, CHANNEL_SUGGESTIONS);
//
//        final AutoCompleteTextView input = new AutoCompleteTextView (this);
//        input.setAdapter(adapter);
//        input.setHint("Enter Channel Path: [i.e. /subject/topic/subtopic]");
//        alertDialogBuilder.setView(input);
//
//        // set dialog message
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("Join",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                mHostInterface.sendCommand("JOIN " + input.getText());
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // show it
//        alertDialog.show();
//    }

//    public void addSuggestedCommand(String suggestedCommand) {
//        String titleString = suggestedCommand
//                .replace("CHANNEL.SUBSCRIBE", "SUBSCRIBE");
//
//        addSuggestedCommand(suggestedCommand, titleString);
//    }

//    public void addSuggestedCommand(String suggestedCommand) {
//        if(CHANNEL_SUGGESTIONS == null)
//            CHANNEL_SUGGESTIONS = new ArrayList<String>();
//
//        for(int i=0; i<CHANNEL_SUGGESTIONS.size(); i++) {
//           if(suggestedCommand.equalsIgnoreCase(CHANNEL_SUGGESTIONS.get(i))) {
//               CHANNEL_SUGGESTIONS.remove(i);
//               break;
//           }
//        }
//
//        Log.v(TAG, "Adding suggested command: " + suggestedCommand);
//        CHANNEL_SUGGESTIONS.add(0, suggestedCommand);
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        Menu menu = navigationView.getMenu(); // findViewById(R.id.nav_suggested_commands_menu);
//        Menu suggestedCommandsMenu = menu.findItem(R.id.nav_suggested_channels_menu).getSubMenu();
//
//        suggestedCommandsMenu.clear();
//        for(String suggestedCommand2: CHANNEL_SUGGESTIONS)
//            suggestedCommandsMenu
//                    .add(1, Menu.FIRST, Menu.FIRST, suggestedCommand2)
//                    .setTitleCondensed(suggestedCommand2)
//                    .setIcon(R.drawable.ic_menu_send);
//
//    }

    public boolean execute(String commandString) {
        if(mHostInterface == null) {
            throw new RuntimeException("Host not set yet");
        }
        mHostInterface.sendCommand(commandString);
        return true;
    }

    public void handleException(Exception e) {
    }


    @Override
    public void processResponse(final String responseString) {
        String command = responseString.split("\\s+|\\.")[0].toLowerCase();
        switch(command) {
            case "event":
                // Log.v("I", responseString);
                handleEventResponse(responseString);
                break;

            case "render":
                break;

            case "channel":
//                Log.v("I", responseString);
                break;

            case "ui":
                Log.v("I", responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleUIResponse(responseString);
                    }
                });
                break;

            default:
                Log.w("I", responseString);
                break;

        }
    }

    private void handleUIResponse(String responseString) {
        List<String> lines = Arrays.asList(responseString.split("\\n"));
        String firstLine = lines.get(0);
        String[] args = firstLine.split("\\s+");
        switch(args[0].toLowerCase()) {
            case "ui.menu.list":
            case "ui.menu.text":
            case "ui.menu":
                rebuildNavigationViewMenu(lines.subList(1, lines.size()));
                break;

            default:
                Log.e("I", "Invalid UI Response: " + responseString);
        }
    }

    private void handleEventResponse(String responseString) {
        String eventCommand = responseString.split("\\s+")[1].toLowerCase();
        switch(eventCommand) {
            default:
                Log.w("I", responseString);
                break;
        }
    }

//            case "channel.search.list":
//
//                String[] newChannels = responseString.split("\\n");
//                if(CHANNEL_SUGGESTIONS == null)
//                    CHANNEL_SUGGESTIONS = new ArrayList<String>();
//                CHANNEL_SUGGESTIONS.clear();
//                CHANNEL_SUGGESTIONS.addAll(
//                        Arrays.asList(newChannels)
//                                .subList(1, newChannels.length));
//
//                rebuildNavigationViewMenu();
////                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
////                Menu menu = navigationView.getMenu(); // findViewById(R.id.nav_suggested_commands_menu);
////                Menu suggestedCommandsMenu = menu.findItem(R.id.nav_suggested_channels_menu).getSubMenu();
////
////                suggestedCommandsMenu.clear();
////                for(String suggestedCommand: CHANNEL_SUGGESTIONS)
////                    suggestedCommandsMenu
////                            .add(1, Menu.FIRST, Menu.FIRST, suggestedCommand)
////                            .setTitleCondensed(suggestedCommand)
////                            .setIcon(R.drawable.ic_menu_send);
//
//                Log.v("I", responseString);
//                break;
//
//            case "keyspace.search.list":
//
//                String[] newIDs = responseString.split("\\n");
//                if(KEYSPACE_SUGGESTIONS == null)
//                    KEYSPACE_SUGGESTIONS = new ArrayList<String>();
//                KEYSPACE_SUGGESTIONS.clear();
//                KEYSPACE_SUGGESTIONS.addAll(
//                        Arrays.asList(newIDs)
//                                .subList(1, newIDs.length));
//
//                rebuildNavigationViewMenu();
////                NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
////                Menu menu2 = navigationView2.getMenu(); // findViewById(R.id.nav_suggested_commands_menu);
////                Menu suggestedCommandsMenu2 = menu2.findItem(R.id.nav_suggested_channels_menu).getSubMenu();
//
////                suggestedCommandsMenu2.clear();
////                for(String suggestedCommand: KEYSPACE_SUGGESTIONS)
////                    suggestedCommandsMenu2
////                            .add(1, Menu.FIRST, Menu.FIRST, suggestedCommand)
////                            .setTitleCondensed(suggestedCommand)
////                            .setIcon(R.drawable.ic_menu_send);
//
//                Log.v("I", responseString);
//                break;

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        // Handled elsewhere
        // Log.e("CONSOLE", cm.messageLevel() + " " + cm.message());
        return false;
    }

    @Override
    public void onClientPageFinished(WebView view, String url) {
        String commandString = getIntent().getStringExtra("command");
        if(commandString != null) {
            getIntent().removeExtra("command");
            mHostInterface.sendCommand(commandString);

        }

        // Refresh Menu
        mHostInterface.sendCommand("UI.MENU.TEXT");

        new ClientLocationListener(this);
        new ClientWIFIListener(this);
        new ClientGeoIPListener(this);

    }



    /** Messenger for communicating with service. */
    private Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound = false;

    /** Some text view we are using to show state information. */
//    TextView mCallbackText;

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RelayService.MSG_SET_VALUE:
//                    mCallbackText.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
//            mCallbackText.setText("Attached.");

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        RelayService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

                // Give it some value as an example.
                msg = Message.obtain(null,
                        RelayService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            // As part of the sample, tell the user what happened.
            Toast.makeText(ClientHostActivity.this, R.string.service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
//            mCallbackText.setText("Disconnected.");

            // As part of the sample, tell the user what happened.
            Toast.makeText(ClientHostActivity.this, R.string.service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(ClientHostActivity.this,
                RelayService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
