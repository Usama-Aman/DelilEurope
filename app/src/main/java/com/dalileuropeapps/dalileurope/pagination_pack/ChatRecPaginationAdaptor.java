package com.dalileuropeapps.dalileurope.pagination_pack;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.User;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRecPaginationAdaptor extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    public List<UserMessages> mPostItems;
    AppCompatActivity ctx;


    RecItemClick recItemClick;

    public interface RecItemClick {
        public void delItemClick(int position);
    }


    public ChatRecPaginationAdaptor(List<UserMessages> postItems, AppCompatActivity ctx, RecItemClick recItemClick) {
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
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

    public void addItems(List<UserMessages> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addOneItem(UserMessages postItem) {
        mPostItems.add(0, postItem);
        notifyDataSetChanged();
    }

    public void removeOneItem(int position) {
        mPostItems.remove(position);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new UserMessages());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public UserMessages getlist(int positionIndex) {
        return mPostItems.get(positionIndex);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        UserMessages item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    UserMessages getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.linUser1)
        public LinearLayout linUser1;
        @BindView(R.id.linuser2)
        RelativeLayout linUser2;
        @BindView(R.id.imgUserProfile)
        ImageView imgUserProfile;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.tvDateTime)
        TextView tvDateTime;
        @BindView(R.id.imgUserProfile2)
        ImageView imgUserProfile2;
        @BindView(R.id.tvMessage2)
        TextView tvMessage2;
        @BindView(R.id.tvDateTime2)
        TextView tvDateTime2;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }


        public void onBind(final int position) {
            super.onBind(position);
//            UserMessages orderModel = mPostItems.get(position);
            UserMessages mItem = mPostItems.get(position);
            User user = SharedPreference.getUserData(ctx);

            int userId = user.getId();

            if (mPostItems.get(position).getFromUserId() != null && mPostItems.get(position).getFromUserId() == userId) {
                linUser1.setVisibility(View.GONE);
                linUser2.setVisibility(View.VISIBLE);
                Glide.with(ctx)
                        .load(mPostItems.get(position).getUserDetailsTo().getFullImage())
                        .error(R.drawable.user_placeholder)
                        .into(imgUserProfile2);
                tvMessage2.setText(mPostItems.get(position).getMessage());

          /*      if (ApplicationUtils.isSet(mPostItems.get(position).getReadableTime())) {
                    tvDateTime2.setText(mPostItems.get(position).getReadableTime());
                } else {
                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dateFormat.parse(mPostItems.get(position).getCreatedAt());
                        SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm a, dd MMM yyyy");
                        String timeDat = timeDateFormatter.format(date);
                        tvDateTime2.setText(timeDat.toUpperCase());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }*/

                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormat.parse(mPostItems.get(position).getCreatedAt());
                    SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm a, dd MMM yyyy");
                    String timeDat = timeDateFormatter.format(date);
                    tvDateTime2.setText(timeDat.toUpperCase());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


//            String datTime[] = mValues.get(position).getCreatedAt().split(" ");
//            if (datTime != null) {
//                if (datTime[0] != null) {
//                    String dt = ApplicationUtils.getDayOrDate(datTime[0]);
//                    holder.tvDateTime.setText(datTime[1] + ",  "  + dt);
//                }
//            }

            } else {


                //
                linUser1.setVisibility(View.VISIBLE);
                linUser2.setVisibility(View.GONE);
                if (mPostItems.get(position).getUserDetailsTo().getFullImage() != null) {
                    Glide.with(ctx)
                            .load(mPostItems.get(position).getUserDetailsTo().getFullImage())
                            .error(R.drawable.user_placeholder)
                            .into(imgUserProfile);
                }
                tvMessage.setText(mPostItems.get(position).getMessage());


             /*   if (ApplicationUtils.isSet(mPostItems.get(position).getReadableTime())) {
                    tvDateTime.setText(mPostItems.get(position).getReadableTime());
                } else {
                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dateFormat.parse(mPostItems.get(position).getCreatedAt());
                        SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm a, dd MMM yyyy");
                        String timeDat = timeDateFormatter.format(date);
                        tvDateTime.setText(timeDat.toUpperCase());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }*/
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormat.parse(mPostItems.get(position).getCreatedAt());
                    SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm a, dd MMM yyyy");
                    String timeDat = timeDateFormatter.format(date);
                    tvDateTime.setText(timeDat.toUpperCase());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


            linUser2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                Toast.makeText(v.getContext(), position + "", Toast.LENGTH_SHORT).show();
                    showDelAlert(position);
                    return false;
                }
            });

        }
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