package com.devthrust.mynotes.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public String originalCourseId, originalNoteTitle,originalNoteText;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState) {
        outState.putString("courseId",originalCourseId );
        outState.putString("note_title", originalNoteTitle);
        outState.putString("note_body", originalNoteText);

    }

    public void restoreState(Bundle savedInstanceState) {
        originalCourseId = savedInstanceState.getString("courseId");
        originalNoteTitle = savedInstanceState.getString("note_title");
        originalNoteText = savedInstanceState.getString("note_body");
    }
}
