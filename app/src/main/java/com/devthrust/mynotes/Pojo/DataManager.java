package com.devthrust.mynotes.Pojo;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.devthrust.mynotes.data.NoteContract;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager ourInstance = null;
    private List<CourseInfo> mCourses = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();

    public static DataManager getInstance(){
        if(ourInstance == null){
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    public List<NoteInfo> getNotes() {
        return mNotes;
    }
    public int getNotesSize(){
        return mNotes.size();
    }

    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    public int createNotes(NoteInfo noteInfo) {
        mNotes.add(noteInfo);
        return mNotes.size() -1;
    }

    public int updateNotes(NoteInfo noteInfoFromUserInput, int noteIndex) {
        mNotes.set(noteIndex, noteInfoFromUserInput);
        return noteIndex;
    }
}
