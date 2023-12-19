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
import com.dalileuropeapps.dalileurope.api.retrofit.ResponseDetailAdsData;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.pagination_pack.BaseViewHolder;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddListAdaptor extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    public List<ResponseDetailAdsData> mPostItems = new ArrayList<>();
    Context ctx;


    RecItemClick recItemClick;

    public interface RecItemClick {
        public void delItemClick(int position);

        public void viewItemClick(int position);

        public void editItemClick(int position);
    }


    public AddListAdaptor(List<ResponseDetailAdsData> postItems, Context ctx, RecItemClick recItemClick) {
//        this.mPostItems = postItems;
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

    public void addItems(List<ResponseDetailAdsData> postItems) {
        //mPostItems=new ArrayList<>();
        mPostItems.addAll(postItems);

        notifyDataSetChanged();
    }


    public void addOneItem(ResponseDetailAdsData postItem) {
        mPostItems.add(0, postItem);
        notifyDataSetChanged();
    }

    public void removeOneItem(int position) {
        mPostItems.remove(position);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new ResponseDetailAdsData());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public ResponseDetailAdsData getlist(int positionIndex) {
        return mPostItems.get(positionIndex);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        ResponseDetailAdsData item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    ResponseDetailAdsData getItem(int position) {
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

            ResponseDetailAdsData mItem = mPostItems.get(position);

            if (mItem != null) {

                User user = SharedPreference.getUserData(ctx);

                if (mItem.getIsActive() != null) {
                    if (mItem.getIsActive() == 1) {
                        imgActiveInActive.setBackgroundResource(R.color.colorHeaderGreen);
                        relItem.setBackgroundResource(R.color.white);
                    } else {
                        imgActiveInActive.setBackgroundResource(R.color.colorGrayLight);
                        relItem.setBackgroundResource(R.color.add_listing_bg_color);
                    }
                }

                if (ApplicationUtils.isSet(mItem.getFullFile()))
                    Glide.with(ctx)
                            .load(mItem.getFullFile())
                            .error(R.drawable.ic_image_placeholder)
                            .into(imgAddIcon);


                if (mItem.getAdsImages() != null && mItem.getAdsImages().size() != 0) {
                    Glide.with(ctx)
                            .load(mItem.getAdsImages().get(0).getFullImage())
                            .placeholder(ctx.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .error(ctx.getResources().getDrawable(R.drawable.ic_image_placeholder_small))
                            .into(imgAddIcon);
                }

                tvBussinessName.setText("");
                if (mItem.getBusinessDetails() != null)
                    if (ApplicationUtils.isSet(mItem.getBusinessDetails().getName()))
                        tvBussinessName.setText(mItem.getBusinessDetails().getName());

                tvAddTitle.setText("");
                if (ApplicationUtils.isSet(mItem.getName()))
                    tvAddTitle.setText(mItem.getName());

                tvAddDetail.setText("");
                if (ApplicationUtils.isSet(mItem.getTag()))
                    tvAddDetail.setText(mItem.getTag());

                tvAddID.setText("");
                if (ApplicationUtils.isSet(mItem.getAdKey()))
                    tvAddID.setText(ctx.getResources().getString(R.string.tv_ad_id) + mItem.getAdKey());

                if (mItem.getIsFeatured() != null) {
                    if (mItem.getIsFeatured() == 1) {
                        imgAddType.setBackgroundResource(0);
                    } else if (mItem.getIsFeatured() == 2) {
                        imgAddType.setBackgroundResource(R.drawable.ic_featured);
                    } else if (mItem.getIsFeatured() == 3) {
                        imgAddType.setBackgroundResource(R.drawable.ic_hot);
                    }
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
    }


    void showPopUp(final int position, ImageView imgDotts) {
        PopupMenu popup = new PopupMenu(ctx, imgDotts);
        //inflating menu from xml resource
        popup.inflate(R.menu.pop_up_menu);
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
                        recItemClick.editItemClick(position);
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