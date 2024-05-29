package com.example.tech.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Exam;
import com.example.tech.backendless.classes.Result;
import com.example.tech.common.RecycleAdapter;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.stone.vega.library.VegaLayoutManager;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class StudentActivity extends AppCompatActivity implements RecycleAdapter.onExamListener, SwipeRefreshLayout.OnRefreshListener {

    SharedPreferences pref;
    String team, dept;
    String id;
    RecyclerView lvExams;
    RecycleAdapter adapter;
    List<Exam> data = new ArrayList<>();
    SwipeRefreshLayout SRLStu;
    HTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        tv = findViewById(R.id.tv);
        tv.setVisibility(View.INVISIBLE);

        SRLStu = findViewById(R.id.SRLStu);
        SRLStu.setOnRefreshListener(this);

        lvExams = findViewById(R.id.lvExams);
        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id","");
        team = pref.getString("team","");
        dept = pref.getString("dept","");

        lvExams.setLayoutManager(new VegaLayoutManager());
        onRefresh();
//        adapter = new RecycleAdapter(StudentActivity.this, data, this, "stu");
//        lvExams.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
//        SRLStu.setRefreshing(true);
        data.clear();
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+team+"'");
        builder.setWhereClause("dept = '"+dept+"'");
        builder.setPageSize(100);
        Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
            @Override
            public void handleResponse(List<Exam> response) {
//                else {
//                    tv.setVisibility(View.INVISIBLE);
//
//                    for (int i = 0; i < response.size(); i++) {
//                        try {
//                            Toast.makeText(StudentActivity.this, ""+checkDateTime(response.get(i).getExamDate()), Toast.LENGTH_LONG).show();
//                            if (checkDateTime(response.get(i).getExamDate()) != -1) {
//                                data.add(response.get(i));
//                                SRLStu.setRefreshing(false);
//                            }
//                            if (i == response.size()-1){
//                                adapter = new RecycleAdapter(StudentActivity.this, data, StudentActivity.this, "stu");
//                                adapter.setData(data);
//                                lvExams.setAdapter(adapter);
//                                SRLStu.setRefreshing(false);
//                            }
//                        } catch (ParseException e) {
//                            Toasty.error(StudentActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
//                        }
//                    }
                    for (Exam exam : response) {
                        try {
                            if (checkDateTime(exam.getExamDate()) != -1) {
                                data.add(exam);
                                tv.setVisibility(View.INVISIBLE);
                            }
                        } catch (ParseException e) {
                            Toasty.error(StudentActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
                        }
                    }
                if (data.size() == 0) {
                    tv.setAnimateType(HTextViewType.RAINBOW);
                    tv.animateText("No Exams For Now");
                    tv.setVisibility(View.VISIBLE);
                    SRLStu.setRefreshing(false);
                }
                adapter = new RecycleAdapter(StudentActivity.this, data, StudentActivity.this, "stu");
                adapter.setData(data);
                lvExams.setAdapter(adapter);
                SRLStu.setRefreshing(false);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(StudentActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show(); }
        });
    }

    @Override
    public void onExamClicked(int position) {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("stuID='"+id+"' and examID='"+data.get(position).getExamID()+"'");

        try {
            if (checkDateTime(data.get(position).getExamDate()) == 0) {
                Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
                    @Override
                    public void handleResponse(List<Result> response) {
                        if (response.size() == 0) {
                            Intent intent = new Intent(StudentActivity.this, StudentExamActivity.class);
                            intent.putExtra("subject", data.get(position).getSubject());
                            intent.putExtra("examID", data.get(position).getExamID());
                            intent.putExtra("type", data.get(position).getType());
                            intent.putExtra("team", data.get(position).getTeam());
                            intent.putExtra("time", data.get(position).getTimeRange());
                            intent.putExtra("quesNum", data.get(position).getQuesNum());
                            intent.putExtra("examDate", data.get(position).getExamDate());
                            intent.putExtra("timeType",data.get(position).getTimeType());
                            startActivity(intent);
                            StudentActivity.this.finish();
                        }
                        else
                            Toasty.info(StudentActivity.this, "You Finished This Exam", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(StudentActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                Toasty.info(StudentActivity.this, "Exam's Time Not Now", Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            Toasty.error(StudentActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
        }
    }

    public static int checkDateTime(String ExamDate) throws ParseException {
        int YY, MM, DD, hh, mm;
        YY = Integer.parseInt((ExamDate.substring(6, 10)));
        MM = Integer.parseInt((ExamDate.substring(3, 5)));
        DD = Integer.parseInt((ExamDate.substring(0, 2)));
        hh = Integer.parseInt((ExamDate.substring(11, 13)));
        mm = Integer.parseInt((ExamDate.substring(14)));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime examDate = LocalDateTime.of(YY,MM,DD,hh,mm);

            if (examDate.isAfter(currentDate))
                return 1;//Exam Time Not Now
//                               5     >=             4                 5         <                 5                  5         ==                 5
            if ((currentDate.getHour() >= examDate.getHour()) && ((currentDate.isBefore(examDate.plusHours(1))) || currentDate.isEqual(examDate.plusHours(1))))
                return 0;//Enter the Exam

            if (currentDate.isAfter(examDate.plusHours(1)))
                return -1;//Exam Will Not Appear
        }
        return 1;
    }

//    private int date(String ExamDate, int hour){
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate, examDate;
//        int dd,MM,yy,hh,mm,edd,eMM,eyy,ehh,emm;
//        //get current date and split it
//        currentDate = new Date();
//        String cDate = format.format(currentDate);
//        dd = Integer.parseInt(cDate.substring(0,2));
//        MM = Integer.parseInt(cDate.substring(3,5));
//        yy = Integer.parseInt(cDate.substring(6,10));
//        hh = Integer.parseInt(cDate.substring(11,13));
//        mm = Integer.parseInt(cDate.substring(14,16));
//        //split exam date
//        edd = Integer.parseInt(ExamDate.substring(0,2));
//        eMM = Integer.parseInt(ExamDate.substring(3,5));
//        eyy = Integer.parseInt(ExamDate.substring(6,10));
//        ehh = Integer.parseInt(ExamDate.substring(11,13));
//        emm = Integer.parseInt(ExamDate.substring(14,16));
//        //collect date
//        examDate = new Date((eyy-1900),(eMM-1),edd,(ehh+hour),emm);//16/11/2020 03:00
//        currentDate = new Date((yy-1900),(MM-1),dd,(hh),mm);//16/11/2020 02:00
//        return examDate.compareTo(currentDate);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ExamsStudentActivity.class);
        startActivity(intent);
        this.finish();
    }

}





















//    private Integer compareDateTime(String UserDate){
//        Date uDate = new Date(UserDate);
//        Date cDate = new Date(currentDate());
//        return uDate.compareTo(cDate);
//    }
//
//    private String currentDate(){
//        Date currentDate = new Date();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        return format.format(currentDate);
//    }


//    public Integer Time(String time){
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate;
//        int hh,uhh;
//        currentDate = new Date();
//        ////////////////////////////////////////////////////////////////////////
//        String cDate = format.format(currentDate);
//        hh = Integer.parseInt(cDate.substring(11,13));
//        ////////////////////////////////////////////////////////////////////////
//        String uDate = String.valueOf(time);
//        uhh = Integer.parseInt(uDate.substring(11,13));
//        ////////////////////////////////////////////////////////////////////////
//        if (hh == uhh)
//            return 1;
//        return 0;
//    }

//    public Integer DateTime(String ExamDate){
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate, modifiedDate, examDate;
//        int dd,MM,yy,hh,mm,udd,uMM,uyy,uhh,umm;
//        currentDate = new Date();
//        ////////////////////////////////////////////////////////////////////////
//        String cDate = format.format(currentDate);
//        dd = Integer.parseInt(cDate.substring(0,2));
//        MM = Integer.parseInt(cDate.substring(3,5));
//        yy = Integer.parseInt(cDate.substring(6,10));
//        hh = Integer.parseInt(cDate.substring(11,13));
//        mm = Integer.parseInt(cDate.substring(14,16));
//        ////////////////////////////////////////////////////////////////////////
//        String uDate = String.valueOf(ExamDate);
//        udd = Integer.parseInt(uDate.substring(0,2));
//        uMM = Integer.parseInt(uDate.substring(3,5));
//        uyy = Integer.parseInt(uDate.substring(6,10));
//        uhh = Integer.parseInt(uDate.substring(11,13));
//        umm = Integer.parseInt(uDate.substring(14,16));
//        ////////////////////////////////////////////////////////////////////////
//        examDate = new Date((uyy-1900),(uMM-1),udd,uhh,umm);//16/11/2020 03:00
//        modifiedDate = new Date((yy-1900),(MM-1),dd,(hh-1),mm);//16/11/2020 02:00
//        ////////////////////////////////////////////////////////////////////////
//        return examDate.compareTo(modifiedDate);
//    }

//    public Integer DateTime2(String ExamDate){
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date currentDate, modifiedDate, examDate;
//        int dd,MM,yy,hh,mm,udd,uMM,uyy,uhh,umm;
//        currentDate = new Date();
//        ////////////////////////////////////////////////////////////////////////
//        String cDate = format.format(currentDate);
//        dd = Integer.parseInt(cDate.substring(0,2));
//        MM = Integer.parseInt(cDate.substring(3,5));
//        yy = Integer.parseInt(cDate.substring(6,10));
//        hh = Integer.parseInt(cDate.substring(11,13));
//        mm = Integer.parseInt(cDate.substring(14,16));
//        ////////////////////////////////////////////////////////////////////////
//        String uDate = String.valueOf(ExamDate);
//        udd = Integer.parseInt(uDate.substring(0,2));
//        uMM = Integer.parseInt(uDate.substring(3,5));
//        uyy = Integer.parseInt(uDate.substring(6,10));
//        uhh = Integer.parseInt(uDate.substring(11,13));
//        umm = Integer.parseInt(uDate.substring(14,16));
//        ////////////////////////////////////////////////////////////////////////
//        examDate = new Date((uyy-1900),(uMM-1),udd,uhh,umm);//16/11/2020 03:00
//        modifiedDate = new Date((yy-1900),(MM-1),dd,(hh),mm);//16/11/2020 02:00
//        ////////////////////////////////////////////////////////////////////////
//        return examDate.compareTo(modifiedDate);
//    }

//    String cDate,hr,min,hrExam, date,eDate;
//    int h,m,hExam;
//    public boolean Date(String ExamDate){  //                  01-34-6789 12:45
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//        Date currentDate = new Date();
//        cDate = format.format(currentDate);
//
//        hr = cDate.substring(11, 13);
//        h = Integer.parseInt(hr);
//        min = cDate.substring(14, 16);
//        m = Integer.parseInt(min);
//        date = cDate.substring(0,10);
//
//        hrExam = ExamDate.substring(11, 13);
//        hExam = Integer.parseInt(hrExam);
//        eDate = ExamDate.substring(0,10);
//
//        return date.equals(eDate) && h == hExam && m <= 59;
//    }

//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("team = '"+team+"'");
//        builder.setWhereClause("dept = '"+dept+"'");
//        builder.setPageSize(100);
//        Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
//            @Override
//            public void handleResponse(List<Exam> response) {
//                DataQueryBuilder builder = DataQueryBuilder.create();
//                for (Exam exam : response) {
//                    builder.setWhereClause("stuID='"+id+"' and examID='"+exam.getExamID()+"'");
//                    Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
//                        @Override
//                        public void handleResponse(List<Result> response1) {
//                            if (response1.size() == 0) {
//                                data.add(exam);
//                                adapter.setData(data);
//                            }
//                            else
//                                response.remove(exam);
//                            }
//                        @Override
//                        public void handleFault(BackendlessFault fault) {
//                            Toast.makeText(StudentActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }); } }
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(StudentActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show(); }
//        });