package com.example.tech.professor;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.StudentsFiles;
import com.example.tech.common.FilesAdapter;
import com.stone.vega.library.VegaLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class StudentsFilesActivity extends AppCompatActivity implements FilesAdapter.onFileListener {

    RecyclerView rvStudentsFiles;
    FilesAdapter adapter;
    SharedPreferences pref;
//    AlertDialog DownloadDialog;
    Dialog DownloadDialog;
//    AlertDialog.Builder builder;
    int position;
    List<StudentsFiles> response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_files);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        rvStudentsFiles = findViewById(R.id.rvStudentsFiles);

        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("profID = "+pref.getString("id", "")+"");
        Backendless.Data.of(StudentsFiles.class).find(builder, new AsyncCallback<List<StudentsFiles>>() {
            @Override
            public void handleResponse(List<StudentsFiles> response) {
                StudentsFilesActivity.this.response = response;

//                activityAdapter = new ActivityAdapter2(response);
//                rvStudentsFiles.setAdapter(adapter);
                rvStudentsFiles.setLayoutManager(new VegaLayoutManager());
                adapter = new FilesAdapter(StudentsFilesActivity.this, response, StudentsFilesActivity.this);
                rvStudentsFiles.setAdapter(adapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(StudentsFilesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onFileClicked(int position) {
        DownloadDialog = new Dialog(this);
        DownloadDialog.setContentView(R.layout.download_file_dialog);
        DownloadDialog.setCancelable(true);
        DownloadDialog.show();

//        LayoutInflater inflater = this.getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.download_dialog, null));
//        builder.setCancelable(true);
//        DownloadDialog = builder.create();
//        DownloadDialog.show();

        ImageView ivDownloadFile;
        ivDownloadFile = DownloadDialog.findViewById(R.id.ivDownloadFile);

        assert ivDownloadFile != null;
        ivDownloadFile.setOnClickListener(v -> downloadFile(position));
    }

    private void downloadFile(int position){
        this.position = position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 1000);
            }
            else
                startDownloading();
        }
        else
            startDownloading();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startDownloading();
            else
                Toasty.error(StudentsFilesActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void startDownloading(){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(response.get(position).getUrl()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading File...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, response.get(position).getFileName());//Environment.DIRECTORY_DOWNLOADS

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        DownloadDialog.cancel();
        Toasty.success(this, "Downloaded File Will Saved in 'Download' Folder in Internal Storage", Toasty.LENGTH_LONG, true).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ProfessorMainActivity.class));
        this.finish();
    }
}