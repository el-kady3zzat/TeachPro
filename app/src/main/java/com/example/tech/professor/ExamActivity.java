package com.example.tech.professor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Exam;
import com.example.tech.backendless.classes.Questions;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ExamActivity extends AppCompatActivity {

    SharedPreferences prefExam, pref;
    TextView tvQuestionTrueFalse, tvQuestionMultiChoice, tvQuesNum;
    RadioGroup RGtf, RGmc;
    RadioButton rbTrue, rbFalse, rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4;
    HamButton.Builder builder;
    ConstraintLayout conTrueFalse, conMultiChoice;
    String id;
    int examID;
    byte i = 0, l = 0;
    String team, dept, type, quesNum, timeType, timeRange, examDate, subject;
    Exam exam = new Exam();
    Questions ques = new Questions();
    CountDownTimerView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        
        tvQuestionTrueFalse = findViewById(R.id.tvQuestionTrueFalse);
        tvQuestionMultiChoice = findViewById(R.id.tvQuestionMultiChoice);
        rbTrue = findViewById(R.id.rbTrue);
        rbFalse = findViewById(R.id.rbFalse);
        rbAnswer1 = findViewById(R.id.rbAnswer1);
        rbAnswer2 = findViewById(R.id.rbAnswer2);
        rbAnswer3 = findViewById(R.id.rbAnswer3);
        rbAnswer4 = findViewById(R.id.rbAnswer4);
        conTrueFalse = findViewById(R.id.conTrueFalse);
        conMultiChoice = findViewById(R.id.conMultiChoice);
        tvQuesNum = findViewById(R.id.tvQuesNum);
        count = findViewById(R.id.mCountDownTimer);
        RGmc = findViewById(R.id.RGmc);
        RGtf = findViewById(R.id.RGtf);

        BoomMenuButton bmb = findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if (i == 0){
                builder = new HamButton.Builder()
                        .normalColor(R.color.main)
                        .imagePadding(new Rect(5, 5, 5, 5))
                        .normalImageRes(R.drawable.ic_next)
                        .shadowEffect(true)
                        .normalTextRes(R.string.next)
                        .listener(index -> {
                            next();
                        });
                bmb.addBuilder(builder);
            }
            else if (i == 1){
                builder = new HamButton.Builder()
                        .normalColor(R.color.main)
                        .imagePadding(new Rect(5, 5, 5, 5))
                        .normalImageRes(R.drawable.ic_save_red).shadowEffect(true)
                        .normalTextRes(R.string.save)
                        .listener(index -> {
                            save();
                        });
                bmb.addBuilder(builder);
            }
        }

        rbTrue.setVisibility(View.INVISIBLE);
        rbFalse.setVisibility(View.INVISIBLE);
        rbAnswer1.setVisibility(View.INVISIBLE);
        rbAnswer2.setVisibility(View.INVISIBLE);
        rbAnswer3.setVisibility(View.INVISIBLE);
        rbAnswer4.setVisibility(View.INVISIBLE);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        prefExam = getSharedPreferences("examData", MODE_PRIVATE);
        id = prefExam.getString("id","");
        subject = prefExam.getString("subject", "");
        quesNum = prefExam.getString("quesNum", "");
        timeRange = prefExam.getString("timeRange", "");
        examDate = prefExam.getString("examDate", "");
        type = prefExam.getString("type", "");
//        checkLang();
        tvQuesNum.setText(String.valueOf(i+1));

        count.start(Integer.parseInt(timeRange) * 60000L);

        if (getIntent().getBooleanExtra("edit", false)){
            if (type.equals("True and False")){
                conMultiChoice.setVisibility(View.GONE);
                tvQuestionTrueFalse.setText(prefExam.getString("ques"+(l)+"",""));
                RGtf.clearCheck();
                rbTrue.setVisibility(View.VISIBLE);
                rbFalse.setVisibility(View.VISIBLE);
            }
            else {
                conTrueFalse.setVisibility(View.GONE);
                tvQuestionMultiChoice.setText(prefExam.getString("ques"+(l)+"",""));
                rbAnswer1.setText(prefExam.getString("answer1"+(l)+"",""));
                rbAnswer2.setText(prefExam.getString("answer2"+(l)+"",""));
                rbAnswer1.setVisibility(View.VISIBLE);
                rbAnswer2.setVisibility(View.VISIBLE);
                if (prefExam.getString("answer3"+(l)+"","").equals("")) {
                    rbAnswer3.setVisibility(View.GONE);
                } else {
                    rbAnswer3.setText(prefExam.getString("answer3" + (l) + "", ""));
                    rbAnswer3.setVisibility(View.VISIBLE);
                }
                if (prefExam.getString("answer4"+(l)+"","").equals("")) {
                    rbAnswer4.setVisibility(View.GONE);
                } else {
                    rbAnswer4.setText(prefExam.getString("answer4" + (l) + "", ""));
                    rbAnswer4.setVisibility(View.VISIBLE);
                }
            }

        }
        else {
            i++;
            if (type.equals("True and False")) {
                conMultiChoice.setVisibility(View.GONE);
                tvQuestionTrueFalse.setText(prefExam.getString("ques" + (i) + "", ""));
                RGtf.clearCheck();
                rbTrue.setVisibility(View.VISIBLE);
                rbFalse.setVisibility(View.VISIBLE);
            } else {
                conTrueFalse.setVisibility(View.GONE);
                tvQuestionMultiChoice.setText(prefExam.getString("ques" + (i) + "", ""));
                rbAnswer1.setText(prefExam.getString("answer1" + (i) + "", ""));
                rbAnswer2.setText(prefExam.getString("answer2" + (i) + "", ""));
                rbAnswer1.setVisibility(View.VISIBLE);
                rbAnswer2.setVisibility(View.VISIBLE);
                if (prefExam.getString("answer3" + (i) + "", "").equals("")) {
                    rbAnswer3.setVisibility(View.GONE);
                } else {
                    rbAnswer3.setText(prefExam.getString("answer3" + (i) + "", ""));
                    rbAnswer3.setVisibility(View.VISIBLE);
                }
                if (prefExam.getString("answer4" + (i) + "", "").equals("")) {
                    rbAnswer4.setVisibility(View.GONE);
                } else {
                    rbAnswer4.setText(prefExam.getString("answer4" + (i) + "", ""));
                    rbAnswer4.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void checkLang(){
        team = prefExam.getString("team","");
        switch (team) {
            case "الفرقة الأولي":
                team = "Team1";
                break;
            case "الفرقة الثانية":
                team = "Team2";
                break;
            case "الفرقة الثالثة":
                team = "Team3";
                break;
            case "الفرقة الرابعة":
                team = "Team4";
                break;
        }

        dept = prefExam.getString("dept", "");
        switch (dept) {
            case "الحاسب الآلي":
                dept = "Computer";
                break;
            case "التربية الفنية":
                dept = "Art Education";
                break;
            case "الاقتصاد المنزلي":
                dept = "Home Economics";
                break;
            case "الإعلام التربوي":
                dept = "Educational Media";
                break;
        }

        type = prefExam.getString("type", "");
        switch (type) {
            case "اختيار من متعدد":
                type = "Multi Choice";
                break;
            case "صح و خطأ":
                type = "True and False";
                break;
        }

        timeType = prefExam.getString("timeType", "");
        switch (timeType) {
            case "دقائق للامتحان كاملاَ":
                timeType = "Minutes For Whole Exam";
                break;
            case "دقيقة لكل سؤال":
                timeType = "Minute For Every Question";
                break;
        }
    }

    public void next() {
        if (getIntent().getBooleanExtra("edit", false)){
            if (l == Integer.parseInt(quesNum)-1) {
                Toasty.info(this,"No More Questions!", Toasty.LENGTH_SHORT, true).show();
                return;
            }

            l++;
            tvQuesNum.setText(String.valueOf(l+1));

            if (type.equals("True and False")) {
                tvQuestionTrueFalse.setText(prefExam.getString("ques" + l + "", ""));
                RGtf.clearCheck();
                rbAnswer1.setText("True");
                rbAnswer1.setText("False");
            }

            else if (type.equals("Multi Choice")) {
                RGmc.clearCheck();
                tvQuestionMultiChoice.setText(prefExam.getString("ques" + l + "", ""));
                rbAnswer1.setText(prefExam.getString("answer1" + l + "", ""));
                rbAnswer2.setText(prefExam.getString("answer2" + l + "", ""));
                if (prefExam.getString("answer3"+ l + "","").equals("")) {
                    rbAnswer3.setVisibility(View.GONE);
                } else {
                    rbAnswer3.setText(prefExam.getString("answer3" + l + "", ""));
                    rbAnswer3.setVisibility(View.VISIBLE);
                }
                if (prefExam.getString("answer4"+ l +"","").equals("")) {
                    rbAnswer4.setVisibility(View.GONE);
                } else {
                    rbAnswer4.setText(prefExam.getString("answer4" + l + "", ""));
                    rbAnswer4.setVisibility(View.VISIBLE);
                }
            }
        }

        else {
            if (i == Integer.parseInt(quesNum)) {
                Toasty.info(this, "No More Questions!", Toasty.LENGTH_SHORT, true).show();
                return;
            }

            i++;
            tvQuesNum.setText(String.valueOf(i));

            if (type.equals("True and False")) {
                tvQuestionTrueFalse.setText(prefExam.getString("ques" + i + "", ""));
                RGtf.clearCheck();
                rbAnswer1.setText("True");
                rbAnswer1.setText("False");

            } else if (type.equals("Multi Choice")) {
                RGmc.clearCheck();
                tvQuestionMultiChoice.setText(prefExam.getString("ques" + i + "", ""));
                rbAnswer1.setText(prefExam.getString("answer1" + i + "", ""));
                rbAnswer2.setText(prefExam.getString("answer2" + i + "", ""));
                if (prefExam.getString("answer3" + i + "", "").equals("")) {
                    rbAnswer3.setVisibility(View.GONE);
                } else {
                    rbAnswer3.setText(prefExam.getString("answer3" + i + "", ""));
                    rbAnswer3.setVisibility(View.VISIBLE);
                }
                if (prefExam.getString("answer4" + i + "", "").equals("")) {
                    rbAnswer4.setVisibility(View.GONE);
                } else {
                    rbAnswer4.setText(prefExam.getString("answer4" + i + "", ""));
                    rbAnswer4.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void save() {
            DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create().setProperties( "Max(examID)");
            Backendless.Data.of( "Exam" ).find( dataQueryBuilder, new AsyncCallback<List<Map>>()
            {
                @Override
                public void handleResponse(List<Map> response) {
                    String max = String.valueOf(response.get(0).get("max"));
                    if (max.equals("null")) examID = 1;
                    else examID = Integer.parseInt(max) + 1;

                    exam.setExamID(examID);
                    exam.setProfID(id);
                    exam.setTeam(team);
                    exam.setDept(dept);
                    exam.setSubject(subject);
                    exam.setType(type);
                    exam.setQuesNum(quesNum);
                    exam.setTimeType(timeType);
                    exam.setTimeRange(timeRange);
                    exam.setExamDate(examDate);
                    Backendless.Data.of(Exam.class).save(exam, new BackendlessCallback<Exam>() {
                        @Override
                        public void handleResponse(Exam response) {
                            for (int j = Integer.parseInt(quesNum); j > 0 ; j--) {
                                ques.setExamID(examID);
                                ques.setQuestion(prefExam.getString("ques"+j+"", ""));
                                if (type.equals("True and False")){
                                    ques.setAnswer1("True");
                                    ques.setAnswer2("False");
                                }
                                else{
                                    ques.setAnswer1(prefExam.getString("answer1"+j+"",""));
                                    ques.setAnswer2(prefExam.getString("answer2"+j+"",""));
                                    ques.setAnswer3(prefExam.getString("answer3"+j+"",""));
                                    ques.setAnswer4(prefExam.getString("answer4"+j+"",""));
                                }
                                ques.setRightAnswer(prefExam.getString("rightAnswer"+j+"",""));
                                Backendless.Data.of(Questions.class).save(ques, new BackendlessCallback<Questions>() {
                                    @Override
                                    public void handleResponse(Questions response) {
                                        Intent in = new Intent(ExamActivity.this, ProfessorMainActivity.class);
                                        startActivity(in);
                                        ExamActivity.this.finish();
                                    }
                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toasty.error(ExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                                    }
                                });
                            }
                            Toasty.success(ExamActivity.this, "Exam Saved Successfully!", Toast.LENGTH_LONG, true).show();
                            SharedPreferences.Editor editor;
                            editor = prefExam.edit();
                            editor.clear();
                            editor.apply();
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(ExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(ExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });
//        }
    }

//    int c = 1;
//    int j = 0;
//    CountDownTimer countDownTimer;
//    private void update(){
//        Map<String, Object> examChanges = new HashMap<>();
//        examChanges.put("subject", prefExam.getString("subject",""));
//        examChanges.put("quesNum", prefExam.getString("quesNum",""));
//        examChanges.put("timeRange", prefExam.getString("timeRange",""));
//        examChanges.put("examDate", prefExam.getString("examDate",""));
//        examChanges.put("team", prefExam.getString("team",""));
//        examChanges.put("dept", prefExam.getString("dept", ""));
//        examChanges.put("type", prefExam.getString("type", ""));
//        examChanges.put("timeType", prefExam.getString("timeType", ""));
//        Backendless.Data.of(Exam.class).update("examID = "+ examID +"", examChanges, new AsyncCallback<Integer>() {
//            @Override
//            public void handleResponse(Integer response) {
//
//                Map<String, Object> questionsChanges = new HashMap<>();
//                countDownTimer = new CountDownTimer(600000, 2000) {
//                    public void onTick(long millisUntilFinished) {
//                        Toast.makeText(ExamActivity.this, prefExam.getString("ques"+j+"", ""), Toast.LENGTH_SHORT).show();
//                        questionsChanges.put("question", prefExam.getString("ques"+j+"", ""));
//                        questionsChanges.put("rightAnswer", prefExam.getString("rightAnswer"+j+"",""));
//
//                        if (type.equals("True and False")){
//                            questionsChanges.put("Answer1", "True");
//                            questionsChanges.put("Answer2", "False");
//                        }
//                        else{
//                            questionsChanges.put("Answer1", prefExam.getString("answer1"+j+"",""));
//                            questionsChanges.put("Answer2", prefExam.getString("answer2"+j+"",""));
//                            questionsChanges.put("Answer3", prefExam.getString("answer3"+j+"",""));
//                            questionsChanges.put("Answer4", prefExam.getString("answer4"+j+"",""));
//                        }
//
//                        countDownTimer.cancel();
//                        Backendless.Data.of(Questions.class).update("examID = "+ examID +"", questionsChanges, new AsyncCallback<Integer>() {
//                            @Override
//                            public void handleResponse(Integer response) {
//                                j++;
//                                c++;
//                                if (c == Integer.parseInt(quesNum)){
//                                    countDownTimer.cancel();
//                                    Toasty.success(ExamActivity.this, "Exam Updated Successfully! " + examID, Toast.LENGTH_LONG, true).show();
//                                    SharedPreferences.Editor editor;
//                                    editor = prefExam.edit();
//                                    editor.clear();
//                                    editor.apply();
//                                    Intent in = new Intent(ExamActivity.this, ProfessorActivity.class);
//                                    startActivity(in);
//                                    ExamActivity.this.finish();
//                                }
//                            }
//
//                            @Override
//                            public void handleFault(BackendlessFault fault) {
//                                Toasty.success(ExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//                            }
//                        });
//                    }
//
//                    public void onFinish() {
//
//                    }
//                }.start();
////                Map<String, Object> questionsChanges = new HashMap<>();
////                for (int j = 0; j < Integer.parseInt(quesNum); j++) {
////                    Toast.makeText(ExamActivity.this, prefExam.getString("ques"+j+"", ""), Toast.LENGTH_SHORT).show();
////                    questionsChanges.put("question", prefExam.getString("ques"+j+"", ""));
////                    questionsChanges.put("rightAnswer", prefExam.getString("rightAnswer"+j+"",""));
////
////                    if (type.equals("True and False")){
////                        questionsChanges.put("Answer1", "True");
////                        questionsChanges.put("Answer2", "False");
////                    }
////                    else{
////                        questionsChanges.put("Answer1", prefExam.getString("answer1"+j+"",""));
////                        questionsChanges.put("Answer2", prefExam.getString("answer2"+j+"",""));
////                        questionsChanges.put("Answer3", prefExam.getString("answer3"+j+"",""));
////                        questionsChanges.put("Answer4", prefExam.getString("answer4"+j+"",""));
////                    }
////
////                    Backendless.Data.of(Questions.class).update("examID = "+ examID +"", questionsChanges, new AsyncCallback<Integer>() {
////                        @Override
////                        public void handleResponse(Integer response) {
////                            c++;
////                            if (c == Integer.parseInt(quesNum)){
////                                Toasty.success(ExamActivity.this, "Exam Updated Successfully! " + examID, Toast.LENGTH_LONG, true).show();
////                                SharedPreferences.Editor editor;
////                                editor = prefExam.edit();
////                                editor.clear();
////                                editor.apply();
////                                Intent in = new Intent(ExamActivity.this, ProfessorActivity.class);
////                                startActivity(in);
////                                ExamActivity.this.finish();
////                            }
////                        }
////
////                        @Override
////                        public void handleFault(BackendlessFault fault) {
////                            Toasty.success(ExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
////                        }
////                    });
////                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(ExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//            }
//        });
//
//    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setMessage("Are You Sure You Want Leave Without Saving?");
        builder.setTitle("CANCEL");
        builder.setPositiveButton("YES", (dialog, which) -> {
            SharedPreferences.Editor editor;
            editor = prefExam.edit();
            editor.clear();
            editor.apply();
            Intent in = new Intent(this, ProfessorActivity.class);
            startActivity(in);
            ExamActivity.this.finish();
        }).setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}