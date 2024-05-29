package com.example.tech.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Survey;
import com.example.tech.backendless.classes.SurveyAnswers;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SurveyStudentActivity extends AppCompatActivity {

    TextView tvSurvey;
    RadioGroup RGmcSurvey;
    RadioButton rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4, rbAnswer5;
    Button btnSurveyNext;
    List<Survey> surveys;
    int counter = 1;
    ArrayList<String> Questions = new ArrayList<>();
    ArrayList<String> Answers = new ArrayList<>();
    int id;
    String name, team, dept, profID, stuID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_student);

        tvSurvey = findViewById(R.id.tvSurvey);
        RGmcSurvey = findViewById(R.id.RGmcSurvey);
        rbAnswer1 = findViewById(R.id.rbAnswer1);
        rbAnswer2 = findViewById(R.id.rbAnswer2);
        rbAnswer3 = findViewById(R.id.rbAnswer3);
        rbAnswer4 = findViewById(R.id.rbAnswer4);
        rbAnswer5 = findViewById(R.id.rbAnswer5);
        btnSurveyNext = findViewById(R.id.btnSurveyNext);

        getSurvey();

        btnSurveyNext.setOnClickListener(v -> next());

        pref = getSharedPreferences("userData", MODE_PRIVATE);
    }

    private void next() {
        if (!rbAnswer1.isChecked() && !rbAnswer2.isChecked() && !rbAnswer3.isChecked() && !rbAnswer4.isChecked() && !rbAnswer5.isChecked()){
            Toasty.warning(this, "Please Choose Answer First",Toasty.LENGTH_LONG, true).show();
            return;
        }
        else {
            Questions.add(tvSurvey.getText().toString());
            if (rbAnswer1.isChecked())
                Answers.add(rbAnswer1.getText().toString());
            else if (rbAnswer2.isChecked())
                Answers.add(rbAnswer2.getText().toString());
            else if (rbAnswer3.isChecked())
                Answers.add(rbAnswer3.getText().toString());
            else if (rbAnswer4.isChecked())
                Answers.add(rbAnswer4.getText().toString());
            else
                Answers.add(rbAnswer5.getText().toString());
        }

        if (counter == surveys.size())
            save();
        else {
            RGmcSurvey.clearCheck();
            tvSurvey.setText(surveys.get(counter).getSurvey());
            rbAnswer1.setText(surveys.get(counter).getAnswer1());
            rbAnswer2.setText(surveys.get(counter).getAnswer2());
            if (surveys.get(counter).getAnswer3().equals(""))
                rbAnswer3.setVisibility(View.GONE);
            rbAnswer3.setText(surveys.get(counter).getAnswer3());
            if (surveys.get(counter).getAnswer4().equals(""))
                rbAnswer4.setVisibility(View.GONE);
            rbAnswer4.setText(surveys.get(counter).getAnswer4());
            if (surveys.get(counter).getAnswer5().equals(""))
                rbAnswer5.setVisibility(View.GONE);
            rbAnswer5.setText(surveys.get(counter).getAnswer5());

            counter++;
        }
    }

    private void save() {
        id = getIntent().getIntExtra("id",0);
        name = getIntent().getStringExtra("name");
        team = getIntent().getStringExtra("team");
        dept = getIntent().getStringExtra("dept");
        profID = getIntent().getStringExtra("profID");
        stuID = getIntent().getStringExtra("stuID");
        SurveyAnswers answers = new SurveyAnswers();
        for (int i = 0; i < Questions.size(); i++) {
            answers.setId(String.valueOf(id));
            answers.setName(name);
            answers.setTeam(team);
            answers.setDept(dept);
            answers.setProfID(profID);
            answers.setStuID(stuID);
            answers.setQuestion(Questions.get(i));
            answers.setAnswer(Answers.get(i));
            Backendless.Data.of(SurveyAnswers.class).save(answers, new AsyncCallback<SurveyAnswers>() {
                @Override
                public void handleResponse(SurveyAnswers response) {
                    if (counter == Questions.size()){
                        Toasty.success(SurveyStudentActivity.this, "Answers Saved Successfully", Toasty.LENGTH_LONG, true).show();
                        startActivity(new Intent(SurveyStudentActivity.this, StudentMainActivity.class));
                        editor = pref.edit();
                        editor.putInt("surveys", (pref.getInt("surveys",0) + 1));
                        editor.apply();
                        SurveyStudentActivity.this.finish();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });
        }
    }

    private void getSurvey(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("id = '"+getIntent().getStringExtra("id")+"'");
        builder.setWhereClause("team = '"+getIntent().getStringExtra("team")+"'");
        builder.setWhereClause("dept = '"+getIntent().getStringExtra("dept")+"'");
        builder.setWhereClause("name = '"+getIntent().getStringExtra("name")+"'");
        Backendless.Data.of(Survey.class).find(builder, new AsyncCallback<List<Survey>>() {
            @Override
            public void handleResponse(List<Survey> response) {
                surveys = response;
                tvSurvey.setText(surveys.get(0).getSurvey());
                rbAnswer1.setText(surveys.get(0).getAnswer1());
                rbAnswer2.setText(surveys.get(0).getAnswer2());
                rbAnswer3.setText(response.get(0).getAnswer3());
                rbAnswer4.setText(response.get(0).getAnswer4());
                if (!response.get(0).getAnswer4().equals(" ")){
                    rbAnswer3.setText(response.get(0).getAnswer3());
                    rbAnswer3.setVisibility(View.VISIBLE);
                }
                if (!response.get(0).getAnswer4().equals(" ")) {
                    rbAnswer4.setText(response.get(0).getAnswer4());
                    rbAnswer4.setVisibility(View.VISIBLE);
                }
                if (!response.get(0).getAnswer5().equals(" ")) {
                    rbAnswer5.setText(response.get(0).getAnswer5());
                    rbAnswer5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

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