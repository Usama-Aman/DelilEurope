package com.dalileuropeapps.dalileurope.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.utils.CustomFilter;
import com.dalileuropeapps.dalileurope.fragments.MessagesListFragment.OnListFragmentInteractionListener;

import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;

import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessagesListRecAdapter extends RecyclerView.Adapter<MessagesListRecAdapter.ViewHolder> implements Filterable {

    public List<UserMessages> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;
    CustomFilter filter;

    public MessagesListRecAdapter(List<UserMessages> items, OnListFragmentInteractionListener listener, Context context) {
        ApplicationUtils.getDatesList();
        mValues = items;
        mListener = listener;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_messages, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Glide.with(context)
                .load(mValues.get(position).getUserDetailsTo().getFullImage())
                .error(R.drawable.user_placeholder)
                .into(holder.imgUserProfile);
        holder.tvSenderName.setText(mValues.get(position).getUserDetailsTo().getFirstName());
        holder.tvDateTime.setText(ApplicationUtils.getDtAndTime(mValues.get(position).getUserDetailsTo().getUpdatedAt()));

//        holder.tvDateTime.setText(ApplicationUtils.getDtAndTime("2019-12-27 12:12:12"));
//        String datTime[] = mValues.get(position).getUser_message().getUserDetailsTo().getUpdatedAt().split(" ");
//        if (datTime != null) {
//            if (datTime[0] != null) {
//                String dt = ApplicationUtils.getDayOrDate(datTime[0]);
//                holder.tvDateTime.setText(dt);
//                if (dt.equalsIgnoreCase("today")) {
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String dateString = null;
//                    try {
//                        Date date = dateFormat.parse(mValues.get(position).getUser_message().getUserDetailsTo().getUpdatedAt().toString());
//                        String strDateFormat = "HH:mm a";
//                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//                        holder.tvDateTime.setText(sdf.format(date));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else if (dt.contains("-")) {
//                    holder.tvDateTime.setText(dt);
//                } else {
//                    holder.tvDateTime.setText(dt.substring(0, 2));
//                }
//            }
//        }
//


        if (mValues.get(position).getUserDetailsTo().getMessageCount() != null
                && mValues.get(position).getUserDetailsTo().getMessageCount() >= 0) {
            holder.tvMessageCounter.setText(mValues.get(position).getUserDetailsTo().getMessageCount().toString());
            holder.tvMessageCounter.setVisibility(View.VISIBLE);
        }
        holder.cardMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item_messages has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public UserMessages mItem;

        ImageView imgUserProfile;
        TextView tvMessageCounter, tvSenderName, tvDateTime;
        CardView cardMessage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);


            imgUserProfile = (ImageView) view.findViewById(R.id.imgUserProfile);
            tvMessageCounter = (TextView) view.findViewById(R.id.tvMessageCounter);
            tvSenderName = (TextView) view.findViewById(R.id.tvSenderName);
            tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            cardMessage = (CardView) view.findViewById(R.id.cardMessage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }


    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(mValues, this);
        }
        return filter;
    }
}
