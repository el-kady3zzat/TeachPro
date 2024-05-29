
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Student
{
  private String dept;
  private String img;
  private Date updated;
  private String id;
  private String objectId;
  private String name;
  private String ownerId;
  private String pass;
  private String team;
  private Date created;
  public String getDept()
  {
    return dept;
  }

  public void setDept( String dept )
  {
    this.dept = dept;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getPass()
  {
    return pass;
  }

  public void setPass( String pass )
  {
    this.pass = pass;
  }

  public String getTeam()
  {
    return team;
  }

  public void setTeam( String team )
  {
    this.team = team;
  }

  public Date getCreated()
  {
    return created;
  }

                                                    
  public Student save()
  {
    return Backendless.Data.of( Student.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Student> callback )
  {
    Backendless.Data.of( Student.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Student.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Student.class ).remove( this, callback );
  }

  public static Student findById( String id )
  {
    return Backendless.Data.of( Student.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Student> callback )
  {
    Backendless.Data.of( Student.class ).findById( id, callback );
  }

  public static Student findFirst()
  {
    return Backendless.Data.of( Student.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Student> callback )
  {
    Backendless.Data.of( Student.class ).findFirst( callback );
  }

  public static Student findLast()
  {
    return Backendless.Data.of( Student.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Student> callback )
  {
    Backendless.Data.of( Student.class ).findLast( callback );
  }

  public static List<Student> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Student.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Student>> callback )
  {
    Backendless.Data.of( Student.class ).find( queryBuilder, callback );
  }

  @Override
  public String toString() {
    return "Student{" +
            "dept='" + dept + '\'' +
            ", updated=" + updated +
            ", id=" + id +
            ", objectId='" + objectId + '\'' +
            ", name='" + name + '\'' +
            ", ownerId='" + ownerId + '\'' +
            ", pass='" + pass + '\'' +
            ", team='" + team + '\'' +
            ", created=" + created +
            '}';
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }
}