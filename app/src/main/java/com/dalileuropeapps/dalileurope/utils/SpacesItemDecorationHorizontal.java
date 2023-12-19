package com.dalileuropeapps.dalileurope.utils;


import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecorationHorizontal extends RecyclerView.ItemDecoration {
    private int space;
    boolean giveNegative = false;
    int spaceNegative = 0;

    public SpacesItemDecorationHorizontal(int space, boolean giveNegative, int spaceNegative) {
        this.space = space;
        this.giveNegative = giveNegative;
        this.spaceNegative = spaceNegative;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0 && giveNegative) {
            outRect.left = -spaceNegative;

        } else {
            outRect.left = space;
            outRect.right = space;
        }
    }
}