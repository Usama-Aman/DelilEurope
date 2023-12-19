package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.local.GalleryModel;
import com.dalileuropeapps.dalileurope.interfaces.OnImagePostAdClick;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class ImagePostAdAdapter extends RecyclerView.Adapter<ImagePostAdAdapter.ViewHolder> {

    private Activity mContext;
    private ArrayList<GalleryModel> mDataSet;
    OnImagePostAdClick mItemClickListener;
    private GalleryModel mItem;

    public ImagePostAdAdapter(Activity context, ArrayList<GalleryModel> list) {
        mContext = context;
        mDataSet = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnImagePostAdClick {
        private final ConstraintLayout categoryItemRoot;
        private final ImageView ivUserImage, ivDelete, ivPlus;

        public ViewHolder(View v) {
            super(v);
            ivPlus = (ImageView) v.findViewById(R.id.ivPlus);
            ivUserImage = (ImageView) v.findViewById(R.id.ivGalleryImageDetails);
            ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
            categoryItemRoot = (ConstraintLayout) v.findViewById(R.id.clGalleryDetails);
            categoryItemRoot.setTag(this);
        }

        @Override
        public void onItemClick(View view, int position) {
            mItemClickListener.onItemClick(view, getPosition()); //OnItemClickListener mItemClickListener;
        }

        @Override
        public void onDeleteClick(View view, int position) {
            mItemClickListener.onDeleteClick(view, getPosition()); //OnItemClickListener mItemClickListener;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image_upload_gallery, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            mItem = mDataSet.get(position);

            if (mItem.isUploaded()) {

                Glide.with(mContext)
                        .load(mItem.getPath())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(holder.ivUserImage);
                holder.ivPlus.setVisibility(View.GONE);
                holder.ivUserImage.setVisibility(View.VISIBLE);
                holder.ivDelete.setVisibility(View.VISIBLE);

            } else {
                if (mItem.getId() == -1) {
                    holder.ivDelete.setVisibility(View.GONE);
                    holder.ivUserImage.setVisibility(View.GONE);
                    holder.ivPlus.setVisibility(View.VISIBLE);
                } else {
                    holder.ivPlus.setVisibility(View.GONE);
                    holder.ivUserImage.setVisibility(View.VISIBLE);
                    holder.ivDelete.setVisibility(View.VISIBLE);
                    holder.ivUserImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    File uri = new File(new URI(mItem.getPath()));
                    Picasso.with(mContext).load(uri)
                            .into(holder.ivUserImage);
                }
            }


            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mItemClickListener.onDeleteClick(view, position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mItemClickListener.onItemClick(view, position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void SetOnItemClickListener(final OnImagePostAdClick mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
