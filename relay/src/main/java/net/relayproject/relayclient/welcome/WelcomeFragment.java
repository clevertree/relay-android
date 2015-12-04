package net.relayproject.relayclient.welcome;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.relayproject.relayclient.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_welcome, container, false);
        TextView txt = (TextView) layoutView.findViewById(R.id.fragment_welcome_text);
        txt.setText(Html.fromHtml(txt.getText().toString()));
        return layoutView;

//        Typeface fontBody = Typeface.createFromAsset(layoutView.getContext().getAssets(), "fonts/DroidSerif-Regular.ttf");
//        Typeface fontHeader = Typeface.createFromAsset(layoutView.getContext().getAssets(), "fonts/Oswald-Bold.ttf");
//        txt.setTypeface(fontBody);

        // Buttons
//        ((Button) layoutView.findViewById(R.id.btn_pgp_create_identity)).setTypeface(fontHeader);
//        ((Button) layoutView.findViewById(R.id.btn_pgp_import_identity)).setTypeface(fontHeader);
//        ((Button) layoutView.findViewById(R.id.btn_pgp_no_identity)).setTypeface(fontHeader);

//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText();
//        getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))
    }
}
