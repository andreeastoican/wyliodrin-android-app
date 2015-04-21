package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andreea Stoican on 21.04.2015.
 */
public class SimpleSeekBar extends SeekBar implements InputDataWidget {

    private String textButton;
    private int width;
    private int height;

    public SimpleSeekBar(Context context) {
        super(context);
    }

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final View.OnLongClickListener onLongClick, final ArrayList<Widget> objects) {
        // set the parameters

        final ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the button properties");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_seek_bar, null);
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

                            addToBoard(activity, layout, onLongClick, objects, Integer.parseInt(width), Integer.parseInt(height), text);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  int width, int height, String buttonText) {

        SimpleSeekBar simpleSeekBar = new SimpleSeekBar(activity);
        //simpleSeekBar.setText(buttonText);

        simpleSeekBar.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        simpleSeekBar.setOnLongClickListener(onLongClick);


        simpleSeekBar.width = width;
        simpleSeekBar.height = height;
        simpleSeekBar.textButton = buttonText;

        layout.addView(simpleSeekBar);
        objects.add(simpleSeekBar);
    }

    @Override
    public void addData(WylioMessage message) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("type", TYPE_SEEK_BAR);
            obj.put("width", width);
            obj.put("height", height);
            obj.put("text_button", textButton);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
