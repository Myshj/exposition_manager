package utils;

import orm.Model;
import orm.repository.Repository;
import utils.managers.ConnectionManager;
import utils.managers.resource.ResourceBundleAccessor;
import utils.managers.resource.ResourceManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Stores model repositories.
 */
public enum RepositoryManager {
    INSTANCE;

    private Map<Class, Repository> repositories = new HashMap<>();
    private Supplier<Connection> connectionSupplier = new ConnectionManager(
            new ResourceManager(
                    new ResourceBundleAccessor().withResource("application")
            )
    );

    /**
     * Returns repository for a given model class.
     *
     * @param clazz class of model.
     * @param <T>   class of model
     * @return repository for a given model class or null if any exception occured
     */
    public <T extends Model> Repository<T> get(Class<T> clazz) {
        try {
            Repository<T> r = new Repository<>(
                    clazz,
                    connectionSupplier.get()
                    //ConnectionServiceProvider.INSTANCE.get()
            );
            repositories.putIfAbsent(clazz, r);
            return repositories.get(clazz);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
}
