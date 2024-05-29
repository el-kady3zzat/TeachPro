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
import com.example.tech.backendless.classes.Content;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private final Context mContext;
    private List<com.example.tech.backendless.classes.Content> arrayList;
    private final onContentListener contentListener;
    private final String cat;

    public void setData(List<Content> Contents){
        arrayList = Contents;
    }

    public ContentAdapter(Context context, List<Content> arrayList, onContentListener contentListener, String cat) {
        this.cat = cat;
        this.mContext = context;
        this.arrayList = arrayList;
        this.contentListener = contentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_files, parent, false);
        return new ViewHolder(view, contentListener);
    }

    Content Content;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Content = arrayList.get(position);
        assert Content != null;
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
        holder.tvContent.setText(Content.getFileName());
        holder.tvContent.setTextColor(mContext.getResources().getColor(colorId));
        holder.ivContentFile.setImageResource(setImage());
        holder.cons.setOnClickListener(view -> contentListener.onContentClicked(position));
    }

    private int setImage(){
        switch (Content.getFileType()) {
            case "pdf":
                return R.drawable.pdf_upload;
            case "word":
                return R.drawable.doc_upload;
            case "image":
                return R.drawable.image_upload;
            default:
                return R.drawable.video_upload;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvContent;
        ImageView ivContentFile;
        ConstraintLayout cons;
        onContentListener contentListener;
        public ViewHolder(@NonNull View itemView, onContentListener contentListener) {
            super(itemView);
            this.contentListener = contentListener;
            cons = itemView.findViewById(R.id.cons);
            tvContent = itemView.findViewById(R.id.tvFileName);
            ivContentFile = itemView.findViewById(R.id.ivContentFile);
        }

        @Override
        public void onClick(View view) {
            contentListener.onContentClicked(getAdapterPosition());
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    public interface onContentListener {
        void onContentClicked(int position);
    }

}