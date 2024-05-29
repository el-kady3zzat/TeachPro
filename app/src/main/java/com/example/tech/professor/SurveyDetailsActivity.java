package com.example.tech.professor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Survey;
import com.example.tech.backendless.classes.SurveyAnswers;
import com.example.tech.student.SurveysActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SurveyDetailsActivity extends AppCompatActivity {

    TextView tvSurveyName, tvSurveyQuestion, tvSurveyAnswer1, tvAnswer1Count, tvSurveyAnswer2, tvAnswer2Count,
            tvSurveyAnswer3, tvAnswer3Count, tvSurveyAnswer4, tvAnswer4Count, tvSurveyAnswer5, tvAnswer5Count;
    ConstraintLayout conAns3, conAns4, conAns5;
    Button btnSurveyDetailsNext;
    List<Survey> surveys;
    int i = 0;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_details);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

        tvSurveyName = findViewById(R.id.tvSurveyName);
        tvSurveyQuestion = findViewById(R.id.tvSurveyQuestion);
        tvSurveyAnswer1 = findViewById(R.id.tvSurveyAnswer1);
        tvAnswer1Count = findViewById(R.id.tvAnswer1Count);
        tvSurveyAnswer2 = findViewById(R.id.tvSurveyAnswer2);
        tvAnswer2Count = findViewById(R.id.tvAnswer2Count);
        tvSurveyAnswer3 = findViewById(R.id.tvSurveyAnswer3);
        tvAnswer3Count = findViewById(R.id.tvAnswer3Count);
        tvSurveyAnswer4 = findViewById(R.id.tvSurveyAnswer4);
        tvAnswer4Count = findViewById(R.id.tvAnswer4Count);
        tvSurveyAnswer5 = findViewById(R.id.tvSurveyAnswer5);
        tvAnswer5Count = findViewById(R.id.tvAnswer5Count);
        conAns3 = findViewById(R.id.conAns3);
        conAns4 = findViewById(R.id.conAns4);
        conAns5 = findViewById(R.id.conAns5);
        btnSurveyDetailsNext = findViewById(R.id.btnSurveyDetailsNext);

        btnSurveyDetailsNext.setOnClickListener(v -> next());

        getSurvey();
    }

    private void getSurvey() {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("id = '"+getIntent().getIntExtra("id", 0)+"'");
        Backendless.Data.of(SurveyAnswers.class).find(builder, new AsyncCallback<List<SurveyAnswers>>() {
            @Override
            public void handleResponse(List<SurveyAnswers> response) {
                if (response.size() == 0) {
                    dialog.cancel();
                    onBackPressed();
                    Toasty.info(SurveyDetailsActivity.this, "No Data For Now!",Toasty.LENGTH_LONG,true).show();
                }
                else {
                    builder.setWhereClause("id = '"+getIntent().getIntExtra("id", 0)+"'");
                    Backendless.Data.of(Survey.class).find(builder, new AsyncCallback<List<Survey>>() {
                        @Override
                        public void handleResponse(List<Survey> response) {
                            surveys = response;
                            fillData();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(SurveyDetailsActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(),Toasty.LENGTH_LONG,true).show();
            }
        });
    }

    private void next() {
        if (i == surveys.size() - 1){
            onBackPressed();
            return;
        }
        dialog.show();
        i++;
        tvAnswer1Count.setText("");
        tvAnswer2Count.setText("");
        tvAnswer3Count.setText("");
        tvAnswer4Count.setText("");
        tvAnswer5Count.setText("");
        fillData();
    }

    private void fillData() {
        tvSurveyName.setText(surveys.get(i).getName());
        tvSurveyQuestion.setText(surveys.get(i).getSurvey());
        tvSurveyAnswer1.setText(surveys.get(i).getAnswer1());
        getCount1();
        tvSurveyAnswer2.setText(surveys.get(i).getAnswer2());
        getCount2();
        if (!surveys.get(i).getAnswer3().equals("_")) {
            tvSurveyAnswer3.setText(surveys.get(i).getAnswer3());
            conAns3.setVisibility(View.VISIBLE);
            getCount3();
        }
        else {
            conAns3.setVisibility(View.GONE);
        }
        if (!surveys.get(i).getAnswer4().equals("_")) {
            tvSurveyAnswer4.setText(surveys.get(i).getAnswer4());
            conAns4.setVisibility(View.VISIBLE);
            getCount4();
        }
        else {
            conAns4.setVisibility(View.GONE);
            dialog.cancel();
        }
        if (!surveys.get(i).getAnswer5().equals("_")) {
            tvSurveyAnswer5.setText(surveys.get(i).getAnswer5());
            conAns5.setVisibility(View.VISIBLE);
            getCount5();
        }
        else {
            conAns5.setVisibility(View.GONE);
            dialog.cancel();
        }
    }

    private void getCount1(){
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause("question = '"+surveys.get(i).getSurvey()+"' and answer = '"+surveys.get(i).getAnswer1()+"'");

        Backendless.Data.of(SurveyAnswers.class).getObjectCount(dataQueryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                tvAnswer1Count.setText(String.valueOf(response));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    private void getCount2(){
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause("question = '"+surveys.get(i).getSurvey()+"' and answer = '"+surveys.get(i).getAnswer2()+"'");

        Backendless.Data.of(SurveyAnswers.class).getObjectCount(dataQueryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                tvAnswer2Count.setText(String.valueOf(response));

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    private void getCount3(){
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause("question = '"+surveys.get(i).getSurvey()+"' and answer = '"+surveys.get(i).getAnswer3()+"'");

        Backendless.Data.of(SurveyAnswers.class).getObjectCount(dataQueryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                tvAnswer3Count.setText(String.valueOf(response));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    private void getCount4(){
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause("question = '"+surveys.get(i).getSurvey()+"' and answer = '"+surveys.get(i).getAnswer4()+"'" );

        Backendless.Data.of(SurveyAnswers.class).getObjectCount(dataQueryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                tvAnswer4Count.setText(String.valueOf(response));
                dialog.cancel();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    private void getCount5(){
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause("question = '"+surveys.get(i).getSurvey()+"' and answer = '"+surveys.get(i).getAnswer5()+"'" );

        Backendless.Data.of(SurveyAnswers.class).getObjectCount(dataQueryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                tvAnswer5Count.setText(String.valueOf(response));
                dialog.cancel();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(SurveyDetailsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SurveysActivity.class);
        intent.putExtra("cat", "prof");
        startActivity(intent);
        this.finish();
    }
}