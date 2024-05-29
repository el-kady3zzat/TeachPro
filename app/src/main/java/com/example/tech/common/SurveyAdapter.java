package com.example.tech.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tech.R;
import com.example.tech.backendless.classes.Survey;

import java.util.List;


public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    private List<Survey> arrayList;
    private final onSurveyListener surveyListener;

    public void setData(List<Survey> exams){
        arrayList = exams;
    }

    public SurveyAdapter(List<Survey> arrayList, onSurveyListener surveyListener) {
        this.arrayList = arrayList;
        this.surveyListener = surveyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey, parent, false);
        return new ViewHolder(view, surveyListener);
    }

    Survey survey;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        survey = arrayList.get(position);
        holder.tvActivityStu.setText(survey.getName());
        holder.tvActivityStu.setOnClickListener(view -> surveyListener.onSurveyClicked(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvActivityStu;
        onSurveyListener surveyListener;
        public ViewHolder(@NonNull View itemView, onSurveyListener surveyListener) {
            super(itemView);
            this.surveyListener = surveyListener;
            tvActivityStu = itemView.findViewById(R.id.tvActivityStu);
            tvActivityStu.setTextSize(22);
        }

        @Override
        public void onClick(View view) {
            surveyListener.onSurveyClicked(getAbsoluteAdapterPosition());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    public interface onSurveyListener{
        void onSurveyClicked(int position);
    }

}