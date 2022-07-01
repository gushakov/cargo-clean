package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.port.presenter.routing.RoutingPresenterOutputPort;
import com.github.cargoclean.infrastructure.adapter.web.presenter.AbstractWebPresenter;
import com.github.cargoclean.infrastructure.adapter.web.presenter.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Scope(scopeName = "request")
public class RoutingPresenter extends AbstractWebPresenter implements RoutingPresenterOutputPort {
    public RoutingPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentCargoForRouting(Cargo cargo) {

        /*
            We are creating a "Response Model" (flat POJO) object
            tailored specifically to the needs of the view at hand.
            It is different from the CA's principles, because we
            are crossing the boundary between Use Cases
            layer and the Interface Adapters layer with a domain
            object as a parameter (instead of a DTO).
         */

        final CargoInfoDto dto = CargoInfoDto.builder()
                .trackingId(cargo.getTrackingId().getId())
                .origin(cargo.getOrigin().getUnLocode().getCode())
                .destination(cargo.getRouteSpecification().getDestination().getUnLocode().getCode())
                .arrivalDate(cargo.getRouteSpecification().getArrivalDeadline()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();

        presentModelAndView(Map.of("cargo", dto), "show");

    }
}
