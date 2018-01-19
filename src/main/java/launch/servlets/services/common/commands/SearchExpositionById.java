package launch.servlets.services.common.commands;

import launch.servlets.ServiceContext;
import launch.servlets.services.admin.commands.generic.includers.IncludeListToRequest;
import launch.servlets.services.commands.ServletCommand;
import models.Exposition;
import models.Ticket;
import models.commands.FindTicketsByExposition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.commands.CommandContext;
import utils.meta.MetaInfoManager;
import utils.transactions.TransactionExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Command to search exposition and related tickets by given id of exposition.
 */
public class SearchExpositionById extends ServletCommand {
    private static final Logger logger = LogManager.getLogger(SearchExpositionById.class);

    private FindTicketsByExposition ticketsFinder;
    private IncludeListToRequest<Ticket> ticketIncluder = new IncludeListToRequest<>(
            context, Ticket.class,
            MetaInfoManager.INSTANCE.get(Ticket.class).getNames().getPlural()
    );

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("started execution");
        new TransactionExecutor(context.getManagers().getConnection().get()).apply(
                () -> {
                    Exposition exposition = context.getManagers().getRepository().get(Exposition.class).getById(
                            Long.valueOf(request.getParameter("id"))
                    ).orElse(null);
                    request.setAttribute("exposition", exposition);

                    if (exposition != null) {
                        ticketIncluder.withList(ticketsFinder.withExposition(exposition).execute())
                                .accept(request, response);
                    }
                }
        );


        dispatcher("/jsp/general/observe-exposition.jsp").forward(request, response);
        logger.info("executed");
    }

    public SearchExpositionById(ServiceContext context) {
        super(context);
        logger.info("started construction");
        try {
            ticketsFinder = new FindTicketsByExposition(
                    new CommandContext<>(
                            Ticket.class,
                            context.getManagers().getRepository(),
                            context.getManagers().getConnection().get()
                    )
                    //ConnectionServiceProvider.INSTANCE.get()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        logger.info("constructed");
    }
}
