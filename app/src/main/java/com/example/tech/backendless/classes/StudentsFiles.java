
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class StudentsFiles
{
  private String fileName;
  private String objectId;
  private String profID;
  private String ownerId;
  private Date created;
  private String stuID;
  private String stuName;
  private String url;
  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getStuID()
  {
    return stuID;
  }

  public void setStuID( String StuID )
  {
    this.stuID = StuID;
  }

  public String getUrl()
  {
    return url;
  }

                                                    
  public StudentsFiles save()
  {
    return Backendless.Data.of( StudentsFiles.class ).save( this );
  }

  public void saveAsync( AsyncCallback<StudentsFiles> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( StudentsFiles.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).remove( this, callback );
  }

  public static StudentsFiles findById(String id )
  {
    return Backendless.Data.of( StudentsFiles.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<StudentsFiles> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).findById( id, callback );
  }

  public static StudentsFiles findFirst()
  {
    return Backendless.Data.of( StudentsFiles.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<StudentsFiles> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).findFirst( callback );
  }

  public static StudentsFiles findLast()
  {
    return Backendless.Data.of( StudentsFiles.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<StudentsFiles> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).findLast( callback );
  }

  public static List<StudentsFiles> find(DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( StudentsFiles.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<StudentsFiles>> callback )
  {
    Backendless.Data.of( StudentsFiles.class ).find( queryBuilder, callback );
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getStuName() {
    return stuName;
  }

  public void setStuName(String stuName) {
    stuName = stuName;
  }
}