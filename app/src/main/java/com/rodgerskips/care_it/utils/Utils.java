package com.rodgerskips.care_it.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.rodgerskips.care_it.R;

public class Utils {
    /*

    Author: kiduyu klaus
    date: 09/09/2020 15:46

     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void SetStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //context.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

    }

    //call this everytime you want to show a toast
    public static void ShowToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
