package project;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 14:54:25
 */
//public interface ModelDAO {
//  public void loadTo(Object model);
//  public void loadFrom(Object model);
//}
public interface ModelDAO<T> {
  public void loadTo(T model);
  public void loadFrom(T model);
}
