package com.microsoft.xbox.idp.util;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

public class FragmentLoaderKey implements Parcelable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final Parcelable.Creator<FragmentLoaderKey> CREATOR = new Parcelable.Creator<FragmentLoaderKey>() { // from class: com.microsoft.xbox.idp.util.FragmentLoaderKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FragmentLoaderKey createFromParcel(Parcel parcel) {
            return new FragmentLoaderKey(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FragmentLoaderKey[] newArray(int i) {
            return new FragmentLoaderKey[i];
        }
    };
    private final String className;
    private final int loaderId;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FragmentLoaderKey(Class<? extends Fragment> cls, int i) {
        this.className = cls.getName();
        this.loaderId = i;
    }

    protected FragmentLoaderKey(Parcel parcel) {
        this.className = parcel.readString();
        this.loaderId = parcel.readInt();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FragmentLoaderKey fragmentLoaderKey = (FragmentLoaderKey) obj;
        if (this.loaderId != fragmentLoaderKey.loaderId) {
            return false;
        }
        return this.className.equals(fragmentLoaderKey.className);
    }

    public int hashCode() {
        return (this.className.hashCode() * 31) + this.loaderId;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.className);
        parcel.writeInt(this.loaderId);
    }
}
