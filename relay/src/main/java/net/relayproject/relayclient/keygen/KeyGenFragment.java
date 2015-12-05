package net.relayproject.relayclient.keygen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.relayproject.relayclient.ClientHostActivity;
import net.relayproject.relayclient.R;
import net.relayproject.relayclient.welcome.WelcomeActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class KeyGenFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_keygen, container, false);
//        Typeface font = Typeface.createFromAsset(layoutView.getContext().getAssets(), "fonts/Oswald-Bold.ttf");

        TextView txt = (TextView) layoutView.findViewById(R.id.form_keygen_status_text);
//        txt.setTypeface(font);
        txt.setText(Html.fromHtml(txt.getText().toString()));

        txt = (TextView) layoutView.findViewById(R.id.form_keygen_name_text);
        txt.setText(Html.fromHtml(txt.getText().toString()));

        txt = (TextView) layoutView.findViewById(R.id.form_keygen_password_text);
        txt.setText(Html.fromHtml(txt.getText().toString()));

        txt = (TextView) layoutView.findViewById(R.id.form_keygen_strength_text);
        txt.setText(Html.fromHtml(txt.getText().toString()));

        Spinner spinner = (Spinner) layoutView.findViewById(R.id.form_keygen_strength_spinner);
        ArrayAdapter<CharSequence> spinnerArray = ArrayAdapter.createFromResource(container.getContext(),
                R.array.pgp_strength_options, android.R.layout.simple_spinner_item);
        spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArray);
        spinner.setSelection(1);

        Button buttonSubmit = (Button) layoutView.findViewById(R.id.form_keygen_submit);
        buttonSubmit.setOnClickListener(this);

        EditText editName = (EditText) layoutView.findViewById(R.id.form_keygen_name_edit);
        editName.requestFocus();
//        InputMethodManager imm = (InputMethodManager) layoutView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return layoutView;
    }

    @Override
    public void onClick(View v) {
        View layoutView = v.getRootView();
        EditText editName = (EditText) layoutView.findViewById(R.id.form_keygen_name_edit);
        EditText editPass = (EditText) layoutView.findViewById(R.id.form_keygen_password_edit);
        Spinner spinnerStrength = (Spinner) layoutView.findViewById(R.id.form_keygen_strength_spinner);

        TextView statusText = (TextView) layoutView.findViewById(R.id.form_keygen_status_text);
        if(editName.getText().length() <= 0) {
            statusText.setText("Error: Invalid User ID");
            statusText.setTextColor(Color.RED);
            editName.requestFocus();
            return;
        }

        String commandString = "PGP.KEYGEN --import" +
            " --bits " + spinnerStrength.getSelectedItem().toString().split(" ")[0] +
            " --name " + editName.getText().toString() +
            (editPass.getText().length() > 0 ? " --pass " + editPass.getText().toString() : "");

        Intent intent = new Intent(v.getContext(), ClientHostActivity.class);
        intent.putExtra("command", commandString);
        startActivity(intent);
    }
}
