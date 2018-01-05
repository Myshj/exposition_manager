package launch.servlets;

import launch.servlets.commands.SearchByNameAndCityNameAndCountryName;
import launch.servlets.commands.generic.includers.IncludeAll;
import models.City;
import models.Street;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

@WebServlet(
        name = "StreetServlet",
        urlPatterns = {"/street"}
)
public class StreetServlet extends ModelServlet<Street> {

    @Override
    public void init() throws ServletException {
        super.init();
        getActions.put(
                "searchByNameAndCityNameAndCountryName",
                new SearchByNameAndCityNameAndCountryName<>(clazz(), this, repository, forwardList)
        );

        IncludeAll<City> includeCities = new IncludeAll<>(City.class, this, "cities");

        addCommandBefore(getActions, Arrays.asList(SEARCH_BY_ID, NEW), includeCities);
    }

    @Override
    protected Class<Street> clazz() {
        return Street.class;
    }

    @Override
    protected String singularName() {
        return "street";
    }

    @Override
    protected String pluralName() {
        return "streets";
    }
}
