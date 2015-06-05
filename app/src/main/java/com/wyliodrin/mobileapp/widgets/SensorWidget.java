package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wyliodrin.mobileapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea Stoican on 03.06.2015.
 */
public class SensorWidget extends TextView implements OutputDataWidget {

    private static Sensor sensor;
    private static Context context;
    private long updateTimeout = 1000;
    private String label;
    private int width;

    public SensorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setSensor(Sensor s) {
        sensor = s;

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(new SensorListener(), sensor, sensor.getType());
    }

    public void setSize(int width) {
        this.width = width;
    }

    private void setTimeout(int timeout) {
        updateTimeout = timeout;
    }

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final View.OnLongClickListener onLongClick, final ArrayList<Widget> objects) {
        // set the parameters

        final ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the sensor");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_sensor, null);
        alertDialogBuilder.setView(alert_dialog_xml);

        final Spinner spinner = (Spinner) alert_dialog_xml.findViewById(R.id.sensor_type_spinner);

        SensorManager mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        final List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        for (int i = 0; i < deviceSensors.size(); i++) {
            spinnerAdapter.add(deviceSensors.get(i).getName());
        }
        spinnerAdapter.notifyDataSetChanged();

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
                        String timeout = "";
                        String label = "";

                        EditText timeoutEditText = (EditText) alert_dialog_xml.findViewById(R.id.timeout);
                        if (timeoutEditText != null) {
                            timeout = timeoutEditText.getText().toString();

                            if (timeout.isEmpty())
                                timeoutEditText.setError("Timeout is required");
                        }

                        EditText labelButton = (EditText) alert_dialog_xml.findViewById(R.id.label);
                        if (labelButton != null) {
                            label = labelButton.getText().toString();

                            if (label.isEmpty())
                                labelButton.setError("Label is required");
                        }

                        EditText widthEditText = (EditText) alert_dialog_xml.findViewById(R.id.width);
                        if (widthEditText != null) {
                            width = widthEditText.getText().toString();

                            if (width.isEmpty())
                                widthEditText.setError("Width is required");
                        }

                        EditText heightEditText = (EditText) alert_dialog_xml.findViewById(R.id.width);
                        if (heightEditText != null) {
                            height = heightEditText.getText().toString();

                            if (height.isEmpty())
                                heightEditText.setError("Height is required");
                        }

                        if (!timeout.isEmpty() && !label.isEmpty() && !width.isEmpty() && !height.isEmpty()) {

                            int sensorIndex = spinner.getSelectedItemPosition();
                            Sensor sensor = deviceSensors.get(sensorIndex);
                            addToBoard(activity, layout, onLongClick, objects, sensor, Integer.parseInt(timeout), Integer.parseInt(width), label);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  Sensor sensor, int timeout, int width, String label) {

        SensorWidget sensorWidget = new SensorWidget(activity, null);

        int padding = width / 40;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(padding, padding, padding, padding);

        sensorWidget.setLayoutParams(layoutParams);
        sensorWidget.setSize(width);
        sensorWidget.setPadding(padding, padding, padding, padding);
        sensorWidget.setSensor(sensor);
        sensorWidget.setBackgroundColor(Color.GRAY);
        sensorWidget.setTimeout(timeout);
        sensorWidget.setLabel(label);

        layout.addView(sensorWidget);
        objects.add(sensorWidget);

        sensorWidget.setOnLongClickListener(onLongClick);
    }

    @Override
    public void sendData(String message) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("type", TYPE_SENSOR);
            obj.put("update_timeout", updateTimeout);
            obj.put("sensor", sensor.getType());
            obj.put("width", width);
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

    private class SensorListener implements SensorEventListener {

        private long lastUpdate = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            long time = System.currentTimeMillis();

            if (time - lastUpdate < updateTimeout)
                return;

            lastUpdate = time;

            JSONArray values = new JSONArray();

            for (int i = 0; i < sensorEvent.values.length; i++) {
                try {
                    values.put(sensorEvent.values[i]);
                } catch (JSONException e) {
                }
            }

            sendData(values.toString());
            float[] parts = sensorEvent.values;

            String sensorValues = sensor.getName() + ":\n";

            for (int i = 0; i < parts.length; i++)
                sensorValues += parts[i] + "\n";

            setMaxLines(parts.length + 1);
            setText(sensorValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
}
