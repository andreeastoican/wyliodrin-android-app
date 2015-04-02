package com.wyliodrin.mobileapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        final ImageView bg = (ImageView) findViewById(R.id.thermometer_bg);

        bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                bg.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                width = bg.getWidth();
                height = bg.getHeight();

                ImageView bar = (ImageView) findViewById(R.id.thermometer_bar);

                LayoutParams params = (LayoutParams) bar.getLayoutParams();
                params.width = (int)(0.226 * width);
                params.height = (int) (0.3 * height);
                params.leftMargin = (int) (0.152 * width);
                params.bottomMargin = (int) (0.145 * height);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                bar.setLayoutParams(params);

                //setLimits(-20, 70);

            }
        });

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
        int i = 0;
        for (double x = max; x >= min; x -= step, i++) {
            TextView text = new TextView(getContext());
            text.setText("" + (int) x);
            text.setTextColor(R.color.black);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(ALIGN_PARENT_RIGHT);
            params.topMargin = (int) (0.1 * height) + i * (int) (0.075 * height);
            text.setLayoutParams(params);

            addView(text);
            labels.add(text);
        }

        //setValue(40);
    }

    public void setValue(double value) {
        this.value = value;

        TextView textView = new TextView(getContext());
        textView.setText((int)value + " C");
        textView.setTextColor(R.color.black);

        LayoutParams paramsTextView = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTextView.addRule(ALIGN_PARENT_BOTTOM);
        paramsTextView.bottomMargin = (int) (0.06 * height);
        paramsTextView.leftMargin = (int) (0.21 * width);
        textView.setLayoutParams(paramsTextView);
        addView(textView);

        if(value < min) value = min;
        if(value > max) value = max;

        ImageView bar = (ImageView) findViewById(R.id.thermometer_bar);

        LayoutParams params = (LayoutParams) bar.getLayoutParams();
        params.height = (int) ((0.065 * height) + ((value - min) / (max - min) * 0.67 * height));
        bar.setLayoutParams(params);
    }

    @Override
    public void addData(WylioMessage message) {

    }

    public void setMin (double minValue) {
        this.min = minValue;
    }

    public void setMax (double maxValue) {
        this.max = maxValue;
    }
}
