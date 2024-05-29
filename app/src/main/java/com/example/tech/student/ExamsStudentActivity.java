package com.example.tech.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.tech.R;
import com.example.tech.common.FinishedExamActivity;

public class ExamsStudentActivity extends AppCompatActivity {

    ConstraintLayout conExams, conFinishedExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_student);

        conExams = findViewById(R.id.conExams);
        conFinishedExams = findViewById(R.id.conFinishedExams);

        conExams.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentActivity.class);
            startActivity(intent);
            this.finish();
        });
        conFinishedExams.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinishedExamActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StudentMainActivity.class);
        startActivity(intent);
        this.finish();
    }
}