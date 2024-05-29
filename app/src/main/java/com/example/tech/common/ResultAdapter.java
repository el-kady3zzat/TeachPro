package com.example.tech.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tech.R;
import com.example.tech.backendless.classes.Result;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private final List<Result> arrayList;
    private final Context context;
    int quesNum;

    public ResultAdapter(Context context, List<Result> arrayList , int quesNum) {
        this.quesNum = quesNum;
        this.arrayList = arrayList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvDate, tvTeam, tvDept;
        ConstraintLayout con;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTeam = itemView.findViewById(R.id.tvTeam);
            tvDept = itemView.findViewById(R.id.tvDept);
            con = itemView.findViewById(R.id.cons);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exams_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position % 2 == 0){
            holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.li1));
        }
        else {
            holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.li2));
            holder.tvSubject.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.white));
        }

        holder.tvSubject.setText(arrayList.get(position).getStuName());
        holder.tvDate.append(context.getResources().getString(R.string.res)
                + " "
                + arrayList.get(position).getResult()
                + " "
                + context.getResources().getString(R.string.of)
                + " "
                + quesNum);

        holder.tvTeam.setVisibility(View.GONE);
        holder.tvDept.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}