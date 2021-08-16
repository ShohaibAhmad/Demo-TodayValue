package com.promoteprovider.demotodayvalue.utils;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
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
