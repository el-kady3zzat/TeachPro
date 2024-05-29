package com.example.tech.professor;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Exam;
import com.example.tech.backendless.classes.Questions;
import com.example.tech.backendless.classes.Result;
import com.example.tech.common.RecycleAdapter;
import com.example.tech.student.StudentActivity;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.stone.vega.library.VegaLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ProfessorActivity extends AppCompatActivity implements RecycleAdapter.onExamListener, SwipeRefreshLayout.OnRefreshListener  {

    DataQueryBuilder builder = DataQueryBuilder.create();
    SharedPreferences pref;
    RecyclerView lvExams;
    String id;
    RecycleAdapter adapter;
    List<Exam> data = new ArrayList<>();
    SwipeRefreshLayout SRL;
    HTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        tv = findViewById(R.id.tv);
        tv.setVisibility(View.INVISIBLE);

        SRL = findViewById(R.id.SRL);
        SRL.setOnRefreshListener(this);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id", "");
        lvExams = findViewById(R.id.lvExams);

        lvExams.setLayoutManager(new VegaLayoutManager());

        onRefresh();
        adapter = new RecycleAdapter(this, data, this, "prof");
        lvExams.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        data.clear();
        SRL.setRefreshing(true);

        builder.setWhereClause("profID = '"+id+"'");
        builder.setSortBy("examID");
        builder.setPageSize(100);
        Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
            @Override
            public void handleResponse(List<Exam> response) {
                for (Exam exam : response) {
                    try {
                        if (checkDateTime(exam.getExamDate()) != -1) {
                            data.add(exam);
                            adapter.setData(data);
                            tv.setVisibility(View.INVISIBLE);
                            lvExams.setAdapter(adapter);
                        }
                    } catch (ParseException e) {
                        Toasty.error(ProfessorActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show();
                    }
                }
                if (data.size() == 0){
                    adapter.setData(data);
                    lvExams.setAdapter(adapter);//adapter.notifyDataSetChanged();
                    tv.setAnimateType(HTextViewType.RAINBOW);
                    tv.animateText("No Exams For Now");
                    tv.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void handleFault(BackendlessFault fault) { Toasty.error(ProfessorActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show(); }
        });
        SRL.setRefreshing(false);
    }

    @Override
    public void onExamClicked(int position) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_or_edit_dialog);
        dialog.setCancelable(true);
        dialog.show();

        ImageView ivEdit, ivDelete;
        ivEdit = dialog.findViewById(R.id.ivEdit);
        ivDelete = dialog.findViewById(R.id.ivDelete);

        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewExamActivity.class);
            intent.putExtra("edit", true);
            intent.putExtra("examID", data.get(position).getExamID());
            this.finish();
            startActivity(intent);
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
                        Toasty.success(ProfessorActivity.this, "Exam Deleted Successfully", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {}
                });

            }).setNegativeButton("NO", (dialog1, which) -> dialog1.cancel());
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        });
    }

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UploadExamActivity.class);
        startActivity(intent);
        this.finish();
    }
}























//            new ItemTouchHelper(swipeLR).attachToRecyclerView(lvExams);
//
//    ItemTouchHelper.SimpleCallback swipeLR = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorActivity.this);
//            builder.setMessage("Are You Sure You Want Delete This Exam?");
//            builder.setTitle("DELETE");
//            builder.setPositiveButton("YES", (dialog, which) -> {
//                int examID = data.get(viewHolder.getAdapterPosition()).getExamID();
//                Backendless.Data.of( "Exam" ).remove( "examID="+examID+"", new AsyncCallback<Integer>()
//                {
//                    @Override
//                    public void handleResponse( Integer objectsDeleted ) {
//                        Toasty.info(ProfessorActivity.this, "Exam Deleted", Toasty.LENGTH_SHORT).show();
//                        data.remove(viewHolder.getAdapterPosition());
////                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void handleFault( BackendlessFault fault ) {
//                        Toasty.error(ProfessorActivity.this, fault.getMessage(), Toasty.LENGTH_SHORT).show();
//                    }
//                } );
//
//            }).setNegativeButton("NO", (dialog, which) ->{
////                    adapter.notifyDataSetChanged()
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    };













//    private String convertTime(String Time, int available){
//        int hours = Short.parseShort(Time.substring(11, 13));//10:00
//        hours = available + 1;//11:00
//
//        if (hours > 12 && hours < 22) {
//            Time = Time.replaceAll(Time.substring(11, 13), "0" + (hours - 12));
//            return Time;
//        }
//        else if (hours >= 22 && hours <= 24) {
//            Time = Time.replaceAll(Time.substring(11, 13), String.valueOf(hours - 12));
//            return Time;
//        }
//        else
//            return Time;
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
//    String cDate,m,y,em,ey;
//    int mm,yy,emm,eyy;
//    public boolean Date(String ExamDate){  //                  01-34-6789 12:45
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
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
//        // exam date = 01-11-2020
//        // current da= 28-10-2020
//
//        return mm <= emm && yy <= eyy;
//    }