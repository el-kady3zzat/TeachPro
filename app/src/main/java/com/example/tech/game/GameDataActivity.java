package com.example.tech.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.tech.R;
import com.example.tech.backendless.classes.Student;
import com.example.tech.student.StudentMainActivity;
import com.example.tech.student.WinnersActivity;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class GameDataActivity extends AppCompatActivity {

    Spinner spnSubject;
    RadioGroup rgLevel, rgPlayersNumber;
    RadioButton rbEasy, rbMedium, rbHard, rb1Player, rb2Players, rb4Players;
    EditText etPlayer2ID, etPlayer3ID, etPlayer4ID, etGameQuestionsNumber;
    HTextView tv2;
    Button btnStartGame;
    SharedPreferences pref;
    String playersNumber, player1ID, player2ID, player3ID, player4ID;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_data);

        pref = getSharedPreferences("userData", MODE_PRIVATE);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        builder.setView(inflate.inflate(R.layout.create_or_join_game, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
        Button btnCreateGame = dialog.findViewById(R.id.btnCreateGame);
        Button btnJoinGame = dialog.findViewById(R.id.btnJoinGame);
        Button btnJoin2Game = dialog.findViewById(R.id.btnJoinGame2);
        EditText etLiveGameID = dialog.findViewById(R.id.etLiveGameID);

        assert btnJoinGame != null;
        btnJoinGame.setOnClickListener(v -> {
            assert etLiveGameID != null;
            etLiveGameID.setVisibility(View.VISIBLE);
            assert btnJoin2Game != null;
            btnJoin2Game.setVisibility(View.VISIBLE);
            btnJoin2Game.setOnClickListener(v1 -> {
                if (etLiveGameID.getText().toString().equals(""))
                    Toasty.warning(this, "Please Enter Game ID First", Toasty.LENGTH_LONG, true).show();
                else {
                    btnJoin2Game.setEnabled(false);
                    DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                    queryBuilder.setWhereClause("gameID = " + (Integer.parseInt(etLiveGameID.getText().toString())) + "");
                    Backendless.Data.of("GameLive").find(new AsyncCallback<List<Map>>() {
                        @Override
                        public void handleResponse(List<Map> response) {
                            if (response.size() != 0) {
                                if (Objects.equals(response.get(0).get("player1ID"), pref.getString("id", "")))
                                    isPlayerReady(1, pref.getString("id", ""), Integer.parseInt(etLiveGameID.getText().toString()));
                                else if (Objects.equals(response.get(0).get("player2ID"), pref.getString("id", "")))
                                    isPlayerReady(2, pref.getString("id", ""), Integer.parseInt(etLiveGameID.getText().toString()));
                                else if (Objects.equals(response.get(0).get("player3ID"), pref.getString("id", "")))
                                    isPlayerReady(3, pref.getString("id", ""), Integer.parseInt(etLiveGameID.getText().toString()));
                                else if (Objects.equals(response.get(0).get("player4ID"), pref.getString("id", "")))
                                    isPlayerReady(4, pref.getString("id", ""), Integer.parseInt(etLiveGameID.getText().toString()));
                                else
                                    Toasty.warning(GameDataActivity.this, "You Isn't Registered in This Game", Toasty.LENGTH_LONG, true).show();
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toasty.warning(GameDataActivity.this, "Game ID Is Not Correct", Toasty.LENGTH_LONG, true).show();
                            btnJoin2Game.setEnabled(true);
                        }
                    });
                }

            });
        });

        assert btnCreateGame != null;
        btnCreateGame.setOnClickListener(v -> {
            dialog.cancel();
            initializeViews();
            getSubjects();
            getPlayersNumber();
            getPlayersIDS();

            btnStartGame.setOnClickListener(v1 -> {
                if (spnSubject.getSelectedItem() == null || getLevel().equals("") || playersNumber.equals("") || etGameQuestionsNumber.getText().toString().equals("")) {
                    Toasty.warning(GameDataActivity.this, "Please Fill Required Fields First", Toasty.LENGTH_LONG, true).show();
                    return;
                }
                checkQuestionsNumber();
            });
        });
    }

    private void isPlayerReady(int playerNumber, String playerID, int gameID){
        Map<String, Object> changes = new HashMap<>();
        String whereClause;
        if (playerNumber == 1) {
            whereClause = ("player1ID = '"+playerID+"'");
            changes.put("player1Ready", "ready");
        }
        else if (playerNumber == 2) {
            whereClause = ("player2ID = '"+playerID+"'");
            changes.put("player2Ready", "ready");
        }
        else if (playerNumber == 3) {
            whereClause = ("player3ID = '"+playerID+"'");
            changes.put("player3Ready", "ready");
        }
        else {
            whereClause = ("player4ID = '"+playerID+"'");
            changes.put("player4Ready", "ready");
        }
        Backendless.Data.of(GameLive.class).update(whereClause, changes, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Intent intent = new Intent(GameDataActivity.this, GameActivity.class);
                intent.putExtra("gameID", gameID);
                dialog.cancel();
                startActivity(intent);
                GameDataActivity.this.finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void initializeViews(){
        spnSubject = findViewById(R.id.spnSubject);
        rgLevel = findViewById(R.id.rgLevel);
        rgPlayersNumber = findViewById(R.id.rgPlayersNumber);
        rbEasy = findViewById(R.id.rbEasy);
        rbMedium = findViewById(R.id.rbMedium);
        rbHard = findViewById(R.id.rbHard);
        rb1Player = findViewById(R.id.rb1Player);
        rb2Players = findViewById(R.id.rb2Players);
        rb4Players = findViewById(R.id.rb4Players);
        etPlayer2ID = findViewById(R.id.etPlayer2ID);
        etPlayer3ID = findViewById(R.id.etPlayer3ID);
        etPlayer4ID = findViewById(R.id.etPlayer4ID);
        etGameQuestionsNumber = findViewById(R.id.etGameQuestionsNumber);
        tv2 = findViewById(R.id.tv2);
        btnStartGame = findViewById(R.id.btnStartGame);
    }

    private void getSubjects(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+pref.getString("team","")+"' and dept = '"+pref.getString("dept","")+"'");
        Backendless.Data.of(GameQuestions.class).find(new BackendlessCallback<List<GameQuestions>>() {
            @Override
            public void handleResponse(List<GameQuestions> response) {
                ArrayList<String> array = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    if (!array.contains(response.get(i).getSubject()))
                        array.add(response.get(i).getSubject());
                }
                ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(GameDataActivity.this, R.layout.spinner_txt, R.id.spnData, array);
                spnSubject.setAdapter(deptAdapter);
            }
        });
    }

    private String getLevel(){
        if (rbEasy.isChecked())
            return "easy";
        else if (rbMedium.isChecked())
            return "medium";
        else if (rbHard.isChecked())
            return "hard";
        else
            return "";
    }

    private void getPlayersNumber(){
        rgPlayersNumber.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rb1Player.getId()){
                playersNumber = "1";
                etPlayer2ID.setVisibility(View.GONE);
                etPlayer3ID.setVisibility(View.GONE);
                etPlayer4ID.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
            }
            else if (checkedId == rb2Players.getId()){
                playersNumber = "2";
                etPlayer2ID.setVisibility(View.VISIBLE);
                etPlayer3ID.setVisibility(View.GONE);
                etPlayer4ID.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);
                tv2.setAnimateType(HTextViewType.RAINBOW);
                tv2.animateText("Sure That Other Players Are Ready Before Start");
            }
            else {
                playersNumber = "4";
                etPlayer2ID.setVisibility(View.VISIBLE);
                etPlayer3ID.setVisibility(View.VISIBLE);
                etPlayer4ID.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv2.setAnimateType(HTextViewType.RAINBOW);
                tv2.animateText("Sure That Other Players Are Ready Before Start");
            }
        });
    }

    private void getPlayersIDS(){
        rb1Player.setOnCheckedChangeListener((buttonView, isChecked) -> player1ID = pref.getString("id", ""));
        rb2Players.setOnCheckedChangeListener((buttonView, isChecked) -> {
            player1ID = pref.getString("id", "");
            player2ID = etPlayer2ID.getText().toString();
        });
        rb4Players.setOnCheckedChangeListener((buttonView, isChecked) -> {
            player1ID = pref.getString("id", "");
            player2ID = etPlayer2ID.getText().toString();
            player3ID = etPlayer3ID.getText().toString();
            player4ID = etPlayer4ID.getText().toString();
        });
    }

    Game game = new Game();
    private void start() {
        game.setSubject(spnSubject.getSelectedItem().toString());
        game.setLevel(getLevel());
        game.setTeam(pref.getString("team",""));
        game.setDept(pref.getString("dept",""));
        game.setQuesNum(Integer.valueOf(etGameQuestionsNumber.getText().toString()));
        if (rb1Player.isChecked()) {
            game.setPlayersCount(1);
            game.setPlayer1ID(pref.getString("id",""));
        }
        else if (rb2Players.isChecked()) {
            game.setPlayersCount(2);
            game.setPlayer1ID(pref.getString("id",""));
            game.setPlayer2ID(etPlayer2ID.getText().toString());
            liveGame();
        }
        else {
            game.setPlayersCount(4);
            game.setPlayer1ID(pref.getString("id",""));
            game.setPlayer2ID(etPlayer2ID.getText().toString());
            game.setPlayer3ID(etPlayer3ID.getText().toString());
            game.setPlayer4ID(etPlayer4ID.getText().toString());
            liveGame();
        }
        Backendless.Data.of(Game.class).save(game, new BackendlessCallback<Game>() {
            @Override
            public void handleResponse(Game response) {
                Intent intent = new Intent(GameDataActivity.this, GameActivity.class);
                intent.putExtra("gameID", game.getGameID());
                startActivity(intent);
                GameDataActivity.this.finish();
            }
        });
    }

    private void liveGame(){
        GameLive gameLive = new GameLive();
        gameLive.setGameID(game.getGameID());
        gameLive.setPlayer1ID(pref.getString("id",""));
        gameLive.setPlayer1Ready("ready");
        gameLive.setPlayer2ID(etPlayer2ID.getText().toString());
        gameLive.setPlayer3ID(etPlayer3ID.getText().toString());
        gameLive.setPlayer4ID(etPlayer4ID.getText().toString());
        Backendless.Data.of(GameLive.class).save(gameLive, new BackendlessCallback<GameLive>() {
            @Override
            public void handleResponse(GameLive response) {

            }
        });
    }

    private void gameID() {
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create().setProperties("Max(gameID)");
        Backendless.Data.of("Game").find(dataQueryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {
                if (String.valueOf(response.get(0).get("max")).equals("null"))
                    game.setGameID(1);
                else
                    game.setGameID(Integer.parseInt((String.valueOf(response.get(0).get("max")))) + 1);
                start();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void checkQuestionsNumber(){
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("team = '"+pref.getString("team","")+"'" +
                "and dept = '"+pref.getString("dept","")+"'" +
                "and subject = '"+spnSubject.getSelectedItem().toString()+"'" +
                "and level = '"+getLevel()+"'");
        Backendless.Data.of(GameQuestions.class).find(builder, new AsyncCallback<List<GameQuestions>>() {
            @Override
            public void handleResponse(List<GameQuestions> response) {
                if (response.size() < Integer.parseInt(etGameQuestionsNumber.getText().toString())) {
                    Toasty.info(GameDataActivity.this, "Max Questions Number is: " + response.size(), Toasty.LENGTH_LONG, true).show();
                    return;
                }
                gameID();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GameDataActivity.this, StudentMainActivity.class));
        GameDataActivity.this.finish();
        super.onBackPressed();
    }
}