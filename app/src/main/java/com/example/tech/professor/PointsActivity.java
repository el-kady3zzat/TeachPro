package com.example.tech.professor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.GPA;
import com.example.tech.common.PointsAdapter;
import com.example.tech.common.RecycleAdapter;
import com.example.tech.register.RegisterActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PointsActivity extends AppCompatActivity {

    Spinner spnTeam, spnDept;
    ImageView ivSearch;
    RecyclerView rvPoints;
    PointsAdapter adapter;
    AlertDialog LoadingDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        initializeViews();
        rvPoints.setLayoutManager(new VegaLayoutManager());

        String[] teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeam.setAdapter(teamAdapter);

        RegisterActivity.getDept(this, spnDept);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        LoadingDialog = builder.create();

        ivSearch.setOnClickListener(v -> {
            LoadingDialog.show();
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setPageSize(100);
            builder.setSortBy("gpa DESC");
            builder.setWhereClause("team = '"+spnTeam.getSelectedItem().toString()+"' and dept = '"+spnDept.getSelectedItem().toString()+"' ");
            Backendless.Data.of(GPA.class).find(builder, new AsyncCallback<List<GPA>>() {
                @Override
                public void handleResponse(List<GPA> response) {
                    if (response.size() == 0){
                        LoadingDialog.cancel();
                        Toasty.info(PointsActivity.this, "No Data!", Toasty.LENGTH_LONG, true).show();
                    }
                    adapter = new PointsAdapter(response, PointsActivity.this);
                    rvPoints.setAdapter(adapter);
                    LoadingDialog.cancel();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(PointsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                }
            });
        });
    }

    private void initializeViews() {
        spnTeam = findViewById(R.id.spnTeam);
        spnDept = findViewById(R.id.spnDept);
        ivSearch = findViewById(R.id.ivSearch);
        rvPoints = findViewById(R.id.rvPoints);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}