package com.example.tech.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.example.tech.R;
import com.example.tech.backendless.classes.Content;
import com.example.tech.register.RegisterActivity;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class UploadContentActivity extends AppCompatActivity {

    ConstraintLayout conPreviewUploadedContent, conPDFUpload, conWORDUpload, conIMAGEUpload, conVIDEOUpload;
    AlertDialog ContentDialog, LoadingDialog;
    AlertDialog.Builder builder;
    Spinner spnTeamContent, spnDeptContent;
    EditText etFileName;
    Button btnUpload;
    String path, fileType;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_content);

        conPDFUpload = findViewById(R.id.conPDFUpload);
        conWORDUpload = findViewById(R.id.conWordUpload);
        conIMAGEUpload = findViewById(R.id.conIMAGEUpload);
        conVIDEOUpload = findViewById(R.id.conVIDEOUpload);
        conPreviewUploadedContent = findViewById(R.id.conPreviewUploadedContent);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        conPDFUpload.setOnClickListener(v -> teamAndDeptDialog("myPDF", "pdf"));
        conWORDUpload.setOnClickListener(v -> teamAndDeptDialog("myWORD", "word"));
        conIMAGEUpload.setOnClickListener(v -> teamAndDeptDialog("myIMAGES", "image"));
        conVIDEOUpload.setOnClickListener(v -> teamAndDeptDialog("myVIDEOS", "video"));
        conPreviewUploadedContent.setOnClickListener(v ->  previewUploadedContent());

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        LoadingDialog = builder.create();
    }

    private void previewUploadedContent() {
        Intent in = new Intent(this, ContentActivity.class);
        startActivity(in);
        this.finish();
    }

    private void teamAndDeptDialog(String path, String fileType){
        this.path = path; this.fileType = fileType;
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.content_dialog, null));
        builder.setCancelable(true);
        ContentDialog = builder.create();
        ContentDialog.show();

        spnTeamContent = ContentDialog.findViewById(R.id.spnTeamContent);
        spnDeptContent = ContentDialog.findViewById(R.id.spnDeptContent);
        etFileName = ContentDialog.findViewById(R.id.etFileName);
        btnUpload = ContentDialog.findViewById(R.id.btnUpload);

        String[] teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeamContent.setAdapter(teamAdapter);

        RegisterActivity.getDept(this, spnDeptContent);

        btnUpload.setOnClickListener(v -> {
            if (etFileName.getText().toString().equals(""))
                Toasty.warning(this, "Please Enter File Name", Toasty.LENGTH_LONG, true).show();
            else
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
                Toasty.error(UploadContentActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void processFile(){
        ContentDialog.cancel();
        FileChooser fileChooser = new FileChooser(UploadContentActivity.this);
        fileChooser.setFileListener(file -> upload(file, path, fileType));
        fileChooser.showDialog();
    }

    private void upload(File File, String path, String fileType) {
        LoadingDialog.show();
        Backendless.Files.upload(File, path, new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                uploadContent(response.getFileURL(), fileType);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(UploadContentActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                LoadingDialog.cancel();
            }
        });
    }

    private void uploadContent(String URL, String fileType){
        Content content = new Content();
        content.setProfID(pref.getString("id", ""));
        content.setTeam(spnTeamContent.getSelectedItem().toString());
        content.setDept(spnDeptContent.getSelectedItem().toString());
        content.setUrl(URL);
        content.setFileType(fileType);
        content.setFileName(etFileName.getText().toString());
        Backendless.Data.of(Content.class).save(content, new AsyncCallback<Content>() {
            @Override
            public void handleResponse(Content response) {
                LoadingDialog.cancel();
                Toasty.success(UploadContentActivity.this, "File Uploaded Successfully!", Toasty.LENGTH_LONG, true).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(UploadContentActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}