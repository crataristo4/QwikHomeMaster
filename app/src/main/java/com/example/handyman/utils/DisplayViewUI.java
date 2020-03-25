package com.example.handyman.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.handyman.R;

public class DisplayViewUI {

    public static   void  displayToast(Context ctx, String s){
        Toast toast = Toast.makeText(ctx, s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static ProgressDialog displayProgress(Context ctx, String message){
        ProgressDialog loading = new ProgressDialog(ctx);
        loading.setCancelable(false);
        loading.setMessage(message);
        // loading.show();

        return loading;
    }

    static public void displayAlertDialog(Context context, String title, String msg, String btnPos, String btnNeg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        if (btnPos != null) builder.setPositiveButton(btnPos, onClickListener);
        if (btnNeg != null) builder.setNegativeButton(btnNeg, onClickListener);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    static public void displayAlertDialogMsg(Context context, String msg, String btnPos,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);
        if (btnPos != null) builder.setPositiveButton(btnPos, onClickListener);
        builder.setIcon(context.getResources().getDrawable(R.drawable.sorry));
        builder.show();
    }
}
