package com.dalileuropeapps.dalileurope.adapter.message;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dalileuropeapps.dalileurope.R;

import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DiffUtilChatRec;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatRecAdaptor extends RecyclerView.Adapter<ChatRecAdaptor.ViewHolder> {

    private final List<UserMessages> mValues;
    Context context;
    RecItemClick recItemClick;


    public ChatRecAdaptor(List<UserMessages> items, Context context, RecItemClick recItemClick) {
        mValues = items;
        this.context = context;
        this.recItemClick = recItemClick;
    }

    public interface RecItemClick {
        public void delItemClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).getFromUserId() == 34) {
            holder.linUser1.setVisibility(View.GONE);
            holder.linUser2.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(mValues.get(position).getUserDetailsTo().getFullImage())
                    .error(R.drawable.user_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgUserProfile2);
            holder.tvMessage2.setText(mValues.get(position).getMessage());
            String datTime[] = mValues.get(position).getCreatedAt().split(" ");
            if (datTime != null) {
                if (datTime[0] != null) {
                    String dt = ApplicationUtils.getDayOrDate(datTime[0]);
                    holder.tvDateTime2.setText(datTime[1] + ",  " + dt);
                }
            }



        } else {

            holder.linUser1.setVisibility(View.VISIBLE);
            holder.linUser2.setVisibility(View.GONE);
            Glide.with(context)
                    .load(mValues.get(position).getUserDetailsTo().getFullImage())
                    .error(R.drawable.user_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgUserProfile);
            holder.tvMessage.setText(mValues.get(position).getMessage());


            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(mValues.get(position).getCreatedAt());
                SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm a, dd MMM yyyy");
                String timeDat = timeDateFormatter.format(date);
                holder.tvDateTime.setText(timeDat.toUpperCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.linUser2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(v.getContext(), position + "", Toast.LENGTH_SHORT).show();
                showDelAlert(position);
                return false;
            }
        });
    }

    public void updateEmployeeListItems(List<UserMessages> employees) {
        final DiffUtilChatRec diffCallback = new DiffUtilChatRec(this.mValues, employees);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.mValues.clear();
        this.mValues.addAll(employees);
        diffResult.dispatchUpdatesTo(this);
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
        public UserMessages mItem;


        ImageView imgUserProfile;
        TextView tvMessage, tvDateTime;

        ImageView imgUserProfile2;
        TextView tvMessage2, tvDateTime2;

        public LinearLayout linUser1;
        public RelativeLayout linUser2;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            linUser1 = (LinearLayout) view.findViewById(R.id.linUser1);
            linUser2 = (RelativeLayout) view.findViewById(R.id.linuser2);
            imgUserProfile = (ImageView) view.findViewById(R.id.imgUserProfile);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            imgUserProfile2 = (ImageView) view.findViewById(R.id.imgUserProfile2);
            tvMessage2 = (TextView) view.findViewById(R.id.tvMessage2);
            tvDateTime2 = (TextView) view.findViewById(R.id.tvDateTime2);

        }


    }

    void showDelAlert(final int position) {
        AlertDialog.Builder aler = new AlertDialog.Builder(context);
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

}
