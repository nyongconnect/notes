package com.devthrust.mynotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.devthrust.mynotes.data.NoteContract.CourseEntry;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;

public class DummyDataFiller {
    static SQLiteDatabase sSQLiteDatabase;
    public DummyDataFiller(SQLiteDatabase sqLiteDatabase){
        sSQLiteDatabase = sqLiteDatabase;
    }

    static void insertDummyNotes(String noteTitle, String noteText, String courseId){
        ContentValues noteContentValues = new ContentValues();
        noteContentValues.put(NoteEntry.COLUMN_TITLE, noteTitle);
        noteContentValues.put(NoteEntry.COLUMN_TEXT, noteText);
        noteContentValues.put(NoteEntry.COLUMN_COURSE_ID, courseId);
        sSQLiteDatabase.insert(NoteEntry.TABLE_NAME, null,noteContentValues);

    }
    public static void insertDummyCourses(String courseId, String courseTitle){
        ContentValues courseContentValues = new ContentValues();
        courseContentValues.put(CourseEntry.TITLE, courseTitle);
        courseContentValues.put(CourseEntry.COLUMN_COURSE_ID, courseId);
        sSQLiteDatabase.insert(CourseEntry.TABLE_NAME, null,courseContentValues);
    }
    public void  insertNotes(){
        insertDummyNotes("Gradles in android", "Gradles are simple file for apk generation","android");
        insertDummyNotes("Activities in android", "Android Activities as a model", "android");
        insertDummyNotes("Kotlin language","Kotlin is powerful beyond human understanding", "kotlin");
    }
    public void insertCourses(){
        insertDummyCourses("android", "Android fundamentals");
        insertDummyCourses("java", "Java real time fundamentals");
        insertDummyCourses("kotlin", "kotlin intro");
        insertDummyCourses("Python", "leveraging the power of python");

    }


}
