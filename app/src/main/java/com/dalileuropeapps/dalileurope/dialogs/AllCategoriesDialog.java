package com.dalileuropeapps.dalileurope.dialogs;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.adapter.AllCategoriesAdapter;
import com.dalileuropeapps.dalileurope.adapter.models.MultiCheckCategories;
import com.dalileuropeapps.dalileurope.api.retrofit.AllCategoriesResponse;
import com.dalileuropeapps.dalileurope.api.retrofit.Category;
import com.dalileuropeapps.dalileurope.api.retrofit.SubCategory;
import com.dalileuropeapps.dalileurope.network.ApiClient;
import com.dalileuropeapps.dalileurope.utils.ApplicationUtils;
import com.dalileuropeapps.dalileurope.utils.DialogFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class AllCategoriesDialog extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    public static final String TAG = "AllCategoriesDialog";

    RelativeLayout rlMain;
    RecyclerView rvCategories;
    private Button btnSubmit;
    ImageButton ibClose;
    LinearLayoutManager layoutManager;
    ArrayList<MultiCheckCategories> mMultiCheckCategories = new ArrayList<>();
    ProgressBar progressBar;
    TextView tvNotFound;
    AllCategoriesAdapter adapter;
    int getSelectedCatIndex = 0;
    int getSelectedSubCatIndex = 0;

    onClickListener listener;
    int selectedCatId = 0;
    int selectedSubCatId = 0;
    View viewFragment;

    public interface onClickListener {
        void onCategoryClick(int catId, String catName);

        void onSubCategoryClick(int catId, int subCatId, String catName, String subCatName);
    }


    public AllCategoriesDialog(int selectedCatId, int selectedSubCatId, onClickListener listener) {
        this.listener = listener;
        this.selectedCatId = selectedCatId;
        this.selectedSubCatId = selectedSubCatId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    private void getActivityData() {
        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle != null) {
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20, 80, 20, 80);
            dialog.getWindow().setBackgroundDrawable(inset);
            DialogFactory.dialogSettings(mContext, dialog, rlMain, 0.6f, 0.9f);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        viewFragment = getActivity().getLayoutInflater().inflate(R.layout.dialog_categorie_subcategories, parent, false);
        viewInitialize(viewFragment);
        callAllCategories();
        return viewFragment;
    }


    private void viewInitialize(View v) {

        progressBar = v.findViewById(R.id.progressBar);
        rlMain = v.findViewById(R.id.rlMain);
        rvCategories = v.findViewById(R.id.rvCategories);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        ibClose = v.findViewById(R.id.ibClose);
        layoutManager = new LinearLayoutManager(mContext);
        tvNotFound = v.findViewById(R.id.tvNotFound);

        rlMain.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        rlMain.setOnClickListener(this);
        ibClose.setOnClickListener(this);


    }


    public void setData() {
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        adapter = new AllCategoriesAdapter(mContext, selectedCatId, selectedSubCatId, mMultiCheckCategories, new AllCategoriesAdapter.onRowClickListener() {
            @Override
            public void onItemClick(int position) {

                Gson gson = new Gson();
                Type type = new TypeToken<Category>() {
                }.getType();
                final Category category = gson.fromJson(mMultiCheckCategories.get(position).getTitle(), type);

                if (!category.isChecked()) {

                    category.setChecked(true);

                    MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                    mMultiCheckCategories.set(position, checkCategories);
                    adapter.notifyDataSetChanged();
                    final Category category2 = gson.fromJson(mMultiCheckCategories.get(position).getTitle(), type);

                    listener.onCategoryClick(category.getId(), category.getName());
                    dismiss();
                } else {

                    category.setChecked(false);

                    MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                    mMultiCheckCategories.set(position, checkCategories);
                    adapter.notifyDataSetChanged();

                    listener.onCategoryClick(0, "");
                    dismiss();
                }


            }

            @Override
            public void onSubItemClick(int pPosition, int cPosition) {


                //  int subCheckedCounter = 0;
                Gson gson = new Gson();
                Type type = new TypeToken<Category>() {
                }.getType();
                final Category category = gson.fromJson(mMultiCheckCategories.get(pPosition).getTitle(), type);
                if (!category.getSubCategories().get(cPosition).isChecked()) {

                    category.setChecked(false);
                    category.getSubCategories().get(cPosition).setChecked(true);


                    MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                    mMultiCheckCategories.set(pPosition, checkCategories);
                    adapter.notifyDataSetChanged();


                    listener.onSubCategoryClick(category.getId(), category.getSubCategories().get(cPosition).getId(), category.getName(), category.getSubCategories().get(cPosition).getName());
                    dismiss();
                } else {
                    category.setChecked(false);
                    category.getSubCategories().get(cPosition).setChecked(false);
                    MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                    mMultiCheckCategories.set(pPosition, checkCategories);
                    adapter.notifyDataSetChanged();
                    listener.onSubCategoryClick(0, 0, "", "");
                    dismiss();
                }


            }

        });

        rvCategories.setAdapter(adapter);
        if (selectedCatId != 0 || selectedSubCatId != 0) {
            // adapter.checkChild(true, getSelectedCatIndex, getSelectedSubCatIndex);

            if (selectedCatId != 0 && selectedSubCatId == 0) {

                Gson gson = new Gson();
                Type type = new TypeToken<Category>() {
                }.getType();
                final Category category = gson.fromJson(mMultiCheckCategories.get(getSelectedCatIndex).getTitle(), type);
                category.setChecked(true);
                MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                mMultiCheckCategories.set(getSelectedCatIndex, checkCategories);
                adapter.notifyDataSetChanged();

            } else if (selectedCatId != 0 && selectedSubCatId != 0) {

                Gson gson = new Gson();
                Type type = new TypeToken<Category>() {
                }.getType();
                final Category category = gson.fromJson(mMultiCheckCategories.get(getSelectedCatIndex).getTitle(), type);
                category.setChecked(false);
                category.getSubCategories().get(getSelectedSubCatIndex).setChecked(true);
                MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                mMultiCheckCategories.set(getSelectedCatIndex, checkCategories);
                adapter.notifyDataSetChanged();

            }



            adapter.onGroupClick(getSelectedCatIndex);

            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:
            case R.id.ibClose:
                ApplicationUtils.hideKeyboard(mContext);
                dismiss();
                break;

        }
    }

    public void callAllCategories() {

        if (!ApplicationUtils.isOnline(mContext)) {


            DialogFactory.showDropDownNotification(mContext,
                    viewFragment,
                    R.id.notificationLayout,
                    mContext.getResources().getColor(R.color.colorRed),
                    mContext.getResources().getString(R.string.tv_msg_error),
                    getResources().getString(R.string.alert_internet_connection));
            return;
        }
        try {

            showProgressBar(true);
            getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showProgressBar(final boolean progressVisible) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void getAllCategories() {

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getAllCategories(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call,
                                   final retrofit2.Response<AllCategoriesResponse> response) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus()) {
                        if (response.body().getData() != null && response.body().getData().getCategories() != null && response.body().getData().getCategories().size() != 0) {
                            tvNotFound.setVisibility(View.GONE);
                            rvCategories.setVisibility(View.VISIBLE);
                            mMultiCheckCategories.clear();
                            addFeaturedCategories(response.body().getData().getCategories());

                        } else {
                            tvNotFound.setVisibility(View.VISIBLE);
                            rvCategories.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (response.code() == 401)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.error_msg_unauthorized_user));
                    if (response.code() == 404)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_api_not_found));
                    if (response.code() == 500)
                        DialogFactory.showDropDownNotificationError(
                                mContext,
                                mContext.getString(R.string.alert_information),
                                mContext.getString(R.string.alert_internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {
                try {
                    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof SocketTimeoutException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_request_timeout));
                else if (t instanceof IOException)
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            mContext.getString(R.string.alert_api_not_found));
                else
                    DialogFactory.showDropDownNotificationError(
                            mContext,
                            mContext.getString(R.string.alert_information),
                            t.getLocalizedMessage());
            }
        });
    }

    public void addFeaturedCategories(List<Category> categories) {

        int parentPosition = 0;
        int childPosition = 0;
        if (categories != null && categories.size() != 0) {

            for (Category category : categories) {

                if (category.getId() == selectedCatId)
                    getSelectedCatIndex = parentPosition;

                if (category.getSubCategories() != null && category.getSubCategories().size() != 0) {
                    childPosition = 0;
                    for (SubCategory subCategory : category.getSubCategories()) {
                        if (subCategory.getId() == selectedSubCatId)
                            getSelectedSubCatIndex = childPosition;

                        subCategory.setParentPosition(parentPosition);
                        subCategory.setChildPosition(childPosition++);
                    }
                }
                category.setPosition(parentPosition++);
                MultiCheckCategories checkCategories = new MultiCheckCategories((new Gson()).toJson(category), category.getSubCategories());
                mMultiCheckCategories.add(checkCategories);

            }


            setData();
        }
    }
}