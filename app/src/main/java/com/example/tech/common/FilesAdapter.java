package com.example.tech.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tech.R;
import com.example.tech.backendless.classes.StudentsFiles;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private final Context mContext;
    private List<StudentsFiles> arrayList;
    private final onFileListener fileListener;

    public void setData(List<StudentsFiles> Files){
        arrayList = Files;
    }

    public FilesAdapter(Context context, List<StudentsFiles> arrayList, onFileListener fileListener) {
        this.mContext = context;
        this.arrayList = arrayList;
        this.fileListener = fileListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_rv, parent, false);
        return new ViewHolder(view, fileListener);
    }

    StudentsFiles Files;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Files = arrayList.get(position);
        assert Files != null;
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
        holder.tvFileName.setText(Files.getFileName());
        holder.tvFileName.setTextColor(mContext.getResources().getColor(colorId));
        holder.tvStuName.setText(Files.getStuName());
        holder.tvStuName.setTextColor(mContext.getResources().getColor(colorId));
        if (position % 2 == 0)
            holder.ivFileImg.setImageResource(R.drawable.ic_baseline_insert_drive_file_24_colored);
        else
            holder.ivFileImg.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        holder.cons.setOnClickListener(view -> fileListener.onFileClicked(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvFileName, tvStuName;
        ImageView ivFileImg;
        ConstraintLayout cons;
        onFileListener fileListener;
        public ViewHolder(@NonNull View itemView, onFileListener fileListener) {
            super(itemView);
            this.fileListener = fileListener;
            cons = itemView.findViewById(R.id.conFiles);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvStuName = itemView.findViewById(R.id.tvStuName);
            ivFileImg = itemView.findViewById(R.id.ivFileImg);
        }

        @Override
        public void onClick(View view) {
            fileListener.onFileClicked(getAbsoluteAdapterPosition());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    public interface onFileListener {
        void onFileClicked(int position);
    }

}