package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvQuestion;
    Spinner spnAnswer;
    Button start, next;
    byte i, score;
    String[] countries = {"Egypt", "UK", "USA", "d", "e", "f", "g"};
    String[] capitals = {"Cairo", "London", "WS", "Damascus", "Beiging", "Khartoum", "Toronto"};
    HashSet<String> cap = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvQuestion = findViewById(R.id.tvQuestion);
        spnAnswer = findViewById(R.id.spnAnswer);
        start = findViewById(R.id.start);
        next = findViewById(R.id.next);
        next.setEnabled(false);
    }

    public void start(View view) {
        i = 0;
        score = 0;
        next.setEnabled(true);
        spnAnswer.setEnabled(true);
        tvQuestion.setText("What Is The Capital Of " + countries[i]);
        start.setEnabled(false);
    }

    public void next(View view) {
        String answer = spnAnswer.getSelectedItem().toString();
        if (answer.equals("Please Select")){
            Toast.makeText(this, "Please Enter Answer", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (cap.contains(answer)){
            Toast.makeText(this, "Choose Another Answer ", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (answer.equals(capitals[i])){
            score++;
            cap.add(capitals[i]);
        }

        i++;
        if (i < 4)
           tvQuestion.setText("What Is The Capital Of " + countries[i]);
        else{
            next.setEnabled(false);
            start.setEnabled(true);
            Toast.makeText(this, "Score is " + score, Toast.LENGTH_SHORT).show();
            answer = ("Please select");
      