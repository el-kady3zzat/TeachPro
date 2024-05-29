
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Exam
{
  private String objectId;
  private String quesNum;//
  private String profID;
  private String examDate;//
  private String dept;//
  private Integer examID;
  private String team;//
  private String timeRange;//
  private String type;//
  private String subject;//
  private Date updated;
  private String timeType;//
  private Date created;
  private String ownerId;

  public String getObjectId()
  {
    return objectId;
  }

  public String getQuesNum()
  {
    return quesNum;
  }

  public void setQuesNum( String quesNum )
  {
    this.quesNum = quesNum;
  }

  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

  public String  getExamDate()
  {
    return examDate;
  }

  public void setExamDate( String examDate )
  {
    this.examDate = examDate;
  }

  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public Integer getExamID()
  {
    return examID;
  }

  public void setExamID( Integer examID )
  {
    this.examID = examID;
  }

  public String getTeam()
  {
    return team;
  }

  public void setTeam( String team )
  {
    this.team = team;
  }

  public String getTimeRange()
  {
    return timeRange;
  }

  public void setTimeRange( String timeRange )
  {
    this.timeRange = timeRange;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getTimeType()
  {
    return timeType;
  }

  public void setTimeType( String timeType )
  {
    this.timeType = timeType;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getOwnerId()
  {
    return ownerId;
  }
                                                    
  public Exam save()
  {
    return Backendless.Data.of( Exam.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Exam> callback ) {
    Backendless.Data.of( Exam.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Exam.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback ) {
    Backendless.Data.of( Exam.class ).remove( this, callback );
  }

  public static Exam findById( String id ) {
    return Backendless.Data.of( Exam.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Exam> callback ) {
    Backendless.Data.of( Exam.class ).findById( id, callback );
  }

  public static Exam findFirst()
  {
    return Backendless.Data.of( Exam.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Exam> callback ) {
    Backendless.Data.of( Exam.class ).findFirst( callback );
  }

  public static Exam findLast()
  {
    return Backendless.Data.of( Exam.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Exam> callback ) {
    Backendless.Data.of( Exam.class ).findLast( callback );
  }

  public static List<Exam> find( DataQueryBuilder queryBuilder ) {
    return Backendless.Data.of( Exam.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Exam>> callback ) {
    Backendless.Data.of( Exam.class ).find( queryBuilder, callback );
  }


//  public Integer getAvailable() {
//    return available;
//  }
//
//  public void setAvailable(Integer available) {
//    this.available = available;
//  }
}