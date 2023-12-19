package com.dalileuropeapps.dalileurope.api.retrofit;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_sv")
    @Expose
    private String nameSv;
    @SerializedName("name_de")
    @Expose
    private String nameDe;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("fullImage")
    @Expose
    private String fullImage;
    @SerializedName("thumbImage")
    @Expose
    private String thumbImage;
    @SerializedName("sub_categories")
    @Expose
    private List<SubCategory> subCategories = null;
    boolean isChecked = false;
    int position = 0;
    int selectedSubCatId = 0;

    protected Category(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        nameSv = in.readString();
        nameDe = in.readString();
        nameAr = in.readString();
        fullImage = in.readString();
        thumbImage = in.readString();
        isChecked = in.readByte() != 0;
        position = in.readInt();
        selectedSubCatId = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  Category() {

    }

    public String getNameSv() {
        return nameSv;
    }

    public void setNameSv(String nameSv) {
        this.nameSv = nameSv;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public int getSelectedSubCatId() {
        return selectedSubCatId;
    }

    public void setSelectedSubCatId(int selectedSubCatId) {
        this.selectedSubCatId = selectedSubCatId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(nameSv);
        dest.writeString(nameDe);
        dest.writeString(nameAr);
        dest.writeString(fullImage);
        dest.writeString(thumbImage);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(position);
        dest.writeInt(selectedSubCatId);
    }
}