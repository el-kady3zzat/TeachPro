
package com.example.tech.game;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class GameQuestions
{
  private String profID;
  private String Answer2;
  private Date created;
  private String Answer4;
  private String Answer1;
  private Date updated;
  private String ownerId;
  private String Answer3;
  private String objectId;
  private String rightAnswer;
  private String question;
  private String subject;
  private String level;
  private String team;
  private String dept;
  private String questionType;

  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

  public String getAnswer2()
  {
    return Answer2;
  }

  public void setAnswer2( String Answer2 )
  {
    this.Answer2 = Answer2;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getAnswer4()
  {
    return Answer4;
  }

  public void setAnswer4( String Answer4 )
  {
    this.Answer4 = Answer4;
  }

  public String getAnswer1()
  {
    return Answer1;
  }

  public void setAnswer1( String Answer1 )
  {
    this.Answer1 = Answer1;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getAnswer3()
  {
    return Answer3;
  }

  public void setAnswer3( String Answer3 )
  {
    this.Answer3 = Answer3;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getRightAnswer()
  {
    return rightAnswer;
  }

  public void setRightAnswer( String rightAnswer )
  {
    this.rightAnswer = rightAnswer;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

                                                    
  public GameQuestions save()
  {
    return Backendless.Data.of( GameQuestions.class ).save( this );
  }

  public void saveAsync( AsyncCallback<GameQuestions> callback )
  {
    Backendless.Data.of( GameQuestions.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( GameQuestions.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( GameQuestions.class ).remove( this, callback );
  }

  public static GameQuestions findById( String id )
  {
    return Backendless.Data.of( GameQuestions.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<GameQuestions> callback )
  {
    Backendless.Data.of( GameQuestions.class ).findById( id, callback );
  }

  public static GameQuestions findFirst()
  {
    return Backendless.Data.of( GameQuestions.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<GameQuestions> callback )
  {
    Backendless.Data.of( GameQuestions.class ).findFirst( callback );
  }

  public static GameQuestions findLast()
  {
    return Backendless.Data.of( GameQuestions.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<GameQuestions> callback )
  {
    Backendless.Data.of( GameQuestions.class ).findLast( callback );
  }

  public static List<GameQuestions> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( GameQuestions.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<GameQuestions>> callback )
  {
    Backendless.Data.of( GameQuestions.class ).find( queryBuilder, callback );
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }
}