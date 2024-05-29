package com.example.tech.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tech.R;
import com.example.tech.backendless.classes.Departments;
import com.example.tech.backendless.classes.Professor;
import com.example.tech.backendless.classes.Student;
import com.example.tech.common.Capture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity{

    TextView tvTeam, tvDept;
    EditText etID, etName, etPass;
    Spinner spnCat, spnTeam, spnDept;
    Button btnBarCodeR, btnRegister, btnLogin;
    BackendlessUser user = new BackendlessUser();
    final Professor prof = new Professor();
    final Student stu = new Student();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spnCat = findViewById(R.id.spnCat);
        etID = findViewById(R.id.etID);
        btnBarCodeR = findViewById(R.id.btnBarCodeR);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPassword);
        tvTeam = findViewById(R.id.tvTeam);
        spnTeam = findViewById(R.id.spnTeam);
        tvDept = findViewById(R.id.tvDept);
        spnDept = findViewById(R.id.spnDept);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        String[] catArr = {"Professor", "Student"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, catArr);
        spnCat.setAdapter(catAdapter);
        String[] teamArr = {"Team1", "Team2", "Team3", "Team4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeam.setAdapter(teamAdapter);

        getDept(this, spnDept);

        tvTeam.setVisibility(View.GONE);
        tvDept.setVisibility(View.GONE);
        spnTeam.setVisibility(View.GONE);
        spnDept.setVisibility(View.GONE);

        spnCat.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnCat.getSelectedItem().equals("Professor")||spnCat.getSelectedItem().equals("أستاذ")){
                    tvTeam.setVisibility(View.GONE);
                    tvDept.setVisibility(View.GONE);
                    spnTeam.setVisibility(View.GONE);
                    spnDept.setVisibility(View.GONE);
                }
                else {
                    tvTeam.setVisibility(View.VISIBLE);
                    tvDept.setVisibility(View.VISIBLE);
                    spnTeam.setVisibility(View.VISIBLE);
                    spnDept.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(view -> register());
        btnLogin.setOnClickListener(view -> login());
        btnBarCodeR.setOnClickListener(view -> scan());
    }

    public static void getDept(Context context, Spinner spnDept){
        Backendless.Data.of(Departments.class).find(new BackendlessCallback<List<Departments>>() {
            @Override
            public void handleResponse(List<Departments> response) {
                String[] array = new String[response.size()];
                for (int i = 0; i < response.size(); i++) {
                    array[i] = response.get(i).geten();
                }
                ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(context, R.layout.spinner_txt, R.id.spnData, array);
                spnDept.setAdapter(deptAdapter);
            }
        });
    }

    private void register() {
        try {
            if (etID.getText().toString().equals("")||etName.getText().toString().equals("")||etPass.getText().toString().equals(""))
                Toasty.warning(this, "Pleas Fill All Fields First!", Toasty.LENGTH_LONG, true).show();
            else {
                btnRegister.setEnabled(false);
                btnLogin.setEnabled(false);
                user.setProperty("id", etID.getText().toString());
                user.setPassword(etPass.getText().toString());
                user.setProperty("name", etName.getText().toString());
                if (spnCat.getSelectedItem().toString().equals("Professor")||spnCat.getSelectedItem().equals("أستاذ"))
                    user.setProperty("cat", "prof");
                else
                    user.setProperty("cat", "stu");
                try {
                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                        @Override public void handleResponse(BackendlessUser response) {
                            if (spnCat.getSelectedItem().equals("Professor")||spnCat.getSelectedItem().equals("أستاذ")) {
                                try {
                                    prof.setId(etID.getText().toString());
                                    prof.setName(etName.getText().toString());
                                    prof.setPassword(etPass.getText().toString());
//                                    checkLang();
                                    Backendless.Data.of(Professor.class).save(prof, new AsyncCallback<Professor>() {
                                        @Override public void handleResponse(Professor response) {
                                            Toasty.success(RegisterActivity.this, "Registration Success", Toast.LENGTH_LONG, true).show();
                                            Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(in);
                                            RegisterActivity.this.finish();
                                        }
                                        @Override public void handleFault(BackendlessFault fault) {
                                            Toasty.error(RegisterActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                                            btnRegister.setEnabled(true);
                                            btnLogin.setEnabled(true);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    stu.setId(etID.getText().toString());
                                    stu.setName(etName.getText().toString());
                                    stu.setPass(etPass.getText().toString());
                                    stu.setDept(spnDept.getSelectedItem().toString());
                                    stu.setTeam(spnTeam.getSelectedItem().toString());
//                                    checkLang();
                                    Backendless.Data.of(Student.class).save(stu, new AsyncCallback<Student>() {
                                        @Override public void handleResponse(Student response) {
                                            Toasty.success(RegisterActivity.this, "Registration Success", Toast.LENGTH_LONG, true).show();
                                            Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(in);
                                            RegisterActivity.this.finish();
                                        }
                                        @Override public void handleFault(BackendlessFault fault) {
                                            Toasty.error(RegisterActivity.this, fault.getMessage(), Toast.LENGTH_SHORT, true).show();
                                            btnRegister.setEnabled(true);
                                            btnLogin.setEnabled(true);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override public void handleFault(BackendlessFault fault) {
                            Toasty.error(RegisterActivity.this, fault.getMessage(), Toast.LENGTH_SHORT, true).show();
                            btnRegister.setEnabled(true);
                            btnLogin.setEnabled(true);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//    private void checkLang(){
//        switch (spnTeam.getSelectedItem().toString()) {
//            case "الفرقة الأولي":
//                stu.setTeam("Team1");
//                break;
//            case "الفرقة الثانية":
//                stu.setTeam("Team2");
//                break;
//            case "الفرقة الثالثة":
//                stu.setTeam("Team3");
//                break;
//            case "الفرقة الرابعة":
//                stu.setTeam("Team4");
//                break;
//        }
//        switch (spnDept.getSelectedItem().toString()) {
//            case "الحاسب الآلي":
//                stu.setDept("Computer");
//                break;
//            case "التربية الفنية":
//                stu.setDept("Art Education");
//                break;
//            case "الاقتصاد المنزلي":
//                stu.setDept("Home Economics");
//                break;
//            case "الإعلام التربوي":
//                stu.setDept("Educational Media");
//                break;
//        }
//    }

    private void scan() {
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){

            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                etID.setText(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", (dialog, which) -> scanCode()).setNegativeButton("Done", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
                Toasty.info(this, "No Result", Toast.LENGTH_LONG, true).show();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

//    private void dialog(){
////        final View customLayout = getLayoutInflater().inflate(R.layout.loading_dialog, null);
////        builder.setView(customLayout);
////        builder.setCancelable(false);
////        dialog.show();
//    }

    private void login() {
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        this.finish();
    }
}