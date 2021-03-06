package utils.globals;

import utils.managers.ConnectionManager;
import utils.managers.MetaInfoManager;
import utils.managers.RepositoryManager;

/**
 * Base interface for all classes containing managers.
 */
public interface IManagers {
    ConnectionManager getConnection();

    RepositoryManager getRepository();

    IResourceManagers getResources();

    MetaInfoManager getMetaInfo();
}
