package launch.servlets.services.admin.commands.generic.includers;

import launch.servlets.ServiceContext;
import launch.servlets.services.admin.commands.generic.ModelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Includes all entities as request attribute.
 *
 * @param <T> Type of entities.
 */
public class IncludeAll<T extends Model> extends ModelCommand<T> {
    private static final Logger logger = LogManager.getLogger(IncludeAll.class);
    private String name;

    public IncludeAll(
            ServiceContext context,
            Class<T> clazz,
            String name
    ) {
        super(context, clazz);
        logger.info("created");
        this.name = name;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("started execution");
        request.setAttribute(
                name,
                repository.getAll()
        );
        logger.info("included successfully");
    }
}
