package com.dalileuropeapps.dalileurope.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.api.retrofit.adslist.FavAddsData;
import com.dalileuropeapps.dalileurope.pagination_pack.BaseViewHolder;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavAddListAdaptor extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    public List<FavAddsData> mPostItems;
    Context ctx;


    RecItemClick recItemClick;

    public interface RecItemClick {
        public void delItemClick(int position);

        public void viewItemClick(int position);
    }


    public FavAddListAdaptor(List<FavAddsData> postItems, Context ctx, RecItemClick recItemClick) {
        this.mPostItems = postItems;
        this.ctx = ctx;
        this.recItemClick = recItemClick;

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.add_list_item, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void addItems(List<FavAddsData> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addOneItem(FavAddsData postItem) {
        mPostItems.add(0, postItem);
        notifyDataSetChanged();
    }

    public void removeOneItem(int position) {
        mPostItems.remove(position);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new FavAddsData());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public FavAddsData getlist(int positionIndex) {
        return mPostItems.get(positionIndex);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        FavAddsData item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    FavAddsData getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.imgActiveInActive)
        ImageView imgActiveInActive;

        @BindView(R.id.imgAddIcon)
        ImageView imgAddIcon;

        @BindView(R.id.tvBussinessName)
        TextView tvBussinessName;

        @BindView(R.id.tvAddTitle)
        TextView tvAddTitle;

        @BindView(R.id.tvAddDetail)
        TextView tvAddDetail;

        @BindView(R.id.tvAddID)
        TextView tvAddID;

        @BindView(R.id.imgAddType)
        ImageView imgAddType;

        @BindView(R.id.imgAddListitemOptions)
        ImageView imgAddListitemOptions;

        @BindView(R.id.relItem)
        RelativeLayout relItem;
        @BindView(R.id.cardItemList)
        CardView cardItemList;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }


        public void onBind(final int position) {
            super.onBind(position);

            FavAddsData mItem = mPostItems.get(position);
            User user = SharedPreference.getUserData(ctx);


            if (mItem.getAds_details().getIsActive() == 1) {
                imgActiveInActive.setBackgroundResource(R.color.colorHeaderGreen);
            } else {
                imgActiveInActive.setBackgroundResource(R.color.colorGrayLight);
                relItem.setBackgroundResource(R.color.add_listing_bg_color);
            }

            if (ApplicationUtils.isSet(mItem.getAds_details().getFullFile()))
                Glide.with(ctx)
                        .load(mItem.getAds_details().getFullFile())
                        .error(R.drawable.ic_image_placeholder)
                        .into(imgAddIcon);


            if (mItem.getAds_details().getAdsImages() != null && mItem.getAds_details().getAdsImages().size() != 0) {
                Glide.with(ctx)
                        .load(mItem.getAds_details().getAdsImages().get(0).getFullImage())
                        .placeholder(ctx.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .error(ctx.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                        .into(imgAddIcon);
            }

            tvBussinessName.setText("");
            if (mItem.getAds_details().getBusinessDetails() != null)
                if (ApplicationUtils.isSet(mItem.getAds_details().getBusinessDetails().getName()))
                    tvBussinessName.setText(mItem.getAds_details().getBusinessDetails().getName());

            tvAddTitle.setText("");
            if (ApplicationUtils.isSet(mItem.getAds_details().getName()))
            tvAddTitle.setText(mItem.getAds_details().getName());

            tvAddDetail.setText("");
            if (ApplicationUtils.isSet(mItem.getAds_details().getTag()))
                tvAddDetail.setText(mItem.getAds_details().getTag());

            tvAddID.setText("");
            if (ApplicationUtils.isSet(mItem.getAds_details().getAdKey()))
                tvAddID.setText(ctx.getResources().getString(R.string.tv_ad_id) + mItem.getAds_details().getAdKey());


            if (mItem.getAds_details().getIsFeatured() == 1) {
                imgAddType.setBackgroundResource(0);
            } else if (mItem.getAds_details().getIsFeatured() == 2) {
                imgAddType.setBackgroundResource(R.drawable.ic_featured);
            } else if (mItem.getAds_details().getIsFeatured() == 3) {
                imgAddType.setBackgroundResource(R.drawable.ic_hot);
            }
            imgAddListitemOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp(position, imgAddListitemOptions);
                }
            });
            cardItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recItemClick.viewItemClick(position);
                }
            });


        }
    }


    void showPopUp(final int position, ImageView imgDotts) {
        PopupMenu popup = new PopupMenu(ctx, imgDotts);
        //inflating menu from xml resource
        popup.inflate(R.menu.fav_add_menu);

        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuActive:
                        //handle menu1 click
                        break;
                    case R.id.menuView:
                        //handle menu2 click
                        recItemClick.viewItemClick(position);
                        break;
                    case R.id.menuEdit:
                        //handle menu3 click
                        break;
                    case R.id.menuDel:
                        //handle menu3 click
//                        Toast.makeText(ctx, position + "", Toast.LENGTH_SHORT).show();
                        showDelAlert(position);
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }


    void showDelAlert(final int position) {
        AlertDialog.Builder aler = new AlertDialog.Builder(ctx);
        aler.setTitle(R.string.sure_to_del);
        aler.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                recItemClick.delItemClick(position);
            }
        });
        aler.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aler.show();
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }
}
