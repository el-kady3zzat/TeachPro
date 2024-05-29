package com.example.tech.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.GPA;
import com.example.tech.backendless.classes.Result;
import com.example.tech.backendless.classes.Student;
import com.example.tech.common.StudentsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class WinnersActivity extends AppCompatActivity {

    KonfettiView konfettiView;
    ImageView ivFirst, ivSecond, ivThird;
    TextView tvFirst, tvSecond, tvThird;
    SharedPreferences pref;
    RecyclerView rvStudents;
    StudentsAdapter adapter;
    List<Result> total = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        konfettiView = findViewById(R.id.konfettiView);
        ivFirst = findViewById(R.id.ivFirst);
        ivSecond = findViewById(R.id.ivSecond);
        ivThird = findViewById(R.id.ivThird);
        tvFirst = findViewById(R.id.tvFirst);
        tvSecond = findViewById(R.id.tvSecond);
        tvThird = findViewById(R.id.tvThird);
        rvStudents = findViewById(R.id.rvStudents);

        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        getWinners();
        celebrate(this, konfettiView);

        rvStudents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState != AbsListView.OnScrollListener.SCROLL_STATE_FLING && newState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    assert layoutManager != null;
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisible = layoutManager.findLastVisibleItemPosition();

                    boolean endHasBeenReached = lastVisible + 10 >= totalItemCount;
                    if (totalItemCount > 0 && endHasBeenReached) {
                        DataQueryBuilder builder = DataQueryBuilder.create();
                        builder.setWhereClause("team = '" + pref.getString("team", "") + "' and dept = '" + pref.getString("dept", "") + "'");
                        builder.setSortBy("finalResult DESC");
                        builder.setPageSize(10).setOffset(totalItemCount);
                        Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
                            @Override
                            public void handleResponse(List<Result> response) {
                                if (response.size() != 0) {
                                    total.addAll(response);
                                    adapter = new StudentsAdapter(WinnersActivity.this, total);
                                    rvStudents.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toasty.error(WinnersActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }
                }
            }


//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (firstScroll) {
//                    firstScroll = false;
//                } else {
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    assert layoutManager != null;
//                    int totalItemCount = layoutManager.getItemCount();
//                    int lastVisible = layoutManager.findLastVisibleItemPosition();
//
//                    boolean endHasBeenReached = lastVisible + 10 >= totalItemCount;
//                    if (totalItemCount > 0 && endHasBeenReached) {
//                        Toast.makeText(WinnersActivity.this, "bottom", Toast.LENGTH_SHORT).show();
//                        //you have reached to the bottom of your recycler view
//                        DataQueryBuilder builder = DataQueryBuilder.create();
//                        builder.setWhereClause("team = '" + pref.getString("team", "") + "' and dept = '" + pref.getString("dept", "") + "'");
//                        builder.setSortBy("gpa DESC");
//                        builder.setPageSize(10).setOffset(10);
//                        Backendless.Data.of(GPA.class).find(builder, new AsyncCallback<List<GPA>>() {
//                            @Override
//                            public void handleResponse(List<GPA> response) {
//                                total.addAll(response);
//                                adapter = new StudentsAdapter(WinnersActivity.this, total);
//                                rvStudents.setAdapter(adapter);
//                            }
//
//                            @Override
//                            public void handleFault(BackendlessFault fault) {
//                                Toasty.error(WinnersActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//                            }
//                        });
//                    }
//
//                }
//            }
        });
    }

    private void getWinners() {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+pref.getString("team","")+"' and dept = '"+pref.getString("dept","")+"'");
        builder.setSortBy("finalResult DESC");
        Backendless.Data.of(Result.class).find(builder, new AsyncCallback<List<Result>>() {
            @Override
            public void handleResponse(List<Result> response) {
                if (response.size() == 0){
                    Toasty.info(WinnersActivity.this, "No Data For Now!", Toast.LENGTH_LONG, true).show();
                    onBackPressed();
                    return;
                }
                else if (response.size() == 1){
                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
                }
                else if (response.size() == 2){
                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
                    tvSecond.setText(getFirstName(response.get(1).getStuName()));
                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
                    getImage("id = '"+response.get(1).getStuID()+"'", ivSecond);
                }
                else {
                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
                    tvSecond.setText(getFirstName(response.get(1).getStuName()));
                    tvThird.setText(getFirstName(response.get(2).getStuName()));
                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
                    getImage("id = '"+response.get(1).getStuID()+"'", ivSecond);
                    getImage("id = '"+response.get(2).getStuID()+"'", ivThird);
                }
                adapter = new StudentsAdapter(WinnersActivity.this, response);
                rvStudents.setAdapter(adapter);
                total.addAll(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(WinnersActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });


//        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
//        dataQueryBuilder.setGroupBy("stuID");
//        dataQueryBuilder.setSortBy("finalResult DESC");
//        Backendless.Data.of(Result.class).find( dataQueryBuilder, new AsyncCallback<List<Result>>()
//        {
//            @Override
//            public void handleResponse( List<Result> response ) {
//                if (response.size() == 0){
//                    Toasty.info(WinnersActivity.this, "No Data For Now!", Toast.LENGTH_LONG, true).show();
//                    onBackPressed();
//                }
//                else if (response.size() == 1){
//                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
//                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
//                }
//                else if (response.size() == 2){
//                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
//                    tvSecond.setText(getFirstName(response.get(1).getStuName()));
//                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
//                    getImage("id = '"+response.get(1).getStuID()+"'", ivSecond);
//                }
//                else {
//                    tvFirst.setText(getFirstName(response.get(0).getStuName()));
//                    tvSecond.setText(getFirstName(response.get(1).getStuName()));
//                    tvThird.setText(getFirstName(response.get(2).getStuName()));
//                    getImage("id = '"+response.get(0).getStuID()+"'", ivFirst);
//                    getImage("id = '"+response.get(1).getStuID()+"'", ivSecond);
//                    getImage("id = '"+response.get(2).getStuID()+"'", ivThird);
//                }
//            }
//
//            @Override
//            public void handleFault( BackendlessFault fault ) {
//                Toasty.error(WinnersActivity.this, fault.getMessage(), Toast.LENGTH_LONG, true).show();
//            }
//        });
    }

    private String getFirstName(String name){
        if (name.contains(" "))
            return name.substring(0, name.indexOf(" "));
        else
            return name;
    }

    private void getImage(String whereClause, ImageView view){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause(whereClause);
        Backendless.Data.of(Student.class).find(builder, new BackendlessCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                    Picasso.get().load(response.get(0).getImg()).into(view);
            }
        });
    }

    public static void celebrate(Context context, KonfettiView konfettiView){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(Objects.requireNonNull(drawable), true);
        EmitterConfig emitterConfig = new Emitter(50L, TimeUnit.MINUTES).perSecond(100);
        Party party = new PartyFactory(emitterConfig)
                .angle(360)
                .spread(360)
                .setSpeedBetween(1f, 5f)
                .timeToLive(20000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();

        konfettiView.start(party);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
        super.onBackPressed();
    }
}