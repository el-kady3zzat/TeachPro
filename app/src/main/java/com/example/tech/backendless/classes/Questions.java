
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Questions
{
  private Date updated;
  private String Answer4;
  private Date created;
  private String Answer1;
  private String Answer3;
  private String Answer2;
  private String rightAnswer;
  private String objectId;
  private String question;
  private String ownerId;
  private Integer examID;
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

  public Integer getExamID()
  {
    return examID;
  }

  public void setExamID( Integer examID )
  {
    this.examID = examID;
  }

                                                    
  public Questions save()
  {
    return Backendless.Data.of( Questions.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Questions> callback )
  {
    Backendless.Data.of( Questions.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Questions.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Questions.class ).remove( this, callback );
  }

  public static Questions findById( String id )
  {
    return Backendless.Data.of( Questions.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Questions> callback )
  {
    Backendless.Data.of( Questions.class ).findById( id, callback );
  }

  public static Questions findFirst()
  {
    return Backendless.Data.of( Questions.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Questions> callback )
  {
    Backendless.Data.of( Questions.class ).findFirst( callback );
  }

  public static Questions findLast()
  {
    return Backendless.Data.of( Questions.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Questions> callback )
  {
    Backendless.Data.of( Questions.class ).findLast( callback );
  }

  public static List<Questions> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Questions.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Questions>> callback )
  {
    Backendless.Data.of( Questions.class ).find( queryBuilder, callback );
  }
}