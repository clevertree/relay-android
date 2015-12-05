package net.relayproject.relayclient.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import net.relayproject.relayclient.ClientHostActivity;
import net.relayproject.relayclient.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView txt = (TextView) layoutView.findViewById(R.id.fragment_login_text);
        Typeface fontBody = Typeface.createFromAsset(layoutView.getContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
        Typeface fontHeader = Typeface.createFromAsset(layoutView.getContext().getAssets(), "fonts/Oswald-Bold.ttf");
        txt.setTypeface(fontBody);
        txt.setText(Html.fromHtml(txt.getText().toString()));

        ((Button) layoutView.findViewById(R.id.btn_pgp_create_identity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = (ViewPager) container;
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

                InputMethodManager imm = (InputMethodManager) viewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });


        ((Button) layoutView.findViewById(R.id.btn_pgp_no_identity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClientHostActivity.class);
                startActivity(intent);
            }
        });


        InputMethodManager imm = (InputMethodManager)layoutView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(container.getWindowToken(), 0);

        // Buttons
//        ((Button) layoutView.findViewById(R.id.btn_pgp_create_identity)).setTypeface(fontHeader);
//        ((Button) layoutView.findViewById(R.id.btn_pgp_import_identity)).setTypeface(fontHeader);
//        ((Button) layoutView.findViewById(R.id.btn_pgp_no_identity)).setTypeface(fontHeader);

        return layoutView;
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText();
//        getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))
    }
}
