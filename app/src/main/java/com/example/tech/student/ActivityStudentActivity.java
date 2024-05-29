package com.example.tech.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Activity;
import com.example.tech.backendless.classes.ActivityResult;
import com.example.tech.common.ActivityAdapter;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ActivityStudentActivity extends AppCompatActivity {

    RecyclerView rvActivity;
    ActivityAdapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    List<ActivityResult> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student2);

        rvActivity = findViewById(R.id.rvActivity);
        rvActivity.setLayoutManager(new VegaLayoutManager());

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        getActivities();


    }

    private void getActivities() {
        String team = getIntent().getStringExtra("team");
        String dept = getIntent().getStringExtra("dept");
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+team+"'");
        builder.setWhereClause("dept = '"+dept+"'");
        Backendless.Data.of(Activity.class).find(builder, new AsyncCallback<List<Activity>>() {
            @Override
            public void handleResponse(List<Activity> response) {
                if (response.size() == 0){
                    onBackPressed();
                    Toasty.info(ActivityStudentActivity.this, "No Activities!",Toasty.LENGTH_LONG,true).show();
                }
                else{
                    for (int i = 0; i < response.size(); i++) {
                        ActivityResult result = new ActivityResult();
                        result.setStuID(pref.getString("id",""));
                        result.setActivity(response.get(i).getActivity());
                        results.add(result);

                            if (i == response.size() - 1){
                            builder.setWhereClause("stuID = '"+pref.getString("id","")+"'");
                            Backendless.Data.of(ActivityResult.class).find(builder, new AsyncCallback<List<ActivityResult>>() {
                                @Override
                                public void handleResponse(List<ActivityResult> response1) {
                                    if (results.size() == response1.size()){
                                        onBackPressed();
                                        Toasty.info(ActivityStudentActivity.this, "No Activities!",Toasty.LENGTH_LONG,true).show();
                                        return;
                                    }

                                    for (int i = 0; i < results.size(); i++) {
                                        for (int j = 0; j < response1.size(); j++) {
                                            if (results.get(i).getActivity().equals(response1.get(j).getActivity())){
                                                results.remove(i);
                                                i--;
                                                break;
                                            }
                                        }
                                    }

                                    adapter = new ActivityAdapter(results, ActivityStudentActivity.this, pref, editor);
                                    rvActivity.setAdapter(adapter);

//                                    Toast.makeText(ActivityStudentActivity.this, ""+response1.size(), Toast.LENGTH_SHORT).show();
//                                    for (int j = 0; j < response1.size(); j++) {
//                                        ActivityResult result;
//                                        result = response1.get(j);
//                                        Toast.makeText(ActivityStudentActivity.this, ""+result.getActivity(), Toast.LENGTH_SHORT).show();
//                                        if (results.contains(result)){
//                                            Toast.makeText(ActivityStudentActivity.this, ""+results.size(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }



//                                    for (int j = 0; j < results.size(); j++) {
//                                        for (int k = 0; k < response1.size(); k++) {
//                                            if (results.get(j).equals(response1.get(k))) {
//                                                results.remove(j);
//                                                if (j == results.size() - 1 && k == response1.size() - 1){
//                                                    Toast.makeText(ActivityStudentActivity.this, ""+results.size(), Toast.LENGTH_SHORT).show();
//                                                    adapter = new ActivityAdapter(results, ActivityStudentActivity.this, pref, editor);
//                                                    rvActivity.setAdapter(adapter);
//                                                }
//                                            }
//                                        }
//                                    }
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toasty.error(ActivityStudentActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(ActivityStudentActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
    }
}