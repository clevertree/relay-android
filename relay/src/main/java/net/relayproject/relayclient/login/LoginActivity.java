package net.relayproject.relayclient.login;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.relayproject.relayclient.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new LoginFragment();
            }
            return new LoginFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.activity_identity_tab);
            }
            return "TODO: Title " + position;
        }

    };

}
