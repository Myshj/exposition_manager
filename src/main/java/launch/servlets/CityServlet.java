package launch.servlets;

import launch.servlets.commands.SearchCitiesByNameAndCountryName;
import launch.servlets.commands.generic.includers.IncludeAll;
import models.City;
import models.Country;
import orm.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

@WebServlet(
        name = "CityServlet",
        urlPatterns = {"/admin/city"}
)
public class CityServlet extends ModelServlet<City> {
    private static String SEARCH_BY_NAME_AND_COUNTRY_NAME = action("searchByNameAndCountryName");

    @Override
    public void init() throws ServletException {
        super.init();
        getActions.put(
                SEARCH_BY_NAME_AND_COUNTRY_NAME,
                new SearchCitiesByNameAndCountryName<>(clazz(), this, repository, forwardList)
        );

        IncludeAll<Country> includeCountries = new IncludeAll<>(
                Country.class,
                this,
                Model.pluralName(Country.class)
        );
        addCommandBefore(getActions, Arrays.asList(SEARCH_BY_ID, NEW), includeCountries);
    }

    @Override
    protected Class<City> clazz() {
        return City.class;
    }
}
