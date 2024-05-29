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
import com.example.tech.backendless.classes.Exam;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private final Context mContext;
    private List<Exam> arrayList;
    private final onExamListener examListener;
    private final String cat;

    public void setData(List<Exam> exams){
        arrayList = exams;
    }

    public RecycleAdapter(Context context, List<Exam> arrayList, onExamListener examListener, String cat) {
        this.cat = cat;
        this.mContext = context;
        this.arrayList = arrayList;
        this.examListener = examListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exams_list, parent, false);
        return new ViewHolder(view, examListener);
    }

    Exam exam;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (cat.equals("stu")){
            holder.tvTeam.setVisibility(View.INVISIBLE);
            holder.tvDept.setVisibility(View.INVISIBLE);
        }

        exam = arrayList.get(position);
        assert exam != null;
        if (position % 2 == 0){
            holder.cons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.li1));
            fillData(R.color.main, holder, position);
        }
        else {
            holder.cons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.li2));
            fillData(R.color.white, holder, position);
        }
    }

    private void fillData(int colorId, @NonNull ViewHolder holder, final int position){
        holder.tvSubject.setText(exam.getSubject());
        holder.tvSubject.setTextColor(mContext.getResources().getColor(colorId));
        holder.tvTeam.setText(exam.getTeam());
        holder.tvTeam.setTextColor(mContext.getResources().getColor(colorId));
        holder.tvDept.setText(exam.getDept());
        holder.tvDept.setTextColor(mContext.getResources().getColor(colorId));
        holder.tvDate.setText(exam.getExamDate());
        holder.tvDate.setTextColor(mContext.getResources().getColor(colorId));
        holder.cons.setOnClickListener(view -> examListener.onExamClicked(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvSubject, tvTeam, tvDept, tvDate;
        ConstraintLayout cons;
        onExamListener examListener;
        public ViewHolder(@NonNull View itemView, onExamListener examListener) {
            super(itemView);
            this.examListener = examListener;
            cons = itemView.findViewById(R.id.cons);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvTeam = itemView.findViewById(R.id.tvTeam);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDept = itemView.findViewById(R.id.tvDept);
        }

        @Override
        public void onClick(View view) {
            examListener.onExamClicked(getAdapterPosition());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    public interface onExamListener{
        void onExamClicked(int position);
    }

}