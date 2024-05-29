package com.example.tech.professor;

import static com.example.tech.register.RegisterActivity.getDept;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tech.R;
import com.example.tech.game.GameQuestions;

import es.dmoral.toasty.Toasty;

public class GameQuestionsActivity extends AppCompatActivity {

    GameQuestions gameQuestions = new GameQuestions();
    Button btnGameUpload;
    TextView tvGameAnswers;
    RadioGroup rgLevel, rgType, rgTFAnswers;
    Spinner spnGameTeam, spnGameDept;
    RadioButton rbEasy, rbMedium, rbHard, rbMC, rbTF, rbT, rbF;
    EditText etGameSubject, etGameQuestion, etGameAnswer1, etGameAnswer2, etGameAnswer3, etGameAnswer4, etGameRightAnswer;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_questions);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        initializeViews();

        String[] teamArr = {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnGameTeam.setAdapter(teamAdapter);
        getDept(this, spnGameDept);

        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbMC.getId()){
                tvGameAnswers.setVisibility(View.VISIBLE);
                etGameAnswer1.setVisibility(View.VISIBLE);
                etGameAnswer2.setVisibility(View.VISIBLE);
                etGameAnswer3.setVisibility(View.VISIBLE);
                etGameAnswer4.setVisibility(View.VISIBLE);
                etGameRightAnswer.setVisibility(View.VISIBLE);
                rgTFAnswers.setVisibility(View.GONE);
            }
            else if (checkedId == rbTF.getId()){
                tvGameAnswers.setVisibility(View.GONE);
                etGameAnswer1.setVisibility(View.GONE);
                etGameAnswer2.setVisibility(View.GONE);
                etGameAnswer3.setVisibility(View.GONE);
                etGameAnswer4.setVisibility(View.GONE);
                etGameRightAnswer.setVisibility(View.GONE);
                rgTFAnswers.setVisibility(View.VISIBLE);
            }
        });
        btnGameUpload.setOnClickListener(v -> upload());
    }

    private void initializeViews(){
        rbT = findViewById(R.id.rbT);
        rbF = findViewById(R.id.rbF);
        rbMC = findViewById(R.id.rbMC);
        rbTF = findViewById(R.id.rbTF);
        rgType = findViewById(R.id.rgType);
        rbEasy = findViewById(R.id.rbEasy);
        rbHard = findViewById(R.id.rbHard);
        rgLevel = findViewById(R.id.rgLevel);
        rbMedium = findViewById(R.id.rbMedium);
        spnGameTeam = findViewById(R.id.spnGameTeam);
        spnGameDept = findViewById(R.id.spnGameDept);
        rgTFAnswers = findViewById(R.id.rgTFAnswers);
        etGameSubject = findViewById(R.id.etGameSubject);
        etGameAnswer1 = findViewById(R.id.etGameAnswer1);
        etGameAnswer2 = findViewById(R.id.etGameAnswer2);
        etGameAnswer3 = findViewById(R.id.etGameAnswer3);
        etGameAnswer4 = findViewById(R.id.etGameAnswer4);
        btnGameUpload = findViewById(R.id.btnGameUpload);
        tvGameAnswers = findViewById(R.id.tvGameAnswers);
        etGameQuestion = findViewById(R.id.etGameQuestion);
        etGameRightAnswer = findViewById(R.id.etGameRightAnswer);
    }

    private void upload() {
        if (spnGameTeam.getSelectedItem().toString().equals("")||spnGameTeam.getSelectedItem().toString().equals("")
        ||etGameQuestion.getText().toString().equals("")||(!rbEasy.isChecked()&&!rbMedium.isChecked()&&!rbHard.isChecked())
        ||(!rbMC.isChecked()&&!rbTF.isChecked())||(etGameRightAnswer.getText().toString().equals("")&&!rbT.isChecked()&& !rbF.isChecked())) {
            Toasty.warning(this, "Please Fill Required Fields First", Toasty.LENGTH_LONG, true).show();
            return;
        }

        gameQuestions = new GameQuestions();
        gameQuestions.setTeam(spnGameTeam.getSelectedItem().toString());
        gameQuestions.setDept(spnGameDept.getSelectedItem().toString());
        gameQuestions.setSubject(etGameSubject.getText().toString());

        if (rbEasy.isChecked())
            gameQuestions.setLevel("easy");
        else if (rbMedium.isChecked())
            gameQuestions.setLevel("medium");
        else
            gameQuestions.setLevel("hard");

        if (rbMC.isChecked()){
            gameQuestions.setQuestionType("mc");
            gameQuestions.setRightAnswer(etGameRightAnswer.getText().toString());
            gameQuestions.setAnswer1(etGameAnswer1.getText().toString());
            gameQuestions.setAnswer2(etGameAnswer2.getText().toString());
            gameQuestions.setAnswer3(etGameAnswer3.getText().toString());
            gameQuestions.setAnswer4(etGameAnswer4.getText().toString());
        }
        else{
            gameQuestions.setQuestionType("tf");
            gameQuestions.setAnswer1("True");
            gameQuestions.setAnswer2("False");
            if (rbT.isChecked())
                gameQuestions.setRightAnswer("true");
            else
                gameQuestions.setRightAnswer("False");
            }

        gameQuestions.setQuestion(etGameQuestion.getText().toString());
        gameQuestions.setProfID(pref.getString("id",""));

        Backendless.Data.of(GameQuestions.class).save(gameQuestions, new AsyncCallback<GameQuestions>() {
            @Override
            public void handleResponse(GameQuestions response) {
                Toasty.success(GameQuestionsActivity.this, "Question Uploaded Successfully!", Toasty.LENGTH_LONG, true).show();
                etGameQuestion.setText("");
                etGameAnswer1.setText("");
                etGameAnswer2.setText("");
                etGameAnswer3.setText("");
                etGameAnswer4.setText("");
                etGameRightAnswer.setText("");
                rgTFAnswers.clearCheck();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(GameQuestionsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}