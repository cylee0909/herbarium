package com.cylee.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cylee on 2017/9/11.
 */

public class StateTextView extends TextView {
    public StateTextView(Context context) {
        super(context);
    }

    public StateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed) {
            setAlpha(0.6f);
        } else {
            setAlpha(1f);
        }
    }
}
