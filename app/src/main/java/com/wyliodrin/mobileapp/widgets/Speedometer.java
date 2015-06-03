package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.wyliodrin.mobileapp.R;
import com.wyliodrin.mobileapp.api.WylioMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andreea Stoican on 14.05.2015.
 */
public class Speedometer extends View implements InputDataWidget {
    private Paint paint, scalePaint;
    private Shader shader = null;
    private Paint innerCirclePaint, scaleTextPaint;

    private static final int totalLines = 60;
    private static final float degreesPerLine = 360.0f / totalLines;
    private int currentValue = 23;

    private float minValue = 0;
    private float maxValue = 40;
    private float valueStep = 5;
    private Path handPath;
    private Paint handPaint;
    private Paint handScrewPaint;
    private int diameter;

    private String label;

    public Speedometer(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(Color.WHITE);
        innerCirclePaint.setStyle(Paint.Style.FILL);
    }

    public void setLimits(float min, float max) {
        this.minValue = min;
        this.maxValue = max;
        this.valueStep = (max - min) / (totalLines / 5 - 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        diameter = getWidth();

        if (shader == null) {
            shader = new LinearGradient(0, 0, 0, diameter, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            scalePaint = new Paint();
            scalePaint.setStyle(Paint.Style.STROKE);
            scalePaint.setColor(0x9f004d0f);
            scalePaint.setStrokeWidth(0.01f * diameter);
            scalePaint.setAntiAlias(true);

            scaleTextPaint = new Paint();
            scaleTextPaint.setTypeface(Typeface.SANS_SERIF);
            scaleTextPaint.setColor(0x9f004d0f);
            scaleTextPaint.setTextSize(0.07f * diameter);
            scaleTextPaint.setTextAlign(Paint.Align.CENTER);

            handPath = new Path();
            handPath.moveTo(0.5f * diameter, 0.5f * diameter + 0.2f * diameter);
            handPath.lineTo(0.5f * diameter - 0.010f * diameter, 0.5f * diameter + 0.2f * diameter - 0.007f * diameter);
            handPath.lineTo(0.5f * diameter - 0.002f * diameter, 0.5f * diameter - 0.32f * diameter);
            handPath.lineTo(0.5f * diameter + 0.002f * diameter, 0.5f * diameter - 0.32f * diameter);
            handPath.lineTo(0.5f * diameter + 0.010f * diameter, 0.5f * diameter + 0.2f * diameter - 0.007f * diameter);
            handPath.lineTo(0.5f * diameter, 0.5f * diameter + 0.2f * diameter);
            handPath.addCircle(0.5f * diameter, 0.5f * diameter, 0.025f * diameter, Path.Direction.CW);

            handPaint = new Paint();
            handPaint.setAntiAlias(true);
            handPaint.setColor(0xff392f2c);
            handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
            handPaint.setStyle(Paint.Style.FILL);

            handScrewPaint = new Paint();
            handScrewPaint.setAntiAlias(true);
            handScrewPaint.setColor(0xff493f3c);
            handScrewPaint.setStyle(Paint.Style.FILL);
        }

        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2 - (float) (0.02 * diameter), innerCirclePaint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        int n = 1;
        int m = 4;
        for (int i = 0; i < totalLines; ++i) {
            float y1 = 0.15f * diameter;
            float y2 = y1 + 0.035f * diameter;
            boolean shown = false;

            if (i % 5 == 0) {
                if (n < 6 || n > 8) {
                    shown = true;
                    m ++;
                    if (n == 9)
                        m = 0;
                }

                n++;
            }

            float value = m * (maxValue - minValue) / 9 + minValue;

            if (shown)
                y1 -= 0.02 * diameter;

            canvas.drawLine(0.5f * diameter, y1, 0.5f * diameter, y2, scalePaint);

            if (shown) {
                String valueString = Integer.toString((int) value);
                canvas.drawText(valueString, 0.5f * diameter, y2 - 0.07f * diameter, scaleTextPaint);
            }

            canvas.rotate(degreesPerLine, 0.5f * diameter, 0.5f * diameter);
        }
        canvas.restore();

        float currentAngle = 240 * (currentValue - maxValue) / (maxValue - minValue) + 120;
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(currentAngle, 0.5f * diameter, 0.5f * diameter);
        canvas.drawPath(handPath, handPaint);
        canvas.restore();

        canvas.drawCircle(0.5f * diameter, 0.5f * diameter, 0.01f * diameter, handScrewPaint);

    }

    public void setCurrentAngle (int value) {
        currentValue = value;
    }

    public static void showAddDialog(final Activity activity, final LinearLayout layout, final OnLongClickListener onLongClick, final ArrayList<Widget> objects) {
        // set the parameters

        ScrollView scroll = new ScrollView(activity);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Choose the speedometer properties");

        LayoutInflater inflater= LayoutInflater.from(activity);
        final View alert_dialog_xml =inflater.inflate(R.layout.alert_dialog_speedometer, null);
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
                        String diameterValue = "";
                        String minValue = "";
                        String maxValue = "";
                        String label = "";

                        EditText diameterEditText = (EditText) alert_dialog_xml.findViewById(R.id.speedometer_diameter);
                        if (diameterEditText != null) {
                            diameterValue = diameterEditText.getText().toString();

                            if (diameterValue.isEmpty())
                                diameterEditText.setError("Diameter is required");
                        }

                        EditText minValueEditText = (EditText) alert_dialog_xml.findViewById(R.id.speedometer_min);
                        if (minValueEditText != null) {
                            minValue = minValueEditText.getText().toString();

                            if (minValue.isEmpty())
                                minValueEditText.setError("Min value is required");
                        }

                        EditText maxValueEditText = (EditText) alert_dialog_xml.findViewById(R.id.speedometer_max);
                        if (maxValueEditText != null) {
                            maxValue = maxValueEditText.getText().toString();

                            if (maxValue.isEmpty())
                                maxValueEditText.setError("Max value is required");
                        }

                        EditText lableEditText = (EditText) alert_dialog_xml.findViewById(R.id.label);
                        if (lableEditText != null) {
                            label = lableEditText.getText().toString();

                            if (label.isEmpty())
                                lableEditText.setError("Label is required");
                        }

                        if (!diameterValue.isEmpty() && !minValue.isEmpty() && !maxValue.isEmpty() && !label.isEmpty()) {

                            addToBoard(activity, layout, onLongClick, objects,
                                    Integer.parseInt(diameterValue),
                                    Float.parseFloat(maxValue), Float.parseFloat(minValue), label);

                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public static void addToBoard(Activity activity, LinearLayout layout, OnLongClickListener onLongClick, ArrayList<Widget> objects,
                                  int diameter, float minValue, float maxValue, String label) {
        // add the thermometer
        Speedometer speedometer = new Speedometer(activity, null);
        speedometer.setLayoutParams(new LinearLayout.LayoutParams(diameter, diameter));
        speedometer.setLimits(maxValue, minValue);
        speedometer.setLabel(label);

        layout.addView(speedometer);
        objects.add(speedometer);

        speedometer.setOnLongClickListener(onLongClick);
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("type", TYPE_THERMOMETER);
            obj.put("diameter", diameter);
            obj.put("min_value", minValue);
            obj.put("max_value", maxValue);
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

    @Override
    public void addData(WylioMessage message) {

    }
}
