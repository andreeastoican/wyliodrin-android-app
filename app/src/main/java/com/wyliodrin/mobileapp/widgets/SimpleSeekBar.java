package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andreea Stoican on 21.04.2015.
 */
public class SimpleSeekBar extends RelativeLayout implements InputDataWidget {

    private int maxValue;
    private int width;

    public SimpleSeekBar(Context context, int width, int maxValue) {
        this(context, null, width, maxValue);
    }

    public SimpleSeekBar(Context context, AttributeSet attrs, int width, int maxValue) {
        super(context, attrs);

        this.width = width;
        this.maxValue = maxValue;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seek_bar_layout, this, true);

        SeekBar seekBar = (SeekBar)findViewById(R.id.seek_bar);
        seekBar.setMax(maxValue);

        final TextView result = (TextView) findViewById(R.id.result);
        TextView minValueTextView = (TextView) findViewById(R.id.min);
        TextView maxValueTextView = (TextView) findViewById(R.id.max);

        result.setText("Value: 0");

        minValueTextView.setText("0");

        maxValueTextView.setText(maxValue+"");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                result.setText("Value:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                        String maxValue = "";

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.width);
                        if (widthEditText != null) {
                            width = widthEditText.getText().toString();

                            if (width.isEmpty())
                                widthEditText.setError("Width is required");
                        }

                        EditText maxValueButton = (EditText) alert_dialog_xml.findViewById(R.id.max_value);
                        if (maxValueButton != null) {
                            maxValue = maxValueButton.getText().toString();

                            if (maxValue.isEmpty())
                                maxValueButton.setError("Max value is required");
                        }

                        if (!width.isEmpty() && !maxValue.isEmpty()) {

                            addToBoard(activity, layout, onLongClick, objects, Integer.parseInt(width), Integer.parseInt(maxValue));

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  int width, int max) {



        SimpleSeekBar simpleSeekBar = new SimpleSeekBar(activity, width, max);

        simpleSeekBar.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        simpleSeekBar.setOnLongClickListener(onLongClick);

        objects.add(simpleSeekBar);

        layout.addView(simpleSeekBar);
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
            obj.put("max_value", maxValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
