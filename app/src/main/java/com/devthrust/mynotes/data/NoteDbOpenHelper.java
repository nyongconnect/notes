package com.devthrust.mynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devthrust.mynotes.data.NoteContract.CourseEntry;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;

import androidx.annotation.Nullable;

public class NoteDbOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "note.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_COURSE_TABLE = "create table " + CourseEntry.TABLE_NAME +"("+
            CourseEntry._ID + " integer primary key autoincrement, "+ CourseEntry.COLUMN_COURSE_ID + " TEXT UNIQUE NOT NULL," + CourseEntry.TITLE+ " TEXT Not null ) ";
    private static final String SQL_CREATE_NOTE_TABLE = "create table "+ NoteEntry.TABLE_NAME +" (" + NoteEntry._ID + " integer primary key autoincrement, "+
            NoteEntry.COLUMN_COURSE_ID + " Text not null, "+ NoteEntry.COLUMN_TITLE + " Text not null, "+ NoteEntry.COLUMN_TEXT + " Text not null)";
    private static final String SQL_DELETE_TABLE = "";
    private static final String SQL_DELETE_NOTE_TABLE = "";

    public NoteDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_COURSE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TABLE);
        DummyDataFiller dummyDataFiller = new DummyDataFiller(sqLiteDatabase);
        dummyDataFiller.insertNotes();
        dummyDataFiller.insertCourses();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_NOTE_TABLE);
        onCreate(sqLiteDatabase);

    }
}
