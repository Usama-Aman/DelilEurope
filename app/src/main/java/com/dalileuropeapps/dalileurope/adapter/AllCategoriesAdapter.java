package com.dalileuropeapps.dalileurope.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalileuropeapps.dalileurope.adapter.models.MultiCheckCategories;
import com.dalileuropeapps.dalileurope.api.retrofit.Category;
import com.dalileuropeapps.dalileurope.api.retrofit.SubCategory;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.dalileuropeapps.dalileurope.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.lang.reflect.Type;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class AllCategoriesAdapter extends
        CheckableChildRecyclerViewAdapter<AllCategoriesAdapter.GenreViewHolder, AllCategoriesAdapter.MultiCheckArtistViewHolder> {

    onRowClickListener listener;
    private Activity mContext;
    String language = "";
    int selectedCatId = 0;
    int selectedSubCatId = 0;

    public interface onRowClickListener {
        void onSubItemClick(int pPosition, int cPosition);

        void onItemClick(int position);
    }


    public AllCategoriesAdapter(Activity context, int selectedCatId, int selectedSubCatId, List<MultiCheckCategories> groups, onRowClickListener listener) {
        super(groups);
        this.mContext = context;
        this.listener = listener;
        language = SharedPreference.getAppLanguage(context);
        this.selectedCatId = selectedCatId;
        this.selectedSubCatId = selectedSubCatId;
    }

    @Override
    public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subcategories, parent, false);
        return new MultiCheckArtistViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(MultiCheckArtistViewHolder holder, int position,
                                           CheckedExpandableGroup group, int childIndex) {
        final SubCategory subCategory = (SubCategory) group.getItems().get(childIndex);
        holder.setDataSubCategory(subCategory, subCategory.getParentPosition(), subCategory.getChildPosition());
    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_categories, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setDataCategory(group);


    }


    public class GenreViewHolder extends GroupViewHolder {

        private TextView genreName;
        private ImageView arrow;
        private CheckedTextView parentCheckedTextView;

        public GenreViewHolder(View itemView) {
            super(itemView);
            genreName = (TextView) itemView.findViewById(R.id.list_item_category_name);
            arrow = (ImageView) itemView.findViewById(R.id.list_item_category_arrow);
            parentCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.list_item_multicheck_category_name);

        }

        public void setDataCategory(ExpandableGroup genre) {

            Gson gson = new Gson();
            Type type = new TypeToken<Category>() {
            }.getType();
            final Category category = gson.fromJson(genre.getTitle(), type);
            genreName.setText(category.getName());


            if (language.equalsIgnoreCase(Constants.swedish)) {
                genreName.setText(category.getNameSv());
            } else if (language.equalsIgnoreCase(Constants.germany)) {
                genreName.setText(category.getNameDe());
            } else if (language.equalsIgnoreCase(Constants.arabic)) {
                genreName.setText(category.getNameAr());
            } else {
                genreName.setText(category.getName());
            }
            genreName.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
            if (selectedCatId != 0 && selectedSubCatId == 0) {
                if (selectedCatId == category.getId())
                    genreName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            }
            Typeface robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
            genreName.setTypeface(robotoRegular);
            parentCheckedTextView.setVisibility(View.GONE);
       /*     if (category.isChecked()) {
                parentCheckedTextView.setChecked(true);
                parentCheckedTextView.setCheckMarkDrawable(R.drawable.ic_mark_check_small);
                parentCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

            } else {
                parentCheckedTextView.setChecked(false);
                parentCheckedTextView.setCheckMarkDrawable(R.drawable.ic_mark_unchek_small);
                parentCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
            }*/


            genreName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    listener.onItemClick(category.getPosition());
                }
            });


        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    public class MultiCheckArtistViewHolder extends CheckableChildViewHolder {

        private CheckedTextView childCheckedTextView;

        public MultiCheckArtistViewHolder(View itemView) {
            super(itemView);
            childCheckedTextView =
                    (CheckedTextView) itemView.findViewById(R.id.list_item_multicheck_subcategory_name);
        }

        @Override
        public Checkable getCheckable() {
            return childCheckedTextView;
        }

        public void setDataSubCategory(SubCategory subCategory, final int pPosition, final int cPosition) {


            if (language.equalsIgnoreCase(Constants.swedish)) {
                childCheckedTextView.setText(subCategory.getNameSv());
            } else if (language.equalsIgnoreCase(Constants.germany)) {
                childCheckedTextView.setText(subCategory.getNameDe());
            } else if (language.equalsIgnoreCase(Constants.arabic)) {
                childCheckedTextView.setText(subCategory.getNameAr());
            } else {
                childCheckedTextView.setText(subCategory.getName());
            }

            childCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorDefaultText));
            if (selectedCatId != 0 && selectedSubCatId != 0) {
                if (selectedSubCatId == subCategory.getId()) {
                    childCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                }
            }


            Typeface robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
            childCheckedTextView.setTypeface(robotoRegular);
            childCheckedTextView.setCheckMarkDrawable(null);
        /*    if (subCategory.isChecked()) {
                childCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                childCheckedTextView.setChecked(true);
                childCheckedTextView.setCheckMarkDrawable(R.drawable.ic_mark_check_small);
            } else {
                childCheckedTextView.setTextColor(mContext.getResources().getColor(R.color.colorBlackGrey));
                childCheckedTextView.setChecked(false);
                childCheckedTextView.setCheckMarkDrawable(R.drawable.ic_mark_unchek_small);
            }
*/
            childCheckedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubItemClick(pPosition, cPosition);
                }
            });
        }
    }


    public void resetSelectedCatSubCat(int selectedCatId, int selectedSubCatId) {
        this.selectedCatId = selectedCatId;
        this.selectedSubCatId = selectedSubCatId;
    }

}
