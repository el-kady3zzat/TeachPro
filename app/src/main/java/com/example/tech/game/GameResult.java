
package com.example.tech.game;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class GameResult
{
  private String objectId;
  private Date created;
  private Integer gameID;
  private String player1ID;
  private Integer player1Result;
  private String player2ID;
  private Integer player2Result;
  private String player3ID;
  private Integer player3Result;
  private String player4ID;
  private Integer player4Result;
  private String level;
  private String ownerId;

  private Date updated;

  public String getObjectId()
  {
    return objectId;
  }

  public Date getCreated()
  {
    return created;
  }

  public Integer getGameID()
  {
    return gameID;
  }

  public void setGameID( Integer gameID )
  {
    this.gameID = gameID;
  }

  public Integer getPlayer1Result()
  {
    return player1Result;
  }

  public void setPlayer1Result(Integer player1Result)
  {
    this.player1Result = player1Result;
  }

  public String getPlayer1ID()
  {
    return player1ID;
  }

  public void setPlayer1ID(String player1ID)
  {
    this.player1ID = player1ID;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

                                                    
  public GameResult save()
  {
    return Backendless.Data.of( GameResult.class ).save( this );
  }

  public void saveAsync( AsyncCallback<GameResult> callback )
  {
    Backendless.Data.of( GameResult.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( GameResult.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( GameResult.class ).remove( this, callback );
  }

  public static GameResult findById( String id )
  {
    return Backendless.Data.of( GameResult.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<GameResult> callback )
  {
    Backendless.Data.of( GameResult.class ).findById( id, callback );
  }

  public static GameResult findFirst()
  {
    return Backendless.Data.of( GameResult.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<GameResult> callback )
  {
    Backendless.Data.of( GameResult.class ).findFirst( callback );
  }

  public static GameResult findLast()
  {
    return Backendless.Data.of( GameResult.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<GameResult> callback )
  {
    Backendless.Data.of( GameResult.class ).findLast( callback );
  }

  public static List<GameResult> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( GameResult.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<GameResult>> callback )
  {
    Backendless.Data.of( GameResult.class ).find( queryBuilder, callback );
  }

  public String getPlayer2ID() {
    return player2ID;
  }

  public void setPlayer2ID(String player2ID) {
    this.player2ID = player2ID;
  }

  public Integer getPlayer2Result() {
    return player2Result;
  }

  public void setPlayer2Result(Integer player2Result) {
    this.player2Result = player2Result;
  }

  public String getPlayer3ID() {
    return player3ID;
  }

  public void setPlayer3ID(String player3ID) {
    this.player3ID = player3ID;
  }

  public Integer getPlayer3Result() {
    return player3Result;
  }

  public void setPlayer3Result(Integer player3Result) {
    this.player3Result = player3Result;
  }

  public String getPlayer4ID() {
    return player4ID;
  }

  public void setPlayer4ID(String player4ID) {
    this.player4ID = player4ID;
  }

  public Integer getPlayer4Result() {
    return player4Result;
  }

  public void setPlayer4Result(Integer player4Result) {
    this.player4Result = player4Result;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }
}