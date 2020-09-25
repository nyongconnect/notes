package com.devthrust.mynotes.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NoteContract {
    private static final String SCHEME = "content://";
    static final String CONTENT_AUTHORITY = "com.devthrust.mynotes";
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME+CONTENT_AUTHORITY);
    static final String PATH_NOTE = "notes";
    static final String PATH_COURSE = "course";
    static final String PATH_EXPANDED = "expanded";
    public static class CourseEntry implements BaseColumns {
        public static final Uri COURSE_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COURSE);
        public static final String TABLE_NAME = "course_info";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_COURSE_ID = "courseId";
        public static final String TITLE = "course_title";

        public static String getQName(String columnName){
            return TABLE_NAME+ "." +columnName;
        }
    }

    public static class NoteEntry implements BaseColumns{
        public static final Uri NOTE_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTE);
        public static final Uri EXPANDED_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPANDED);
        public static final String TABLE_NAME = "note_info";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_COURSE_ID = "courseId";
        public static final String COLUMN_TITLE = "note_title";
        public static final String COLUMN_TEXT = "text";

        public static String getQName(String columnName){
            return TABLE_NAME+ "." +columnName;
        }

    }
}
