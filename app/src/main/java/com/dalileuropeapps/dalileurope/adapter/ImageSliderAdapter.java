
package com.dalileuropeapps.dalileurope.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dalileuropeapps.dalileurope.utils.PhotoPopUpconstants;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.dalileuropeapps.dalileurope.R;

import java.util.List;


/**
 * Adapter to show Image gallery slider
 */

public class ImageSliderAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    //List of Images
    List<String> images;

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    public ImageSliderAdapter(Context context, List<String> _images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        images = _images;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_popup_photo_full, container, false);
        try {
            PhotoView ivProvider = (PhotoView) itemView.findViewById(R.id.image);
            ProgressBar loading = (ProgressBar) itemView.findViewById(R.id.loading);
            ViewGroup parent;
            ivProvider.setMaximumScale(6);
            parent = (ViewGroup) ivProvider.getParent();

            loading.setIndeterminate(true);
            loading.setVisibility(View.VISIBLE);
            loadImages(loading, ivProvider, parent, position, true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void onPalette(Palette palette, PhotoView ivProvider) {
        if (null != palette) {
            ViewGroup parent = (ViewGroup) ivProvider.getParent().getParent();
            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
        }
    }

    /*public void showAtLocation(View parent, int gravity, int x, int y) {
        mParentRootView = new WeakReference<>(parent.getRootView());
        showAtLocation(parent.getWindowToken(), gravity, x, y);
    }*/

    public void loadImages(final ProgressBar loading, final PhotoView ivProvider, final ViewGroup parent, final int position, final Boolean toCache) {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap resource, Picasso.LoadedFrom from) {
                if (Build.VERSION.SDK_INT >= 16) {
                    parent.setBackground(new BitmapDrawable(mContext.getResources(), PhotoPopUpconstants.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
                } else {
                    onPalette(Palette.from(resource).generate(), ivProvider);
                }
                ivProvider.setImageBitmap(resource);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable d) {
                if (toCache) {
                    loadImages(loading, ivProvider, parent, position, false);

                } else {
                    loading.setIndeterminate(false);
                    loading.setBackgroundColor(Color.LTGRAY);
                }
            }

            @Override
            public void onPrepareLoad(Drawable d) {
            }
        };

        if (toCache)
            Picasso.with(mContext)
                    .load( images.get(position))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(target);
        else
            Picasso.with(mContext)
                    .load( images.get(position))
                    .into(target);
    }
}