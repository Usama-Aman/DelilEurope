package com.dalileuropeapps.dalileurope.dialogs.popup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.dalileuropeapps.dalileurope.R;

import androidx.viewpager.widget.ViewPager;


import com.dalileuropeapps.dalileurope.adapter.ImageSliderAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


public class VPPhotoFullPopupWindow extends PopupWindow {

    View view;
    Context mContext;
    ViewPager vpSlider;
    List<String> servicesImageList = new ArrayList<>();


    public VPPhotoFullPopupWindow(Context ctx, int layout, View v, List<String> servicesImageList, Bitmap bitmap, int pos) {
        super(((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pop_vp, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.servicesImageList = servicesImageList;
        this.view = getContentView();
        ImageButton closeButton = (ImageButton) this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dismiss();
            }
        });
        //---------Begin customising this popup--------------------

        vpSlider = (ViewPager) view.findViewById(R.id.vpSlider);
        CirclePageIndicator cpIndicator = (CirclePageIndicator) view.findViewById(R.id.cpIndicator);
        ImageSliderAdapter mCustomPagerAdapter = new ImageSliderAdapter(mContext, servicesImageList);

        vpSlider.setAdapter(mCustomPagerAdapter);
        vpSlider.setCurrentItem(pos);
        cpIndicator.setVisibility(View.VISIBLE);
        cpIndicator.setViewPager(vpSlider);
        final float density = mContext.getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        cpIndicator.setRadius(5 * density);

        //------------------------------
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    public VPPhotoFullPopupWindow(Context ctx, int layout, View v, String imageName, Bitmap bitmap, int pos) {
        super(((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pop_vp, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.servicesImageList.clear();
        servicesImageList.add(imageName);
        this.view = getContentView();
        ImageButton closeButton = (ImageButton) this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dismiss();
            }
        });
        //---------Begin customising this popup--------------------

        vpSlider = (ViewPager) view.findViewById(R.id.vpSlider);
        CirclePageIndicator cpIndicator = (CirclePageIndicator) view.findViewById(R.id.cpIndicator);
        ImageSliderAdapter mCustomPagerAdapter = new ImageSliderAdapter(mContext, servicesImageList);

        vpSlider.setAdapter(mCustomPagerAdapter);
        vpSlider.setCurrentItem(pos);
        cpIndicator.setVisibility(View.VISIBLE);
        cpIndicator.setViewPager(vpSlider);
        final float density = mContext.getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        cpIndicator.setRadius(5 * density);

        //------------------------------
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }


}