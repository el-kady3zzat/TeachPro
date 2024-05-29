package com.example.tech.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tech.R;
import com.example.tech.backendless.classes.GPA;
import com.example.tech.backendless.classes.Result;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Result> arrayList;

    public StudentsAdapter(Context mContext, List<Result> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clStudents;
        TextView tvStudents, tvNum, tvStuPoints;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clStudents = itemView.findViewById(R.id.clStudents);
            tvStudents = itemView.findViewById(R.id.tvStudents);
            tvNum = itemView.findViewById(R.id.tvNum);
            tvStuPoints = itemView.findViewById(R.id.tvStuPoints);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_students, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position % 2 == 0){
//            holder.clStudents.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.li1));
            holder.clStudents.setElevation(100);

//            holder.tvStudents.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.et));
//            holder.tvNum.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.et));

            //            holder.tvStudents.setTextColor(mContext.getResources().getColor(R.color.main));
            //            holder.tvNum.setTextColor(mContext.getResources().getColor(R.color.main));
        }
        else {
//            holder.clStudents.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.li2));
            holder.clStudents.setElevation(-50);

//            holder.tvStudents.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.et1));
//            holder.tvNum.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.et1));

            //            holder.tvStudents.setTextColor(mContext.getResources().getColor(R.color.white));
            //            holder.tvNum.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        holder.tvStudents.setText(arrayList.get(position).getStuName());
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvStuPoints.setText(String.valueOf(arrayList.get(position).getFinalResult()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}