
package com.example.tech.backendless.classes;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class Departments
{
  private Date created;
  private Date updated;
  private String en;
  private String ownerId;
  private String objectId;
  private String ar;
  public Date getCreated()
  {
    return created;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String geten()
  {
    return en;
  }

  public void seten( String en )
  {
    this.en = en;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getar()
  {
    return ar;
  }

  public void setar( String ar )
  {
    this.ar = ar;
  }

                                                    
  public Departments save()
  {
    return Backendless.Data.of( Departments.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Departments> callback )
  {
    Backendless.Data.of( Departments.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Departments.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Departments.class ).remove( this, callback );
  }

  public static Departments findById( String id )
  {
    return Backendless.Data.of( Departments.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Departments> callback )
  {
    Backendless.Data.of( Departments.class ).findById( id, callback );
  }

  public static Departments findFirst()
  {
    return Backendless.Data.of( Departments.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Departments> callback )
  {
    Backendless.Data.of( Departments.class ).findFirst( callback );
  }

  public static Departments findLast()
  {
    return Backendless.Data.of( Departments.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Departments> callback )
  {
    Backendless.Data.of( Departments.class ).findLast( callback );
  }

  public static List<Departments> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Departments.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Departments>> callback )
  {
    Backendless.Data.of( Departments.class ).find( queryBuilder, callback );
  }
}