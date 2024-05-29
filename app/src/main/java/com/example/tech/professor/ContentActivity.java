package com.example.tech.professor;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.tech.register.RegisterActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContentActivity extends AppCompatActivity implements ContentAdapter.onContentListener, SwipeRefreshLayout.OnRefreshListener   {

    Button btnGetData;
    AlertDialog ContentDialog, LoadingDialog, DownloadDialog;
    AlertDialog.Builder builder;
    Spinner spnTeamQuery, spnDeptQuery;
    CheckBox cbPDF, cbWORD, cbIMAGE, cbVIDEO;
    SwipeRefreshLayout SRLContent;
    RecyclerView rvFiles;
    ContentAdapter adapter;
    ImageView ivGetContent;
    List<Content> content;
    int position;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.content_query, null));
        builder.setCancelable(true);
        ContentDialog = builder.create();
        ContentDialog.show();

        SRLContent = findViewById(R.id.SRLContent);
        rvFiles = findViewById(R.id.rvFiles);
        SRLContent.setOnRefreshListener(this);
        rvFiles.setLayoutManager(new VegaLayoutManager());

        spnTeamQuery = ContentDialog.findViewById(R.id.spnTeamQuery);
        spnDeptQuery = ContentDialog.findViewById(R.id.spnDeptQuery);
        cbPDF = ContentDialog.findViewById(R.id.cbPDF);
        cbWORD = ContentDialog.findViewById(R.id.cbWORD);
        cbIMAGE = ContentDialog.findViewById(R.id.cbIMAGE);
        cbVIDEO = ContentDialog.findViewById(R.id.cbVIDEO);
        btnGetData = ContentDialog.findViewById(R.id.btnGetData);

        String[] teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeamQuery.setAdapter(teamAdapter);
        RegisterActivity.getDept(this, spnDeptQuery);

        btnGetData.setOnClickListener(v -> getData());

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        builder.setView(inflate.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        LoadingDialog = builder.create();

        ivGetContent = findViewById(R.id.ivGetContent);
        ivGetContent.setOnClickListener(v -> ContentDialog.show());
    }

    private void getData() {
        ContentDialog.cancel();
        LoadingDialog.show();

        String profID = pref.getString("id", "");
        String team = spnTeamQuery.getSelectedItem().toString();
        String dept = spnDeptQuery.getSelectedItem().toString();
        String whereClause = "profID = '"+profID+"' and team = '"+team+"' and dept = '"+dept+"'";
        DataQueryBuilder builder = DataQueryBuilder.create();
        if (cbPDF.isChecked())
            whereClause = whereClause.concat(" and fileType = 'pdf'");
//            builder.setWhereClause("fileType = 'pdf'");
        if (cbWORD.isChecked())
            whereClause = whereClause.concat(" and fileType = 'word'");
//            builder.setWhereClause("fileType = 'word'");
        if (cbIMAGE.isChecked())
            whereClause = whereClause.concat(" and fileType = 'image'");
//            builder.setWhereClause("fileType = 'image'");
        if (cbVIDEO.isChecked())
            whereClause = whereClause.concat(" and fileType = 'video'");
//            builder.setWhereClause("fileType = 'video'");
        builder.setWhereClause(whereClause);
        builder.setPageSize(100);
        Backendless.Data.of(Content.class).find(builder, new AsyncCallback<List<Content>>() {
            @Override
            public void handleResponse(List<Content> response) {
                content = response;
                fillData();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ContentActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillData(){
        adapter = new ContentAdapter(this, content, this, "prof");
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
        ivDownloadContent = DownloadDialog.findViewById(R.id.ivDownloadContent);
        ivPreviewContent = DownloadDialog.findViewById(R.id.ivPreviewContent);
        ivDeleteContent = DownloadDialog.findViewById(R.id.ivDeleteContent);

        assert ivDownloadContent != null;
        ivDownloadContent.setOnClickListener(v -> {
            DownloadDialog.cancel();
            downloadFiles(position);
        });

        assert ivPreviewContent != null;
        ivPreviewContent.setOnClickListener(v -> {
            DownloadDialog.cancel();
            Intent intent = new Intent(this, PreviewFilesActivity.class);
            intent.putExtra("fileType", content.get(position).getFileType());
            intent.putExtra("fileName", content.get(position).getFileName());
            intent.putExtra("url", content.get(position).getUrl());
            intent.putExtra("cat", "prof");
            startActivity(intent);
            this.finish();
        });

        assert ivDeleteContent != null;
        ivDeleteContent.setOnClickListener(v -> {
            DownloadDialog.cancel();
            delete(position);
        });
    }

    private void downloadFiles(int position){
        this.position = position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 1000);
            }
            else {
                startDownloading();
            }
        }
        else{
            startDownloading();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startDownloading();
            else
                Toasty.error(ContentActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void startDownloading(){
        if (!this.getDir("Tech", Context.MODE_PRIVATE).isDirectory())
            this.getDir("Tech", Context.MODE_PRIVATE); //Creating an internal dir;

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
        Toasty.success(this, "Downloaded File Saved in 'Downloads' Folder in Internal Storage", Toasty.LENGTH_LONG, true).show();
    }

    private void delete(int position) {
        LoadingDialog.show();
        String filename = content.get(position).getUrl();
        String urlFolder = "";
        if (content.get(position).getFileType().equals("pdf"))
            urlFolder = "myPDF";
        if (content.get(position).getFileType().equals("word"))
            urlFolder = "myWORD";
        if (content.get(position).getFileType().equals("image"))
            urlFolder = "myIMAGES";
        if (content.get(position).getFileType().equals("video"))
            urlFolder = "myVIDEOS";

        filename = filename.substring(filename.lastIndexOf('/'));

        Backendless.Files.remove(urlFolder + filename, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Backendless.Data.of(Content.class).remove("objectId = '" + content.get(position).getObjectId() + "'", new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        Toasty.success(ContentActivity.this, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                        LoadingDialog.cancel();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.error(ContentActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        LoadingDialog.cancel();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(ContentActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                LoadingDialog.cancel();
            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
        SRLContent.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, UploadContentActivity.class));
        this.finish();
    }
}