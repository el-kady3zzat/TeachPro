package com.example.tech.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.GPA;
import com.example.tech.backendless.classes.Student;
import com.example.tech.game.Game;
import com.example.tech.game.GameResult;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class BadgesActivity extends AppCompatActivity {

    Button btnComplex, btnDetailed;
    ConstraintLayout clComplex, clDetailed;
    ImageView ivLecturesEasy, ivLecturesMedium, ivLecturesHard;
    ImageView ivExamsEasy, ivExamsMedium, ivExamsHard;
    ImageView ivActivitiesEasy, ivActivitiesMedium, ivActivitiesHard;
    ImageView ivSurveysEasy, ivSurveysMedium, ivSurveysHard;
    ImageView ivGamesEasy, ivGamesMedium, ivGamesHard;
    ImageView ivJunior, ivJunior2;
    ImageView ivEasy, ivMedium, ivHard, ivBadgesProfile;
    TextView tv3;

    SharedPreferences pref;
    String id;
    short counter = 0;

    AlertDialog dialog;
    AlertDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        pref = getSharedPreferences("userData", MODE_PRIVATE);
        id = pref.getString("id","");

        AlertDialog.Builder builder1;
        builder1 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder1.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder1.setCancelable(false);
        LoadingDialog = builder1.create();
        LoadingDialog.show();

        initializeViews();
        ivEasy.setImageAlpha(50);
        ivMedium.setImageAlpha(50);
        ivHard.setImageAlpha(50);
        ivJunior2.setImageAlpha(50);
        complex();

//        btnComplex.setOnClickListener(v -> {
//            complex();
//            btnComplex.setElevation(1000);
//            btnDetailed.setElevation(0);
//            clComplex.setVisibility(View.VISIBLE);
//            clDetailed.setVisibility(View.GONE);
//        });
//        btnDetailed.setOnClickListener(v -> {
//            detailed();
//            btnComplex.setElevation(0);
//            btnDetailed.setElevation(1000);
//            clDetailed.setVisibility(View.VISIBLE);
//            clComplex.setVisibility(View.GONE);
//        });
        findViewById(R.id.ivInfo).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Badges Information");
            builder.setMessage("to win first badge you should get 90% or more of easy game mode, second one 90% of medium mode, and third one 90% of difficult mode");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
            dialog = builder.create();
            dialog.show();
        });
    }

    private void initializeViews() {
        clComplex = findViewById(R.id.clComplex);
        clDetailed = findViewById(R.id.clDetailed);

        btnComplex = findViewById(R.id.btnComblex);
        btnDetailed = findViewById(R.id.btnDetailed);

        ivLecturesEasy = findViewById(R.id.ivLecturesEasy);
        ivLecturesMedium = findViewById(R.id.ivLecturesMedium);
        ivLecturesHard = findViewById(R.id.ivLecturesHard);

        ivExamsEasy = findViewById(R.id.ivExamsEasy);
        ivExamsMedium = findViewById(R.id.ivExamsMedium);
        ivExamsHard = findViewById(R.id.ivExamsHard);

        ivActivitiesEasy = findViewById(R.id.ivActivitiesEasy);
        ivActivitiesMedium = findViewById(R.id.ivActivitiesMedium);
        ivActivitiesHard = findViewById(R.id.ivActivitiesHard);

        ivSurveysEasy = findViewById(R.id.ivSurveysEasy);
        ivSurveysMedium = findViewById(R.id.ivSurveysMedium);
        ivSurveysHard = findViewById(R.id.ivSurveysHard);

        ivGamesEasy = findViewById(R.id.ivGamesEasy);
        ivGamesMedium = findViewById(R.id.ivGamesMedium);
        ivGamesHard = findViewById(R.id.ivGamesHard);

        ivJunior = findViewById(R.id.ivJunior);
        ivJunior2 = findViewById(R.id.ivJunior2);

        ivEasy = findViewById(R.id.ivEasy);
        ivMedium = findViewById(R.id.ivMedium);
        ivHard = findViewById(R.id.ivHard);

        ivBadgesProfile = findViewById(R.id.ivBadgesProfile);
        tv3 = findViewById(R.id.tv3);
    }

    private String getFirstName(String name){
        if (name.contains(" "))
            return name.substring(0, name.indexOf(" "));
        else
            return name;
    }

    List<Integer> Ids = new ArrayList<>();
    String name;
    int quesNumEasy = 0, quesNumMedium = 0, quesNumDifficult = 0, totalQues = 0,  sum = 0;
    private void complex() {
        DataQueryBuilder queryBuilder1 = DataQueryBuilder.create();
        queryBuilder1.setWhereClause("id = '"+pref.getString("id","")+"'");
        Backendless.Data.of(Student.class).find(queryBuilder1, new AsyncCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                name = getFirstName(response.get(0).getName());
                Picasso.get().load(response.get(0).getImg()).into(ivBadgesProfile);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });




        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getSumOfEasyQues();
                getSumOfMediumQues();
                getSumOfDifficultQues();
//                getSumOfQuesByLevel("easy");
//                getSumOfQuesByLevel("medium");
//                getSumOfQuesByLevel("hard");
//                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
//                queryBuilder.setWhereClause("stuID = '"+pref.getString("id","")+"'");
//                Backendless.Data.of(GPA.class).find(queryBuilder, new AsyncCallback<List<GPA>>() {
//                    @Override
//                    public void handleResponse(List<GPA> response) {
//                        if (response.size() == 0){
//                            ivEasy.setImageAlpha(50);
//                            ivMedium.setImageAlpha(50);
//                            ivHard.setImageAlpha(50);
//                            ivJunior2.setImageAlpha(50);
//                        }
//                        else {
//                            if (response.get(0).getGpa() >= 100 && response.get(0).getGpa() < 300) {
//                                tv3.append("Congratulation "+name+" You Got ACTIVENESS Badge");
//                                ivMedium.setImageAlpha(50);
//                                ivHard.setImageAlpha(50);
//                                ivJunior2.setImageAlpha(50);
//                            }
//                            else if (response.get(0).getGpa() >= 300 && response.get(0).getGpa() < 600) {
//                                tv3.append("Congratulation "+name+" You Got PERSISTENCE Badge");
//                                ivHard.setImageAlpha(50);
//                                ivJunior2.setImageAlpha(50);
//                            }
//                            else if (response.get(0).getGpa() >= 600 && response.get(0).getGpa() < 1000) {
//                                tv3.append("Congratulation " + name + " You Got CHAMPION Badge");
//                                ivJunior2.setImageAlpha(50);
//                            }
//                            else {
//                                tv3.append("Congratulation " + name + " You Got GRADUATE Badge");
//                            }
//                        }
//                        LoadingDialog.cancel();
//                    }
//
//                    @Override
//                    public void handleFault(BackendlessFault fault) {
//                        Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//                    }
//                });
            }
        }.start();
    }

    private void getSumOfEasyQues(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("(Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+") and level='easy' ");
        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
            @Override
            public void handleResponse(List<Game> response) {
                for (int i = 0; i < response.size(); i++) {
                    quesNumEasy = quesNumEasy + response.get(i).getQuesNum();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }
    private void getSumOfMediumQues(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("(Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+") and level='medium' ");
        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
            @Override
            public void handleResponse(List<Game> response) {
                for (int i = 0; i < response.size(); i++) {
                    quesNumMedium = quesNumMedium + response.get(i).getQuesNum();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }
    private void getSumOfDifficultQues(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("(Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+") and level='difficult' ");
        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
            @Override
            public void handleResponse(List<Game> response) {
                for (int i = 0; i < response.size(); i++) {
                    quesNumDifficult = quesNumDifficult + response.get(i).getQuesNum();
                    if (i == response.size() - 1) {
                        getSumOfPlayer1Easy();
                        getSumOfPlayer2Easy();
                        getSumOfPlayer3Easy();
                        getSumOfPlayer4Easy();
                        getSumOfPlayer1Medium();
                        getSumOfPlayer2Medium();
                        getSumOfPlayer3Medium();
                        getSumOfPlayer4Medium();
                        getSumOfPlayer1Difficult();
                        getSumOfPlayer2Difficult();
                        getSumOfPlayer3Difficult();
                        getSumOfPlayer4Difficult();
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }


    //////////////////////////////////////////
//    private void getResultOfEasyLevel(int gameID){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("gameID = "+gameID+"");
//        Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
//            @Override
//            public void handleResponse(List<GameResult> response) {
//                for (int i = 0; i < response.size(); i++) {
//                    Toast.makeText(BadgesActivity.this, "", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//            }
//        });
//    }
    //////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //EASY
    DataQueryBuilder builder = DataQueryBuilder.create();
    int totalSumP1Easy = 0, totalSumP2Easy = 0, totalSumP3Easy = 0, totalSumP4Easy = 0, totalSumEasy = 0;
    private void getSumOfPlayer1Easy(){
        builder.setWhereClause("player1ID = "+id+" and level = 'easy'");
        Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP1Easy = totalSumP1Easy + response.get(i).getPlayer1Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer2Easy(){
        builder.setWhereClause("player2ID = "+id+" and level = 'easy'");
        Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP2Easy = totalSumP2Easy + response.get(i).getPlayer2Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer3Easy(){
        builder.setWhereClause("player3ID = "+id+" and level = 'easy'");
        Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP3Easy = totalSumP3Easy + response.get(i).getPlayer3Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer4Easy(){
        builder.setWhereClause("player4ID = "+id+" and level = 'easy'");
        Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                if (response.size() == 0){
                    totalSumEasy = totalSumP1Easy + totalSumP2Easy + totalSumP3Easy + totalSumP4Easy;
                }
                else {
                    for (int i = 0; i < response.size(); i++) {
                        totalSumP4Easy = totalSumP4Easy + response.get(i).getPlayer4Result();
                        if (i == response.size() - 1) {
                            totalSumEasy = totalSumP1Easy + totalSumP2Easy + totalSumP3Easy + totalSumP4Easy;
                        }
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Medium
    DataQueryBuilder builder1 = DataQueryBuilder.create();
    int totalSumP1Medium = 0, totalSumP2Medium = 0, totalSumP3Medium = 0, totalSumP4Medium = 0, totalSumMedium = 0;
    private void getSumOfPlayer1Medium(){
        builder1.setWhereClause("player1ID = "+id+" and level = 'medium'");
        Backendless.Data.of(GameResult.class).find(builder1, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP1Medium = totalSumP1Medium + response.get(i).getPlayer1Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer2Medium(){
        builder1.setWhereClause("player2ID = "+id+" and level = 'medium'");
        Backendless.Data.of(GameResult.class).find(builder1, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP2Medium = totalSumP2Medium + response.get(i).getPlayer2Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer3Medium(){
        builder1.setWhereClause("player3ID = "+id+" and level = 'medium'");
        Backendless.Data.of(GameResult.class).find(builder1, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP3Medium = totalSumP3Medium + response.get(i).getPlayer3Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer4Medium(){
        builder1.setWhereClause("player4ID = "+id+" and level = 'medium'");
        Backendless.Data.of(GameResult.class).find(builder1, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                if (response.size() == 0){
                    totalSumMedium = totalSumP1Medium + totalSumP2Medium + totalSumP3Medium + totalSumP4Medium;
                }
                else {
                    for (int i = 0; i < response.size(); i++) {
                        totalSumP4Medium = totalSumP4Medium + response.get(i).getPlayer4Result();
                        if (i == response.size() - 1) {
                            totalSumMedium = totalSumP1Medium + totalSumP2Medium + totalSumP3Medium + totalSumP4Medium;
                        }
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Difficult
    DataQueryBuilder builder2 = DataQueryBuilder.create();
    int totalSumP1Difficult = 0, totalSumP2Difficult = 0, totalSumP3Difficult = 0, totalSumP4Difficult = 0, totalSumDifficult = 0;
    private void getSumOfPlayer1Difficult(){
        builder2.setWhereClause("player1ID = "+id+" and level = 'difficult'");
        Backendless.Data.of(GameResult.class).find(builder2, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP1Difficult = totalSumP1Difficult + response.get(i).getPlayer1Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer2Difficult(){
        builder2.setWhereClause("player2ID = "+id+" and level = 'difficult'");
        Backendless.Data.of(GameResult.class).find(builder2, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP2Difficult = totalSumP2Difficult + response.get(i).getPlayer2Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer3Difficult(){
        builder2.setWhereClause("player3ID = "+id+" and level = 'difficult'");
        Backendless.Data.of(GameResult.class).find(builder2, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                for (int i = 0; i < response.size(); i++) {
                    totalSumP3Difficult = totalSumP3Difficult + response.get(i).getPlayer3Result();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getSumOfPlayer4Difficult(){
        builder2.setWhereClause("player4ID = "+id+" and level = 'difficult'");
        Backendless.Data.of(GameResult.class).find(builder2, new AsyncCallback<List<GameResult>>() {
            @Override
            public void handleResponse(List<GameResult> response) {
                if (response.size() == 0){
                    LoadingDialog.cancel();
                    totalSumDifficult = totalSumP1Difficult + totalSumP2Difficult + totalSumP3Difficult + totalSumP4Difficult;
                    setAlpha();
                }
                else {
                    for (int i = 0; i < response.size(); i++) {
                        totalSumP4Difficult = totalSumP4Difficult + response.get(i).getPlayer4Result();
                        if (i == response.size() - 1) {
                            LoadingDialog.cancel();
                            totalSumDifficult = totalSumP1Difficult + totalSumP2Difficult + totalSumP3Difficult + totalSumP4Difficult;
                            setAlpha();
                        }
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BadgesActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void setAlpha(){
        if ((totalSumEasy/quesNumEasy)*100 >= 90) {
            ivEasy.setImageAlpha(255);
            tv3.setText("");
            tv3.append("Congratulation "+name+" You Got ACTIVENESS Badge");
        }
        if ((totalSumMedium/quesNumMedium)*100 >= 90) {
            ivMedium.setImageAlpha(255);
            tv3.setText("");
            tv3.append("Congratulation "+name+" You Got PERSISTENCE Badge");
        }
        if ((totalSumDifficult/quesNumDifficult)*100 >= 90) {
            ivHard.setImageAlpha(255);
            tv3.setText("");
            tv3.append("Congratulation "+name+" You Got CHAMPION Badge");
        }
        if ((totalSumEasy/quesNumEasy)*100 >= 90 && (totalSumMedium/quesNumMedium)*100 >= 90 && (totalSumDifficult/quesNumDifficult)*100 >= 90) {
            ivJunior2.setImageAlpha(255);
            tv3.setText("");
            tv3.append("WOW "+name+" You Got GRADUATE Badge");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StudentMainActivity.class));
        this.finish();
    }



//    private void getSumOfEasyResults(String level){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        for (int j : Ids) {
//            builder.setWhereClause("gameID=" + j + " ");
//            Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
//                @Override
//                public void handleResponse(List<GameResult> response) {
//                    for (int i = 0; i < response.size(); i++) {
//                        if (response.get(i).getPlayer1ID().equals(id))
//                            sum = sum + response.get(i).getPlayer1Result();
//                        else if (response.get(i).getPlayer2ID().equals(id)) {
//                            sum = sum + response.get(i).getPlayer2Result();
//                        } else if (response.get(i).getPlayer3ID().equals(id)) {
//                            sum = sum + response.get(i).getPlayer3Result();
//                        } else {
//                            sum = sum + response.get(i).getPlayer4Result();
//                        }
//
//                        if (i == response.size() -1){
//                            Toast.makeText(BadgesActivity.this, "quesNum: "+quesNum, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(BadgesActivity.this, "sum: "+sum, Toast.LENGTH_SHORT).show();
//                            if (level.equals("easy") && ((sum/quesNum)*100) <= 90){
//                                ivEasy.setImageAlpha(50);
//                                Toast.makeText(BadgesActivity.this, "sum = "+(sum/quesNum)*100, Toast.LENGTH_SHORT).show();
//                            }
////                            else if (level.equals("medium") && ((sum/quesNum)*100) >= 90)
////                                ivExamsMedium.setImageAlpha(50);
////                            else
////                                ivExamsHard.setImageAlpha(50);
//                            LoadingDialog.cancel();
//                        }
//                    }
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//                    Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//                }
//            });
//        }
//    }
//
//    int j = 0;
//    private void getSumOfResults(String level){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        new CountDownTimer(1000,  1000L) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                builder.setWhereClause("gameID=" + Ids.get(j) + " ");
//                Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
//                    @Override
//                    public void handleResponse(List<GameResult> response) {
//                        for (int i = 0; i < response.size(); i++) {
//                            if (response.get(i).getPlayer1ID().equals(id)){
//                                sum = sum + response.get(i).getPlayer1Result();
//                                Toast.makeText(BadgesActivity.this, "sum: " + i +": " + sum, Toast.LENGTH_SHORT).show();
//                            }
//                            else if (response.get(i).getPlayer2ID().equals(id)) {
//                                sum = sum + response.get(i).getPlayer2Result();
//                            } else if (response.get(i).getPlayer3ID().equals(id)) {
//                                sum = sum + response.get(i).getPlayer3Result();
//                            } else {
//                                sum = sum + response.get(i).getPlayer4Result();
//                            }
//
//                            if (i == response.size() -1){
//                                if (level.equals("easy") && ((sum/quesNum)*100) >= 90){
//                                    ivEasy.setImageAlpha(255);
//                                }
//                                else if (level.equals("medium") && ((sum/quesNum)*100) >= 90)
//                                    ivMedium.setImageAlpha(50);
//                                else
//                                    ivHard.setImageAlpha(50);
//                                LoadingDialog.cancel();
//                                j = 1;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void handleFault(BackendlessFault fault) {
//                        Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//                    }
//                });
//            }
//        }.start();
////        for (int j : Ids) {//Player1ID=" + id + " or Player2ID=" + id + " or Player3ID=" + id + " or Player4ID=" + id + " and
////            builder.setWhereClause("gameID=" + j + " ");
////            Backendless.Data.of(GameResult.class).find(builder, new AsyncCallback<List<GameResult>>() {
////                @Override
////                public void handleResponse(List<GameResult> response) {
////                    for (int i = 0; i < response.size(); i++) {
////                        if (response.get(i).getPlayer1ID().equals(id)){
////                            sum = sum + response.get(i).getPlayer1Result();
////                            Toast.makeText(BadgesActivity.this, "sum: " + i +": " + sum, Toast.LENGTH_SHORT).show();
////                        }
////                        else if (response.get(i).getPlayer2ID().equals(id)) {
////                            sum = sum + response.get(i).getPlayer2Result();
////                        } else if (response.get(i).getPlayer3ID().equals(id)) {
////                            sum = sum + response.get(i).getPlayer3Result();
////                        } else {
////                            sum = sum + response.get(i).getPlayer4Result();
////                        }
////
////                        if (i == response.size() -1){
////                            if (level.equals("easy") && ((sum/quesNum)*100) >= 90){
////                                ivEasy.setImageAlpha(255);
////                            }
////                            else if (level.equals("medium") && ((sum/quesNum)*100) >= 90)
////                                ivMedium.setImageAlpha(50);
////                            else
////                                ivHard.setImageAlpha(50);
////                        }
////                        LoadingDialog.cancel();
////                    }
////                }
////
////                @Override
////                public void handleFault(BackendlessFault fault) {
////                    Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
////                }
////            });
////        }
//    }
}





//    private void detailed() {
//        easyGames();
//        mediumGames();
//        hardGames();
//
//
//        String[] Names = {"lectures","activities","surveys","games"};
//
//        ImageView[] EasyViews = {ivLecturesEasy,ivActivitiesEasy,ivSurveysEasy,ivGamesEasy};
//        ImageView[] MediumViews = {ivLecturesMedium,ivActivitiesMedium,ivSurveysMedium,ivGamesMedium};
//        ImageView[] HardViews = {ivLecturesHard,ivActivitiesHard,ivSurveysHard,ivGamesHard};
//
//        for (int i = 0; i < 4; i++) {
//            alphaEasy(Names[i], EasyViews[i]);
//        }
//        for (int i = 0; i < 4; i++) {
//            alphaMedium(Names[i], MediumViews[i]);
//        }
//        for (int i = 0; i < 4; i++) {
//            alphaHard(Names[i], HardViews[i]);
//        }
//
////        String[] Names = {"lectures","exams","activities","surveys","games"};
////
////        ImageView[] EasyViews = {ivLecturesEasy,ivExamsEasy,ivActivitiesEasy,ivSurveysEasy,ivGamesEasy};
////        ImageView[] MediumViews = {ivLecturesMedium,ivExamsMedium,ivActivitiesMedium,ivSurveysMedium,ivGamesMedium};
////        ImageView[] HardViews = {ivLecturesHard,ivExamsHard,ivActivitiesHard,ivSurveysHard,ivGamesHard};
////
////        for (int i = 0; i < 5; i++) {
////            alphaEasy(Names[i], EasyViews[i]);
////        }
////        for (int i = 0; i < 5; i++) {
////            alphaMedium(Names[i], MediumViews[i]);
////        }
////        for (int i = 0; i < 5; i++) {
////            alphaHard(Names[i], HardViews[i]);
////        }
//    }
//
//    private void easyGames(){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+" and level=easy ");
//        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
//            @Override
//            public void handleResponse(List<Game> response) {
//                Toast.makeText(BadgesActivity.this, response.size()+"", Toast.LENGTH_SHORT).show();
//                if (response.size() < 3)
//                    ivExamsEasy.setImageAlpha(50);
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//            }
//        });
//    }
//    private void mediumGames(){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+" and level=medium ");
//        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
//            @Override
//            public void handleResponse(List<Game> response) {
//                Toast.makeText(BadgesActivity.this, response.size()+"", Toast.LENGTH_SHORT).show();
//                if (response.size() < 3)
//                    ivExamsMedium.setImageAlpha(50);
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//            }
//        });
//    }
//    private void hardGames(){
//        DataQueryBuilder builder = DataQueryBuilder.create();
//        builder.setWhereClause("Player1ID="+id+" or Player2ID="+id+" or Player3ID="+id+" or Player4ID="+id+" and level=hard ");
//        Backendless.Data.of(Game.class).find(builder, new AsyncCallback<List<Game>>() {
//            @Override
//            public void handleResponse(List<Game> response) {
//                Toast.makeText(BadgesActivity.this, response.size()+"", Toast.LENGTH_SHORT).show();
//                if (response.size() < 3)
//                    ivExamsHard.setImageAlpha(50);
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toasty.error(BadgesActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
//            }
//        });
//    }
//
//    private void alphaEasy(String name, ImageView view){
//        if (pref.getInt(name, 0) == 0){
//            view.setImageAlpha(50);
//        }
//    }
//
//    private void alphaMedium(String name, ImageView view){
//        if (pref.getInt(name, 0) < 3){
//            view.setImageAlpha(50);
//        }
//    }
//
//    private void alphaHard(String name, ImageView view){
//        if (pref.getInt(name, 0) < 5){
//            view.setImageAlpha(50);
//            counter++;
//        }
//        if (counter < 5){
//            ivJunior.setImageAlpha(50);
//        }
//    }