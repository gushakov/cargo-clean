package com.github.cargoclean.infrastructure.adapter.web.editlocations;

import com.github.cargoclean.core.port.presenter.editlocations.EditLocationsPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.LocalDispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
                .build();
        presentModelAndView(Map.of("addLocationForm", form), "add-location");
    }
}
