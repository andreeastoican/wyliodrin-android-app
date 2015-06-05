package com.wyliodrin.mobileapp.widgets;

import com.wyliodrin.mobileapp.api.WylioMessage;

/**
 * Created by andreea on 12/10/14.
 */
public interface OutputDataWidget extends Widget {
    void sendData(String message);
    void sendData(double message);
}