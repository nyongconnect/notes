package com.devthrust.mynotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.devthrust.mynotes.data.NoteContract.CourseEntry;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteProvider extends ContentProvider {
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private static final int NOTES = 100;
    private static final int NOTES_ID = 101;
    private static final int COURSES = 102;
    private static final int COURSES_ID = 103;
    private  String jointTable = NoteEntry.TABLE_NAME + " JOIN "+ CourseEntry.TABLE_NAME + " ON "+ NoteEntry.getQName(NoteEntry.COLUMN_COURSE_ID)
            +" = "+ CourseEntry.getQName(CourseEntry.COLUMN_COURSE_ID);

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int EXPANDED = 104;

    static {
        URI_MATCHER.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_NOTE,NOTES);
        URI_MATCHER.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_NOTE+"/#",NOTES_ID);
        URI_MATCHER.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_COURSE,COURSES);
        URI_MATCHER.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_COURSE+"/#",COURSES_ID);
        URI_MATCHER.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.PATH_EXPANDED, EXPANDED);
    }

    @Override
    public boolean onCreate() {
        mNoteDbOpenHelper = new NoteDbOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mNoteDbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = URI_MATCHER.match(uri);
        switch (match){
            case NOTES:
                cursor = db.query(NoteEntry.TABLE_NAME, projection, null,null,null, null,sortOrder);
                break;
            case NOTES_ID:
                selection = NoteEntry._ID + " =?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(NoteEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case COURSES:
                cursor = db.query(CourseEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);
                break;
            case EXPANDED:
                cursor = db.query(jointTable, projection, null, null,null,null,sortOrder);
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mNoteDbOpenHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        switch (match){
            case NOTES:
                db.insert(NoteEntry.TABLE_NAME,null, contentValues);
            case COURSES:
                db.insert(CourseEntry.TABLE_NAME, null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArg) {
        SQLiteDatabase db = mNoteDbOpenHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        long update = -6;
        switch (match){
            case NOTES_ID:
                selection = NoteEntry._ID + " =?";
                selectionArg = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                update = db.update(NoteEntry.TABLE_NAME, contentValues, selection, selectionArg);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return (int) update;
    }
}
