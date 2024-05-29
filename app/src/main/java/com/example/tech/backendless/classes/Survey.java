
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Survey
{
  private String answer2;
  private Date created;
  private String team;
  private String name;
  private Date updated;
  private String survey;
  private String answer3;
  private String answer4;
  private String answer5;
  private String ownerId;
  private String profID;
  private String dept;
  private Integer id;
  private String objectId;
  private String answer1;

  public String getAnswer2()
  {
    return answer2;
  }

  public void setAnswer2( String answer2 )
  {
    this.answer2 = answer2;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getTeam()
  {
    return team;
  }

  public void setTeam( String team )
  {
    this.team = team;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getSurvey()
  {
    return survey;
  }

  public void setSurvey( String survey )
  {
    this.survey = survey;
  }

  public String getAnswer3()
  {
    return answer3;
  }

  public void setAnswer3( String answer3 )
  {
    this.answer3 = answer3;
  }

  public String getAnswer4()
  {
    return answer4;
  }

  public void setAnswer4( String answer4 )
  {
    this.answer4 = answer4;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getAnswer1()
  {
    return answer1;
  }

  public void setAnswer1( String answer1 )
  {
    this.answer1 = answer1;
  }

                                                    
  public Survey save()
  {
    return Backendless.Data.of( Survey.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Survey> callback )
  {
    Backendless.Data.of( Survey.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Survey.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Survey.class ).remove( this, callback );
  }

  public static Survey findById( String id )
  {
    return Backendless.Data.of( Survey.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Survey> callback )
  {
    Backendless.Data.of( Survey.class ).findById( id, callback );
  }

  public static Survey findFirst()
  {
    return Backendless.Data.of( Survey.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Survey> callback )
  {
    Backendless.Data.of( Survey.class ).findFirst( callback );
  }

  public static Survey findLast()
  {
    return Backendless.Data.of( Survey.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Survey> callback )
  {
    Backendless.Data.of( Survey.class ).findLast( callback );
  }

  public static List<Survey> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Survey.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Survey>> callback )
  {
    Backendless.Data.of( Survey.class ).find( queryBuilder, callback );
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAnswer5() {
    return answer5;
  }

  public void setAnswer5(String answer5) {
    this.answer5 = answer5;
  }
}