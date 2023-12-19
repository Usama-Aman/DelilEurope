package com.dalileuropeapps.dalileurope.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tapadoo.alerter.Alerter;
import com.dalileuropeapps.dalileurope.R;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

public class DialogFactory {

    public static RelativeLayout rl_message;

    public static void showDropDownNotificationError(Activity mContext, String title, String message) {
        Alerter.create(mContext)
                .setTitle(title)
                .setText(message)
                .enableSwipeToDismiss()
                .setBackgroundColorRes(R.color.alert_dialog_error)
                .setIcon(R.drawable.ic_alert)
                .setIconColorFilter(0)
                .setDuration(2000)
                .show();
    }


    public static void showDropDownNotificationSuccess(Activity mContext, String title, String message) {
        Alerter.create(mContext)
                .setTitle(title)
                .setText(message)
                .enableSwipeToDismiss()
                .setBackgroundColorRes(R.color.alert_dialog_success)
                .setIcon(R.drawable.ic_success)
                .setIconColorFilter(0)
                .setDuration(2000)
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                    }
                })
                .show();
    }


    public static void showDropDownNotificationSuccessWithFinish(final Activity mContext, String title, String message) {
        Alerter.create(mContext)
                .setTitle(title)
                .setText(message)
                .enableSwipeToDismiss()
                .setBackgroundColorRes(R.color.alert_dialog_success)
                .setIcon(R.drawable.ic_success)
                .setIconColorFilter(0)
                .setDuration(1000)
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        mContext.finish();
                    }
                })
                .show();
    }


    public static void dialogSettings(Context ctx, Dialog dialog, View parentView, float h, float w) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * w);
        int heightLcl = (int) (displayMetrics.heightPixels * h);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                parentView.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height = heightLcl;
        paramsLcl.gravity = Gravity.CENTER;
        parentView.setLayoutParams(paramsLcl);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void showInfoToUser(Context ctx, String title, String msg) {
        AlertDialog.Builder aler = new AlertDialog.Builder(ctx);
        aler.setTitle(title);
        aler.setMessage(msg);
        aler.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aler.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aler.show();
    }


    public static void showDropDownNotification(Context mContext, View viewFragment, int id, int color, String title, String message) {
        if (viewFragment.findViewById(id) != null) {

            screenNotificationCrossClick();

            rl_message = (RelativeLayout) viewFragment.findViewById(id);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialog_notification = inflater.inflate(R.layout.show_messages_small, null);
            rl_message.addView(dialog_notification);
            rl_message.setVisibility(View.VISIBLE);

            RelativeLayout bg_layout = (RelativeLayout) dialog_notification.findViewById(R.id.animatedLayout);
            bg_layout.setBackgroundColor(color);

            TextView tv_Message = (TextView) dialog_notification.findViewById(R.id.message_detail);
            tv_Message.setText(message);


            TextView tv_title = (TextView) dialog_notification.findViewById(R.id.msg_title);
            tv_title.setText(title);

            ImageView notificationImage = (ImageView) dialog_notification.findViewById(R.id.error_icon);

            if ((tv_title.getText().toString().equalsIgnoreCase(mContext.getResources().getString(R.string.tv_msg_error)) ||
                    tv_title.getText().toString().equalsIgnoreCase(mContext.getResources().getString(R.string.tv_msg_info))) && color == mContext.getResources().getColor(R.color.colorRed)) {
                notificationImage.setImageResource(R.drawable.ic_alert);
            } else {
                notificationImage.setImageResource(R.drawable.ic_success);

            }

            notificationImage.setClickable(true);
            notificationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    screenNotificationCrossClick();
                }
            });

            if (dialog_notification != null) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_down = AnimationUtils.loadAnimation(mContext.getApplicationContext(),
                                R.anim.slide_down);
                        rl_message.startAnimation(slide_down);
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rl_message.getVisibility() == View.VISIBLE) {
                                    Animation slide_up = AnimationUtils.loadAnimation(mContext.getApplicationContext(),
                                            R.anim.slide_up);
                                    rl_message.startAnimation(slide_up);
                                    rl_message.setVisibility(View.GONE);

                                } else {

                                }

                            }
                        });
                    }
                }, 2000);


            }
        }
    }


    public static void screenNotificationCrossClick() {
        try {
            if (rl_message != null) {
                rl_message.clearAnimation();
                rl_message.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
