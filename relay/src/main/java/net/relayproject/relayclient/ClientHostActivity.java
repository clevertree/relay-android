package net.relayproject.relayclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.TimeZone;

public class ClientHostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ClientInterface mClientInterface;
    private ClientLocationListener mLocationListener;

    private static final String TAG = ClientHostActivity.class.getSimpleName();
    private ClientWIFIListener mWifiListener = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.openDrawer(GravityCompat.START);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        WebView webView = (WebView) findViewById(R.id.web_view_host);

        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        mClientInterface = new ClientInterface(webView);

        webView.loadUrl("file:///android_asset/www/client-phone.html");


        addSuggestedCommand("JOIN /" + TimeZone.getDefault().getID().toLowerCase());
        mLocationListener = new ClientLocationListener(this);
        mWifiListener = new ClientWIFIListener(this);

//        Menu navigationMenu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
//        navigationMenu.findItem(R.id.nav_recent_commands_menu).setVisible(false);
//        navigationMenu.findItem(R.id.nav_suggested_commands_menu).setVisible(false);

//        addSuggestedCommand("JOIN omg");
    }

    private void onFABClick(View view) {
//        mClientInterface.sendCommand();
        Snackbar.make(view, "Private Messaging Coming Soon", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Menu navigationMenu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();

        switch(item.getItemId()) {
            case R.id.nav_command_tab_chat:
                navigationMenu.findItem(R.id.nav_recent_commands_menu).setVisible(false);
                navigationMenu.findItem(R.id.nav_suggested_commands_menu).setVisible(false);
                navigationMenu.findItem(R.id.nav_suggested_channels_menu).setVisible(true);
                navigationMenu.findItem(R.id.nav_active_channels_menu_item).setVisible(true);
                return true;

            case R.id.nav_command_tab_home:
                navigationMenu.findItem(R.id.nav_recent_commands_menu).setVisible(true);
                navigationMenu.findItem(R.id.nav_suggested_commands_menu).setVisible(true);
                navigationMenu.findItem(R.id.nav_suggested_channels_menu).setVisible(false);
                navigationMenu.findItem(R.id.nav_active_channels_menu_item).setVisible(false);
                return true;

            case R.id.nav_command_join:
                sendCommandJoinChannel();
                break;
            case R.id.nav_command_put:
                mClientInterface.sendCommand("PUT");
                break;

            case R.id.nav_command_feed:
                mClientInterface.sendCommand("FEED");
                break;

            case R.id.action_pgp_keygen:
            case R.id.nav_command_keygen:
                mClientInterface.sendCommand("PGP.KEYGEN");
                break;

            case R.id.action_pgp_import:
                mClientInterface.sendCommand("PGP.IMPORT");
                break;
            case R.id.action_pgp_export:
                mClientInterface.sendCommand("PGP.IMPORT");
                break;

            case R.id.action_pgp_manage:
            case R.id.nav_command_manage:
                mClientInterface.sendCommand("PGP.MANAGE");
                break;

            case R.id.action_about:
            case R.id.nav_command_about:
                mClientInterface.sendCommand("ABOUT");
                break;

            case R.id.action_about_alpha:
                String url = "https://www.facebook.com/relay.dev/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            case R.id.action_reload_client:
                mClientInterface.sendCommand("RELOAD");
                break;

            case R.id.action_render_nav:
                mClientInterface.sendCommand("RENDER {nav}");
                break;

            default:
                String commandString = (String) item.getTitleCondensed();
                if(commandString != null && commandString.length()>0) {
                    mClientInterface.sendCommand(commandString);

                } else {
                    throw new IllegalArgumentException("Unhandled Nav ID: " + item.getItemId());
                }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static ArrayList<String> CHANNEL_SUGGESTIONS = null;

    private void sendCommandJoinChannel() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CHANNEL_SUGGESTIONS);

        final AutoCompleteTextView input = new AutoCompleteTextView (this);
        input.setAdapter(adapter);
        input.setHint("Enter Channel Path: [i.e. /subject/topic/subtopic]");
        alertDialogBuilder.setView(input);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Join",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mClientInterface.sendCommand("JOIN " + input.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void addSuggestedCommand(String suggestedCommand) {
        if(CHANNEL_SUGGESTIONS == null)
            CHANNEL_SUGGESTIONS = new ArrayList<String>();

        if(CHANNEL_SUGGESTIONS.contains(suggestedCommand)) {
            Log.i(TAG, "Ignoring repeat suggested command: " + suggestedCommand);
            return;
        }

        Log.v(TAG, "Adding suggested command: " + suggestedCommand);
        CHANNEL_SUGGESTIONS.add(0, suggestedCommand);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu(); // findViewById(R.id.nav_suggested_commands_menu);
        Menu suggestedCommandsMenu = menu.findItem(R.id.nav_suggested_channels_menu).getSubMenu();
        suggestedCommandsMenu.clear();
        for(String suggestedCommand2: CHANNEL_SUGGESTIONS)
            suggestedCommandsMenu
                    .add(1, Menu.FIRST, Menu.FIRST, suggestedCommand2)
                    .setTitleCondensed(suggestedCommand2)
                    .setIcon(R.drawable.ic_menu_send);

    }

    public void handleException(Exception e) {
    }
}
