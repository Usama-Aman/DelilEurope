package com.dalileuropeapps.dalileurope.dialogs.popup;

/**
 * Created by Noureen on 28-Feb-18.
 */

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;


import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;

import com.dalileuropeapps.dalileurope.R;

import android.view.View;
import android.view.WindowManager;

import android.widget.PopupWindow;
import android.widget.RadioButton;

public class FilterPopup implements View.OnClickListener {


    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;

    RadioButton rbAZ;
    RadioButton rbZA;
    RadioButton rbDistance;
    RadioButton rbRating;

    onClickListener listener;
    int selectedFilter = 0;
    int mToggleTextUnChecked = 0;
    int mToggleTextChecked = 0;

    public interface onClickListener {
        void onButtonClick(int filter);
    }


    public FilterPopup(final Context ctx, int selectedFilter, onClickListener listener) {
        try {

            this.ctx = ctx;
            this.selectedFilter = selectedFilter;
            this.listener = listener;
            tipWindow = new PopupWindow(ctx);
            tipWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.popup_filter, null);

            mToggleTextUnChecked = ctx.getResources().getColor(R.color.colorToggleText);
            mToggleTextChecked = ctx.getResources().getColor(R.color.colorPrimary);
            initView();
            set();
            unSelectViews(selectedFilter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        rbAZ = (RadioButton) contentView.findViewById(R.id.rbAZ);
        rbZA = (RadioButton) contentView.findViewById(R.id.rbZA);
        rbDistance = (RadioButton) contentView.findViewById(R.id.rbDistance);
        rbRating = (RadioButton) contentView.findViewById(R.id.rbRating);
    }

    public void set() {
        rbAZ.setOnClickListener(this);
        rbZA.setOnClickListener(this);
        rbDistance.setOnClickListener(this);
        rbRating.setOnClickListener(this);
    }

    public void showToolTip(View anchor) {
        try {
            if (tipWindow != null && ctx != null) {
                tipWindow.setHeight((int) ctx.getResources().getDimension(R.dimen._220sdp));
                tipWindow.setWidth((int) ctx.getResources().getDimension(R.dimen._220sdp));

                tipWindow.setOutsideTouchable(true);
                tipWindow.setTouchable(true);
                tipWindow.setFocusable(true);
                tipWindow.setBackgroundDrawable(new BitmapDrawable());

                tipWindow.setContentView(contentView);

                int screen_pos[] = new int[2];
                // Get location of anchor view on screen
                anchor.getLocationOnScreen(screen_pos);

                // Get rect for anchor view
                Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                        + anchor.getWidth(), screen_pos[1] + anchor.getHeight());

                // Call view measure to calculate how big your view should be.
                contentView.measure(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                int contentViewHeight = contentView.getMeasuredHeight();
                int contentViewWidth = (int) ctx.getResources().getDimension(R.dimen._100sdp);
                // In this case , i dont need much calculation for x and y position of
                // tooltip
                // For cases if anchor is near screen border, you need to take care of
                // direction as well
                // to show left, right, above or below of anchor view
                int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
                int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);


                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;

                int rightMargin = 1;

                for (int i = anchor_rect.left; i < width; i++) {
                    rightMargin = rightMargin + 1;
                }


                tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x - rightMargin, position_y + 35);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing()) {
            tipWindow.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbAZ:
                unSelectViews(1);


                if(selectedFilter==1){
                    listener.onButtonClick(0);
                }else{
                    listener.onButtonClick(1);
                }

                dismissTooltip();
                break;
            case R.id.rbZA:
                unSelectViews(2);


                if(selectedFilter==2){
                    listener.onButtonClick(0);
                }else{
                    listener.onButtonClick(2);
                }

                dismissTooltip();
                break;
            case R.id.rbDistance:
                if(selectedFilter==4){
                    listener.onButtonClick(0);
                }else{
                    listener.onButtonClick(4);
                }
                unSelectViews(3);
                dismissTooltip();
                break;
            case R.id.rbRating:


                if(selectedFilter==3){
                    listener.onButtonClick(0);
                }else{
                    listener.onButtonClick(3);
                }
                unSelectViews(4);

                dismissTooltip();
                break;

        }
    }

    public void unSelectViews(int filter) {
        rbAZ.setChecked(false);
        rbZA.setChecked(false);
        rbDistance.setChecked(false);
        rbRating.setChecked(false);
        rbAZ.setTextColor(mToggleTextUnChecked);
        rbZA.setTextColor(mToggleTextUnChecked);
        rbDistance.setTextColor(mToggleTextUnChecked);
        rbRating.setTextColor(mToggleTextUnChecked);
        rbAZ.setBackgroundResource(R.drawable.shape_toggle_unselect);
        rbZA.setBackgroundResource(R.drawable.shape_toggle_unselect);
        rbDistance.setBackgroundResource(R.drawable.shape_toggle_unselect);
        rbRating.setBackgroundResource(R.drawable.shape_toggle_unselect);
        if (filter != 0) {
            switch (filter) {
                case 1:
                    rbAZ.setChecked(true);
                    rbAZ.setBackgroundResource(R.drawable.shape_toggle_select);
                    rbAZ.setTextColor(mToggleTextChecked);
                    break;
                case 2:
                    rbZA.setChecked(true);
                    rbZA.setBackgroundResource(R.drawable.shape_toggle_select);
                    rbZA.setTextColor(mToggleTextChecked);
                    break;
                case 3:
                    rbRating.setChecked(true);
                    rbRating.setBackgroundResource(R.drawable.shape_toggle_select);
                    rbRating.setTextColor(mToggleTextChecked);
                    break;
                case 4:
                    rbDistance.setChecked(true);
                    rbDistance.setBackgroundResource(R.drawable.shape_toggle_select);
                    rbDistance.setTextColor(mToggleTextChecked);
                    break;
            }

        }
    }
}