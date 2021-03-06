package data_access.queries;

import models.Order;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.repository.impl.sql.queries.ListEntitiesQuery;
import orm.repository.impl.sql.queries.SqlQueryContext;

import java.sql.SQLException;

/**
 * Finds all orders made by user.
 */
public class FindOrdersByUser extends ListEntitiesQuery<Order> {
    private static final Logger logger = LogManager.getLogger(FindOrdersByUser.class);

    public FindOrdersByUser withUser(User user) {
        logger.info("started remembering user");
        try {
            statement.setLong(1, user.getId().getValue());
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.info("remembered user");
        return this;
    }

    public FindOrdersByUser(SqlQueryContext<Order> context) {
        super(
                context,
                "SELECT * FROM orders WHERE user_id=?;"
        );
    }
}
