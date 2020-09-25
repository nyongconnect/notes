package com.devthrust.mynotes.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devthrust.mynotes.NoteActivity;
import com.devthrust.mynotes.R;
import com.devthrust.mynotes.data.NoteContract.CourseEntry;
import com.devthrust.mynotes.data.NoteContract.NoteEntry;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListAdapterViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater;
    private int mTitlePosition;
    private int mIdPosition;
    private int mCourseTitlePosition;
    private int mTextPosition;
    private String mCourseTitle;
    private String mNoteStringTitle;
    private String mNoteText;

    public NoteListAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor == null)
            return;
        mTitlePosition = mCursor.getColumnIndex(NoteEntry.getQName(NoteEntry.COLUMN_TITLE));
        mIdPosition = mCursor.getColumnIndex(NoteEntry.getQName(NoteEntry._ID));
        mCourseTitlePosition = mCursor.getColumnIndex(CourseEntry.getQName(CourseEntry.TITLE));
        mTextPosition = mCursor.getColumnIndex(NoteEntry.getQName(NoteEntry.COLUMN_TEXT));

    }
    public void changeCursor(Cursor cursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_notes,parent,false);
        return new NoteListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapterViewHolder holder, int position) {
        if (mCursor!=null){
        mCursor.moveToPosition(position);
            mCourseTitle = mCursor.getString(mCourseTitlePosition);
            mNoteStringTitle = mCursor.getString(mTitlePosition);
            mNoteText = mCursor.getString(mTextPosition);

        holder.mCourseTitle.setText(mCourseTitle);
        holder.mNoteTitle.setText(mNoteStringTitle);
        holder.mId = mCursor.getLong(mIdPosition);
        holder.mUri = ContentUris.withAppendedId(NoteEntry.NOTE_CONTENT_URI, holder.mId);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null)
            return mCursor.getCount();
        return 0;
    }

    class  NoteListAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView mCourseTitle, mNoteTitle;
        long mId;
        Uri mUri = null;
        public NoteListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseTitle = itemView.findViewById(R.id.tv_course_title);
            mNoteTitle = itemView.findViewById(R.id.tv_note_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.setData(mUri);
                    mContext.startActivity(intent);

                }
            });
        }
    }
}
