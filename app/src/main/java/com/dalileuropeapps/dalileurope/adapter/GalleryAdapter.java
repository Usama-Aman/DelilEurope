package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.callbacks.PaginationAdapterCallback;
import com.dalileuropeapps.dalileurope.api.retrofit.GalleryResponseDataDetail;
import com.dalileuropeapps.dalileurope.dialogs.popup.VPPhotoFullPopupWindow;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter to set image list of a user's event
 * includes image deletion and  downloading
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        public void onView(final int position);
    }

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private PaginationAdapterCallback mCallback;
    private Activity mContext;
    private ArrayList<GalleryResponseDataDetail> mDataSet;
    OnItemClickListener listener;
    private GalleryResponseDataDetail mItem;
    List<String> fullImages;
    int selectedCounter = 0;
    String errorMsg;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    public GalleryAdapter(Activity context, ArrayList<GalleryResponseDataDetail> list, OnItemClickListener listener) {
        mContext = context;
        mDataSet = list;
        this.listener = listener;
        this.mContext = context;

        if (mDataSet != null && mDataSet.size() != 0) {
            fullImages = new ArrayList<>();
            for (GalleryResponseDataDetail image : mDataSet)
                fullImages.add(image.getFullImage());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPhotoImage;


        public ViewHolder(View v) {
            super(v);
            ivPhotoImage = (ImageView) v.findViewById(R.id.ivPhotoImage);
        }

        public void bind(final int position, final OnItemClickListener listener) {

        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress_bar, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }


    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_gallery, parent, false);
        viewHolder = new ViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        mItem = mDataSet.get(position);


        switch (getItemViewType(position)) {
            case ITEM:
                try {


                    final ViewHolder holder = (ViewHolder) vHolder;
                    holder.ivPhotoImage.setVisibility(View.VISIBLE);

                    if (ApplicationUtils.isSet(mItem.getFullImage())) {


                        Glide.with(mContext)
                                .load(mItem.getFullImage())
                                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                .error(mContext.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }

                                })
                                .into(holder.ivPhotoImage);
                        // holder.ivPhotoImage.setTag(mItem.getFullImage());
                        holder.ivPhotoImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Code to show image in full screen:
                                new VPPhotoFullPopupWindow(mContext, R.layout.dialog_pop_vp, view, fullImages, null, position);
                            }
                        });
                    }
                    ((ViewHolder) holder).bind(position, listener);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) vHolder;
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);
                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mContext.getString(R.string.alert_msg_unknown));
                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }


    }


    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }



    /*
     * Displays Pagination retry footer view along with appropriate errorMsg
     * @param show
     * @param errorMsg to display if page load fails
     */

    @Override
    public int getItemViewType(int position) {
        return (position == mDataSet.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /************************************************************************
     * Helper Function
     ************************************************************************/


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public GalleryResponseDataDetail getItem(int position) {
        return mDataSet.get(position);
    }

    public void resetList() {
        selectedCounter = 0;
        notifyDataSetChanged();
    }


    public ArrayList<GalleryResponseDataDetail> getList() {
        return mDataSet;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);


            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mDataSet.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(GalleryResponseDataDetail mc) {
        mDataSet.add(mc);
        notifyItemInserted(mDataSet.size() - 1);
    }

    public void addAll(List<GalleryResponseDataDetail> mcList) {
        for (GalleryResponseDataDetail mc : mcList) {
            add(mc);
        }
    }

    public void remove(GalleryResponseDataDetail city) {
        int position = mDataSet.indexOf(city);
        if (position > -1) {
            mDataSet.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new GalleryResponseDataDetail());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mDataSet.size() - 1;
        GalleryResponseDataDetail item = getItem(position);

        if (item != null) {
            mDataSet.remove(position);
            notifyItemRemoved(position);
        }
    }


}