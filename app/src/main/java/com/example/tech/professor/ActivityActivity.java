package com.example.tech.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tech.R;
import com.example.tech.backendless.classes.Activity;
import com.example.tech.register.RegisterActivity;

import es.dmoral.toasty.Toasty;

public class ActivityActivity extends AppCompatActivity {

    Spinner spnTeamActivity, spnDeptActivity;
    EditText etActivity;
    Button btnUploadActivity;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        spnTeamActivity = findViewById(R.id.spnTeamActivity);
        spnDeptActivity = findViewById(R.id.spnDeptActivity);
        etActivity = findViewById(R.id.etActivity);
        btnUploadActivity = findViewById(R.id.btnUploadActivity);

        String[] teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeamActivity.setAdapter(teamAdapter);

        RegisterActivity.getDept(this, spnDeptActivity);

        btnUploadActivity.setOnClickListener(v -> upload());
    }

    private void upload() {
        Activity activity = new Activity();
        activity.setProfID(pref.getString("id", ""));
        activity.setTeam(spnTeamActivity.getSelectedItem().toString());
        activity.setDept(spnDeptActivity.getSelectedItem().toString());
        activity.setActivity(etActivity.getText().toString());
        Backendless.Data.of(Activity.class).save(activity, new BackendlessCallback<Activity>(){
            @Override
            public void handleResponse(Activity response) {
                Toasty.success(ActivityActivity.this, "Activity Uploaded Successfully", Toast.LENGTH_LONG, true).show();
                onBackPressed();
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(ActivityActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
        super.onBackPressed();
    }
}