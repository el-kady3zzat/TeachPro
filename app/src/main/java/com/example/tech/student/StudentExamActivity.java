package com.example.tech.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Questions;
import com.example.tech.backendless.classes.Result;
import com.example.tech.backendless.classes.Student;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class StudentExamActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView tvQuestionTrueFalse;
    TextView tvQuesNum;
    RadioGroup RGtfS, RGmcS;
    RadioButton rbTrue, rbFalse, rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;

    ConstraintLayout conTrueFalse, conMultiChoice;
    String id;
    int examID, i = 0;
    boolean firstTime = true;
    String type, quesNum, timeRange, examDate, subject, rightAnswer, stuName, timeType;
    Result result = new Result();

    long START_TIME;
    CountDownTimerView Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_exam);

        tvQuestionTrueFalse = findViewById(R.id.tvQuestionTrueFalse);
        rbTrue = findViewById(R.id.rbTrue);
        rbFalse = findViewById(R.id.rbFalse);
        rbAnswer1 = findViewById(R.id.rbAnswer1);
        rbAnswer2 = findViewById(R.id.rbAnswer2);
        rbAnswer3 = findViewById(R.id.rbAnswer3);
        rbAnswer4 = findViewById(R.id.rbAnswer4);
        conTrueFalse = findViewById(R.id.conTrueFalse);
        conMultiChoice = findViewById(R.id.conMultiChoice);
        tvQuesNum = findViewById(R.id.tvQuesNum);
        RGmcS = findViewById(R.id.RGmcS);
        RGtfS = findViewById(R.id.RGtfS);

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

        Time = findViewById(R.id.mCountDownTimer);
        rbTrue.setVisibility(View.INVISIBLE);
        rbFalse.setVisibility(View.INVISIBLE);
        rbAnswer1.setVisibility(View.INVISIBLE);
        rbAnswer2.setVisibility(View.INVISIBLE);
        rbAnswer3.setVisibility(View.INVISIBLE);
        rbAnswer4.setVisibility(View.INVISIBLE);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id", "");
        type = getIntent().getStringExtra("type");
        assert type != null;
        if (type.equals("True and False")) conMultiChoice.setVisibility(View.GONE);
        else conTrueFalse.setVisibility(View.GONE);
        examID = getIntent().getIntExtra("examID", 0);
        subject = getIntent().getStringExtra("subject");
        quesNum = getIntent().getStringExtra("quesNum");
        timeRange = getIntent().getStringExtra("time");
        examDate = getIntent().getStringExtra("examDate");
        timeType = getIntent().getStringExtra("timeType");
        START_TIME = Integer.parseInt(timeRange) * 60000L;

        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("id='"+id+"'");
        Backendless.Data.of(Student.class).find(builder, new AsyncCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                stuName = response.get(0).getName();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });

        getQues(i);
        Time.start(Integer.parseInt(timeRange) * 60000L);
        if (timeType.equals("Minutes For Whole Exam")) {
            Time.setOnTimeFinish(() -> {
                Toasty.info(StudentExamActivity.this, "Time Out", Toast.LENGTH_LONG).show();
                if (firstTime) {
                    result.setResult(0);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                            startActivity(in);
                            StudentExamActivity.this.finish();
                        }
                    });
                }
                else {
                    Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                    startActivity(in);
                    StudentExamActivity.this.finish();
                }
                Time.failure();
            });
        }
        else{
            Time.setOnTimeFinish(() -> {
                if (i == Integer.parseInt(quesNum)){
                    Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                    startActivity(in);
                    Time.stop();
                    StudentExamActivity.this.finish();
                }
                if (firstTime) {
                    result.setResult(0);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                            getQues(i=i+1);

                        }
                    });
                }
                else {
                    getQues(i++);
                }
                Time.failure();
            });
        }
    }

    public void next() {
        if (type.equals("True and False")) {
            if (!rbTrue.isChecked() && !rbFalse.isChecked()) {
                Toasty.warning(this, "Choose Answer First", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            if (!rbAnswer1.isChecked() && !rbAnswer2.isChecked() && !rbAnswer3.isChecked() && !rbAnswer4.isChecked()) {
                Toasty.warning(this, "Choose Answer First", Toast.LENGTH_LONG).show();
                return;
            }
        }
//        explode();
        setRightAnswer(rightAnswer);
        i++;
        getQues(i);
        RGtfS.clearCheck();
        RGmcS.clearCheck();
        rbAnswer1.setChecked(false);
        rbAnswer2.setChecked(false);
        rbAnswer3.setChecked(false);
        rbAnswer4.setChecked(false);
        rbTrue.setChecked(false);
        rbFalse.setChecked(false);
        if (!timeType.equals("Minutes For Whole Exam")){
            Time.stop();
            Time.start(Integer.parseInt(timeRange) * 60000L);
        }
    }

    public void getQues(int i){
        if ((i) == (Byte.parseByte(quesNum))) {
            countDownTimer = new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {}

                public void onFinish() {
                    countDownTimer.cancel();
                    finalResult();
                    Toasty.success(StudentExamActivity.this, "Exam Finished", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                    startActivity(in);
                    Time.stop();
                    StudentExamActivity.this.finish();
                }
            }.start();
        }
        else {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setWhereClause("examID = '" + examID + "'");
            Backendless.Data.of(Questions.class).find(builder, new AsyncCallback<List<Questions>>() {
                @Override
                public void handleResponse(List<Questions> response) {
                    rightAnswer = response.get(i).getRightAnswer();
                    if (type.equals("True and False")){
                        tvQuestionTrueFalse.setText(response.get(i).getQuestion());
                        tvQuesNum.setText(String.valueOf(i + 1));
                        rbTrue.setVisibility(View.VISIBLE);
                        rbFalse.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvQuestionTrueFalse.setText(response.get(i).getQuestion());
                        tvQuesNum.setText(String.valueOf(i + 1));
                        rbAnswer1.setText(response.get(i).getAnswer1());
                        rbAnswer2.setText(response.get(i).getAnswer2());
                        rbAnswer1.setVisibility(View.VISIBLE);
                        rbAnswer2.setVisibility(View.VISIBLE);
                        if (response.get(i).getAnswer3().equals("")){
                            rbAnswer3.setVisibility(View.GONE);
                        } else{
                            rbAnswer3.setText(response.get(i).getAnswer3());
                            rbAnswer3.setVisibility(View.VISIBLE);
                        }
                        if (response.get(i).getAnswer4().equals("")) {
                            rbAnswer4.setVisibility(View.GONE);
                        }
                        else{
                            rbAnswer4.setText(response.get(i).getAnswer4());
                            rbAnswer4.setVisibility(View.VISIBLE);
                        }
                    }
                    if (timeType.equals("Minute For Every Question")){
                        Time.start(Integer.parseInt(timeRange) * 60000L);
                    }
                }
                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(StudentExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    int res;
    public void setRightAnswer(String rightAnswer) {
        if (type.equals("True and False")) {
            if ((rightAnswer.equalsIgnoreCase("True") && rbTrue.isChecked()) || (rightAnswer.equalsIgnoreCase("False") && rbFalse.isChecked())) {
                if (firstTime) {
                    result.setResult(1);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    result.setTeam(pref.getString("team", ""));
                    result.setDept(pref.getString("dept", ""));
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                        }
                    });
                    firstTime = false;
                }
                else {
                    DataQueryBuilder Builder = DataQueryBuilder.create().setProperties("Max(result)");
                    Builder.setWhereClause("stuID='"+id+"' and examID='"+examID+"'");
                    Backendless.Data.of("Result").find(Builder, new AsyncCallback<List<Map>>() {
                        @Override
                        public void handleResponse(List<Map> response) {
                            res = Integer.parseInt(String.valueOf(response.get(0).get("max"))) + 1;
                            Map<String, Object> changes = new HashMap<>();
                            changes.put("result", res);
                            Backendless.Data.of("Result").update("subject='" + subject + "' and stuID='" + id + "'", changes, new AsyncCallback<Integer>() {
                                @Override
                                public void handleResponse(Integer response) {
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toasty.error(StudentExamActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(StudentExamActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                if (firstTime) {
                    result.setResult(0);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    result.setTeam(pref.getString("team", ""));
                    result.setDept(pref.getString("dept", ""));
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                        }
                    });
                    firstTime = false;
                }
            }
        }
        else {
            if ((rightAnswer.equals(rbAnswer1.getText().toString()) && rbAnswer1.isChecked()) ||
                    (rightAnswer.equals(rbAnswer2.getText().toString()) && rbAnswer2.isChecked()) ||
                    (rightAnswer.equals(rbAnswer3.getText().toString()) && rbAnswer3.isChecked()) ||
                    (rightAnswer.equals(rbAnswer4.getText().toString()) && rbAnswer4.isChecked())) {
                if (firstTime) {
                    result.setResult(1);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    result.setTeam(pref.getString("team", ""));
                    result.setDept(pref.getString("dept", ""));
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                        }
                    });
                    firstTime = false;
                }
                else {
                    DataQueryBuilder Builder = DataQueryBuilder.create().setProperties("Max(result)");
                    Builder.setWhereClause("stuID='"+id+"' and examID='"+examID+"'");
                    Backendless.Data.of("Result").find(Builder, new AsyncCallback<List<Map>>() {
                        @Override
                        public void handleResponse(List<Map> response) {
                            res = Integer.parseInt(String.valueOf(response.get(0).get("max"))) + 1;
                            Map<String, Object> changes = new HashMap<>();
                            changes.put("result", res);
                            Backendless.Data.of("Result").update("subject='" + subject + "' and stuID='" + id + "'", changes, new AsyncCallback<Integer>() {
                                @Override
                                public void handleResponse(Integer response) {

                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                }
                            });
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(StudentExamActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                if (firstTime) {
                    result.setResult(0);
                    result.setExamDate(examDate);
                    result.setStuID(String.valueOf(id));
                    result.setSubject(subject);
                    result.setExamID(examID);
                    result.setStuName(stuName);
                    result.setTeam(pref.getString("team", ""));
                    result.setDept(pref.getString("dept", ""));
                    Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                        @Override
                        public void handleResponse(Result response) {
                            Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                    firstTime = false;
                }

            }
        }
    }

    int finalResult = 0;
    private void finalResult(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("stuID = "+id+"");
        builder.setPageSize(100);
        Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
            @Override
            public void handleResponse(List<Result> response) {
                for (int j = 0; j < response.size(); j++) {
                    finalResult = response.get(0).getFinalResult() + response.get(j).getResult();
                    if (j == response.size() - 1){
                        updateFinalResult(finalResult);
                    }

                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(StudentExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    private void updateFinalResult(int finalResult){
        Map<String, Object> changes = new HashMap<>();
        changes.put("finalResult", finalResult);
        Backendless.Data.of("Result").update("stuID='" + id + "'", changes, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                badges();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(StudentExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();

            }
        });
    }

    private void badges(){
        editor = pref.edit();
        editor.putInt("exams", (pref.getInt("exams",0) + 1));
        editor.apply();
    }
    CountDownTimer countDownTimer;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentExamActivity.this);
        builder.setMessage("If You Exit The Exam, You Can't Open It Again!");
        builder.setTitle("Exit");
        builder.setPositiveButton("Exit", (dialog, which) -> {
            if (firstTime) {
                result.setResult(0);
                result.setExamDate(examDate);
                result.setStuID(String.valueOf(id));
                result.setSubject(subject);
                result.setExamID(examID);
                result.setStuName(stuName);
                result.setTeam(pref.getString("team", ""));
                result.setDept(pref.getString("dept", ""));
                Backendless.Data.of(Result.class).save(result, new BackendlessCallback<Result>() {
                    @Override
                    public void handleResponse(Result response) {
                        Toasty.success(StudentExamActivity.this, "Result Saved", Toast.LENGTH_LONG).show();
                        finalResult();
                        Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                        startActivity(in);
                        Time.stop();
                        StudentExamActivity.this.finish();
                    }
                });
            }
            else {
                Intent in = new Intent(StudentExamActivity.this, StudentActivity.class);
                startActivity(in);
                StudentExamActivity.this.finish();
            }
        }).setNegativeButton("Continue", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}