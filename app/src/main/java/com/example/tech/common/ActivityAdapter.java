package com.example.tech.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tech.R;
import com.example.tech.backendless.classes.ActivityResult;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private final List<ActivityResult> arrayList;
    private final Context mContext;
    private final SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public ActivityAdapter(List<ActivityResult> arrayList, Context context, SharedPreferences pref, SharedPreferences.Editor editor) {
        this.arrayList = arrayList;
        this.mContext = context;
        this.pref = pref;
        this.editor = editor;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout conActivity;
        TextView tvActivity;
        ImageView ivCheck;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conActivity = itemView.findViewById(R.id.conActivity);
            tvActivity = itemView.findViewById(R.id.tvActivityStu);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvActivity.setText(arrayList.get(position).getActivity());
        holder.ivCheck.setOnClickListener(v -> {
            holder.ivCheck.setEnabled(false);
            holder.ivCheck.setImageResource(R.drawable.ic_baseline_check_box_24_colored);
            editor = pref.edit();
            editor.putInt("activities", (pref.getInt("activities",0) + 1));
            editor.apply();

            ActivityResult result = new ActivityResult();
            result.setActivity(arrayList.get(position).getActivity());
            result.setStuID(pref.getString("id",""));
            Backendless.Data.of(ActivityResult.class).save(result, new AsyncCallback<ActivityResult>() {
                @Override
                public void handleResponse(ActivityResult response) {

                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}