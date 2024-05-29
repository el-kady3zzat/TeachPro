package com.example.tech.professor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.tech.R;
import com.example.tech.common.FinishedExamActivity;

public class UploadExamActivity extends AppCompatActivity {

    ConstraintLayout conUploadedExams, conNewEXAM, conFINISHED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_exam);

        conNewEXAM = findViewById(R.id.conNEWEXAMUpload);
        conFINISHED = findViewById(R.id.conFINISHEDUpload);
        conUploadedExams = findViewById(R.id.conUploadedExams);

        conNewEXAM.setOnClickListener(v -> newExam());
        conFINISHED.setOnClickListener(v -> finishedExams());
        conUploadedExams.setOnClickListener(v -> uploadedExams());
    }

    private void uploadedExams() {
        startActivity(new Intent(this, ProfessorActivity.class));
        this.finish();
    }

    public void newExam() {
        startActivity(new Intent(this, NewExamActivity.class));
        this.finish();
    }

    public void finishedExams() {
        startActivity(new Intent(this, FinishedExamActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}