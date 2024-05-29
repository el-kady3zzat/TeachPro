package com.example.tech.student;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Content;
import com.example.tech.common.ContentAdapter;
import com.example.tech.common.PreviewFilesActivity;
import com.example.tech.game.GameActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class StudentContentActivity extends AppCompatActivity implements ContentAdapter.onContentListener, SwipeRefreshLayout.OnRefreshListener{

    AlertDialog LoadingDialog, DownloadDialog;
    AlertDialog.Builder builder;
    SwipeRefreshLayout SRLContent;
    RecyclerView rvFiles;
    ContentAdapter adapter;
    List<Content> content;
    int position;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_content);

        SRLContent = findViewById(R.id.SRLStudentContent);
        rvFiles = findViewById(R.id.rvStudentFiles);
        SRLContent.setOnRefreshListener(this);
        rvFiles.setLayoutManager(new VegaLayoutManager());

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        builder.setView(inflate.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        LoadingDialog = builder.create();
        getData();
    }

    private void getData() {
        LoadingDialog.show();

        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+pref.getString("team","")+"' ");
        builder.setWhereClause("dept = '"+pref.getString("dept","")+"' ");
        Backendless.Data.of(Content.class).find(builder, new AsyncCallback<List<Content>>() {
            @Override
            public void handleResponse(List<Content> response) {
                content = response;
                fillData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(StudentContentActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillData(){
        adapter = new ContentAdapter(this, content, this, "stu");
        rvFiles.setAdapter(adapter);
        LoadingDialog.cancel();
    }

    @Override
    public void onContentClicked(int position) {
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.download_dialog, null));
        builder.setCancelable(true);
        DownloadDialog = builder.create();
        DownloadDialog.show();

        ImageView ivDownloadContent, ivPreviewContent, ivDeleteContent;
        TextView tv38;
        ivDownloadContent = DownloadDialog.findViewById(R.id.ivDownloadContent);
        ivPreviewContent = DownloadDialog.findViewById(R.id.ivPreviewContent);
        ivDeleteContent = DownloadDialog.findViewById(R.id.ivDeleteContent);
        tv38 = DownloadDialog.findViewById(R.id.textView38);

        assert ivDeleteContent != null;
        ivDeleteContent.setVisibility(View.GONE);
        assert tv38 != null;
        tv38.setVisibility(View.GONE);

        assert ivDownloadContent != null;
        ivDownloadContent.setOnClickListener(v -> downloadFiles(position));

        assert ivPreviewContent != null;
        ivPreviewContent.setOnClickListener(v -> {
            Intent intent = new Intent(this, PreviewFilesActivity.class);
            intent.putExtra("fileType", content.get(position).getFileType());
            intent.putExtra("fileName", content.get(position).getFileName());
            intent.putExtra("url", content.get(position).getUrl());
            intent.putExtra("cat", "stu");
            startActivity(intent);
            badges();
            this.finish();
        });
    }

    private void downloadFiles(int position){
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
                Toasty.error(StudentContentActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void startDownloading(){
//        if (!this.getDir("Tech", Context.MODE_PRIVATE).isDirectory())
//            this.getDir("Tech", Context.MODE_PRIVATE); //Creating an internal dir;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(content.get(position).getUrl()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading File...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, content.get(position).getFileName());//Environment.DIRECTORY_DOWNLOADS

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        DownloadDialog.cancel();
        Toasty.success(this, "Downloaded File Will Saved in 'Download' Folder in Internal Storage", Toasty.LENGTH_LONG, true).show();
        badges();
    }

    @Override
    public void onRefresh() {
        getData();
        SRLContent.setRefreshing(false);
    }

    private void badges(){
        editor = pref.edit();
        editor.putInt("lectures", (pref.getInt("lectures",0) + 1));
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
    }
}