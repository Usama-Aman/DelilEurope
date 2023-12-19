package com.dalileuropeapps.dalileurope.utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;


import com.dalileuropeapps.dalileurope.api.retrofit.message.UserMessages;

import java.util.List;

public class DiffUtilChatRec extends DiffUtil.Callback {

    private final List<UserMessages> mOldEmployeeList;
    private final List<UserMessages> mNewEmployeeList;

    public DiffUtilChatRec(List<UserMessages> oldEmployeeList, List<UserMessages> newEmployeeList) {
        this.mOldEmployeeList = oldEmployeeList;
        this.mNewEmployeeList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldEmployeeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEmployeeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEmployeeList.get(oldItemPosition).getCreatedAt() == mNewEmployeeList.get(
                newItemPosition).getCreatedAt();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final UserMessages oldEmployee = mOldEmployeeList.get(oldItemPosition);
        final UserMessages newEmployee = mNewEmployeeList.get(newItemPosition);

        return oldEmployee.getCreatedAt().equals(newEmployee.getCreatedAt());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

