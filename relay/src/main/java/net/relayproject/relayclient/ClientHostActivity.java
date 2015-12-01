package net.relayproject.relayclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClientHostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ClientInterface mClientInterface;

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

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener = new ClientLocationListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);

            locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);
        }

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
        switch(item.getItemId()) {
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
        }

//        if (id == R.id.nav_camera) {
//            WebView webView = (WebView) findViewById(R.id.web_view_host);
//            webView.loadUrl("javascript:ClientSocketWorker.sendCommand('render {nav}');");
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//            WebView bookingView = (WebView) findViewById(R.id.web_view_host);
//            bookingView.getSettings().setJavaScriptEnabled(true);
//            bookingView.loadUrl("http://relayproject.net/client-browser.html");
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static ArrayList<String> SUGGESTIONS_JOIN = null;

    private void sendCommandJoinChannel() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SUGGESTIONS_JOIN);

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
        if(SUGGESTIONS_JOIN == null)
            SUGGESTIONS_JOIN = new ArrayList<String>();

        SUGGESTIONS_JOIN.add(suggestedCommand);

        Menu menu = (Menu) findViewById(R.id.nav_suggested_commands_menu);
        menu
            .add(1, Menu.FIRST, Menu.FIRST, suggestedCommand)
                .setIcon(R.drawable.ic_menu_send);

        Toast.makeText(getApplicationContext(), suggestedCommand,
                Toast.LENGTH_LONG).show();
    }
}
