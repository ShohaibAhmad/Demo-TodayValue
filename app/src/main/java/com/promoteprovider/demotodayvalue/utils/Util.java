package com.promoteprovider.demotodayvalue.utils;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.promoteprovider.demotodayvalue.R;

public class Util {



    public static AlertDialog getAlertDialog(Activity activity,String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_prod,null,false);
        TextView messageTv = view.findViewById(R.id.message);
        messageTv.setText(message);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
