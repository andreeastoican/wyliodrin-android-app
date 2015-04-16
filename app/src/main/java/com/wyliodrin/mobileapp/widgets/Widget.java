package com.wyliodrin.mobileapp.widgets;

import android.app.Activity;

import org.json.JSONObject;

/**
 * Created by andreea on 12/10/14.
 */
public interface Widget {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_THERMOMETER = 1;
    public static final int TYPE_GRAPH = 2;
    public static final int TYPE_BUTTON = 3;

    public JSONObject toJson();
}
