package com.example.tech.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.tech.R;
import com.example.tech.game.GameActivity;
import com.example.tech.game.GameDataActivity;
import com.example.tech.professor.UploadContentActivity;
import com.example.tech.register.LoginActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class StudentMainActivity extends AppCompatActivity {

    ConstraintLayout conContent, conExams, conActivity, conSurveyUpload, conContact, conProfile, conBadges, conWinners, conUpload, conPlay, conLOGOUT;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        conContent = findViewById(R.id.conContentStu);
        conExams = findViewById(R.id.conExamsUploadStu);
        conLOGOUT = findViewById(R.id.conLOGUploadStu);
        conActivity = findViewById(R.id.conActivityUploadStu);
        conSurveyUpload = findViewById(R.id.conSurveyUploadStu);
        conContact = findViewById(R.id.conContactStu);
        conProfile = findViewById(R.id.conProfile);
        conWinners = findViewById(R.id.conWinners);
        conPlay = findViewById(R.id.conPlay);
        conUpload = findViewById(R.id.conUpload);
        conBadges = findViewById(R.id.conBadges);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        conContent.setOnClickListener(v -> content());
        conExams.setOnClickListener(v -> exams());
        conActivity.setOnClickListener(v -> activity());
        conSurveyUpload.setOnClickListener(v -> survey());
        conLOGOUT.setOnClickListener(v -> logout());
        conContact.setOnClickListener(v -> contact());
        conProfile.setOnClickListener(v -> profile());
        conWinners.setOnClickListener(v -> winners());
        conPlay.setOnClickListener(v -> play());
        conUpload.setOnClickListener(v -> upload());
        conBadges.setOnClickListener(v -> badges());
    }

    private void profile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id", pref.getString("id", ""));
        startActivity(intent);
        this.finish();
    }

    private void content() {
        startActivity(new Intent(this, StudentContentActivity.class));
        this.finish();
    }

    private void exams() {
        startActivity(new Intent(this, ExamsStudentActivity.class));
        this.finish();
    }

    private void activity() {
        Intent intent = new Intent(this, ActivityStudentActivity.class);
        intent.putExtra("team", pref.getString("team", ""));
        intent.putExtra("dept", pref.getString("dept", ""));
        startActivity(intent);
        this.finish();
    }

    private void survey() {
        Intent intent = new Intent(this, SurveysActivity.class);
        intent.putExtra("team", pref.getString("team", ""));
        intent.putExtra("dept", pref.getString("dept", ""));
        intent.putExtra("stuID", pref.getString("id", ""));
        intent.putExtra("cat", "stu");
        startActivity(intent);
        this.finish();
    }

    private void contact() {
        String url = "https://api.whatsapp.com/send?phone="+"201026141278";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void winners() {
        startActivity(new Intent(this, WinnersActivity.class));
        this.finish();
    }

    private void badges() {
        startActivity(new Intent(this, BadgesActivity.class));
        this.finish();
    }

    private void play() {
        startActivity(new Intent(this, GameDataActivity.class));
        this.finish();
    }

    private void upload() {
        Intent in = new Intent(this, UploadFilesActivity.class);
        startActivity(in);
        this.finish();
    }

    private void logout() {
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