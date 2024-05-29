
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class SurveyAnswers
{
  private String profID;
  private Date updated;
  private String stuID;
  private String id;
  private String dept;
  private String name;
  private String team;
  private String objectId;
  private String answer;
  private String ownerId;
  private Date created;
  private String question;
  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getStuID()
  {
    return stuID;
  }

  public void setStuID( String stuID )
  {
    this.stuID = stuID;
  }

  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getTeam()
  {
    return team;
  }

  public void setTeam( String team )
  {
    this.team = team;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getAnswer()
  {
    return answer;
  }

  public void setAnswer( String answer )
  {
    this.answer = answer;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

                                                    
  public SurveyAnswers save()
  {
    return Backendless.Data.of( SurveyAnswers.class ).save( this );
  }

  public void saveAsync( AsyncCallback<SurveyAnswers> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( SurveyAnswers.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).remove( this, callback );
  }

  public static SurveyAnswers findById( String id )
  {
    return Backendless.Data.of( SurveyAnswers.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<SurveyAnswers> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).findById( id, callback );
  }

  public static SurveyAnswers findFirst()
  {
    return Backendless.Data.of( SurveyAnswers.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<SurveyAnswers> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).findFirst( callback );
  }

  public static SurveyAnswers findLast()
  {
    return Backendless.Data.of( SurveyAnswers.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<SurveyAnswers> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).findLast( callback );
  }

  public static List<SurveyAnswers> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( SurveyAnswers.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<SurveyAnswers>> callback )
  {
    Backendless.Data.of( SurveyAnswers.class ).find( queryBuilder, callback );
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}