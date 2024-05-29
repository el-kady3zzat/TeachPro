
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class GPA
{
  private Date updated;
  private Date created;
  private Integer gpa;
  private String stuName;
  private String ownerId;
  private String team;
  private String objectId;
  private String dept;
  private String stuID;
  public Date getUpdated()
  {
    return updated;
  }

  public Date getCreated()
  {
    return created;
  }

  public Integer getGpa()
  {
    return gpa;
  }

  public void setGpa( Integer gpa )
  {
    this.gpa = gpa;
  }

  public String getOwnerId()
  {
    return ownerId;
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

  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public String getStuID()
  {
    return stuID;
  }

  public void setStuID( String stuID )
  {
    this.stuID = stuID;
  }

                                                    
  public GPA save()
  {
    return Backendless.Data.of( GPA.class ).save( this );
  }

  public void saveAsync( AsyncCallback<GPA> callback )
  {
    Backendless.Data.of( GPA.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( GPA.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( GPA.class ).remove( this, callback );
  }

  public static GPA findById(String id )
  {
    return Backendless.Data.of( GPA.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<GPA> callback )
  {
    Backendless.Data.of( GPA.class ).findById( id, callback );
  }

  public static GPA findFirst()
  {
    return Backendless.Data.of( GPA.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<GPA> callback )
  {
    Backendless.Data.of( GPA.class ).findFirst( callback );
  }

  public static GPA findLast()
  {
    return Backendless.Data.of( GPA.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<GPA> callback )
  {
    Backendless.Data.of( GPA.class ).findLast( callback );
  }

  public static List<GPA> find(DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( GPA.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<GPA>> callback )
  {
    Backendless.Data.of( GPA.class ).find( queryBuilder, callback );
  }

  public String getStuName() {
    return stuName;
  }

  public void setStuName(String stuName) {
    this.stuName = stuName;
  }
}