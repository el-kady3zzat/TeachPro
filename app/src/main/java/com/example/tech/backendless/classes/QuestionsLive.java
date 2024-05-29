
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class QuestionsLive
{
  private Date updated;
  private String Answer4;
  private Date created;
  private String Answer1;
  private String Answer3;
  private String Answer2;
  private String rightAnswer;
  private String questionType;
  private String objectId;
  private String question;
  private String ownerId;
  private Integer gameID;
  public Date getUpdated()
  {
    return updated;
  }

  public String getAnswer4()
  {
    return Answer4;
  }

  public void setAnswer4( String Answer4 )
  {
    this.Answer4 = Answer4;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getAnswer1()
  {
    return Answer1;
  }

  public void setAnswer1( String Answer1 )
  {
    this.Answer1 = Answer1;
  }

  public String getAnswer3()
  {
    return Answer3;
  }

  public void setAnswer3( String Answer3 )
  {
    this.Answer3 = Answer3;
  }

  public String getAnswer2()
  {
    return Answer2;
  }

  public void setAnswer2( String Answer2 )
  {
    this.Answer2 = Answer2;
  }

  public String getRightAnswer()
  {
    return rightAnswer;
  }

  public void setRightAnswer( String rightAnswer )
  {
    this.rightAnswer = rightAnswer;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
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

                                                    
  public QuestionsLive save()
  {
    return Backendless.Data.of( QuestionsLive.class ).save( this );
  }

  public void saveAsync( AsyncCallback<QuestionsLive> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( QuestionsLive.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).remove( this, callback );
  }

  public static QuestionsLive findById(String id )
  {
    return Backendless.Data.of( QuestionsLive.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<QuestionsLive> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).findById( id, callback );
  }

  public static QuestionsLive findFirst()
  {
    return Backendless.Data.of( QuestionsLive.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<QuestionsLive> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).findFirst( callback );
  }

  public static QuestionsLive findLast()
  {
    return Backendless.Data.of( QuestionsLive.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<QuestionsLive> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).findLast( callback );
  }

  public static List<QuestionsLive> find(DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( QuestionsLive.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<QuestionsLive>> callback )
  {
    Backendless.Data.of( QuestionsLive.class ).find( queryBuilder, callback );
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }
}