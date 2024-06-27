package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.location.Region;
import com.github.cargoclean.core.usecase.editlocation.EditLocationsPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@Slf4j
public class EditLocationsPresenter extends AbstractWebPresenter implements EditLocationsPresenterOutputPort {
    public EditLocationsPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentAddNewLocationForm() {
        AddLocationForm form = AddLocationForm.builder()
                .unLocode("")
                .location("")
                .region("")
                .allRegions(Arrays.stream(Region.values())
                        .filter(region -> region != Region.UNKNOWN)
                        .map(Region::toString).collect(Collectors.joining(", ")))
                .build();
        presentModelAndView(Map.of("addLocationForm", form), "add-location");
    }

    @Override
    public void presentResultOfSuccessfulRegistrationOfNewLocation(Location location) {
        log.debug("[Edit Locations] Successfully registered new location: %s"
                .formatted(location.toString()));
        redirect("/", Map.of());
    }

    @Override
    public void presentUpdateLocationForm(List<Location> locations) {
        UpdateLocationForm form = UpdateLocationForm.builder()
                .locations(locations.stream().map(Location::toString).toList())
                .build();
        presentModelAndView(Map.of("updateLocationForm", form), "update-location");
    }
}
