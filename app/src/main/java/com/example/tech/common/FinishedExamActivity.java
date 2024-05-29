package com.example.tech.common;

import static com.example.tech.student.StudentActivity.checkDateTime;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Exam;
import com.example.tech.backendless.classes.Questions;
import com.example.tech.backendless.classes.Result;
import com.example.tech.professor.ExamDetailsActivity;
import com.example.tech.professor.UploadExamActivity;
import com.example.tech.student.ExamsStudentActivity;
import com.example.tech.student.StudentActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class FinishedExamActivity extends AppCompatActivity implements RecycleAdapter.onExamListener {

    SharedPreferences pref;
    String team, dept, cat;
    String id;
    RecyclerView rvExams;
    RecycleAdapter adapter;
    List<Exam> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_exam);
        rvExams = findViewById(R.id.rvExams);
        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id", "");
        team = pref.getString("team", "");
        dept = pref.getString("dept", "");
        cat = pref.getString("cat", "");

        rvExams.setLayoutManager(new VegaLayoutManager());
        adapter = new RecycleAdapter(FinishedExamActivity.this, data, this, "stu");
        rvExams.setAdapter(adapter);

        if (cat.equals("stu")) {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setWhereClause("team = '" + team + "'");
            builder.setWhereClause("dept = '" + dept + "'");
            builder.setPageSize(100);
            Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
                @Override
                public void handleResponse(List<Exam> response) {
                    for (Exam exam : response) {
                        try {
                            if (checkDateTime(exam.getExamDate()) == -1) {
                                data.add(exam);
                                adapter.setData(data);
                                rvExams.setAdapter(adapter);
                            }
                        } catch (ParseException e) {
                            Toasty.error(FinishedExamActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
                        }
                    }
                    if (response.size() == 0){
                        onBackPressed();
                        Toasty.info(FinishedExamActivity.this, "No Finished Exams!", Toast.LENGTH_LONG, true).show();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });
        }
        else if (cat.equals("prof")) {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setWhereClause("profID = '" + id + "'");
            builder.setSortBy("examID");
            builder.setPageSize(100);
            Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
                @Override
                public void handleResponse(List<Exam> response) {
                    for (Exam exam : response) {
                        try {
                            if (checkDateTime(exam.getExamDate()) == -1) {
                                data.add(exam);
                                adapter.setData(data);
                                rvExams.setAdapter(adapter);
                            }
                        } catch (ParseException e) {
                            Toasty.error(FinishedExamActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
                        }
                    }
                    if (response.size() == 0){
                        onBackPressed();
                        Toasty.info(FinishedExamActivity.this, "No Finished Exams!", Toast.LENGTH_LONG, true).show();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });
        }
    }

    @Override
    public void onExamClicked(int position) {
        if (cat.equals("stu")) {
            DataQueryBuilder builder = DataQueryBuilder.create();
            builder.setWhereClause("stuID='" + id + "' and examID=" + data.get(position).getExamID() + "");
            Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
                @Override
                public void handleResponse(List<Result> response) {
                    if (response.size() == 0)
                        Toasty.warning(FinishedExamActivity.this, "You Didn't Enter This Exam", Toast.LENGTH_LONG).show();
                    else{
//                        Toasty.info(FinishedExamActivity.this, "Your Result = " + response.get(0).getResult(), Toast.LENGTH_LONG).show();
                        Intent in = new Intent(FinishedExamActivity.this, ExamDetailsActivity.class);
                        in.putExtra("cat","stu");
                        in.putExtra("subject", data.get(position).getSubject());
                        in.putExtra("team", data.get(position).getTeam());
                        in.putExtra("dept", data.get(position).getDept());
                        in.putExtra("date", data.get(position).getExamDate());
                        in.putExtra("quesNum", data.get(position).getQuesNum());
                        in.putExtra("result", response.get(0).getResult());
                        startActivity(in);
                    }

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (cat.equals("prof")) {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.delete_or_preview_dialog);
            dialog.setCancelable(true);
            dialog.show();

            ImageView ivPreview, ivDelete;
            ivPreview = dialog.findViewById(R.id.ivPreview);
            ivDelete = dialog.findViewById(R.id.ivDelete);

            ivPreview.setOnClickListener(v -> {
                dialog.cancel();
                DataQueryBuilder builder = DataQueryBuilder.create();
                builder.setWhereClause("examID="+data.get(position).getExamID()+"");
                Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
                    @Override
                    public void handleResponse(List<Result> response) {
                        if (response.size() == 0)
                            Toasty.info(FinishedExamActivity.this, "No Data For Now!", Toast.LENGTH_LONG, true).show();
                        else{
                            Intent in = new Intent(FinishedExamActivity.this, ExamDetailsActivity.class);
                            in.putExtra("subject", data.get(position).getSubject());
                            in.putExtra("team", data.get(position).getTeam());
                            in.putExtra("dept", data.get(position).getDept());
                            in.putExtra("date", data.get(position).getExamDate());
                            in.putExtra("examID", data.get(position).getExamID());
                            in.putExtra("quesNum", data.get(position).getQuesNum());
                            in.putExtra("size", response.size());
                            startActivity(in);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                    }
                });
            });

            ivDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Delete This Exam?");
                builder.setTitle("DELETE EXAM");
                builder.setPositiveButton("YES", (dialog1, which) -> {
                    dialog.cancel();

                    Backendless.Data.of(Result.class).remove("examID = " + data.get(position).getExamID() + "", new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {}

                        @Override
                        public void handleFault(BackendlessFault fault) {}
                    });

                    Backendless.Data.of(Questions.class).remove("examID = " + data.get(position).getExamID() + "", new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {}

                        @Override
                        public void handleFault(BackendlessFault fault) {}
                    });

                    Backendless.Data.of(Exam.class).remove("examID = " + data.get(position).getExamID() + "", new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {
                            Toasty.success(FinishedExamActivity.this, "Exam Deleted Successfully", Toasty.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {}
                    });

                }).setNegativeButton("NO", (dialog1, which) -> dialog1.cancel());
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            });


        }
    }
//
//    private String currentDate(){
//        Date currentDate = new Date();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        return format.format(currentDate);
//    }
//
//    private Integer compareDateTime(String UserDate){
//        Date uDate = new Date(UserDate);
//        Date cDate = new Date(currentDate());
//        return uDate.compareTo(cDate);
//    }
//
//    public Integer DateTime(String UserDate) {
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate, modifiedDate, userDate;
//        int dd, MM, yy, hh, mm, udd, uMM, uyy, uhh, umm;
//        currentDate = new Date();
//        ////////////////////////////////////////////////////////////////////////
//        String cDate = format.format(currentDate);
//        dd = Integer.parseInt(cDate.substring(0, 2));
//        MM = Integer.parseInt(cDate.substring(3, 5));
//        yy = Integer.parseInt(cDate.substring(6, 10));
//        hh = Integer.parseInt(cDate.substring(11, 13));
//        mm = Integer.parseInt(cDate.substring(14, 16));
//        ////////////////////////////////////////////////////////////////////////
//        String uDate = String.valueOf(UserDate);
//        udd = Integer.parseInt(uDate.substring(0, 2));
//        uMM = Integer.parseInt(uDate.substring(3, 5));
//        uyy = Integer.parseInt(uDate.substring(6, 10));
//        uhh = Integer.parseInt(uDate.substring(11, 13));
//        umm = Integer.parseInt(uDate.substring(14, 16));
//        ////////////////////////////////////////////////////////////////////////
//        userDate = new Date((uyy - 1900), (uMM - 1), udd, uhh, umm);
//        modifiedDate = new Date((yy - 1900), (MM - 1), dd, hh, mm);
//        ////////////////////////////////////////////////////////////////////////
//        return userDate.compareTo(modifiedDate);
//    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (cat.equals("prof"))
            intent = new Intent(this, UploadExamActivity.class);
        else
            intent = new Intent(this, ExamsStudentActivity.class);
        startActivity(intent);
        this.finish();
    }
}



    //            Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
//                @Override
//                public void handleResponse(List<Exam> response) {
//                    DataQueryBuilder builder = DataQueryBuilder.create();
//                    for (Exam exam : response) {
//
////                        builder.setWhereClause("stuID='" + id + "' and examID='" + exam.getExamID() + "'");
////                        Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
////                            @Override
////                            public void handleResponse(List<Result> response1) {
////                                if (response1.size() == 0) {
////                                    response.remove(exam);
////                                } else {
////                                    data.add(exam);
////                                    adapter.setData(data);
////                                    res.addAll(response1);
////                                } }
////                            @Override
////                            public void handleFault(BackendlessFault fault) { Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show(); }});
//                    } }
//                @Override
//                public void handleFault(BackendlessFault fault) { Toasty.error(FinishedExamActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show(); }
//            });

    //    String cDate,m,y,em,ey;
//    int mm,yy,emm,eyy;
//    public boolean Date(String ExamDate){  //                                                    01/34/6789 12:45
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate = new Date();
//        cDate = format.format(currentDate);
//
////        d = cDate.substring(0, 2);
////        dd = Integer.parseInt(d);
//        m = cDate.substring(3, 5);
//        mm = Integer.parseInt(m);
//        y = cDate.substring(6,10);
//        yy = Integer.parseInt(y);
//
////        ed = ExamDate.substring(0, 2);
////        edd = Integer.parseInt(ed);
//        em = ExamDate.substring(3, 5);
//        emm = Integer.parseInt(em);
//        ey = ExamDate.substring(6,10);
//        eyy = Integer.parseInt(ey);
//
//        return mm <= emm && yy <= eyy;// 30-11-2020; 10-10-2020;
//    }