
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Content
{
  private Date updated;
  private Date created;
  private String url;
  private String fileType;
  private String fileName;
  private String ownerId;
  private String team;
  private String objectId;
  private String dept;
  private String profID;
  public Date getUpdated()
  {
    return updated;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
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

  public String getProfID()
  {
    return profID;
  }

  public void setProfID( String profID )
  {
    this.profID = profID;
  }

                                                    
  public Content save()
  {
    return Backendless.Data.of( Content.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Content> callback )
  {
    Backendless.Data.of( Content.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Content.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Content.class ).remove( this, callback );
  }

  public static Content findById( String id )
  {
    return Backendless.Data.of( Content.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Content> callback )
  {
    Backendless.Data.of( Content.class ).findById( id, callback );
  }

  public static Content findFirst()
  {
    return Backendless.Data.of( Content.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Content> callback )
  {
    Backendless.Data.of( Content.class ).findFirst( callback );
  }

  public static Content findLast()
  {
    return Backendless.Data.of( Content.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Content> callback )
  {
    Backendless.Data.of( Content.class ).findLast( callback );
  }

  public static List<Content> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Content.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Content>> callback )
  {
    Backendless.Data.of( Content.class ).find( queryBuilder, callback );
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}