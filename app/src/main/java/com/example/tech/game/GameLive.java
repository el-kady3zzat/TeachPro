
package com.example.tech.game;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class GameLive
{
  private Date updated;
  private int movedBy;
  private String objectId;
  private String ownerId;
  private Integer gameID;
  private String player1ID;
  private String player1Ready;
  private Integer player1Result;
  private String player2ID;
  private String player2Ready;
  private Integer player2Result;
  private String player3ID;
  private String player3Ready;
  private Integer player3Result;
  private String player4ID;
  private String player4Ready;
  private Integer player4Result;
  private Date created;
  public Date getUpdated()
  {
    return updated;
  }

  public int getMovedBy()
  {
    return movedBy;
  }

  public void setMovedBy( int movedBy )
  {
    this.movedBy = movedBy;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Integer getGameID()
  {
    return gameID;
  }

  public void setGameID( Integer gameID )
  {
    this.gameID = gameID;
  }

  public Date getCreated()
  {
    return created;
  }

                                                    
  public GameLive save()
  {
    return Backendless.Data.of( GameLive.class ).save( this );
  }

  public void saveAsync( AsyncCallback<GameLive> callback )
  {
    Backendless.Data.of( GameLive.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( GameLive.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( GameLive.class ).remove( this, callback );
  }

  public static GameLive findById( String id )
  {
    return Backendless.Data.of( GameLive.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<GameLive> callback )
  {
    Backendless.Data.of( GameLive.class ).findById( id, callback );
  }

  public static GameLive findFirst()
  {
    return Backendless.Data.of( GameLive.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<GameLive> callback )
  {
    Backendless.Data.of( GameLive.class ).findFirst( callback );
  }

  public static GameLive findLast()
  {
    return Backendless.Data.of( GameLive.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<GameLive> callback )
  {
    Backendless.Data.of( GameLive.class ).findLast( callback );
  }

  public static List<GameLive> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( GameLive.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<GameLive>> callback )
  {
    Backendless.Data.of( GameLive.class ).find( queryBuilder, callback );
  }

  public String getPlayer1ID() {
    return player1ID;
  }

  public void setPlayer1ID(String player1ID) {
    this.player1ID = player1ID;
  }

  public Integer getPlayer1Result() {
    return player1Result;
  }

  public void setPlayer1Result(Integer player1Result) {
    this.player1Result = player1Result;
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

  public String getPlayer1Ready() {
    return player1Ready;
  }

  public void setPlayer1Ready(String player1Ready) {
    this.player1Ready = player1Ready;
  }

  public String getPlayer2Ready() {
    return player2Ready;
  }

  public void setPlayer2Ready(String player2Ready) {
    this.player2Ready = player2Ready;
  }

  public String getPlayer3Ready() {
    return player3Ready;
  }

  public void setPlayer3Ready(String player3Ready) {
    this.player3Ready = player3Ready;
  }

  public String getPlayer4Ready() {
    return player4Ready;
  }

  public void setPlayer4Ready(String player4Ready) {
    this.player4Ready = player4Ready;
  }
}