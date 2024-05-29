package com.example.tech.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Professor;
import com.example.tech.backendless.classes.Student;
import com.example.tech.professor.ProfessorActivity;
import com.example.tech.professor.ProfessorMainActivity;
import com.example.tech.student.StudentActivity;
import com.example.tech.student.StudentContentActivity;
import com.example.tech.student.StudentMainActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String cat, stayLogin;
    int intent = 0;
    String id, ID;
    DataQueryBuilder builder = DataQueryBuilder.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Backendless.initApp(this,"56C255CA-8800-7C44-FFAD-D9BC04B33400","DF2B4F63-F6FF-49C4-AA15-AB1DFA74EA23");
//        Backendless.initApp(this,"F5A57444-E0D8-830B-FF39-FECAF8AA9000","DF7D2AFC-902D-40E7-89AC-F8B52188891F");
//        Backendless.initApp(this,"63041062-BCA3-E3A4-FF5F-20B50AC1F900","9E37773D-8F68-4086-B73D-2362E319FED0");
//        Backendless.initApp(this,"F9580F13-23DC-2BEF-FFD9-2F59224FF400","BC8B424C-71D1-40A2-A3CC-FA67B82FAC41");
        Backendless.initApp(this,"09503F5A-7499-8764-FF0D-42A04A22C200","2EC76FE4-30D0-4194-B9D9-9F3BDBED2C4A");

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        id = pref.getString("id", "");
        cat = pref.getString("cat", "");
        stayLogin = pref.getString("stayLogin", "");

        intent = getIntent().getIntExtra("intent", 0);
        if (id.equals("") && intent == 0){
            Intent in = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(in);
            this.finish();
        }
        else {
            if (intent == 1){
                ID = getIntent().getStringExtra("id");
                stayLogin = getIntent().getStringExtra("stayLogin");
                builder.setWhereClause("id = '" + ID + "'");
                Backendless.Data.of(Professor.class).find(builder, new AsyncCallback<List<Professor>>() {
                    @Override
                    public void handleResponse(List<Professor> response) {
                        for (Professor professor : response) {
                            if (ID.equals(professor.getId())) {
                                editor = pref.edit();
                                editor.putString("id", ID);
                                editor.putString("cat", "prof");
                                editor.putString("stayLogin", stayLogin);
                                editor.apply();

                                Intent in = new Intent(SplashActivity.this, ProfessorMainActivity.class);
                                startActivity(in);
                                SplashActivity.this.finish();
                            }
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(SplashActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                    }
                });
                Backendless.Data.of(Student.class).find(builder, new AsyncCallback<List<Student>>() {
                    @Override public void handleResponse(List<Student> response) {///////////////PROBLEM
                        for (Student student : response) {
                            if (ID.equals(student.getId())) {
                                editor = pref.edit();
                                editor.putString("id", ID);
                                editor.putString("cat", "stu");
                                editor.putString("stayLogin", stayLogin);
                                editor.putString("team", student.getTeam());
                                editor.putString("dept", student.getDept());
                                editor.apply();
                                Intent in = new Intent(SplashActivity.this, StudentMainActivity.class);
                                startActivity(in);
                                SplashActivity.this.finish();
                            }
                        }
                    }
                    @Override public void handleFault(BackendlessFault fault) {
                        Toasty.error(SplashActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                    }
                });
            }
            else if (stayLogin.equals("true")) {
                Intent in;
                if (cat.equals("prof")) {
                    in = new Intent(SplashActivity.this, ProfessorMainActivity.class);
                }
                else {
                    in = new Intent(SplashActivity.this, StudentMainActivity.class);
                }
                startActivity(in);
                this.finish();
            }
            else if (stayLogin.equals("false")) {
                Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(in);
                this.finish();
            }
        }
    }
}