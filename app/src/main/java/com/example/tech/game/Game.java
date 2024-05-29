
package com.example.tech.game;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Game
{
  private String ownerId;
  private String Player3ID;
  private Date created;
  private String level;
  private String subject;
  private String objectId;
  private String team;
  private String Player1ID;
  private String Player2ID;
  private String dept;
  private Integer quesNum;
  private String Player4ID;
  private Integer playersCount;
  private Date updated;
  private Integer gameID;

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getPlayer3ID()
  {
    return Player3ID;
  }

  public void setPlayer3ID( String Player3ID )
  {
    this.Player3ID = Player3ID;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getLevel()
  {
    return level;
  }

  public void setLevel(String level)
  {
    this.level = level;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getTeam()
  {
    return team;
  }

  public void setTeam( String team )
  {
    this.team = team;
  }

  public String getPlayer1ID()
  {
    return Player1ID;
  }

  public void setPlayer1ID( String Player1ID )
  {
    this.Player1ID = Player1ID;
  }

  public String getPlayer2ID()
  {
    return Player2ID;
  }

  public void setPlayer2ID( String Player2ID )
  {
    this.Player2ID = Player2ID;
  }

  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public Integer getQuesNum()
  {
    return quesNum;
  }

  public void setQuesNum( Integer quesNum )
  {
    this.quesNum = quesNum;
  }

  public String getPlayer4ID()
  {
    return Player4ID;
  }

  public void setPlayer4ID( String Player4ID )
  {
    this.Player4ID = Player4ID;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Integer getGameID()
  {
    return gameID;
  }

  public void setGameID( Integer gameID )
  {
    this.gameID = gameID;
  }

                                                    
  public Game save()
  {
    return Backendless.Data.of( Game.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Game> callback )
  {
    Backendless.Data.of( Game.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Game.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Game.class ).remove( this, callback );
  }

  public static Game findById( String id )
  {
    return Backendless.Data.of( Game.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Game> callback )
  {
    Backendless.Data.of( Game.class ).findById( id, callback );
  }

  public static Game findFirst()
  {
    return Backendless.Data.of( Game.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Game> callback )
  {
    Backendless.Data.of( Game.class ).findFirst( callback );
  }

  public static Game findLast()
  {
    return Backendless.Data.of( Game.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Game> callback )
  {
    Backendless.Data.of( Game.class ).findLast( callback );
  }

  public static List<Game> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Game.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Game>> callback )
  {
    Backendless.Data.of( Game.class ).find( queryBuilder, callback );
  }

  public Integer getPlayersCount() {
    return playersCount;
  }

  public void setPlayersCount(Integer playersCount) {this.playersCount = playersCount;}
}