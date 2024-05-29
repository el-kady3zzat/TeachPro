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
import com.example.tech.backendless.classes.Survey;
import com.example.tech.backendless.classes.SurveyAnswers;
import com.example.tech.common.SurveyAdapter;
import com.example.tech.professor.ProfessorSurveyActivity;
import com.example.tech.professor.SurveyDetailsActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SurveysActivity extends AppCompatActivity implements SurveyAdapter.onSurveyListener{

    RecyclerView rvSurveys;
    SurveyAdapter adapter;
    String team, dept;
    List<Survey> surveys = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        rvSurveys = findViewById(R.id.rvSurveys);
        rvSurveys.setLayoutManager(new VegaLayoutManager());

        if (getIntent().getStringExtra("cat").equals("prof"))
            getSurveysProf();
        else
            checkSurveys();
    }
    
    private void checkSurveys(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("stuID = '"+getIntent().getStringExtra("stuID")+"'");
        builder.setGroupBy("name");
        Backendless.Data.of(SurveyAnswers.class).find(builder, new AsyncCallback<List<SurveyAnswers>>() {
            @Override
            public void handleResponse(List<SurveyAnswers> response) {
                if (response.size() == 0)
                    getSurveys("");
                else if (response.size() == 1)
                    getSurveys(response.get(0).getName());
                else {
                    for (int i = 0; i < response.size(); i++) {
                        names.add(response.get(i).getName());
                        if (i == response.size() - 1)
                            getSurveys("many");
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveysActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

    private void getSurveys(String name) {
        team = getIntent().getStringExtra("team");
        dept = getIntent().getStringExtra("dept");
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+team+"'");
        builder.setWhereClause("dept = '"+dept+"'");
        if (!name.equals("") && !name.equals("many"))
            builder.setWhereClause("name != '" + name + "'");
        else if (name.equals("many")){
            String whereClause = "";
            for (int i = 0; i < names.size(); i++) {
                if (i == names.size() - 1)
                    whereClause = whereClause.concat("name != " + names.get(i));
                else
                    whereClause = whereClause.concat("name != " + names.get(i) + " and ");
                builder.setWhereClause(whereClause);
            }
        }
        builder.setGroupBy("name");
        Backendless.Data.of(Survey.class).find(builder, new AsyncCallback<List<Survey>>() {
            @Override
            public void handleResponse(List<Survey> response) {
                if (response.size() == 0) {
                    onBackPressed();
                    Toasty.info(SurveysActivity.this, "No Surveys!",Toasty.LENGTH_LONG,true).show();
                }
                else {
                    adapter = new SurveyAdapter(response, SurveysActivity.this);
                    rvSurveys.setAdapter(adapter);
                    surveys = response;
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveysActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

    private void getSurveysProf() {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("profID = "+pref.getString("id", "")+"");
        builder.setGroupBy("name");
        Backendless.Data.of(Survey.class).find(builder, new AsyncCallback<List<Survey>>() {
            @Override
            public void handleResponse(List<Survey> response) {
                if (response.size() == 0) {
                    onBackPressed();
                    Toasty.info(SurveysActivity.this, "No Surveys!",Toasty.LENGTH_LONG,true).show();
                }
                else {
                    adapter = new SurveyAdapter(response, SurveysActivity.this);
                    rvSurveys.setAdapter(adapter);
                    surveys = response;
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveysActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

    @Override
    public void onSurveyClicked(int position) {
        if (getIntent().getStringExtra("cat").equals("prof")){
            Intent intent = new Intent(this, SurveyDetailsActivity.class);
            intent.putExtra("id", surveys.get(position).getId());
            startActivity(intent);
            this.finish();
        }
        else {
            Intent intent = new Intent(this, SurveyStudentActivity.class);
            intent.putExtra("id", surveys.get(position).getId());
            intent.putExtra("team", team);
            intent.putExtra("dept", dept);
            intent.putExtra("name", surveys.get(position).getName());
            intent.putExtra("profID", surveys.get(position).getProfID());
            intent.putExtra("stuID", getIntent().getStringExtra("stuID"));
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra("cat").equals("prof"))
            startActivity(new Intent(this, ProfessorSurveyActivity.class));
        else
            startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
    }
}