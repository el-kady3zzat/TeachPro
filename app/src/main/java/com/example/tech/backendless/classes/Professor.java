
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.util.List;
import java.util.Date;

public class Professor
{
  private Date created;
  private String name;
  private String objectId;
  private String password;
  private String id;
  private String ownerId;
  private Date updated;
  public Date getCreated()
  {
    return created;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

                                                    
  public Professor save()
  {
    return Backendless.Data.of( Professor.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Professor> callback )
  {
    Backendless.Data.of( Professor.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Professor.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Professor.class ).remove( this, callback );
  }

  public static Professor findById( String id )
  {
    return Backendless.Data.of( Professor.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Professor> callback )
  {
    Backendless.Data.of( Professor.class ).findById( id, callback );
  }

  public static Professor findFirst()
  {
    return Backendless.Data.of( Professor.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Professor> callback )
  {
    Backendless.Data.of( Professor.class ).findFirst( callback );
  }

  public static Professor findLast()
  {
    return Backendless.Data.of( Professor.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Professor> callback )
  {
    Backendless.Data.of( Professor.class ).findLast( callback );
  }

  public static List<Professor> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Professor.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Professor>> callback )
  {
    Backendless.Data.of( Professor.class ).find( queryBuilder, callback );
  }
}