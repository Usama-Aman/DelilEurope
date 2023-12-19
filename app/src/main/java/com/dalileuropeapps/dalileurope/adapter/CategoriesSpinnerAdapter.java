
package com.dalileuropeapps.dalileurope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.api.retrofit.Category;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import java.util.ArrayList;

public class CategoriesSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context mContext;
    ArrayList<Category> mCategoriesList;

    String language = "";

    public CategoriesSpinnerAdapter(Context context, ArrayList<Category> mCategoriesList) {
        mContext = context;
        this.mCategoriesList = mCategoriesList;
        language = SharedPreference.getAppLanguage(context);
    }

    public int getCount() {
        return mCategoriesList.size();
    }

    public Object getItem(int i) {
        return mCategoriesList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category category = mCategoriesList.get(position);
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.item_sp_dropdown, null);

        TextView tvSp = (TextView) convertView.findViewById(R.id.tvSp);

        if (language.equalsIgnoreCase(Constants.swedish)) {
            tvSp.setText(category.getNameSv());
        } else if (language.equalsIgnoreCase(Constants.germany)) {
            tvSp.setText(category.getNameDe());
        } else if (language.equalsIgnoreCase(Constants.arabic)) {
            tvSp.setText(category.getNameAr());
        } else {
            tvSp.setText(category.getName());
        }

        if (tvSp.getText().toString().equals(mContext.getResources().getString(R.string.tv_select_category))) {
            tvSp.setTextColor(mContext.getResources().getColor(R.color.colorGreyHintText));
        } else {
            tvSp.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
        }

        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup viewgroup) {
        Category category = mCategoriesList.get(position);

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.item_sp, null);
        convertView.setPadding(0, convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSp);

        if (language.equalsIgnoreCase("sv")) {
            tvTitle.setText(category.getNameSv());
        } else if (language.equalsIgnoreCase("de")) {
            tvTitle.setText(category.getNameDe());
        } else if (language.equalsIgnoreCase("ar")) {
            tvTitle.setText(category.getNameAr());
        } else {
            tvTitle.setText(category.getName());
        }

        if (tvTitle.getText().toString().equals(mContext.getResources().getString(R.string.tv_select_category))) {
            tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorGreyHintText));
        } else {
            tvTitle.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
        }

        return convertView;
    }


}


