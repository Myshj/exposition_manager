package launch.servlets.general.commands;

import launch.servlets.admin.commands.generic.includers.IncludeAddress;
import launch.servlets.admin.commands.generic.includers.IncludeListToRequest;
import models.Exposition;
import models.Showroom;
import models.commands.FindActiveExpositionsByShowroom;
import utils.ConnectionManager;
import utils.ModelNameManager;
import utils.RepositoryManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SearchShowroomById extends ServletCommand {
    private IncludeListToRequest<Exposition> expositionIncluder = new IncludeListToRequest<>(
            this.servlet,
            ModelNameManager.INSTANCE.pluralName(Exposition.class)
    );

    private IncludeAddress addressIncluder = new IncludeAddress(this.servlet, "address");

    private FindActiveExpositionsByShowroom expositionFinder;

    public SearchShowroomById(HttpServlet servlet) {
        super(servlet);
        try {
            expositionFinder = new FindActiveExpositionsByShowroom(
                    Exposition.class,
                    ConnectionManager.INSTANCE.get()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Showroom showroom = RepositoryManager.INSTANCE.get(Showroom.class).getById(
                Long.valueOf(request.getParameter("id"))
        ).orElse(null);
        request.setAttribute("showroom", showroom);

        if (showroom != null) {
            expositionIncluder.withList(
                    expositionFinder.withShowroom(showroom)
                            .withDateTime(LocalDateTime.now())
                            .execute()
            ).accept(request, response);

            addressIncluder.withBuilding(showroom.getBuilding().getValue())
                    .accept(request, response);
        }

        dispatcher("/jsp/general/observe-showroom.jsp").forward(request, response);
    }
}
