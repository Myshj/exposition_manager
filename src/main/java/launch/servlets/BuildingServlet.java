package launch.servlets;

import launch.servlets.commands.SearchByNameAndStreetNameAndCityNameAndCountryName;
import launch.servlets.commands.generic.includers.IncludeAll;
import models.Building;
import models.Street;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

@WebServlet(
        name = "BuildingServlet",
        urlPatterns = {"/building"}
)
public class BuildingServlet extends ModelServlet<Building> {


    @Override
    protected Class<Building> clazz() {
        return Building.class;
    }

    @Override
    protected String singularName() {
        return "building";
    }

    @Override
    protected String pluralName() {
        return "buildings";
    }

    @Override
    public void init() throws ServletException {
        super.init();
        getActions.put(
                "searchByNameAndStreetNameAndCityNameAndCountryName",
                new SearchByNameAndStreetNameAndCityNameAndCountryName<>(clazz(), this, repository, forwardList)
        );

        IncludeAll<Street> includeStreets = new IncludeAll<>(Street.class, this, "streets");
        addCommandBefore(getActions, Arrays.asList(SEARCH_BY_ID, NEW), includeStreets);
    }
}
