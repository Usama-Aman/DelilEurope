package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.BusinessImage;
import com.dalileuropeapps.dalileurope.interfaces.OnImagePostAdClick;

import java.util.ArrayList;

public class BusinessImagesAdaptor extends RecyclerView.Adapter<BusinessImagesAdaptor.ViewHolder> {

    private Activity mContext;
    private ArrayList<BusinessImage> mDataSet;
    private int pos;
    OnImagePostAdClick mItemClickListener;
    private BusinessImage mItem;

    public BusinessImagesAdaptor(Activity context, ArrayList<BusinessImage> list) {
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


    public void addItems(ArrayList<BusinessImage> images) {
        Log.d("amir",images.size()+"");
        mDataSet = new ArrayList<>();
        mDataSet=images;
//        if (mDataSet.size() > 5)
//            mDataSet.remove(0);
        notifyDataSetChanged();
    }

    void addItem(BusinessImage businessImage) {

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

            if (mItem.getId() == -1) {
                if (mItem.getImgFile() != null) {
                    Glide.with(mContext)
                            .load(mItem.getImgFile()).centerCrop().apply(new RequestOptions().override(150, 150))
                            .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .into(holder.ivUserImage);
                    holder.ivPlus.setVisibility(View.GONE);
                    holder.ivDelete.setVisibility(View.VISIBLE);
                    holder.ivUserImage.setVisibility(View.VISIBLE);
                } else {
                    holder.ivDelete.setVisibility(View.GONE);
                    holder.ivUserImage.setVisibility(View.GONE);
                    holder.ivPlus.setVisibility(View.VISIBLE);
                }
            } else if (mItem.getFullImage() != null) {
                Glide.with(mContext)
                        .load(mItem.getFullImage())
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(holder.ivUserImage);
                holder.ivPlus.setVisibility(View.GONE);
                holder.ivUserImage.setVisibility(View.VISIBLE);
                holder.ivDelete.setVisibility(View.VISIBLE);
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
           /* holder.ivUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), mDataSet.get(position).isUploaded() + "", Toast.LENGTH_SHORT).show();
                }
            });*/

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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}