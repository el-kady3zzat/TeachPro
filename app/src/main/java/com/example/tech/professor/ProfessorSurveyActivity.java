package com.example.tech.professor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.tech.R;
import com.example.tech.student.SurveysActivity;

public class ProfessorSurveyActivity extends AppCompatActivity {

    ConstraintLayout conProfSurvey, conProfSurveyCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_survey);

        conProfSurvey = findViewById(R.id.conProfSurvey);
        conProfSurveyCreate = findViewById(R.id.conProfSurveyCreate);

        conProfSurvey.setOnClickListener(v -> {
            Intent intent = new Intent(this, SurveysActivity.class);
            intent.putExtra("cat", "prof");
            startActivity(intent);
            this.finish();
        });
        conProfSurveyCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, SurveyActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}