package uk.ac.cam.echo.data;

import java.io.Serializable;

public interface Tag extends Serializable{
   public long getId();
   public String getName();
   public void setName(String name);
}
