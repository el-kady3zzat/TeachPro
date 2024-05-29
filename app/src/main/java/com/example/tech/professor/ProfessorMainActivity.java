package com.example.tech.professor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tech.R;
import com.example.tech.register.LoginActivity;

public class ProfessorMainActivity extends AppCompatActivity {

    ConstraintLayout conContent, conExams, conActivity, conSurveyUpload, conContact, conLOGOUT, conGame, conStudentsFiles, conPoints;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_main);

        conContent = findViewById(R.id.conContent);
        conExams = findViewById(R.id.conExamsUpload);
        conLOGOUT = findViewById(R.id.conLOGUpload);
        conActivity = findViewById(R.id.conActivityUpload);
        conSurveyUpload = findViewById(R.id.conSurveyUpload);
        conContact = findViewById(R.id.conContact);
        conGame = findViewById(R.id.conGame);
        conStudentsFiles = findViewById(R.id.conStudentsFiles);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        conContent.setOnClickListener(v -> content());
        conExams.setOnClickListener(v -> exams());
        conActivity.setOnClickListener(v -> activity());
        conSurveyUpload.setOnClickListener(v -> surveyUpload());
        conLOGOUT.setOnClickListener(v -> logout());
        conContact.setOnClickListener(v -> contact());
        conGame.setOnClickListener(v -> game());
        conStudentsFiles.setOnClickListener(v -> studentsFiles());

        findViewById(R.id.conPoints).setOnClickListener(v -> {
            startActivity(new Intent(this, PointsActivity.class));
            this.finish();
        });
    }

    private void contact() {
        String url = "https://api.whatsapp.com/send?phone="+"201026141278";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void surveyUpload() {
        startActivity(new Intent(this, ProfessorSurveyActivity.class));
        this.finish();
    }

    private void activity() {
        startActivity(new Intent(this, ActivityActivity.class));
        this.finish();
    }

    private void content() {
        Intent in = new Intent(this, UploadContentActivity.class);
        startActivity(in);
        this.finish();
    }

    private void exams() {
        Intent in = new Intent(this, UploadExamActivity.class);
        startActivity(in);
        this.finish();
    }

    private void game() {
        Intent in = new Intent(this, GameQuestionsActivity.class);
        startActivity(in);
        this.finish();
    }

    private void studentsFiles() {
        Intent in = new Intent(this, StudentsFilesActivity.class);
        startActivity(in);
        this.finish();
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want Logout?");
        builder.setTitle("LOGOUT");
        builder.setPositiveButton("YES", (dialog, which) -> {
            Intent in = new Intent(this, LoginActivity.class);
            SharedPreferences.Editor editor;
            editor = pref.edit();
            editor.clear();
            editor.apply();
            startActivity(in);
            this.finish();
        }).setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        super.onBackPressed();
    }
}