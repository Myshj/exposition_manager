package utils;

import orm.Model;
import orm.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public enum RepositoryManager {
    INSTANCE;

    private Map<Class, Repository> repositories = new HashMap<>();

    public <T extends Model> Repository<T> get(Class<T> clazz) {
        try {
            Repository<T> r = new Repository<>(clazz, ConnectionManager.INSTANCE.get());
            repositories.putIfAbsent(clazz, r);
            return repositories.get(clazz);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
}
