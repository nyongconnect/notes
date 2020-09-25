package com.devthrust.mynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.devthrust.mynotes.Pojo.CourseInfo;
import com.devthrust.mynotes.Pojo.DataManager;
import com.devthrust.mynotes.data.NoteContract.CourseEntry;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;
import com.devthrust.mynotes.notification.Notification;
import com.devthrust.mynotes.viewmodel.NoteActivityViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    List<CourseInfo> course;
    EditText mEditTextTitle, mEditTextBody;
    NoteActivityViewModel mViewModel;
    private Spinner mCourseSpinner;

    private Uri mContentUri;

    private Cursor mCursor;
    private String mNoteTitle;
    private String mNoteText;
    private int mNoteLoader = 1;
    private int mCourseLoader =2;
    private String mCourseId;
    private boolean mHasNoteFinishedLoading = false;
    private SimpleCursorAdapter mSpinnerAdapter;
    private boolean mHasCourseFinishedLoading;
    private String mChannel;
    private ModuleStatusView mModuleStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableStrictMode();
        mEditTextBody = findViewById(R.id.et_course_body);
        mEditTextTitle = findViewById(R.id.et_course_title);
        course = DataManager.getInstance().getCourses();
        mCourseSpinner = findViewById(R.id.spinner_courses);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if (mViewModel.mIsNewlyCreated && savedInstanceState != null){
            mViewModel.restoreState(savedInstanceState);
        }
        mViewModel.mIsNewlyCreated =false;

        mSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,null, new String[]{CourseEntry.TITLE},new int[]{android.R.id.text1},0);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseSpinner.setAdapter(mSpinnerAdapter);

        getSupportLoaderManager().initLoader(mCourseLoader, null,this);


        restoreOriginalState();
        Intent intent = getIntent();
        mContentUri = intent.getData();
        if(mContentUri != null){
            getSupportLoaderManager().initLoader(mNoteLoader, null, this);
            mModuleStatusView = findViewById(R.id.moduleStatusView2);
            loadModelStatusValues();
        }
    }

    private void loadModelStatusValues() {
        int totalAvailableModules = 10;
        int completedModules= 7;
        boolean[] moduleStatus = new boolean[totalAvailableModules];

        for (int moduleIndex = 0; moduleIndex < completedModules; moduleIndex++){
            moduleStatus[moduleIndex] = true;
        }
        mModuleStatusView.setModuleStatus(moduleStatus);
    }

    private void enableStrictMode() {
        if (BuildConfig.DEBUG){
            StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void loadData(Cursor cursor) { cursor.moveToNext();
       mNoteTitle = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
       mNoteText = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TEXT));
       mCourseId = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_COURSE_ID));
    }

    private void displayNoteInActivity() {
        mEditTextTitle.setText(mNoteTitle);
        mEditTextBody.setText(mNoteText);
        makeSpinnerSelection(mSpinnerAdapter.getCursor(), 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNotes();
    }

    private void saveNotes() {
        if (mContentUri != null){

            Cursor cursor = mSpinnerAdapter.getCursor();
            cursor.moveToPosition(mCourseSpinner.getSelectedItemPosition());
            String courseId = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_COURSE_ID));
            final String noteText = mEditTextBody.getText().toString();
            final String noteTitle = mEditTextTitle.getText().toString();

            final ContentValues contentValues = new ContentValues();
            contentValues.put(NoteEntry.COLUMN_TEXT, noteText);
            contentValues.put(NoteEntry.COLUMN_TITLE, noteTitle);
            contentValues.put(NoteEntry.COLUMN_COURSE_ID, courseId);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int rowIndex = getContentResolver().update(mContentUri, contentValues, null,null);
                    Toast.makeText(NoteActivity.this, rowIndex+" "  + " "+noteText+" "+noteTitle, Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }


    }

    private void getNoteInfoFromUserInput() {

    }

    private void restoreOriginalState() {
        int courseIndex = course.indexOf(mViewModel.originalCourseId);
        mCourseSpinner.setSelection(courseIndex);
        mEditTextBody.setText(mViewModel.originalNoteText);
        mEditTextTitle.setText(mViewModel.originalNoteTitle);
    }

    private void saveOriginalState() {
        mViewModel.originalCourseId  = mCourseSpinner.getSelectedItem().toString();
        mViewModel.originalNoteText = mEditTextBody.getText().toString();
        mViewModel.originalNoteTitle = mEditTextTitle.getText().toString();
    }
    @Override
    protected void onStop() {
        super.onStop();
        saveOriginalState();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            mViewModel.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        int listSize = DataManager.getInstance().getNotes().size() - 1;
//        MenuItem menuItem = menu.findItem(R.id.action_next);
//        menuItem.setEnabled(mContentUri < listSize);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_next:
//                ++mContentUri;
//                NoteInfo noteInfo = DataManager.getInstance().getNotes().get(mContentUri);
//                displayNoteInActivity();
//                invalidateOptionsMenu();
                break;

            case R.id.action_notify:
                mChannel = "time Notification";
                Notification.makeNotification(this, mChannel, mContentUri);
                break;

            case R.id.action_send_email:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello there");
                intent.putExtra(Intent.EXTRA_TEXT, "full story");
                startActivity(intent);
                break;
            case R.id.action_undo:
                finish();
                break;
        }



        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = null;
        if (id == mNoteLoader) {
            mHasNoteFinishedLoading = false;
              loader = createNoteLoader();
        }
        else if (id == mCourseLoader){
            mHasCourseFinishedLoading = false;
            loader = createCourseLoader();
        }
        return loader;
    }

    private CursorLoader createCourseLoader() {
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                Cursor cursor = null;
                cursor = getContentResolver().query(CourseEntry.COURSE_CONTENT_URI, null, null, null,null);
                return cursor;
            }
        };
    }

    private CursorLoader createNoteLoader() {
        return  new CursorLoader(this){
            Cursor cursor =null;
            @Override
            public Cursor loadInBackground() {
                cursor = getContentResolver().query(mContentUri, null,null,null,null);
                return cursor;

            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == mNoteLoader){
            mHasNoteFinishedLoading = true;
            loadData(data);
        }
        else if (loader.getId() == mCourseLoader){
            mSpinnerAdapter.changeCursor(data);
            mHasCourseFinishedLoading = true;
        }

        if (mHasNoteFinishedLoading && mHasCourseFinishedLoading){
            displayNoteInActivity();
        }


    }

    private void makeSpinnerSelection(Cursor cursor, int spinnerSelectionIndex) {
        while (cursor.moveToNext()){

            if(mCourseId.equals(cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_COURSE_ID)))){
                break;
            }
            spinnerSelectionIndex++;
        }
        mCourseSpinner.setSelection(spinnerSelectionIndex);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == mCourseLoader){
            mSpinnerAdapter.changeCursor(null);
        }
        else {
            if (mCursor!= null){
                mCursor.close();
            }
        }


    }
}
