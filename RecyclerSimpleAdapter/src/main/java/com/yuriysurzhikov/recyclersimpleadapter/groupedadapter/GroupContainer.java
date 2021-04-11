package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * GroupContainer made for manage recycler view group
 * 01.04.2021 by Yurii Surzhykov
 *
 * @version 1.0
 */
public class GroupContainer<T extends Parcelable> implements Parcelable {
    @Nullable
    private String title;
    @Nullable
    private List<T> mItems;

    public GroupContainer(@Nullable String title, @Nullable List<T> mItems) {
        this.title = title;
        this.mItems = mItems;
    }

    protected GroupContainer(Parcel in) {
        title = in.readString();
        int hasItems = in.readInt();
        if (hasItems == 1) {
            int size = in.readInt();
            mItems = new ArrayList<T>(size);
            Class<?> type = (Class<?>) in.readSerializable();
            in.readList(mItems, type.getClassLoader());
        } else {
            mItems = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        if (hasItems()) {
            dest.writeInt(1);
            dest.writeInt(getItemCount());
            final Class<?> objectsType = mItems.get(0).getClass();
            dest.writeSerializable(objectsType);
            dest.writeList(mItems);
        } else {
            dest.writeInt(0);
            dest.writeInt(0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Nullable
    public List<T> getItems() {
        return mItems == null ? new ArrayList<>() : new ArrayList<>(mItems);
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public T get(int index) {
        return hasItems() ? mItems.get(index) : null;
    }

    public int getGroupItemsCount() {
        return mItems == null ? 1 : 1 + mItems.size();
    }

    public boolean hasItems() {
        return mItems != null && !mItems.isEmpty();
    }

    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public static final Creator<GroupContainer> CREATOR = new Creator<GroupContainer>() {
        @Override
        public GroupContainer createFromParcel(Parcel in) {
            return new GroupContainer(in);
        }

        @Override
        public GroupContainer[] newArray(int size) {
            return new GroupContainer[size];
        }
    };
}
