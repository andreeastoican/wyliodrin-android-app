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
import android.widget.ToggleButton;

import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andreea Stoican on 20.04.2015.
 */
public class SimpleToggleButton extends ToggleButton implements InputDataWidget {

    private String textON;
    private String textOFF;
    private int width;
    private int height;
    private String label;

    public SimpleToggleButton(Context context) {
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
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_simple_toggle_button, null);
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
                        String textOn = "";
                        String textOff = "";
                        String label = "";

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

                        EditText textOnButtOn = (EditText) alert_dialog_xml.findViewById(R.id.textON);
                        if (textOnButtOn != null) {
                            textOn = textOnButtOn.getText().toString();

                            if (textOn.isEmpty())
                                textOnButtOn.setError("Text button on is required");
                        }

                        EditText textOnButtOff = (EditText) alert_dialog_xml.findViewById(R.id.textOFF);
                        if (textOnButtOff != null) {
                            textOff = textOnButtOff.getText().toString();

                            if (textOff.isEmpty())
                                textOnButtOff.setError("Text button off is required");
                        }

                        EditText labelEditText = (EditText) alert_dialog_xml.findViewById(R.id.label);
                        if (labelEditText != null) {
                            label = labelEditText.getText().toString();

                            if (label.isEmpty())
                                labelEditText.setError("Label is required");
                        }

                        if (!width.isEmpty() && !height.isEmpty() && !textOn.isEmpty() && !textOff.isEmpty() && !label.isEmpty()) {

                            addToBoard(activity, layout, onLongClick, objects, Integer.parseInt(width), Integer.parseInt(height), textOn, textOff, label);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  int width, int height, String buttonTextOn, String buttonTextOff, String label) {

        SimpleToggleButton simpleToggleButton = new SimpleToggleButton(activity);
        simpleToggleButton.setTextOn(buttonTextOn);
        simpleToggleButton.setTextOff(buttonTextOff);
        simpleToggleButton.setLabel(label);

        simpleToggleButton.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        simpleToggleButton.setOnLongClickListener(onLongClick);

        simpleToggleButton.width = width;
        simpleToggleButton.height = height;
        simpleToggleButton.textON = buttonTextOn;
        simpleToggleButton.textOFF = buttonTextOff;

        layout.addView(simpleToggleButton);
        objects.add(simpleToggleButton);
    }

    @Override
    public void addData(WylioMessage message) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("type", TYPE_TOGGLE_BUTTON);
            obj.put("width", width);
            obj.put("height", height);
            obj.put("text_button_on", textON);
            obj.put("text_button_off", textOFF);
            obj.put("label", label);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}
