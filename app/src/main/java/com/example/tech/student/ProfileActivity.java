package com.example.tech.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Student;
import com.example.tech.register.LoginActivity;
import com.example.tech.register.RegisterActivity;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity implements IPickResult {

    SelectableRoundedImageView imageView;
    Spinner spnTeamProfile, spnDeptProfile;
    Button btnUpdate;
    EditText etNameProfile, etUserNameProfile, etPassWordProfile;
    Bitmap img;
    BackendlessUser user;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    String[] teamArr;
    List<Student> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

        imageView = findViewById(R.id.image);
        btnUpdate = findViewById(R.id.btnUpdate);
        etNameProfile = findViewById(R.id.etNameProfile);
        etUserNameProfile = findViewById(R.id.etUserNameProfile);
        etPassWordProfile = findViewById(R.id.etPassWordProfile);
        spnTeamProfile = findViewById(R.id.spnTeamProfile);
        spnDeptProfile = findViewById(R.id.spnDeptProfile);

//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCornerRadiiDP(1, 0, 0, 0);
        imageView.setBorderWidthDP(2);
        imageView.setBorderColor(R.color.main);
        imageView.setOval(true);

        teamArr = new String[] {"Level1", "Level2", "Level3", "Level4"};
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.spinner_txt, R.id.spnData, teamArr);
        spnTeamProfile.setAdapter(teamAdapter);
        RegisterActivity.getDept(this, spnDeptProfile);

        load();

        imageView.setOnClickListener(v ->
                PickImageDialog.build(Setup()).setOnPickResult(r ->{
                            img = r.getBitmap();
                            getImg(r.getBitmap());
                        }).setOnPickCancel(() -> {}).show(ProfileActivity.this)
        );

        btnUpdate.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Data is About to Change, Are You Sure?");
            builder.setTitle("UPDATING Data");
            builder.setPositiveButton("YES", (dialog, which) -> {
                ProfileActivity.this.dialog.show();
                upload();
            }).setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    private void upload(){
        Toasty.info(this, "Uploading...", Toast.LENGTH_SHORT,true).show();
        if (img != null) {
            try {
                Backendless.Files.Android.upload(img, Bitmap.CompressFormat.PNG, 100,
                        getIntent().getStringExtra("id") + ".png", "myPics", true,
                        new AsyncCallback<BackendlessFile>() {
                            @Override
                            public void handleResponse(BackendlessFile response) {
                                updateWithImage(response.getFileURL(), user);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toasty.error(ProfileActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                            }
                        });
            } catch (Exception e) {
                Toasty.error(this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_LONG).show();
            }
        }
        else
            updateWithoutImage(user);
    }

    private void login(String id, String pass){
        Backendless.UserService.login(id, pass, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                user = response;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.info(ProfileActivity.this, fault.getMessage(), Toast.LENGTH_SHORT,true).show();

            }
        });
    }

    private void updateWithImage(String url, BackendlessUser user){
        Map<String, Object> changes = new HashMap<>();
        changes.put("name", etNameProfile.getText().toString());
        changes.put("id", etUserNameProfile.getText().toString());
        changes.put("pass", etPassWordProfile.getText().toString());
        changes.put("team", spnTeamProfile.getSelectedItem().toString());
        changes.put("dept", spnDeptProfile.getSelectedItem().toString());
        changes.put("img", url);
        Backendless.Data.of(Student.class).update("id = " + getIntent().getStringExtra("id") + "", changes, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                user.setProperty("id", etUserNameProfile.getText().toString());
                user.setPassword(etPassWordProfile.getText().toString());
                user.setProperty("name", etNameProfile.getText().toString());
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        if (!data.get(0).getTeam().equals(spnTeamProfile.getSelectedItem().toString())||!data.get(0).getDept().equals(spnDeptProfile.getSelectedItem().toString())){
                            SharedPreferences pref;
                            SharedPreferences.Editor editor;
                            pref = getSharedPreferences("userData", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putInt("lectures", 0);
                            editor.putInt("exams", 0);
                            editor.putInt("activities", 0);
                            editor.putInt("surveys", 0);
                            editor.putInt("games", 0);
                            editor.apply();
                        }
                        dialog.cancel();
                        Toasty.success(ProfileActivity.this, "Data Updated Successfully", Toasty.LENGTH_LONG, true).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        ProfileActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.success(ProfileActivity.this, "Data Updated Successfully", Toasty.LENGTH_LONG, true).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(ProfileActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    private void updateWithoutImage(BackendlessUser user){
        Map<String, Object> changes = new HashMap<>();
        changes.put("name", etNameProfile.getText().toString());
        changes.put("id", etUserNameProfile.getText().toString());
        changes.put("pass", etPassWordProfile.getText().toString());
        changes.put("team", spnTeamProfile.getSelectedItem().toString());
        changes.put("dept", spnDeptProfile.getSelectedItem().toString());
        Backendless.Data.of(Student.class).update("id = " + getIntent().getStringExtra("id") + "", changes, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                user.setProperty("id", etUserNameProfile.getText().toString());
                user.setPassword(etPassWordProfile.getText().toString());
                user.setProperty("name", etNameProfile.getText().toString());
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        if (!data.get(0).getTeam().equals(spnTeamProfile.getSelectedItem().toString())||!data.get(0).getDept().equals(spnDeptProfile.getSelectedItem().toString())){
                            SharedPreferences pref;
                            SharedPreferences.Editor editor;
                            pref = getSharedPreferences("userData", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putInt("lectures", 0);
                            editor.putInt("exams", 0);
                            editor.putInt("activities", 0);
                            editor.putInt("surveys", 0);
                            editor.putInt("games", 0);
                            editor.apply();
                        }
                        dialog.cancel();
                        Toasty.success(ProfileActivity.this, "Data Updated Successfully", Toasty.LENGTH_LONG, true).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        ProfileActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toasty.success(ProfileActivity.this, "Data Updated Successfully", Toasty.LENGTH_LONG, true).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(ProfileActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    private void load(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("id = '"+getIntent().getStringExtra("id")+"'");
        Backendless.Data.of(Student.class).find(builder, new BackendlessCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                data = response;
                etNameProfile.setText(response.get(0).getName());
                etUserNameProfile.setText(response.get(0).getId());
                etPassWordProfile.setText(response.get(0).getPass());
                for (int i = 0; i < 4; i++) {
                    if (response.get(0).getTeam().equals(teamArr[i])) {
                        spnTeamProfile.setSelection(i);
                        break;
                    }
                }
                ArrayList<String> deptList = new ArrayList<>();
                new CountDownTimer(2000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < spnDeptProfile.getCount(); i++) {
                            deptList.add(spnDeptProfile.getItemAtPosition(i).toString());
                            if (i == spnDeptProfile.getCount()-1){
                                for (int j = 0; j < deptList.size(); j++) {
                                    if (response.get(0).getDept().equals(deptList.get(j))) {
                                        spnDeptProfile.setSelection(j);
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }.start();

                loadImage(response.get(0).getImg());
                login(response.get(0).getId(), response.get(0).getPass());
            }
        });
    }

    private void loadImage(String url){
        Picasso.get().load(url).into(imageView);
        dialog.cancel();
    }

    private PickSetup Setup(){
        return new PickSetup()
                .setWidth(350)
                .setHeight(250)
                .setTitle("Choose Image From:")
                .setTitleColor(R.color.main)
                .setCameraButtonText("Camera")
                .setGalleryButtonText("Gallery")
                .setButtonTextColor(R.color.main)
                .setCancelText("Cancel")
                .setCancelTextColor(R.color.main)
                .setIconGravity(Gravity.TOP)
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setGalleryIcon(R.drawable.ic_baseline_photo_24)
                .setCameraIcon(R.drawable.ic_baseline_photo_camera_24)
                .setGalleryChooserTitle("Choose a Photo")
                .setCameraChooserTitle("Take a Photo")
                .setSystemDialog(false)
                .setProgressText("Getting Photo")
                .setProgressTextColor(R.color.main);
    }

    private void getImg(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        imageView.setImageDrawable(d);
    }

    @Override
    public void onPickResult(PickResult r) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StudentMainActivity.class);
        startActivity(intent);
        this.finish();
    }
}