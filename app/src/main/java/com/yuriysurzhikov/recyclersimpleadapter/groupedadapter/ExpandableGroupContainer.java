package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * ExpandableGroupContainer Container for items in {@link ExpandableRecyclerViewAdapter}
 * @param <T> Parcelable item type, that will used in adapter.
 */
public class ExpandableGroupContainer<T extends Parcelable> extends GroupContainer<T> {

    private boolean isExpanded;

    public ExpandableGroupContainer(@Nullable String title, @Nullable List<T> mItems) {
        super(title, mItems);
        isExpanded = false;
    }

    protected ExpandableGroupContainer(Parcel in) {
        super(in);
        isExpanded = in.readBoolean();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBoolean(isExpanded);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }
}
