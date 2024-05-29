
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Activity
{
  private String activity;
  private String team;
  private String objectId;
  private String profID;
  private String ownerId;
  private Date created;
  private String dept;
  private Date updated;
  public String getActivity()
  {
    return activity;
  }

  public void setActivity( String activity )
  {
    this.activity = activity;
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

                                                    
  public Activity save()
  {
    return Backendless.Data.of( Activity.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Activity> callback )
  {
    Backendless.Data.of( Activity.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Activity.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Activity.class ).remove( this, callback );
  }

  public static Activity findById( String id )
  {
    return Backendless.Data.of( Activity.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Activity> callback )
  {
    Backendless.Data.of( Activity.class ).findById( id, callback );
  }

  public static Activity findFirst()
  {
    return Backendless.Data.of( Activity.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Activity> callback )
  {
    Backendless.Data.of( Activity.class ).findFirst( callback );
  }

  public static Activity findLast()
  {
    return Backendless.Data.of( Activity.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Activity> callback )
  {
    Backendless.Data.of( Activity.class ).findLast( callback );
  }

  public static List<Activity> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Activity.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Activity>> callback )
  {
    Backendless.Data.of( Activity.class ).find( queryBuilder, callback );
  }
}