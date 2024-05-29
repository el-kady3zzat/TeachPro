package com.example.tech.professor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Survey;
import com.example.tech.register.RegisterActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SurveyActivity extends AppCompatActivity {

    Spinner spnTeamSurvey, spnDeptSurvey;
    EditText etSurveyName, etSurvey, etAnswer1Survey, etAnswer2Survey, etAnswer3Survey, etAnswer4Survey, etAnswer5Survey;
    ImageView ivSaveSurvey, ivNewSurvey, ivUploadSurvey;
    Survey survey = new Survey();
    List<Survey> surveyList =  new ArrayList<>();
    int id, counter = 0;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        spnTeamSurvey = findViewById(R.id.spnTeamSurvey);
        spnDeptSurvey = findViewById(R.id.spnDeptSurvey);
        etSurveyName = findViewById(R.id.etSurveyName);
        etSurvey = findViewById(R.id.etSurvey);
        etAnswer1Survey = findViewById(R.id.etAnswer1Survey);
        etAnswer2Survey = findViewById(R.id.etAnswer2Survey);
        etAnswer3Survey = findViewById(R.id.etAnswer3Survey);
        etAnswer4Survey = findViewById(R.id.etAnswer4Survey);
        etAnswer5Survey = findViewById(R.id.etAnswer5Survey);
        ivSaveSurvey = findViewById(R.id.ivSaveSurvey);
        ivNewSurvey = findViewById(R.id.ivNewSurvey);
        ivUploadSurvey = findViewById(R.id.ivUploadSurvey);

        String[] teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeamSurvey.setAdapter(teamAdapter);
        RegisterActivity.getDept(this, spnDeptSurvey);

        ivSaveSurvey.setOnClickListener(v -> saveSurvey());
        ivNewSurvey.setOnClickListener(v -> newQuestion());
        ivUploadSurvey.setOnClickListener(v -> uploadSurvey());

        getID();
    }

    private void saveSurvey() {
        if (etSurveyName.getText().toString().equals("") || etSurvey.getText().toString().equals("") || etAnswer1Survey.getText().toString().equals("") || etAnswer2Survey.getText().toString().equals("")){
            Toasty.warning(this, "Please Fill Required Fields First", Toasty.LENGTH_LONG, true).show();
        }
        else {
            survey.setId(id);
            survey.setProfID(pref.getString("id", ""));
            survey.setTeam(spnTeamSurvey.getSelectedItem().toString());
            survey.setDept(spnDeptSurvey.getSelectedItem().toString());
            survey.setName(etSurveyName.getText().toString());
            survey.setSurvey(etSurvey.getText().toString());
            survey.setAnswer1(etAnswer1Survey.getText().toString());
            survey.setAnswer2(etAnswer2Survey.getText().toString());
            if (!etAnswer3Survey.getText().toString().equals(""))
                survey.setAnswer3(etAnswer3Survey.getText().toString());
            if (!etAnswer4Survey.getText().toString().equals(""))
                survey.setAnswer4(etAnswer4Survey.getText().toString());
            if (!etAnswer5Survey.getText().toString().equals(""))
                survey.setAnswer5(etAnswer5Survey.getText().toString());

            surveyList.add(survey);
            survey = new Survey();
            spnTeamSurvey.setEnabled(false);
            spnDeptSurvey.setEnabled(false);
            etSurveyName.setEnabled(false);
            Toasty.success(SurveyActivity.this, "Survey Saved Successfully!", Toast.LENGTH_LONG, true).show();
        }
    }

    private void newQuestion() {
        etSurvey.setText("");
        etAnswer1Survey.setText("");
        etAnswer2Survey.setText("");
        etAnswer3Survey.setText("");
        etAnswer4Survey.setText("");
        etAnswer5Survey.setText("");
    }

    private void getID() {
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create().setProperties("Max(id)");
        Backendless.Data.of("Survey").find(dataQueryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {
                if (response.get(0).get("max") == null)
                    id = 0;
                else
                    id = Integer.parseInt(Objects.requireNonNull(response.get(0).get("max")).toString()) + 1;
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void uploadSurvey() {
        if (surveyList.size() == 0) {
            Toasty.error(SurveyActivity.this, "No Saved Surveys", Toast.LENGTH_LONG, true).show();
        }
        else {
            for (int i = 0; i < surveyList.size(); i++) {
                Backendless.Data.of(Survey.class).save(surveyList.get(i), new BackendlessCallback<Survey>() {
                    @Override
                    public void handleResponse(Survey response) {
                        counter ++;
                        if (counter == surveyList.size()) {
                            Toasty.success(SurveyActivity.this, "Survey Uploaded Successfully!", Toast.LENGTH_LONG, true).show();
                            surveyList.clear();
                            counter = 0;
                            onBackPressed();
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(SurveyActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ProfessorSurveyActivity.class));
        this.finish();
        super.onBackPressed();
    }
}