package com.dalileuropeapps.dalileurope.pagination_pack;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.fragments.MessagesListFragment;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.examples.dalileurope.models.UserMessages;

public class MessagesThreadPAginationAdaptor extends RecyclerView.Adapter<BaseViewHolder> implements Filterable {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public List<UserMessages> mPostItems;
    Context ctx;


    public final MessagesListFragment.OnListFragmentInteractionListener mListener;
    Context context;
    CustomPaginationFilter filter;


    RecItemClick recItemClick;

    public interface RecItemClick {
        public void ItemClick(int position);
    }


    public MessagesThreadPAginationAdaptor(List<UserMessages> postItems, Context ctx, MessagesListFragment.OnListFragmentInteractionListener listener) {
        this.mPostItems = postItems;
        this.ctx = ctx;
        this.context = ctx;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages, parent, false));
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

        @BindView(R.id.imgUserProfile)
        ImageView imgUserProfile;
        @BindView(R.id.tvMessageCounter)
        TextView tvMessageCounter;
        @BindView(R.id.tvSenderName)
        TextView tvSenderName;
        @BindView(R.id.tvDateTime)
        TextView tvDateTime;
        @BindView(R.id.cardMessage)
        CardView cardMessage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }


        public void onBind(final int position) {
            super.onBind(position);
//            UserMessages orderModel = mPostItems.get(position);
            final UserMessages mItem = mPostItems.get(position);
            Glide.with(context)
                    .load(mPostItems.get(position).getTo_user_details().getFullImage())
                    .error(R.drawable.user_placeholder)
                    .into(imgUserProfile);
            tvSenderName.setText(mPostItems.get(position).getTo_user_details().getFirstName());



            if (ApplicationUtils.isSet(mPostItems.get(position).getReadableTime())) {
                tvDateTime.setText(mPostItems.get(position).getReadableTime());
            } else {
                tvDateTime.setText(ApplicationUtils.getDtAndTime(mPostItems.get(position).getUpdatedAt()));
            }


            if (mPostItems.get(position).getCountUnread() != null
                    && mPostItems.get(position).getCountUnread() > 0) {
                tvMessageCounter.setText(mPostItems.get(position).getCountUnread().toString());
                tvMessageCounter.setVisibility(View.VISIBLE);
            } else {
                tvMessageCounter.setVisibility(View.GONE);
            }
            cardMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item_messages has been selected.
                        mPostItems.get(position).setCountUnread(0);
                        mListener.onListFragmentInteraction(mItem);
                        notifyDataSetChanged();
                        tvMessageCounter.setVisibility(View.GONE);
                    }
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
                recItemClick.ItemClick(position);
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


//    public void addCounterOrItem(UserMessages userMessages) {
//        if (userMessages != null) {
//            Boolean exist = false;
//            userMessages.setCountUnread(userMessages.getCount_unread_notify());
//            for (int i = 0; i < mPostItems.size(); i++) {
//                if (mPostItems.get(i).getId() == userMessages.getId()) {
//                    mPostItems.get(i).setCountUnread(userMessages.getCountUnread());
//                    exist = true;
//                    break;
//                }
//            }
//            if (!exist) {
//                mPostItems.add(0, userMessages);
//            }
//            Collections.sort(mPostItems,comparator);
//            Collections.reverse(mPostItems);
//            notifyDataSetChanged();
//        }
//    }
//
//
//
//
//    Comparator<UserMessages> comparator = new Comparator<UserMessages>() {
//        @Override
//        public int compare(UserMessages o1, UserMessages o2) {
//            return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
//        }
//    };




    public void addCounterOrItem(UserMessages userMessages) {
        if (userMessages != null) {
            Boolean exist = false;
            userMessages.setCountUnread(userMessages.getCount_unread_notify());
            for (int i = 0; i < mPostItems.size(); i++) {
                if (mPostItems.get(i).getId() == userMessages.getId()) {
                    mPostItems.get(i).setCountUnread(userMessages.getCountUnread());
                    mPostItems.get(i).setUpdatedAt(userMessages.getUpdatedAt());
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                mPostItems.add(0, userMessages);
            }
            Collections.sort(mPostItems,comparator);
            Collections.reverse(mPostItems);
            notifyDataSetChanged();
        }
    }


    Comparator<UserMessages> comparator = new Comparator<UserMessages>() {
        @Override
        public int compare(UserMessages o1, UserMessages o2) {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            int options=0;
            try {
                Date dt1=dateFormat.parse(o1.getUpdatedAt());
                Date dt2=dateFormat.parse(o2.getUpdatedAt());
                options= dt1.compareTo(dt2);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return options;
        }
    };


    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomPaginationFilter(mPostItems, this);
        }
        return filter;
    }
}
