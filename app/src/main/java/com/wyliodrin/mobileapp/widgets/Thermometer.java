package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;

import java.util.ArrayList;

/**
 * Created by Andreea Stoican on 31.03.2015.
 */
public class Thermometer extends RelativeLayout implements InputDataWidget {

    private int width, height;

    private double min;
    private double max;

    private ArrayList<TextView> labels;

    private double value;

    public Thermometer(Context context) {
        this(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        labels = new ArrayList<TextView>();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thermometer_layout, this, true);
    }
    public Thermometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLimits(double min, double max) {
        this.max = max;
        this.min = min;

        for (TextView text : labels) {
            removeView(text);
        }
        labels.clear();

        double step = (max - min) / 9;
        double x = max;
        for (int i=0; i < 10; i++) {

            if (x < min)
                x = min;

            TextView text = new TextView(getContext());
            text.setText("" + (int) x);
            text.setTextColor(R.color.black);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(ALIGN_PARENT_RIGHT);
            params.topMargin = (int) (0.1 * height) + i * (int) (0.075 * height);
            text.setLayoutParams(params);

            addView(text);
            labels.add(text);

            x -= step;
        }

        setValue(55);
    }

    public void setValue(double value) {
        this.value = value;

        if(value < min) value = min;
        if(value > max) value = max;

        TextView textView = new TextView(getContext());
        textView.setText((int)value + " °C");
        textView.setTextColor(R.color.black);

        LayoutParams paramsTextView = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTextView.addRule(ALIGN_PARENT_BOTTOM);
        paramsTextView.bottomMargin = (int) (0.06 * height);
        paramsTextView.leftMargin = (int) (0.21 * width);
        textView.setLayoutParams(paramsTextView);
        addView(textView);

        ImageView bar = (ImageView) findViewById(R.id.thermometer_bar);

        LayoutParams params = (LayoutParams) bar.getLayoutParams();
        params.width = (int)(0.226 * width);
        params.height = (int) ((0.065 * height) + ((value - min) / (max - min) * 0.67 * height));
        params.leftMargin = (int) (0.152 * width);
        params.bottomMargin = (int) (0.145 * height);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bar.setLayoutParams(params);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void addData(WylioMessage message) {

    }

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final OnLongClickListener onLongClick) {
        // set the parameters

        ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the thermometer properties");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_properties, null);
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
                        String minDegree = "";
                        String maxDegree = "";

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_width);
                        if (widthEditText != null) {
                            width = widthEditText.getText().toString();

                            if (width.isEmpty())
                                widthEditText.setError("Width is required");
                        }

                        EditText heightEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_height);
                        if (heightEditText != null) {
                            height = heightEditText.getText().toString();

                            if (height.isEmpty())
                                heightEditText.setError("Height is required");
                        }

                        EditText minDegreeEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_min);
                        if (minDegreeEditText != null) {
                            minDegree = minDegreeEditText.getText().toString();

                            if (minDegree.isEmpty())
                                minDegreeEditText.setError("Min degree is required");
                        }

                        EditText maxDegreeEditText = (EditText) alert_dialog_xml.findViewById(R.id.thermometer_max);
                        if (maxDegreeEditText != null) {
                            maxDegree = maxDegreeEditText.getText().toString();

                            if (maxDegree.isEmpty())
                                maxDegreeEditText.setError("Max degree is required");
                        }

                        if (!width.isEmpty() && !height.isEmpty() && !minDegree.isEmpty() && !maxDegree.isEmpty()) {

                            // add the thermometer
                            Thermometer thermometer = new Thermometer(activity);
                            thermometer.setLayoutParams(new LinearLayout.LayoutParams(Integer.parseInt(width), Integer.parseInt(height)));
                            thermometer.setSize(Integer.parseInt(width), Integer.parseInt(height));
                            thermometer.setLimits(Float.parseFloat(minDegree), Float.parseFloat(maxDegree));

                            layout.addView(thermometer);

                            thermometer.setOnLongClickListener(onLongClick);

                            alertDialog.dismiss();

                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

}
