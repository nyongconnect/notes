package com.devthrust.mynotes.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

class Module implements Parcelable {
    String mModuleId, mTitle;
    boolean mIsComplete;

    public  Module(String moduleId,String title, boolean isComplete){
        mModuleId =moduleId;
        mTitle = title;
        mIsComplete = isComplete;
    }
    protected Module(Parcel in) {
        mModuleId = in.readString();
        mTitle = in.readString();
        mIsComplete = in.readByte() == 1;
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mModuleId);
        parcel.writeString(mTitle);
    }
}
