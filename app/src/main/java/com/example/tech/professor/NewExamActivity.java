package com.example.tech.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.tech.register.RegisterActivity;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class NewExamActivity extends AppCompatActivity {
    Spinner spnTeam, spnDept, spnType, spnTimeType;
    EditText etSubject,etQuesNum,etTimeRange;//, etAvailable;
    SharedPreferences pref, examPref;
    SharedPreferences.Editor editor;
    String id;
    boolean edit;
    int examID;
    TextView tvDateTime ;
    String[] typeArr, timeTypeArr, teamArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);
        spnTeam = findViewById(R.id.spnTeam);
        spnDept = findViewById(R.id.spnDept);
        etSubject = findViewById(R.id.etSubject);
        spnType = findViewById(R.id.spnType);
        spnTimeType = findViewById(R.id.spnTimeType);
        etQuesNum = findViewById(R.id.etQuesNum);
        etTimeRange = findViewById(R.id.etTimeRange);
        tvDateTime = findViewById(R.id.tvDateTime);
        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id", "");

        typeArr = new String[] {"Multi Choice", "True and False"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, typeArr);
        spnType.setAdapter(typeAdapter);

        timeTypeArr = new String[] {"Minutes For Whole Exam", "Minute For Every Question"};
        ArrayAdapter<String> timeTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, timeTypeArr);
        spnTimeType.setAdapter(timeTypeAdapter);

        teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeam.setAdapter(teamAdapter);

        RegisterActivity.getDept(this, spnDept);

        BoomMenuButton bmb = findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if (i == 0){
                HamButton.Builder builder = new HamButton.Builder()
                        .normalColor(R.color.main)
                        .imagePadding(new Rect(5, 5, 5, 5))
                        .normalImageRes(R.drawable.ic_next).shadowEffect(true)
                        .normalTextRes(R.string.Next)
                        .listener(index -> {
                            next();
                        });
                bmb.addBuilder(builder);
            }
            else if (i == 1){
                HamButton.Builder builder = new HamButton.Builder()
                        .normalColor(R.color.main)
                        .imagePadding(new Rect(5, 5, 5, 5))
                        .normalImageRes(R.drawable.ic_baseline_close_24)
                        .shadowEffect(true)
                        .normalTextRes(R.string.cancel)
                        .listener(index -> {
                            cancel();
                        });
                bmb.addBuilder(builder);
            }
        }

        ImageView ivDateTime = findViewById(R.id.ivDateTime);
        ivDateTime.setOnClickListener(v -> dateTimePicker());

        edit = getIntent().getBooleanExtra("edit", false);
        examID = getIntent().getIntExtra("examID", 0);
        if (edit)
            edit();
    }

    public void next() {
        if (edit){
            if (etSubject.getText().toString().equals("")||etQuesNum.getText().toString().equals("")||etTimeRange.getText().toString().equals("")||tvDateTime.getText().equals(""))
                Toasty.warning(this, "Please Fill All Fields!", Toast.LENGTH_LONG, true).show();
            else {
                Map<String, Object> examChanges = new HashMap<>();
                examChanges.put("subject", etSubject.getText().toString());
                examChanges.put("quesNum", etQuesNum.getText().toString());
                examChanges.put("timeRange", etTimeRange.getText().toString());
                examChanges.put("examDate", tvDateTime.getText().toString());
                examChanges.put("team", spnTeam.getSelectedItem().toString());
                examChanges.put("dept", spnDept.getSelectedItem().toString());
                examChanges.put("type", spnType.getSelectedItem().toString());
                examChanges.put("timeType", spnTimeType.getSelectedItem().toString());
                Backendless.Data.of(Exam.class).update("examID = " + examID + "", examChanges, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        Toasty.success(NewExamActivity.this, "Exam Details Edited Successfully!", Toasty.LENGTH_LONG, true).show();
                        Intent intent = new Intent(NewExamActivity.this, QuestionsActivity.class);
                        intent.putExtra("edit", true);
                        intent.putExtra("examID", examID);
                        intent.putExtra("quesNum", etQuesNum.getText().toString());
                        intent.putExtra("type", spnType.getSelectedItem().toString());
                        startActivity(intent);
                        NewExamActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(NewExamActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                    }
                });
            }
        }

        else {
            if (etSubject.getText().toString().equals("") || etQuesNum.getText().toString().equals("") || etTimeRange.getText().toString().equals("") || tvDateTime.getText().equals(""))
                Toasty.warning(this, "Please Fill All Fields!", Toast.LENGTH_LONG, true).show();
            else {
                examPref = getSharedPreferences("examData", MODE_PRIVATE);
                editor = examPref.edit();
                editor.putString("id", id);
                editor.putString("team", spnTeam.getSelectedItem().toString());
                editor.putString("dept", spnDept.getSelectedItem().toString());
                editor.putString("subject", etSubject.getText().toString());
                editor.putString("type", spnType.getSelectedItem().toString());
                editor.putString("quesNum", etQuesNum.getText().toString());
                editor.putString("timeType", spnTimeType.getSelectedItem().toString());
                editor.putString("timeRange", etTimeRange.getText().toString());
                editor.putString("examDate", tvDateTime.getText().toString());
                editor.apply();
                Intent in = new Intent(this, QuestionsActivity.class);
                if (edit) {
                    in.putExtra("edit", true);
                    in.putExtra("examID", examID);
                }
                startActivity(in);
                this.finish();
            }
        }
    }

    public void dateTimePicker() {
        tvDateTime.setText("");
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (View, year, monthOfYear, dayOfMonth) ->
                tvDateTime.append(date(dayOfMonth, monthOfYear + 1, year))
                , mYear, mMonth, mDay);

            final Calendar c1 = Calendar.getInstance();
            int mHour;
            int mMinute;
            mHour = c1.get(Calendar.HOUR_OF_DAY);
            mMinute = c1.get(Calendar.MINUTE);
            final TimePickerDialog timePickerDialog = new TimePickerDialog(NewExamActivity.this, (view1, hourOfDay, minute) ->
                      tvDateTime.append(time(hourOfDay, minute))
                    , mHour, mMinute, true);
            timePickerDialog.show();

        datePickerDialog.show();
    }

    private String date(int day, int month, int year){
        String Day, Month;
        if (day < 10)
            Day = "0" + day;
        else
            Day = day + "";

        if (month < 10)
            Month = "0" + month;
        else
            Month = month + "";

        return(Day + "/" + Month + "/" + year + " ");
    }

    private String time(int Hour, int minute){
        String hour, min;
        if (Hour < 10)
            hour = "0" + Hour;
        else if (Hour == 10 || Hour == 11)
            hour = Hour + "";
        else
            hour = Hour + "";

        if (minute < 10)
            min = "0" + minute;
        else
            min = minute + "";

        return (hour + ":" + min);
    }

    public void cancel() {
        Intent in = new Intent(this, ProfessorActivity.class);
        startActivity(in);
        this.finish();
    }

    private void edit(){
        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                DataQueryBuilder builder = DataQueryBuilder.create();
                builder.setWhereClause("examID = "+examID+"");
                Backendless.Data.of(Exam.class).find(builder, new BackendlessCallback<List<Exam>>() {
                    @Override
                    public void handleResponse(List<Exam> response) {
                        for (int i = 0; i < teamArr.length; i++) {
                            spnTeam.setSelection(i);
                            if (spnTeam.getSelectedItem().toString().equals(response.get(0).getTeam()))
                                break;
                        }
                        for (int i = 0; i < typeArr.length; i++) {
                            spnType.setSelection(i);
                            if (spnType.getSelectedItem().toString().equals(response.get(0).getType()))
                                break;
                        }
                        for (int i = 0; i < timeTypeArr.length; i++) {
                            spnTimeType.setSelection(i);
                            if (spnTimeType.getSelectedItem().toString().equals(response.get(0).getTimeType()))
                                break;
                        }
                        for (int i = 0; i < spnDept.getCount(); i++) {
                            spnDept.setSelection(i);
                            if (spnDept.getSelectedItem().toString().equals(response.get(0).getDept()))
                                break;
                        }
                        etSubject.setText(response.get(0).getSubject());
                        etQuesNum.setText(response.get(0).getQuesNum());
                        etTimeRange.setText(response.get(0).getTimeRange());
                        tvDateTime.setText(response.get(0).getExamDate());
                    }
                });
            }
        }.start();
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UploadExamActivity.class);
        startActivity(intent);
        this.finish();
    }
}
















//    public void dateTime(View view) {
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                if (dayOfMonth <= 9)
//                    tvDateTime.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + " ");
//                else if (monthOfYear <= 9)
//                    tvDateTime.setText(dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year + " ");
//                else
//                    tvDateTime.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + " ");
//
//                final Calendar c = Calendar.getInstance();
//                mHour = c.get(Calendar.HOUR_OF_DAY);
////                mMinute = c.get(Calendar.MINUTE);
//                TimePickerDialog timePickerDialog = new TimePickerDialog(NewExamActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        if (minute <= 9) {
//                            if (hourOfDay < 10)
//                                tvDateTime.append("0"+hourOfDay + ":" + "0" + "0");
//                            else if (hourOfDay > 12 &&  hourOfDay < 22)
//                                tvDateTime.append("0"+(hourOfDay-12) + ":" + "0" + "0");
//                            else if (hourOfDay == 22 || hourOfDay == 23 || hourOfDay == 24)
//                                tvDateTime.append((hourOfDay-12) + ":" + "0" + "0");
//                            else
//                                tvDateTime.append(hourOfDay + ":" + "0" + "0");
//                        }
//                        else {
//                            if (hourOfDay < 10)
//                                tvDateTime.append("0"+hourOfDay + ":" + "00");
//                            else if (hourOfDay > 12 &&  hourOfDay < 22)
//                                tvDateTime.append("0"+(hourOfDay-12) + ":" + "00");
//                            else if (hourOfDay == 22 || hourOfDay == 23 || hourOfDay == 24)
//                                tvDateTime.append((hourOfDay-12) + ":" + "00");
//                            else
//                                tvDateTime.append(hourOfDay + ":" + "00");
//                        }
//                    }
//                }, mHour, mMinute, false);
//                timePickerDialog.show();
//            }
//        }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }