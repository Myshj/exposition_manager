package orm.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base class for all commands querying database for lists of entities.
 *
 * @param <T>
 */
public abstract class ListEntitiesCommand<T extends Model> extends QueryCommand<T> {
    private static final Logger logger = LogManager.getLogger(ListEntitiesCommand.class);

    public ListEntitiesCommand(
            Class<T> clazz,
            Connection connection,
            String sql
    ) {
        super(clazz, connection, sql);
        logger.info("constructed");
    }

    /**
     *
     * @return returned entities or empty list if SQLException occured.
     */
    public final List<T> execute() {
        logger.info("started execution");
        List<T> result = new ArrayList<>();
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Optional.ofNullable(converter.apply(rs)).ifPresent(result::add);
            }
        } catch (SQLException e) {
            logger.error("returned empty list");
            logger.error(e);
        }

        logger.info("executed");
        return result;
    }
}
