package com.devthrust.mynotes.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NoteInfo implements Parcelable{

    private String  mTitle, mText;
    private String mCourse;
    public NoteInfo(String courseId, String title, String text){
        this.mCourse = courseId;
        this.mTitle = title;
        this.mText = text;
    }

    public NoteInfo(Parcel parcel) {
        mCourse = parcel.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = parcel.readString();
        mTitle = parcel.readString();

    }

    public void setCourse(String course) {
        mCourse = course;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getCourse() {
        return mCourse;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return getCompareKey();
    }

    private String getCompareKey() {
        return getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mCourse);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
    }
    private static final Parcelable.Creator<NoteInfo> CREATOR = new Parcelable.Creator<NoteInfo>(){

        @Override
        public NoteInfo createFromParcel(Parcel parcel) {
            return new NoteInfo(parcel);
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[size];
        }
    };
}
