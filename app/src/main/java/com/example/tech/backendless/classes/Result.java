
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class Result
{
  private String ownerId;
  private String team;
  private String dept;
  private String stuName;
  private String stuID;
  private Date created;
  private String examDate;
  private String subject;
  private Integer result;
  private Integer finalResult;
  private Date updated;
  private String objectId;
  private Integer examID;
  public String getOwnerId()
  {
    return ownerId;
  }

  public String getStuID()
  {
    return stuID;
  }

  public void setStuID( String stuID )
  {
    this.stuID = stuID;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getExamDate()
  {
    return examDate;
  }

  public void setExamDate( String examDate )
  {
    this.examDate = examDate;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public Integer getResult()
  {
    return result;
  }

  public void setResult( Integer result )
  {
    this.result = result;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getObjectId()
  {
    return objectId;
  }

                                                    
  public Result save()
  {
    return Backendless.Data.of( Result.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Result> callback )
  {
    Backendless.Data.of( Result.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Result.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Result.class ).remove( this, callback );
  }

  public static Result findById( String id )
  {
    return Backendless.Data.of( Result.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Result> callback )
  {
    Backendless.Data.of( Result.class ).findById( id, callback );
  }

  public static Result findFirst()
  {
    return Backendless.Data.of( Result.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Result> callback )
  {
    Backendless.Data.of( Result.class ).findFirst( callback );
  }

  public static Result findLast()
  {
    return Backendless.Data.of( Result.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Result> callback )
  {
    Backendless.Data.of( Result.class ).findLast( callback );
  }

  public static List<Result> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Result.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Result>> callback )
  {
    Backendless.Data.of( Result.class ).find( queryBuilder, callback );
  }

  public Integer getExamID() {
    return examID;
  }

  public void setExamID(Integer examID) {
    this.examID = examID;
  }

  public String getStuName() {
    return stuName;
  }

  public void setStuName(String stuName) {
    this.stuName = stuName;
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

  public Integer getFinalResult() {
    return finalResult;
  }

  public void setFinalResult(Integer finalResult) {
    this.finalResult = finalResult;
  }
}