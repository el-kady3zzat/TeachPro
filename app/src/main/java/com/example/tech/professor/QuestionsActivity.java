package com.example.tech.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Exam;
import com.example.tech.backendless.classes.Questions;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class QuestionsActivity extends AppCompatActivity {

    SharedPreferences pref, examPref;
    SharedPreferences.Editor editor;
    EditText etQuestion,etAnswer1,etAnswer2,etAnswer3,etAnswer4,etRightAnswer;
    TextView tvQuesNum, tvAnswers;
    String id;
    String type,quesNum;
    byte i;
    byte counter = 1;
    boolean edit;
    int examID;
    int currentQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer1 = findViewById(R.id.etAnswer1);
        etAnswer2 = findViewById(R.id.etAnswer2);
        etAnswer3 = findViewById(R.id.etAnswer3);
        etAnswer4 = findViewById(R.id.etAnswer4);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        tvQuesNum = findViewById(R.id.tvQuesNum);
        tvAnswers = findViewById(R.id.tvAnswers);

        BoomMenuButton bmb = findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if (i == 0){
                HamButton.Builder builder = new HamButton.Builder()
                        .normalColor(R.color.main)
                        .imagePadding(new Rect(5, 5, 5, 5))
                        .normalImageRes(R.drawable.ic_next)
                        .shadowEffect(true)
                        .normalTextRes(R.string.Next)
                        .listener(index -> next());
                bmb.addBuilder(builder);
            }
        }

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id", "");
        examPref = getSharedPreferences("examData", MODE_PRIVATE);
        type = examPref.getString("type", "");
        quesNum = examPref.getString("quesNum", "0");

        if (type.equals("True and False")){
            tvAnswers.setVisibility(View.GONE);
            etAnswer1.setVisibility(View.GONE);
            etAnswer2.setVisibility(View.GONE);
            etAnswer3.setVisibility(View.GONE);
            etAnswer4.setVisibility(View.GONE);
            etRightAnswer.setHint("True or False");
        }

        tvQuesNum.append("Question: " + counter + " of " + quesNum);
        i = Byte.parseByte(quesNum);

        edit = getIntent().getBooleanExtra("edit", false);
        if (edit) {
            examID = getIntent().getIntExtra("examID", 0);
            type = getIntent().getStringExtra("type");
            ImageView ivSaveQuestion = findViewById(R.id.ivSaveQuestion);
            TextView tvSaveQuestion = findViewById(R.id.tvSaveQuestion);

            ivSaveQuestion.setVisibility(View.VISIBLE);
            tvSaveQuestion.setVisibility(View.VISIBLE);
            ivSaveQuestion.setOnClickListener(v -> update(currentQuestion));
            edit();
        }
    }

    List<Questions> data;

    private void edit() {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("examID = "+examID+"");
        Backendless.Data.of(Questions.class).find(builder, new BackendlessCallback<List<Questions>>() {
            @Override
            public void handleResponse(List<Questions> response) {
                data = response;
                fillIfEdit();
            }
        });
    }

    public void next() {
        if (edit){
            currentQuestion++;
            if (etQuestion.getText().toString().equals("") || etRightAnswer.getText().toString().equals("")) {
                Toasty.warning(this, "Fill Required Fields First!", Toast.LENGTH_LONG, true).show();
            }
            else {

                if (type.equals("True and False")){
                    etQuestion.setText(data.get(counter-1).getQuestion());
                    etRightAnswer.setText(data.get(counter-1).getRightAnswer());
                    counter++;
                }
                else {
                    etQuestion.setText(data.get(counter-1).getQuestion());
                    etAnswer1.setText(data.get(counter-1).getAnswer1());
                    etAnswer2.setText(data.get(counter-1).getAnswer2());
                    etAnswer3.setText(data.get(counter-1).getAnswer3());
                    etAnswer4.setText(data.get(counter-1).getAnswer4());
                    etRightAnswer.setText(data.get(counter-1).getRightAnswer());
                    counter++;
                }

                Toasty.success(this, "Question Edited Successfully!", Toast.LENGTH_SHORT, true).show();

                if (counter > data.size()){
                    Intent in = new Intent(this, ProfessorActivity.class);
                    startActivity(in);
                    this.finish();
                }
                else {
                    tvQuesNum.setText("");
                    tvQuesNum.append("Question: " + counter + " of " + quesNum);
                    fillIfEdit();
                }
            }
        }

        else {
            if (type.equals("True and False")) {
                if (etQuestion.getText().toString().equals("") || etRightAnswer.getText().toString().equals("")) {
                    Toasty.warning(this, "Fill Required Fields First!", Toast.LENGTH_LONG, true).show();
                    return;
                }
                editor = examPref.edit();
                editor.putString("ques" + counter + "", etQuestion.getText().toString());
            } else {
                if (etQuestion.getText().toString().equals("") || etAnswer1.getText().toString().equals("") || etAnswer2.getText().toString().equals("") || etRightAnswer.getText().toString().equals("")) {
                    Toasty.warning(this, "Fill Required Fields First!", Toast.LENGTH_LONG, true).show();
                    return;
                }
                editor = examPref.edit();
                editor.putString("ques" + counter + "", etQuestion.getText().toString());
                editor.putString("answer1" + counter + "", etAnswer1.getText().toString());
                editor.putString("answer2" + counter + "", etAnswer2.getText().toString());
                editor.putString("answer3" + counter + "", etAnswer3.getText().toString());
                editor.putString("answer4" + counter + "", etAnswer4.getText().toString());
            }

            editor.putString("rightAnswer" + counter + "", etRightAnswer.getText().toString());
            editor.apply();

            counter++;
            tvQuesNum.setText("");
            Toasty.success(this, "Question Added Successfully!", Toast.LENGTH_SHORT, true).show();

            if (counter > i) {
                Intent in = new Intent(this, ExamActivity.class);
                startActivity(in);
                this.finish();
            } else {
                tvQuesNum.append("Question: " + counter + " of " + quesNum);
                etQuestion.setText("");
                etAnswer1.setText("");
                etAnswer2.setText("");
                etAnswer3.setText("");
                etAnswer4.setText("");
                etRightAnswer.setText("");
            }
        }

    }

    private void fillIfEdit(){
        if (type.equals("True and False")){
            etQuestion.setText(data.get(counter-1).getQuestion());
            etRightAnswer.setText(data.get(counter-1).getRightAnswer());
        }
        else {
            etQuestion.setText(data.get(counter-1).getQuestion());
            etAnswer1.setText(data.get(counter-1).getAnswer1());
            etAnswer2.setText(data.get(counter-1).getAnswer2());
            etAnswer3.setText(data.get(counter-1).getAnswer3());
            etAnswer4.setText(data.get(counter-1).getAnswer4());
            etRightAnswer.setText(data.get(counter-1).getRightAnswer());
        }
    }

    private void update(int currentQuestion){
        Map<String, Object> questionsChanges = new HashMap<>();
                questionsChanges.put("question", etQuestion.getText().toString());
                questionsChanges.put("rightAnswer", etRightAnswer.getText().toString());

                if (type.equals("True and False")){
                    questionsChanges.put("Answer1", "True");
                    questionsChanges.put("Answer2", "False");
                }
                else{
                    questionsChanges.put("Answer1", etAnswer1.getText().toString());
                    questionsChanges.put("Answer2", etAnswer2.getText().toString());
                    questionsChanges.put("Answer3", etAnswer3.getText().toString());
                    questionsChanges.put("Answer4", etAnswer4.getText().toString());
                }

                Backendless.Data.of(Questions.class).update("objectId = '"+data.get(currentQuestion).getObjectId()+"' and examID = "+examID+"", questionsChanges, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                            Toasty.success(QuestionsActivity.this, "Question Updated Successfully! ", Toast.LENGTH_LONG, true).show();
                            if (counter > data.size()) {
                                Intent in = new Intent(QuestionsActivity.this, ProfessorActivity.class);
                                startActivity(in);
                                QuestionsActivity.this.finish();
                            }
                        }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(QuestionsActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProfessorActivity.class);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }
}