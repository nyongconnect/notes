package com.devthrust.mynotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devthrust.mynotes.NoteActivity;
import com.devthrust.mynotes.Pojo.CourseInfo;
import com.devthrust.mynotes.Pojo.NoteInfo;
import com.devthrust.mynotes.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListAdapterViewHolder>  {
    Context mContext;
    List<CourseInfo> mCourseList;
    LayoutInflater mLayoutInflater;
    public CourseListAdapter(Context context, List<CourseInfo> courseList){
        mContext = context;
        this.mCourseList = courseList;
        mLayoutInflater = LayoutInflater.from(mContext);

    }
    @NonNull
    @Override
    public CourseListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.course_list,parent,false);
        return new CourseListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapterViewHolder holder, int position) {
        holder.mCourseTitle.setText(mCourseList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        Log.e("NoteListActivityAdapter", mCourseList.size()+"");
        return mCourseList.size();
    }

    public class  CourseListAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView mCourseTitle;
        int mCurrentPosition;
        public CourseListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseTitle = itemView.findViewById(R.id.tv_my_course_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(mContext, NoteActivity.class);
//                    intent.putExtra("note", mCurrentPosition);
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
