package com.rodgerskips.care_it.Ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;

public class StatusBarView extends View {
    /*

   Author: kiduyu klaus
   date: 09/09/2020 15:32

    */
    private int mStatusBarHeight;

    public StatusBarView(Context context) {
        this(context, null);

    }

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBarHeight = insets.getSystemWindowInsetTop();
            return insets.consumeSystemWindowInsets();
        }
        return insets;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStatusBarHeight);
    }
}
