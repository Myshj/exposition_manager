package launch.servlets.admin.commands;

import launch.servlets.admin.commands.generic.ForwardingCommand;
import launch.servlets.admin.commands.generic.ShowList;
import models.commands.FindEntitiesByName;
import orm.Model;
import orm.repository.Repository;
import utils.ConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchByName<T extends Model> extends ForwardingCommand<T> {
    private FindEntitiesByName<T> finder;

    public SearchByName(
            Class<T> clazz,
            HttpServlet servlet,
            Repository<T> repository,
            ShowList<T> showList
    ) {
        super(servlet, repository, showList);
        try {
            finder = new FindEntitiesByName<>(
                    clazz,
                    ConnectionManager.INSTANCE.get()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<T> result = null;
        try {
            result = repository.filter(
                    finder.withName(
                            request.getParameter("name")
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        showList.withList(result).execute(request, response);
    }
}
