
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class ActivityResult
{
  private String objectId;
  private String ownerId;
  private Date created;
  private Date updated;
  private String activity;
  private String stuID;

  public String getObjectId()
  {
    return objectId;
  }

 public String getOwnerId()
  {
    return ownerId;
  }

  public Date getCreated()
  {
    return created;
  }

 public Date getUpdated()
  {
    return updated;
  }

                                                    
  public ActivityResult save()
  {
    return Backendless.Data.of( ActivityResult.class ).save( this );
  }

  public void saveAsync( AsyncCallback<ActivityResult> callback )
  {
    Backendless.Data.of( ActivityResult.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( ActivityResult.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( ActivityResult.class ).remove( this, callback );
  }

  public static ActivityResult findById(String id )
  {
    return Backendless.Data.of( ActivityResult.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<ActivityResult> callback )
  {
    Backendless.Data.of( ActivityResult.class ).findById( id, callback );
  }

  public static ActivityResult findFirst()
  {
    return Backendless.Data.of( ActivityResult.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<ActivityResult> callback )
  {
    Backendless.Data.of( ActivityResult.class ).findFirst( callback );
  }

  public static ActivityResult findLast()
  {
    return Backendless.Data.of( ActivityResult.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<ActivityResult> callback )
  {
    Backendless.Data.of( ActivityResult.class ).findLast( callback );
  }

  public static List<ActivityResult> find(DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( ActivityResult.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<ActivityResult>> callback )
  {
    Backendless.Data.of( ActivityResult.class ).find( queryBuilder, callback );
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public String getStuID() {
    return stuID;
  }

  public void setStuID(String stuID) {
    this.stuID = stuID;
  }
}