package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.wyliodrin.mobileapp.R;

/**
 * Created by Andreea Stoican on 07.04.2015.
 */
public class SimpleButton {

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final View.OnLongClickListener onLongClick) {
        // set the parameters

        ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the button properties");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_simple_button, null);
        alertDialogBuilder.setView(alert_dialog_xml);

        alertDialogBuilder.setPositiveButton("Done", null);

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        String width = "";
                        String height = "";
                        String text = "";

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.width);
                        if (widthEditText != null) {
                            width = widthEditText.getText().toString();

                            if (width.isEmpty())
                                widthEditText.setError("Width is required");
                        }

                        EditText heightEditText = (EditText) alert_dialog_xml.findViewById(R.id.height);
                        if (heightEditText != null) {
                            height = heightEditText.getText().toString();

                            if (height.isEmpty())
                                heightEditText.setError("Height is required");
                        }

                        EditText textButton = (EditText) alert_dialog_xml.findViewById(R.id.text);
                        if (textButton != null) {
                            text = textButton.getText().toString();

                            if (text.isEmpty())
                                textButton.setError("Text button is required");
                        }

                        if (!width.isEmpty() && !height.isEmpty() && !text.isEmpty()) {

                            Button button = new Button(activity);
                            button.setText(text);

                            button.setLayoutParams(new LinearLayout.LayoutParams(Integer.parseInt(width), Integer.parseInt(height)));
                            button.setOnLongClickListener(onLongClick);

                            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.widgetsContainer);
                            layout.addView(button);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

}