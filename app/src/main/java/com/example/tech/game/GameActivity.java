package com.example.tech.game;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.QuestionsLive;
import com.example.tech.backendless.classes.Student;
import com.example.tech.student.WinnersActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import pl.droidsonroids.gif.GifImageView;
import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends AppCompatActivity {

    ConstraintLayout conMoves;
    TextView tvGameQuestion;
    RadioGroup rgGameAnswers;
    RadioButton rbGameAnswer1, rbGameAnswer2, rbGameAnswer3, rbGameAnswer4;
    GifImageView GIFViewBlueHole, GIFViewSpaceShip, GIFViewSpaceShip1, GIFViewSpaceShip2, GIFViewSpaceShip3, GIFViewSpaceShip4, GIFViewRocket;
    ImageView ivFireExplosion;
    TextView tvPlayer1Name, tvPlayer2Name, tvPlayer3Name, tvPlayer4Name;

    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
    List<QuestionsLive> gameQuestions = new ArrayList<>();
    int questionNumber = 0, questionNumberMulti = 0, result = 0;
    String rightAnswer;
    RadioButton[] rb;

    int gameID, quesNum, playersNum;
    String subject, team, dept, level, player1ID, player2ID, player3ID, player4ID;
    final float[] top = {0};

    float movingBy, counter = 1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    float space;

    AlertDialog DetailsDialog;
    AlertDialog.Builder builder;

    List<Student> names = new ArrayList<>();
    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //initialize views
        {
            conMoves = findViewById(R.id.conMoves);
            tvGameQuestion = findViewById(R.id.tvGameQuestion);
            rgGameAnswers = findViewById(R.id.rgGameAnswers);
            rbGameAnswer1 = findViewById(R.id.rbGameAnswer1);
            rbGameAnswer2 = findViewById(R.id.rbGameAnswer2);
            rbGameAnswer3 = findViewById(R.id.rbGameAnswer3);
            rbGameAnswer4 = findViewById(R.id.rbGameAnswer4);

            ivFireExplosion = findViewById(R.id.ivGameFire);
            GIFViewRocket = findViewById(R.id.GIFViewRocket);
            GIFViewBlueHole = findViewById(R.id.GIFViewBlueHole);
            GIFViewSpaceShip = findViewById(R.id.GIFViewSpaceShip);
            GIFViewSpaceShip1 = findViewById(R.id.GIFViewSpaceShip1);
            GIFViewSpaceShip2 = findViewById(R.id.GIFViewSpaceShip2);
            GIFViewSpaceShip3 = findViewById(R.id.GIFViewSpaceShip3);
            GIFViewSpaceShip4 = findViewById(R.id.GIFViewSpaceShip4);

            tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
            tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
            tvPlayer3Name = findViewById(R.id.tvPlayer3Name);
            tvPlayer4Name = findViewById(R.id.tvPlayer4Name);

            rb = new RadioButton[]{rbGameAnswer1, rbGameAnswer2, rbGameAnswer3, rbGameAnswer4};
        }

        //get game data
        {
            pref = getSharedPreferences("userData", MODE_PRIVATE);
            gameID = getIntent().getIntExtra("gameID",0);

            queryBuilder.setWhereClause("gameID = "+gameID+" ");
            Backendless.Data.of(Game.class).find(queryBuilder, new AsyncCallback<List<Game>>() {
                @Override
                public void handleResponse(List<Game> response) {
                    quesNum = response.get(0).getQuesNum();
                    playersNum = response.get(0).getPlayersCount();
                    subject = response.get(0).getSubject();
                    team = response.get(0).getTeam();
                    dept = response.get(0).getDept();
                    level = response.get(0).getLevel();

                    //if single player
                    {
                        if (playersNum == 1){
                            getQuestionsForSinglePlayer();
                            GIFViewRocket.setOnClickListener(v -> checkAnswersForSinglePlayer());
                        }
                    }

                    //if 2 || 4 players
                    {
                        if (playersNum == 2){
                            builder = new AlertDialog.Builder(GameActivity.this);
                            LayoutInflater inflate = GameActivity.this.getLayoutInflater();
                            builder.setView(inflate.inflate(R.layout.loading_players, null));
                            builder.setCancelable(false);
                            DetailsDialog = builder.create();
                            DetailsDialog.show();

                            TextView tvGameIDIs = DetailsDialog.findViewById(R.id.tvGameIDIs);
                            assert tvGameIDIs != null;
                            tvGameIDIs.setText("Game ID: " + gameID);

                            checkIfAllPlayersAreReady();
                            GIFViewSpaceShip.setVisibility(View.GONE);

                            player1ID = response.get(0).getPlayer1ID();
                            GIFViewSpaceShip1.setVisibility(View.VISIBLE);
                            tvPlayer1Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player1ID+"'", tvPlayer1Name);

                            player2ID = response.get(0).getPlayer2ID();
                            GIFViewSpaceShip2.setVisibility(View.VISIBLE);
                            tvPlayer2Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player2ID+"'", tvPlayer2Name);

                            GIFViewRocket.setOnClickListener(v -> checkAnswersForMultiPlayers());
                        }
                        else if (playersNum == 4){
                            builder = new AlertDialog.Builder(GameActivity.this);
                            LayoutInflater inflate = GameActivity.this.getLayoutInflater();
                            builder.setView(inflate.inflate(R.layout.loading_players, null));
                            builder.setCancelable(false);
                            DetailsDialog = builder.create();
                            DetailsDialog.show();

                            TextView tvGameIDIs = DetailsDialog.findViewById(R.id.tvGameIDIs);
                            assert tvGameIDIs != null;
                            tvGameIDIs.setText("Game ID: " + gameID);

                            checkIfAllPlayersAreReady();
                            GIFViewSpaceShip.setVisibility(View.GONE);

                            player1ID = response.get(0).getPlayer1ID();
                            GIFViewSpaceShip1.setVisibility(View.VISIBLE);
                            tvPlayer1Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player1ID+"'", tvPlayer1Name);

                            player2ID = response.get(0).getPlayer2ID();
                            GIFViewSpaceShip2.setVisibility(View.VISIBLE);
                            tvPlayer2Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player2ID+"'", tvPlayer2Name);

                            player3ID = response.get(0).getPlayer3ID();
                            GIFViewSpaceShip3.setVisibility(View.VISIBLE);
                            tvPlayer3Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player3ID+"'", tvPlayer3Name);

                            player4ID = response.get(0).getPlayer4ID();
                            GIFViewSpaceShip4.setVisibility(View.VISIBLE);
                            tvPlayer4Name.setVisibility(View.VISIBLE);
                            getPlayersNames("id = '"+player4ID+"'", tvPlayer4Name);

                            GIFViewRocket.setOnClickListener(v -> checkAnswersForMultiPlayers());
                        }
                    }
                    space = GIFViewRocket.getY() - ivFireExplosion.getY();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toasty.error(GameActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //single player methods
    private void getQuestionsForSinglePlayer(){
        QuestionsLive gameLiveQuestions = new QuestionsLive();
        queryBuilder.setWhereClause("subject='"+subject+"' and team='"+team+"' and dept='"+dept+"' and level='"+level+"'");
        Backendless.Data.of(GameQuestions.class).find(queryBuilder, new AsyncCallback<List<GameQuestions>>() {
            @Override
            public void handleResponse(List<GameQuestions> response) {
                Collections.shuffle(response);

                for (int i = 0; i < quesNum; i++) {
                    gameLiveQuestions.setGameID(gameID);
                    gameLiveQuestions.setQuestionType(response.get(i).getQuestionType());
                    gameLiveQuestions.setQuestion(response.get(i).getQuestion());
                    gameLiveQuestions.setAnswer1(response.get(i).getAnswer1());
                    gameLiveQuestions.setAnswer2(response.get(i).getAnswer2());
                    gameLiveQuestions.setAnswer3(response.get(i).getAnswer3());
                    gameLiveQuestions.setAnswer4(response.get(i).getAnswer4());
                    gameLiveQuestions.setRightAnswer(response.get(i).getRightAnswer());
                    int finalI = i;
                    Backendless.Data.of(QuestionsLive.class).save(gameLiveQuestions, new AsyncCallback<QuestionsLive>() {
                        @Override
                        public void handleResponse(QuestionsLive response) {
                            if (finalI == quesNum - 1){

                                queryBuilder.setWhereClause("gameID = '"+gameID+"' ");
                                Backendless.Data.of(QuestionsLive.class).find(queryBuilder, new AsyncCallback<List<QuestionsLive>>() {
                                    @Override
                                    public void handleResponse(List<QuestionsLive> response) {
                                        gameQuestions = response;
                                        Toast.makeText(GameActivity.this, response.get(0).getQuestionType(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(GameActivity.this, response.get(1).getQuestionType(), Toast.LENGTH_SHORT).show();

                                        movingBy = (float) ((conMoves.getWidth() / 2) / quesNum) - 25;
                                        top[0] = GIFViewRocket.getY();
                                        fillQAForSingle();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(GameActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(GameActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    private void fillQAForSingle(){
        if (questionNumber == gameQuestions.size()){
            if (playersNum == 1)
                saveGameResultsForSinglePlayer();
            return;
        }
        tvGameQuestion.setText(gameQuestions.get(questionNumber).getQuestion());
        rightAnswer = gameQuestions.get(questionNumber).getRightAnswer();
        if (gameQuestions.get(questionNumber).getQuestionType().equals("mc")){
            rbGameAnswer1.setText(gameQuestions.get(questionNumber).getAnswer1());
            rbGameAnswer2.setText(gameQuestions.get(questionNumber).getAnswer2());
            rbGameAnswer3.setText(gameQuestions.get(questionNumber).getAnswer3());
            rbGameAnswer4.setText(gameQuestions.get(questionNumber).getAnswer4());
            rbGameAnswer3.setVisibility(View.VISIBLE);
            rbGameAnswer4.setVisibility(View.VISIBLE);
        }
        else {
            rbGameAnswer1.setText(R.string.True);
            rbGameAnswer2.setText(R.string.False);
            rbGameAnswer3.setVisibility(View.INVISIBLE);
            rbGameAnswer4.setVisibility(View.INVISIBLE);
        }
        questionNumber++;
    }

    private void checkAnswersForSinglePlayer(){
        if (!rbGameAnswer1.isChecked() && !rbGameAnswer2.isChecked() && !rbGameAnswer3.isChecked() && !rbGameAnswer4.isChecked()) {
            Toasty.warning(this, "Please Choose Answer First",Toasty.LENGTH_LONG,true).show();
        }
        else {
            explodeForAll();
            if (rb[0].isChecked() && rb[0].getText().toString().equals(rightAnswer)){
                result++;
                moveSpaceShipForSinglePlayer();
                return;
            }
            if (rb[1].isChecked() && rb[1].getText().toString().equals(rightAnswer)){
                result++;
                moveSpaceShipForSinglePlayer();
                return;
            }
            if (rb[2].isChecked() && rb[2].getText().toString().equals(rightAnswer)){
                result++;
                moveSpaceShipForSinglePlayer();
                return;
            }
            if (rb[3].isChecked() && rb[3].getText().toString().equals(rightAnswer)){
                result++;
                moveSpaceShipForSinglePlayer();
            }
        }
    }

    private void moveSpaceShipForSinglePlayer() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewSpaceShip, "translationX", -(movingBy*result));
        animation.setDuration(200);
        animation.start();
    }

    private void saveGameResultsForSinglePlayer() {
        GameResult gameResult = new GameResult();
        gameResult.setGameID(gameID);
        gameResult.setPlayer1ID(pref.getString("id",""));
        gameResult.setPlayer1Result(result);
        Backendless.Data.of(GameResult.class).save(gameResult, new BackendlessCallback<GameResult>() {
            @Override
            public void handleResponse(GameResult response) {
                Toasty.success(GameActivity.this, "Game Finished", Toast.LENGTH_LONG, true).show();
                editor = pref.edit();
                editor.putInt("games", (pref.getInt("games",0) + 1));
                editor.apply();
                onBackPressed();
            }
        });
    }

    //common
    CountDownTimer timer;
    private void explodeForAll(){
        ExplosionField explosionField = ExplosionField.attach2Window(this);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.rocket_engine);
        final MediaPlayer mp1 = MediaPlayer.create(this, R.raw.hq_explosion);
        mp.start();
        ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewRocket, "translationY", -space);
        animation.setDuration(2000);
        animation.start();
        timer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                explosionField.explode(ivFireExplosion);
                mp.stop();
                mp1.start();
                GIFViewRocket.setVisibility(View.INVISIBLE);

                tvGameQuestion.setText("");
                rgGameAnswers.clearCheck();
                rbGameAnswer1.setText("");
                rbGameAnswer2.setText("");
                rbGameAnswer3.setText("");
                rbGameAnswer4.setText("");
                timer = new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        GIFViewRocket.setVisibility(View.VISIBLE);
                        GIFViewRocket.setY(top[0] + GIFViewRocket.getHeight());
                        buildForAll();
                    }
                }.start();

            }
        }.start();
    }

    private void buildForAll(){
        timer = new CountDownTimer(1000, 100) {
            public void onTick(long millisUntilFinished) {
                GIFViewRocket.setY(GIFViewRocket.getY()- 30);
            }
            public void onFinish() {
                if (playersNum == 1)
                    fillQAForSingle();
                else {
                    questionNumberMulti++;
                    fillQAForMulti();
                }
            }
        }.start();
    }

    //multi players methods
    CountDownTimer ready;
    private void checkIfAllPlayersAreReady(){
        ready = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ready.cancel();
                DataQueryBuilder builder = DataQueryBuilder.create().setWhereClause("gameID = "+gameID+"");
                Backendless.Data.of("GameLive").find(builder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        if (playersNum == 2) {
                            if (Objects.equals(response.get(0).get("player1Ready"), "ready") && Objects.equals(response.get(0).get("player2Ready"), "ready")) {
                                ready.cancel();
                                DetailsDialog.cancel();
                                getQuestionsForMultiPlayer();
                                getOtherPlayersPositions();
                            }
                            else
                                ready.start();
                        }
                        else
                            if (Objects.equals(response.get(0).get("player1Ready"), "ready") && Objects.equals(response.get(0).get("player2Ready"), "ready") && Objects.equals(response.get(0).get("player3Ready"), "ready") && Objects.equals(response.get(0).get("player4Ready"), "ready")) {
                                ready.cancel();
                                DetailsDialog.cancel();
                                getQuestionsForMultiPlayer();
                                getOtherPlayersPositions();
                            }
                            else
                                ready.start();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            }

            @Override
            public void onFinish() {
                Toasty.info(GameActivity.this, "Not All Players Are Ready, Try Again Later", Toasty.LENGTH_LONG, true).show();
            }
        }.start();
    }

    private void getPlayersNames(String id, TextView tvName) {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause(id);
        Backendless.Data.of(Student.class).find(builder, new AsyncCallback<List<Student>>() {
            @Override
            public void handleResponse(List<Student> response) {
                tvName.setText(getFirstName(response.get(0).getName()));
                names.add(response.get(0));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(GameActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }

    private String getFirstName(String name) {
        if (name.contains(" "))
            return name.substring(0, name.indexOf(" "));
        else
            return name;
    }

    private void getQuestionsForMultiPlayer(){
        QuestionsLive gameLiveQuestions = new QuestionsLive();
        queryBuilder.setWhereClause("subject='"+subject+"' and team='"+team+"' and dept='"+dept+"' and level='"+level+"'");
        Backendless.Data.of(GameQuestions.class).find(queryBuilder, new AsyncCallback<List<GameQuestions>>() {
            @Override
            public void handleResponse(List<GameQuestions> response) {
                Collections.shuffle(response);

                for (int i = 0; i < quesNum; i++) {
                    gameLiveQuestions.setGameID(gameID);
                    gameLiveQuestions.setQuestionType(response.get(i).getQuestionType());
                    gameLiveQuestions.setQuestion(response.get(i).getQuestion());
                    gameLiveQuestions.setAnswer1(response.get(i).getAnswer1());
                    gameLiveQuestions.setAnswer2(response.get(i).getAnswer2());
                    gameLiveQuestions.setAnswer3(response.get(i).getAnswer3());
                    gameLiveQuestions.setAnswer4(response.get(i).getAnswer4());
                    gameLiveQuestions.setRightAnswer(response.get(i).getRightAnswer());
                    int finalI = i;
                    Backendless.Data.of(QuestionsLive.class).save(gameLiveQuestions, new AsyncCallback<QuestionsLive>() {
                        @Override
                        public void handleResponse(QuestionsLive response) {
                            if (finalI == quesNum - 1){

                                queryBuilder.setWhereClause("gameID = '"+gameID+"' ");
                                Backendless.Data.of(QuestionsLive.class).find(queryBuilder, new AsyncCallback<List<QuestionsLive>>() {
                                    @Override
                                    public void handleResponse(List<QuestionsLive> response) {
                                        gameQuestions = response;
                                        movingBy = (float) ((conMoves.getWidth() / 2) / quesNum) - 25;
                                        top[0] = GIFViewRocket.getY();
                                        fillQAForMulti();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.error(GameActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toasty.error(GameActivity.this, fault.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
  }

    private void fillQAForMulti(){
        if (questionNumberMulti == gameQuestions.size()){
            return;
        }
        tvGameQuestion.setText(gameQuestions.get(questionNumberMulti).getQuestion());
        rightAnswer = gameQuestions.get(questionNumberMulti).getRightAnswer();
        if (gameQuestions.get(questionNumberMulti).getQuestionType().equals("mc")){
            rbGameAnswer1.setText(gameQuestions.get(questionNumberMulti).getAnswer1());
            rbGameAnswer2.setText(gameQuestions.get(questionNumberMulti).getAnswer2());
            rbGameAnswer3.setText(gameQuestions.get(questionNumberMulti).getAnswer3());
            rbGameAnswer4.setText(gameQuestions.get(questionNumberMulti).getAnswer4());
            rbGameAnswer3.setVisibility(View.VISIBLE);
            rbGameAnswer4.setVisibility(View.VISIBLE);
        }
        else {
            rbGameAnswer1.setText(R.string.True);
            rbGameAnswer2.setText(R.string.False);
            rbGameAnswer4.setVisibility(View.INVISIBLE);
            rbGameAnswer3.setVisibility(View.INVISIBLE);
        }
    }

    private void checkAnswersForMultiPlayers(){
        if (!rbGameAnswer1.isChecked() && !rbGameAnswer2.isChecked() && !rbGameAnswer3.isChecked() && !rbGameAnswer4.isChecked()) {
            Toasty.warning(this, "Please Choose Answer First",Toasty.LENGTH_LONG,true).show();
        }
        else {
            explodeForAll();
            if (player1ID.equals(pref.getString("id",""))){
                for (int i = 0; i < 4; i++) {
                    if (rb[i].isChecked() && rb[i].getText().toString().equals(rightAnswer)){
                        result++;
                        updateResult("1", result);
                        moveLiveSpaceShipForCurrentUser(GIFViewSpaceShip1);
                        break;
                    }
                }
            }
            else if (player2ID.equals(pref.getString("id",""))){
                for (int i = 0; i < 4; i++) {
                    if (rb[i].isChecked() && rb[i].getText().toString().equals(rightAnswer)){
                        result++;
                        updateResult("2", result);
                        moveLiveSpaceShipForCurrentUser(GIFViewSpaceShip2);
                        break;
                    }
                }
            }
            else if (player3ID.equals(pref.getString("id",""))){
                for (int i = 0; i < 4; i++) {
                    if (rb[i].isChecked() && rb[i].getText().toString().equals(rightAnswer)){
                        result++;
                        updateResult("3", result);
                        moveLiveSpaceShipForCurrentUser(GIFViewSpaceShip3);
                        break;
                    }
                }
            }
            else if (player4ID.equals(pref.getString("id",""))){
                for (int i = 0; i < 4; i++) {
                    if (rb[i].isChecked() && rb[i].getText().toString().equals(rightAnswer)){
                        result++;
                        updateResult("4", result);
                        moveLiveSpaceShipForCurrentUser(GIFViewSpaceShip4);
                        break;
                    }
                }
            }
        }
    }

    private void updateResult(String playerNumber, int result){
        Map<String, Object> gameLive = new HashMap<>();
        switch (playerNumber) {
            case "1":
                gameLive.put("player1Result", result);
                break;
            case "2":
                gameLive.put("player2Result", result);
                break;
            case "3":
                gameLive.put("player3Result", result);
                break;
            default:
                gameLive.put("player4Result", result);
                break;
        }
        Backendless.Data.of(GameLive.class).update("gameID = "+gameID+"", gameLive, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(GameActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveLiveSpaceShipForCurrentUser(GifImageView spaceShip) {
        if (spaceShip == GIFViewSpaceShip1 || spaceShip == GIFViewSpaceShip3){
            ObjectAnimator animation;
            if (spaceShip == GIFViewSpaceShip1) {
                animation = ObjectAnimator.ofFloat(GIFViewSpaceShip1, "translationX", -(movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip1.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            else {
                animation = ObjectAnimator.ofFloat(GIFViewSpaceShip3, "translationX", -(movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip3.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            animation.setDuration(200);
            animation.start();
        }
        else {
            ObjectAnimator animation;
            if (spaceShip == GIFViewSpaceShip2) {
                animation = ObjectAnimator.ofFloat(GIFViewSpaceShip2, "translationX", (movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip2.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            else{
                animation = ObjectAnimator.ofFloat(GIFViewSpaceShip4, "translationX", (movingBy*counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip4.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            animation.setDuration(200);
            animation.start();
        }
        counter++;
    }

    CountDownTimer Timer;
    List<GameLive> response;
    private void getOtherPlayersPositions(){
        Timer = new CountDownTimer(quesNum * 60 * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                DataQueryBuilder builder = DataQueryBuilder.create().setWhereClause("gameID = '"+gameID+"'");
                Backendless.Data.of(GameLive.class).find(builder, new AsyncCallback<List<GameLive>>() {
                    @Override
                    public void handleResponse(List<GameLive> response) {
                        if ((response.get(0).getPlayer1Result() == quesNum)||(response.get(0).getPlayer2Result() == quesNum)||response.get(0).getPlayer3Result() == quesNum||response.get(0).getPlayer4Result() == quesNum) {
                            GameActivity.this.response = response;
                            saveLiveGameResults(
                                    response.get(0).getPlayer1ID(), response.get(0).getPlayer1Result()
                                    , response.get(0).getPlayer2ID(), response.get(0).getPlayer2Result()
                                    , response.get(0).getPlayer3ID(), response.get(0).getPlayer3Result()
                                    , response.get(0).getPlayer4ID(), response.get(0).getPlayer4Result());
                            Timer.cancel();
                        }
                        else{
                            if (!response.get(0).getPlayer1ID().equals(pref.getString("id", ""))) {
                                moveOtherPlayersSpaceShips(GIFViewSpaceShip1, response.get(0).getPlayer1Result());
                            } else if (!response.get(0).getPlayer2ID().equals(pref.getString("id", ""))) {
                                moveOtherPlayersSpaceShips(GIFViewSpaceShip2, response.get(0).getPlayer2Result());
                            } else if (!response.get(0).getPlayer3ID().equals(pref.getString("id", ""))) {
                                moveOtherPlayersSpaceShips(GIFViewSpaceShip3, response.get(0).getPlayer3Result());
                            } else if (!response.get(0).getPlayer4ID().equals(pref.getString("id", ""))) {
                                moveOtherPlayersSpaceShips(GIFViewSpaceShip4, response.get(0).getPlayer4Result());
                            }
                        }
                    }
                    @Override
                    public void handleFault(BackendlessFault fault) {}
                });
            }
            @Override
            public void onFinish() {}
        }.start();
    }

    private void moveOtherPlayersSpaceShips(GifImageView spaceShip, float counter) {
        if (spaceShip == GIFViewSpaceShip1 || spaceShip == GIFViewSpaceShip3){
            if (spaceShip == GIFViewSpaceShip1) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewSpaceShip1, "translationX", -(movingBy*counter));
                animation.setDuration(200);
                animation.start();
//                if ((ship1X) > (ship1X - (movingBy * counter)))
//                    GIFViewSpaceShip1.setX(GIFViewSpaceShip1.getX() - (movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip1.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            else {
                ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewSpaceShip3, "translationX", -(movingBy*counter));
                animation.setDuration(200);
                animation.start();
//                if ((ship3X) > (ship3X - (movingBy * counter)))
//                    GIFViewSpaceShip3.setX(GIFViewSpaceShip3.getX() - (movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip3.setVisibility(View.INVISIBLE);}
                    }.start();
            }
        }
        else {
            if (spaceShip == GIFViewSpaceShip2) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewSpaceShip2, "translationX", (movingBy*counter));
                animation.setDuration(200);
                animation.start();
//                if ((ship2X) <= (ship2X + (movingBy * counter)))
//                    GIFViewSpaceShip2.setX(GIFViewSpaceShip2.getX() + (movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip2.setVisibility(View.INVISIBLE);}
                    }.start();
            }
            else {
                ObjectAnimator animation = ObjectAnimator.ofFloat(GIFViewSpaceShip4, "translationX", (movingBy*counter));
                animation.setDuration(200);
                animation.start();
//                if ((ship4X) < (ship4X + (movingBy * counter)))
//                    GIFViewSpaceShip4.setX(GIFViewSpaceShip4.getX() + (movingBy * counter));
                if (counter == quesNum)
                    new CountDownTimer(200, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        @Override
                        public void onFinish() {GIFViewSpaceShip4.setVisibility(View.INVISIBLE);}
                    }.start();
            }
        }
    }

    private void saveLiveGameResults(String player1ID, int player1Result, String player2ID, int player2Result, String player3ID, int player3Result, String player4ID, int player4Result) {
        ready.cancel();
        Timer.cancel();
        GameResult gameResult = new GameResult();
        gameResult.setGameID(gameID);
        gameResult.setPlayer1ID(player1ID);
        gameResult.setPlayer1Result(player1Result);
        gameResult.setPlayer2ID(player2ID);
        gameResult.setPlayer2Result(player2Result);
        gameResult.setLevel(level);
        if (playersNum == 4){
            gameResult.setPlayer3ID(player3ID);
            gameResult.setPlayer3Result(player3Result);
            gameResult.setPlayer4ID(player4ID);
            gameResult.setPlayer4Result(player4Result);
        }
        Backendless.Data.of(GameResult.class).save(gameResult, new BackendlessCallback<GameResult>() {
            @Override
            public void handleResponse(GameResult response) {
                getWinner();
                editor = pref.edit();
                editor.putInt("games", (pref.getInt("games",0) + 1));
                editor.apply();
            }
        });
    }

    private void getWinner(){
        if (playersNum == 2){
            if (response.get(0).getPlayer1Result() > response.get(0).getPlayer2Result()){
                finishGame(response.get(0).getPlayer1ID(), response.get(0).getPlayer2ID(), "", "");
            }
            else{
                finishGame(response.get(0).getPlayer2ID(), response.get(0).getPlayer1ID(), "", "");
            }
        }
        else {
            String first="", second="", third="", fourth="";
            List<Integer> results = new ArrayList<>();
            results.add(response.get(0).getPlayer1Result());
            results.add(response.get(0).getPlayer2Result());
            results.add(response.get(0).getPlayer3Result());
            results.add(response.get(0).getPlayer4Result());
            Collections.sort(results);
            Collections.reverse(results);
            if (results.get(0).equals(response.get(0).getPlayer1Result()))
                first = response.get(0).getPlayer1ID();
            else if (results.get(1).equals(response.get(0).getPlayer1Result()))
                second = response.get(0).getPlayer1ID();
            else if (results.get(2).equals(response.get(0).getPlayer1Result()))
                third = response.get(0).getPlayer1ID();
            else
                fourth = response.get(0).getPlayer1ID();

            if (results.get(0).equals(response.get(0).getPlayer2Result()))
                first = response.get(0).getPlayer2ID();
            else if (results.get(1).equals(response.get(0).getPlayer2Result()))
                second = response.get(0).getPlayer2ID();
            else if (results.get(2).equals(response.get(0).getPlayer2Result()))
                third = response.get(0).getPlayer2ID();
            else
                fourth = response.get(0).getPlayer2ID();

            if (results.get(0).equals(response.get(0).getPlayer3Result()))
                first = response.get(0).getPlayer3ID();
            else if (results.get(1).equals(response.get(0).getPlayer3Result()))
                second = response.get(0).getPlayer3ID();
            else if (results.get(2).equals(response.get(0).getPlayer3Result()))
                third = response.get(0).getPlayer3ID();
            else
                fourth = response.get(0).getPlayer3ID();

            if (results.get(0).equals(response.get(0).getPlayer4Result()))
                first = response.get(0).getPlayer4ID();
            else if (results.get(1).equals(response.get(0).getPlayer4Result()))
                second = response.get(0).getPlayer4ID();
            else if (results.get(2).equals(response.get(0).getPlayer4Result()))
                third = response.get(0).getPlayer4ID();
            else
                fourth = response.get(0).getPlayer4ID();

            finishGame(first, second, third, fourth);
        }
    }

    public void finishGame(String FirstPlayerID, String SecondPlayerID, String ThirdPlayerID, String FourthPlayerID){
        AlertDialog dialog;
        AlertDialog.Builder alertBuilder;
        alertBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        alertBuilder.setView(inflate.inflate(R.layout.game_winners, null));
        alertBuilder.setCancelable(false);
        dialog = alertBuilder.create();
        dialog.show();

        KonfettiView celebrateView;
        TextView tvFirstPlayer, tvSecondPlayer, tvThirdPlayer, tvFourthPlayer;
        celebrateView = dialog.findViewById(R.id.celebrateView);
        tvFirstPlayer = dialog.findViewById(R.id.HtvFirstPlayer);
        tvSecondPlayer = dialog.findViewById(R.id.tvSecondPlayer);
        tvThirdPlayer = dialog.findViewById(R.id.tvThirdPlayer);
        tvFourthPlayer = dialog.findViewById(R.id.tvFourthPlayer);

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).getId().equals(FirstPlayerID)) {
        assert tvFirstPlayer != null;
        tvFirstPlayer.setText(names.get(i).getName());
                names.remove(i);
                break;
            }
        }

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).getId().equals(SecondPlayerID)){
        assert tvSecondPlayer != null;
        tvSecondPlayer.setText(names.get(i).getName());
                names.remove(i);
                break;
            }
        }

        if (playersNum == 4){
            assert tvThirdPlayer != null;
            tvThirdPlayer.setVisibility(View.VISIBLE);
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).getId().equals(ThirdPlayerID)){
                    tvThirdPlayer.setText(names.get(i).getName());
                    names.remove(i);
                    break;
                }
            }

            assert tvFourthPlayer != null;
            tvFourthPlayer.setVisibility(View.VISIBLE);
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).getId().equals(FourthPlayerID)){
                    tvFourthPlayer.setText(names.get(i).getName());
                    break;
                }
            }
        }

        Button btnBack = dialog.findViewById(R.id.btnBack);
        assert btnBack != null;
        btnBack.setOnClickListener(v -> {
            dialog.cancel();
            onBackPressed();
        });

        assert celebrateView != null;
        WinnersActivity.celebrate(this, celebrateView);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GameDataActivity.class));
        this.finish();
        super.onBackPressed();
    }
}