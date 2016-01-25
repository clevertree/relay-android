package net.relayproject.relayclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import net.relayproject.relayclient.proximity.ClientGeoIPListener;
import net.relayproject.relayclient.proximity.ClientLocationListener;
import net.relayproject.relayclient.proximity.ClientWIFIListener;
import net.relayproject.relayclient.view.ManagedWebView;

import java.util.Arrays;
import java.util.List;

public class ClientHostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ManagedWebView mWebViewService;
    private ManagedWebView mWebViewClient;
    /** Messenger for communicating with service. */
//    private Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
//    private boolean mIsBound = false;

//    private HostInterface mHostInterface;
    private NavigationView mNavigationView;

    private static final String TAG = ClientHostActivity.class.getSimpleName();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        doBindService();

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

        // Retrieve UI elements
        mWebViewClient = ((ManagedWebView) findViewById(R.id.webViewClient));
        mWebViewClient.setWebViewServiceInstance(mWebViewService);

        initServiceWebView();
        initClientWebView();

//        Menu navigationMenu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
//        navigationMenu.findItem(R.id.nav_recent_commands_menu).setVisible(false);
//        navigationMenu.findItem(R.id.nav_suggested_commands_menu).setVisible(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewService.setWebViewClientInstance(null);
    }

    private void onFABClick(View view) {
        mWebViewClient.execute("UI.CONTACTS");
        view.setVisibility(View.INVISIBLE);

//        Snackbar.make(view, "Private Messaging Coming Soon", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
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
                mWebViewClient.execute("PGP.KEYGEN");
                break;

            case R.id.action_pgp_import:
                mWebViewClient.execute("PGP.IMPORT");
                break;
            case R.id.action_pgp_export:
                mWebViewClient.execute("PGP.IMPORT");
                break;

            case R.id.action_pgp_manage:
                mWebViewClient.execute("PGP.MANAGE");
                break;

            case R.id.action_about:
            case R.id.nav_command_about:
                mWebViewClient.execute("ABOUT");
                break;

            case R.id.action_about_alpha:
                String url = "https://www.facebook.com/relay.dev/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            case R.id.action_reload_client:
                mWebViewClient.execute("RELOAD");
                break;

            case R.id.action_render_menu:
                mWebViewClient.execute("RENDER {nav:menu}");
                break;

            default:
                String commandString = (String) item.getTitleCondensed();
                if(commandString == null || commandString.length() == 0)
                    throw new IllegalArgumentException("Unhandled Nav ID: " + item.getItemId());

                mWebViewClient.execute(commandString);


                if(commandString.substring(0, 7).equalsIgnoreCase("UI.MENU")) {
                    // Don't close drawer

                } else {
                    // Refresh Menu
                    mWebViewClient.execute("UI.MENU.TEXT");
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    /** Some text view we are using to show state information. */
//    TextView mCallbackText;

    // Execute to service
    public void execute(String commandString) {
        mWebViewClient.execute(commandString);
    }


    @JavascriptInterface
    // Process Response to WebView
    public void processResponse(String responseString) {
        mWebViewClient.processResponse(responseString);
        Log.v(TAG, "Response: " + responseString);
    }



    protected void initClientWebView() {

        WebSettings settings = mWebViewClient.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        mWebViewClient.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebViewClient.setScrollbarFadingEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);


        String commandString = getIntent().getStringExtra("command");
        if(commandString != null) {
            getIntent().removeExtra("command");
            mWebViewClient.execute(commandString);
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mWebViewClient.loadUrl("file:///android_asset/client-android-portrait.html");

        } else {
            mWebViewClient.loadUrl("file:///android_asset/client-android.html");

        }


        // Refresh Menu
        mWebViewClient.execute("UI.MENU.TEXT");
    }

    protected void initServiceWebView() {

        // Retrieve UI elements
        FrameLayout webViewPlaceholder = ((FrameLayout) findViewById(R.id.webViewServicePlaceholder));

        // Initialize the WebView if necessary
        if (mWebViewService == null) {
            // Create the webview
            mWebViewService = new ManagedWebView(this);
            mWebViewService.setWebViewClientInstance(mWebViewClient);
            mWebViewClient.setWebViewServiceInstance(mWebViewService);

            WebSettings settings = mWebViewService.getSettings();
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            mWebViewService.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebViewService.setScrollbarFadingEnabled(true);
            settings.setLoadsImagesAutomatically(true);
            settings.setAllowFileAccess(true);
            settings.setJavaScriptEnabled(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setDomStorageEnabled(true);

            mWebViewService.loadUrl("file:///android_asset/service-android.html");

            // Attach the WebView to its placeholder
            webViewPlaceholder.addView(mWebViewService);

            new ClientLocationListener(mWebViewService);
            new ClientWIFIListener(mWebViewService);
            new ClientGeoIPListener(mWebViewService);

        } else {
            FrameLayout oldPlaceholder = (FrameLayout) mWebViewService.getParent();
            oldPlaceholder.removeView(mWebViewService);

            // Attach the WebView to its placeholder
            webViewPlaceholder.addView(mWebViewService);

            mWebViewService.setWebViewClientInstance(mWebViewClient);
            mWebViewClient.setWebViewServiceInstance(mWebViewService);
            mWebViewService.execute("LOG INIT");
        }
    }


}
