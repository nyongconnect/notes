package com.devthrust.mynotes.Pojo;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public final class CourseInfo implements Parcelable {


    private String mCourseId, mTitle;
    private List<Module> mModules;
    public CourseInfo(String mCourseId, String mTitle, List<Module> modules){
        this.mCourseId = mCourseId;
        this.mTitle = mTitle;
        this.mModules = modules;
    }

    private CourseInfo(Parcel source){
        mCourseId = source.readString();
        mTitle = source.readString();
        mModules = new ArrayList<>();
        source.readTypedList(mModules, Module.CREATOR);
    }

    @NonNull
    @Override
    public String toString() {
        return getTitle();
    }

    public static final Parcelable.Creator<CourseInfo> CREATOR = new Parcelable.Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel in) {
            return new CourseInfo(in);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };

    public String getCourseId() {
        return mCourseId;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<Module> getModules() {
        return mModules;
    }

    public void setCourseId(String courseId) {
        mCourseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCourseId);
        parcel.writeString(mTitle);
        parcel.writeTypedList(mModules);

    }
}
