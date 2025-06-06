import java.sql.*;
import java.util.List;

public abstract class GenericService<T> 
{
    private static final java.util.Map<Class<?>, GenericService<?>> instances = new java.util.HashMap<>();

    @SuppressWarnings("unchecked")
    public static synchronized <T> GenericService<T> getInstance(Class<? extends GenericService<T>> daoClass) 
    {
        if (!instances.containsKey(daoClass)) 
        {
            try 
            {
                instances.put(daoClass, daoClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) 
            {
                throw new RuntimeException("Cannot instantiate DAO: " + daoClass, e);
            }
        }
        return (GenericService<T>) instances.get(daoClass);
    }

    public abstract void add(T obj) throws SQLException;
    public abstract List<T> getAll() throws SQLException;
    public abstract void update(T obj) throws SQLException;
    public abstract void delete(T obj) throws SQLException;
}