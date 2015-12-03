package net.relayproject.relayclient.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.relayproject.relayclient.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText();
//        getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))
    }
}
