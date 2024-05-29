package com.example.tech.student;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Professor;
import com.example.tech.backendless.classes.Student;
import com.example.tech.backendless.classes.StudentsFiles;
import com.example.tech.professor.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class UploadFilesActivity extends AppCompatActivity {

    Spinner spnProfName;
    EditText etStuFileName;
    Button btnStuUpload;
    List<Professor> response;
    ArrayList<String> names = new ArrayList<>();
    SharedPreferences pref;
    AlertDialog LoadingDialog;
    AlertDialog.Builder builder;
    String StuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);

        spnProfName = findViewById(R.id.spnProfName);
        etStuFileName = findViewById(R.id.etStuFileName);
        btnStuUpload = findViewById(R.id.btnStuUpload);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        Backendless.Data.of(Professor.class).find(new AsyncCallback<List<Professor>>() {
            @Override
            public void handleResponse(List<Professor> response) {
                UploadFilesActivity.this.response = response;
                for (int i = 0; i < response.size(); i++) {
                    names.add(response.get(i).getName());
                    if (i == response.size()-1){
                        ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(UploadFilesActivity.this, R.layout.spinner_txt, R.id.spnData, names);
                        spnProfName.setAdapter(namesAdapter);
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause("id = "+pref.getString("id", "")+"");
        Backendless.Data.of(Student.class).find(new AsyncCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                StuName = response.get(0).getName();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        btnStuUpload.setOnClickListener(v -> {
            if (spnProfName.getSelectedItem().toString().equals("") || etStuFileName.getText().toString().equals("")){
                Toasty.warning(UploadFilesActivity.this, "Please Fill Required Fields First", Toast.LENGTH_LONG, true).show();
                return;
            }
            builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading_dialog, null));
            builder.setCancelable(false);
            LoadingDialog = builder.create();
            requestPermission();
        });
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, 9999);
            }
            else
                processFile();
        }
        else
            processFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9999) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                processFile();
            else
                Toasty.error(UploadFilesActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void processFile(){
        FileChooser fileChooser = new FileChooser(UploadFilesActivity.this);
        fileChooser.setFileListener(this::upload);
        fileChooser.showDialog();
    }

    private void upload(File File) {
        Backendless.Files.upload(File, "StudentsFiles", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                uploadContent(response.getFileURL());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(UploadFilesActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void uploadContent(String URL){
        LoadingDialog.show();
        StudentsFiles studentsFiles = new StudentsFiles();
        studentsFiles.setProfID(response.get(spnProfName.getSelectedItemPosition()).getId());
        studentsFiles.setStuID(pref.getString("id", ""));
        studentsFiles.setStuName(StuName);
        studentsFiles.setUrl(URL);
        studentsFiles.setFileName(etStuFileName.getText().toString());
        Backendless.Data.of(StudentsFiles.class).save(studentsFiles, new AsyncCallback<StudentsFiles>() {
            @Override
            public void handleResponse(StudentsFiles response) {
                LoadingDialog.cancel();
                Toasty.success(UploadFilesActivity.this, "File Uploaded Successfully!", Toasty.LENGTH_LONG, true).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(UploadFilesActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
    }
}