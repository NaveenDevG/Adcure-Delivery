package com.adcure.deliverypartner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class Adialog {
    public static void showAlert(String title, String message, Activity context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.customdialogtheme);

            AlertDialog dialog = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog1) {
                    Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    ok.setBackgroundResource(R.drawable.onalertbutton);
                    ok.setTextColor(Color.parseColor("#FFFFFF"));
                    ok.setWidth(70);
                }
            });
            dialog.setTitle(title);
            dialog.setMessage(message);
            //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

            TextView msg = (TextView) dialog.findViewById(android.R.id.message);
            msg.setTextSize(context.getResources().getDimension(R.dimen.alertTextSize));

            Resources resources = dialog.getContext().getResources();

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(context.getResources().getColor(R.color.StripColor));

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
//            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
//            if (titleDivider != null) {
//                titleDivider.setBackgroundColor(context.getResources().getColor(R.color.StripColor));
//            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void handleConfirmDailogResponse(boolean b) {

    }

}
