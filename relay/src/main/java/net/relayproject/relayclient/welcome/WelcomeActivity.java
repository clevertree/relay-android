package net.relayproject.relayclient.welcome;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.relayproject.relayclient.R;
import net.relayproject.relayclient.keygen.KeyGenFragment;
import net.relayproject.relayclient.login.LoginFragment;
import net.relayproject.relayclient.proximity.KeySpaceScanFragment;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
//        toolBarTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolBarTitle.setTypeface(font);


        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private FragmentPagerAdapter sectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new WelcomeFragment();
                case 1:
                    return new LoginFragment();
                case 2:
                    return new KeyGenFragment();
                case 3:
                    return new KeySpaceScanFragment();
            }
            return new LoginFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.activity_welcome_tab);
                case 1:
                    return getString(R.string.activity_identity_tab);
                case 2:
                    return getString(R.string.activity_keygen_tab);
                case 3:
                    return getString(R.string.activity_pgp_scan_tab);
            }
            return "TODO: Title " + position;
        }

    };

}
