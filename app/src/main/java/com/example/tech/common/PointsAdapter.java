package com.example.tech.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tech.R;
import com.example.tech.backendless.classes.GPA;

import java.util.List;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {
    private final List<GPA> arrayList;
    private final Context mContext;
    int[] color = {R.color.a,R.color.b,R.color.c,R.color.d,R.color.e,R.color.f,R.color.g,R.color.h,R.color.i,R.color.j, R.color.main};
    int tColor;

    public PointsAdapter(List<GPA> arrayList, Context context) {
        this.arrayList = arrayList;
        this.mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout conPoints;
        TextView tvCount, tvRange, tvName, tvPoints;
        ImageView ivStar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conPoints = itemView.findViewById(R.id.conPoints);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvRange = itemView.findViewById(R.id.tvRange);
            tvName = itemView.findViewById(R.id.tvName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            ivStar = itemView.findViewById(R.id.ivStar);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position % 2 == 0){
            holder.conPoints.setElevation(20);
        }
        else {
            holder.conPoints.setElevation(0);
        }
        holder.tvCount.setText(String.valueOf(position+1));
        holder.tvRange.setText(String.valueOf(range(position)));
        holder.tvName.setText(arrayList.get(position).getStuName());
        holder.tvPoints.setText(String.valueOf(arrayList.get(position).getGpa()));
//        holder.ivStar.setColorFilter(color(position));
        holder.ivStar.setColorFilter( ContextCompat.getColor(mContext, color(position)) , android.graphics.PorterDuff.Mode.SRC_IN );
//        holder.ivStar.setColorFilter(ContextCompat.getColor(mContext,color(position)), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private int range(int position) {
        for (int i = 0; i < 11; i++) {
            if (arrayList.get(position).getGpa() < 100) {
                return 0;
            }
            else if (arrayList.get(position).getGpa() >= 1000) {
                return 10;
            }
            else if (arrayList.get(position).getGpa() >= i*100 && arrayList.get(position).getGpa() < (i+1)*100) {
                return i;
            }
        }
        return 0;
    }

    private int color(int position) {
        for (int i = 0; i <= 10; i++) {
            if (arrayList.get(position).getGpa() < 100) {
                return color[0];
            }
            else if (arrayList.get(position).getGpa() >= 1000) {
                return color[10];
            }
            else if (arrayList.get(position).getGpa() >= i*100 && arrayList.get(position).getGpa() < (i+1)*100) {
                return color[i];
            }

//            if (arrayList.get(position).getGpa() <= i*100) {
//                return color[i];
//            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}