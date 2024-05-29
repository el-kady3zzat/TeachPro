package com.example.tech.professor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Result;
import com.example.tech.backendless.classes.Student;
import com.example.tech.common.FinishedExamActivity;
import com.example.tech.common.ResultAdapter;
import com.stone.vega.library.VegaLayoutManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ExamDetailsActivity extends AppCompatActivity {

    TextView tvSubject,tvTeam,tvDept,tvDate,tvStuCount,tvSucRate,tvRepRate;
    int examID, quesNum, stuCount;
    String team, dept;
    RecyclerView lvResult;
    ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_details);
        tvSubject = findViewById(R.id.tvSubject);
        tvTeam = findViewById(R.id.tvTeam);
        tvDept = findViewById(R.id.tvDept);
        tvDate = findViewById(R.id.tvDate);
        tvStuCount = findViewById(R.id.tvStudentCount);
        tvSucRate = findViewById(R.id.tvSuccessRate);
        tvRepRate = findViewById(R.id.tvRepetitionRate);
        lvResult = findViewById(R.id.lvResult);

        tvSubject.setText(getIntent().getStringExtra("subject"));
        tvTeam.setText(getIntent().getStringExtra("team"));
        tvDept.setText(getIntent().getStringExtra("dept"));
        tvDate.setText(getIntent().getStringExtra("date"));
        examID = getIntent().getIntExtra("examID",0);
        quesNum = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("quesNum")));
        team = getIntent().getStringExtra("team");
        dept = getIntent().getStringExtra("dept");

        if (getIntent().getStringExtra("cat").equals("stu")){
            tvSubject.setText(getIntent().getStringExtra("subject"));
            tvTeam.setText(getIntent().getStringExtra("team"));
            tvDept.setText(getIntent().getStringExtra("dept"));
            tvDate.setText(getIntent().getStringExtra("date"));
            tvStuCount.setText("Your Result: " + getIntent().getIntExtra("result",0) + " OF " + quesNum);
            tvSucRate.setText("Correct: " + getIntent().getIntExtra("result",0));
            tvRepRate.setText("Wrong: " + (quesNum - getIntent().getIntExtra("result",0)));
        }
        else {
            stuCount = getIntent().getIntExtra("size", 0);
            DataQueryBuilder builder = DataQueryBuilder.create();
            tvStuCount.append(String.valueOf(getIntent().getIntExtra("size", 0) * 20));
            builder.setWhereClause("examID=" + examID + "");//and result >= " + (quesNum / 2) + "
            builder.setPageSize(100);
            Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
                @Override
                public void handleResponse(List<Result> response) {
                    int success = 0;
                    for (int i = 0; i < response.size(); i++) {
                        if (response.get(i).getResult() >= (quesNum / 2))
                            success++;
                    }
                    if (success != 0)
                        tvSucRate.append((success * 100) / (stuCount) + "%");
                    else
                        tvSucRate.append("0%");

                    tvRepRate.append((100 - ((success * 100) / stuCount)) + "%");
                    lvResult.setLayoutManager(new VegaLayoutManager());
                    adapter = new ResultAdapter(ExamDetailsActivity.this, response, quesNum);
                    lvResult.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(ExamDetailsActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, FinishedExamActivity.class));
        super.onBackPressed();
    }

    //        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("examID="+examID+"");
//        Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
//            @Override
//            public void handleResponse(List<Result> response) {
//                if (response.size() == 0)
//                    Toasty.info(FinishedExamActivity.this, "No Data For Now!", Toast.LENGTH_LONG, true).show();
//                else{
//                    Intent in = new Intent(FinishedExamActivity.this, ExamDetailsActivity.class);
//                    in.putExtra("subject", data.get(position).getSubject());
//                    in.putExtra("team", data.get(position).getTeam());
//                    in.putExtra("dept", data.get(position).getDept());
//                    in.putExtra("date", data.get(position).getExamDate());
//                    in.putExtra("examID", data.get(position).getExamID());
//                    in.putExtra("quesNum", data.get(position).getQuesNum());
//                    startActivity(in);
//                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//            }
//        });

//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setProperties("Count(objectId)");
//        builder.setWhereClause("examID="+examID+"");
//        Backendless.Data.of("Result").find(builder, new AsyncCallback<List<Map>>() {
//            @Override
//            public void handleResponse(List<Map> response) {
//                stuCount = (int) response.get(0).get("count");
//                if (stuCount==0){
//                    Toasty.info(ExamDetailsActivity.this, "No Data For Now!", Toast.LENGTH_LONG, true).show();
//                    Intent intent = new Intent(ExamDetailsActivity.this, FinishedExamActivity.class);
//                    startActivity(intent);
//                    ExamDetailsActivity.this.finish();
//                }
//                else {
//                    DataQueryBuilder builder1 = DataQueryBuilder.create();
//                    tvStuCount.append(String.valueOf(response.get(0).get("count")));
//                    builder1.setWhereClause("examID=" + examID + " and result >= " + (quesNum / 2) + "");
//                    Backendless.Data.of(Result.class).find(builder1, new AsyncCallback<List<Result>>() {
//                        @Override
//                        public void handleResponse(List<Result> response) {
//                            if (response.size() != 0)
//                                tvSucRate.append((response.size() * 100) / (stuCount) + "%");
//                            else
//                                tvSucRate.append("0%");
//
//                            tvRepRate.append((100 - ((response.size() * 100) / stuCount)) + "%");
//                        }
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                            Toasty.error(ExamDetailsActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//                        }
//                    }); } }
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(ExamDetailsActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//            }});

//        ArrayList<Result> result = new ArrayList<>();
//        lvResult.setLayoutManager(new VegaLayoutManager());
//        adapter = new ResultAdapter(ExamDetailsActivity.this, result, quesNum);
//        lvResult.setAdapter(adapter);

//        DataQueryBuilder builder1 = DataQueryBuilder.create();
//        builder1.setWhereClause("team='"+team+"' and dept='"+dept+"'");
//        Backendless.Data.of(Student.class).find(builder1, new AsyncCallback<List<Student>>() {
//            @Override
//            public void handleResponse(List<Student> response) {
//                for (Student student : response) {
//                    DataQueryBuilder builder2 = DataQueryBuilder.create();
//                    builder2.setWhereClause("stuID="+student.getId()+" and examID='"+examID+"'");
//                    Backendless.Data.of(Result.class).find(builder2, new AsyncCallback<List<Result>>() {
//                        @Override
//                        public void handleResponse(List<Result> response) {
//                            result.addAll(response);
//                            adapter.notifyDataSetChanged();
//                        }
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                            Toasty.error(ExamDetailsActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//                        }
//                    }); } }
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(ExamDetailsActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//            }});

}