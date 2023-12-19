package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponseDataDetail;
import com.dalileuropeapps.dalileurope.dialogs.popup.VPPhotoFullPopupWindow;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdsPhotoListAdapter extends RecyclerView.Adapter<AdsPhotoListAdapter.ViewHolder> {

    private Activity mContext;
    private ArrayList<GalleryResponseDataDetail> mDataSet;
    List<String> fullImages = new ArrayList<>();
    private GalleryResponseDataDetail mItem;

    public AdsPhotoListAdapter(Activity context, ArrayList<GalleryResponseDataDetail> list) {
        this.mContext = context;
        this.mDataSet = list;

        setPopupViewImages();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout categoryItemRoot;
        private final ImageView ivUserImage;

        public ViewHolder(View v) {
            super(v);
            ivUserImage = (ImageView) v.findViewById(R.id.ivGalleryImageDetails);
            categoryItemRoot = (ConstraintLayout) v.findViewById(R.id.clGalleryDetails);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_photo_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mItem = mDataSet.get(position);

        if (ApplicationUtils.isSet(mItem.getFullImage())) {
            Picasso.with(mContext)
                    .load(mItem.getFullImage())
                    .placeholder(R.drawable.ic_image_placeholder_small) // optional
                    .error(R.drawable.ic_image_placeholder_small)         // optional
                    .into(holder.ivUserImage);
        }


        holder.ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VPPhotoFullPopupWindow(mContext, R.layout.dialog_pop_vp, view, fullImages, null, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setPopupViewImages()
    {
        if (mDataSet != null && mDataSet.size() != 0) {
            fullImages = new ArrayList<>();
            for (GalleryResponseDataDetail image : mDataSet)
                fullImages.add(image.getFullImage());

        }
    }

}

