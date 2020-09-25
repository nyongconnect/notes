package com.devthrust.mynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devthrust.mynotes.Pojo.CourseInfo;
import com.devthrust.mynotes.Pojo.DataManager;
import com.devthrust.mynotes.Pojo.NoteInfo;
import com.devthrust.mynotes.adapter.CourseListAdapter;
import com.devthrust.mynotes.adapter.NoteListAdapter;
import com.devthrust.mynotes.data.DummyDataFiller;
import com.devthrust.mynotes.data.NoteContract;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView mRecyclerView;
    private NoteListAdapter mNoteListAdapter;
    private List<CourseInfo> mCourseInfos;
    private CourseListAdapter mCourseListAdapter;
    private List<NoteInfo> mNotes;
    private LoaderManager mLoaderManager;
    private final int NOTE_LIST_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enableStrictMode();

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(1, null, this);

        mCourseInfos = DataManager.getInstance().getCourses();
        mNotes = DataManager.getInstance().getNotes();

        mNoteListAdapter = new NoteListAdapter(this, null);

        mCourseListAdapter = new CourseListAdapter(this, mCourseInfos);
        populateNoteRecyclerView();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void enableStrictMode() {
        if (BuildConfig.DEBUG){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);

        }
    }

    private void populateNoteRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mNoteListAdapter);

        setNavigationCheckedItem(R.id.nav_title);

    }

    private void populateCourseRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_value));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mCourseListAdapter);

        setNavigationCheckedItem(R.id.nav_course);

    }

    private void setNavigationCheckedItem(int menuItem) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(menuItem).setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavHeader();
        mLoaderManager.restartLoader(NOTE_LIST_LOADER, null, this);


    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView headerNameview = header.findViewById(R.id.header_name);
        TextView headerEmailview = header.findViewById(R.id.header_email);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String name = pref.getString("Name", "");
        String email = pref.getString("email", "");

        headerNameview.setText(name);
        headerEmailview.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i("NoteActivity", "change recyclerview");
        switch (menuItem.getItemId()) {
            case R.id.nav_course:
                populateCourseRecyclerView();
                Log.i("NoteActivity", "change recyclerview");
                break;
            case R.id.nav_title:
                populateNoteRecyclerView();
                break;
            case R.id.nav_action_send:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello friend");
                intent.putExtra(Intent.EXTRA_TEXT, "you have reach the help line of the note notification center");
                startActivity(intent);
                break;
            case R.id.nav_action_share:

                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent1.setType("plain/text");
                intent1.putExtra(Intent.EXTRA_TEXT, "hello, click here for like");
//                ShareCompat.IntentBuilder.from(this).setType("plain/text").st

                break;
            case R.id.insert_dummy:



        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {NoteEntry.getQName(NoteEntry._ID), NoteEntry.getQName(NoteEntry.COLUMN_TITLE),NoteEntry.getQName(NoteEntry.COLUMN_TEXT),
                NoteEntry.getQName(NoteEntry.COLUMN_COURSE_ID), NoteContract.CourseEntry.getQName(NoteContract.CourseEntry.TITLE)
        };
        return new CursorLoader(this, NoteEntry.EXPANDED_CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mNoteListAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNoteListAdapter.changeCursor(null);

    }


//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.nav_course:
//                populateCourseRecyclerView();
//                Log.i("NoteActivity","change recyclerview");
//                break;
//            case R.id.nav_title:
//                populateNoteRecyclerView();
//                break;
////            case R.id.nav_action_send:
////                break;
////            case R.id.nav_action_share:
////                break;
//        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
