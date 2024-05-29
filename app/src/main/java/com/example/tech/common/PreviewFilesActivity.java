package com.example.tech.common;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tech.R;
import com.example.tech.professor.ContentActivity;
import com.example.tech.student.StudentContentActivity;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PreviewFilesActivity extends AppCompatActivity {

    String fileType, fileName, url;
    AlertDialog LoadingDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_files);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);
        LoadingDialog = builder.create();

        fileType = getIntent().getStringExtra("fileType");
        fileName = getIntent().getStringExtra("fileName");
        url = getIntent().getStringExtra("url");

        switch (fileType) {
            case "pdf":
            case "word":
                viewPDF();
                break;
            case "image":
                viewIMAGE();
                break;
            case "video":
                viewVIDEO();
                break;
        }

    }

    private void viewPDF() {
        WebView wvPDF = findViewById(R.id.wvPDF);
        wvPDF.setVisibility(View.VISIBLE);
        wvPDF.getSettings().setJavaScriptEnabled(true);
        wvPDF.getSettings().setBuiltInZoomControls(true);
        wvPDF.setWebChromeClient(new WebChromeClient());
        wvPDF.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
    }

    private void viewIMAGE() {
        LoadingDialog.show();
        ZoomageView zoomView = findViewById(R.id.ZoomView);
        zoomView.setVisibility(View.VISIBLE);
        Picasso.get().load(url).into(zoomView, new Callback() {
            @Override
            public void onSuccess() {
                LoadingDialog.cancel();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void viewVIDEO() {
        LoadingDialog.show();
        ExoVideoView videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.VISIBLE);
        videoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        SimpleMediaSource mediaSource = new SimpleMediaSource(url);
        mediaSource.setDisplayName(fileName);
        videoView.play(mediaSource, false);
        LoadingDialog.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if (getIntent().getStringExtra("cat").equals("stu"))
            intent = new Intent(this, StudentContentActivity.class);
        else
            intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
        this.finish();
    }
}