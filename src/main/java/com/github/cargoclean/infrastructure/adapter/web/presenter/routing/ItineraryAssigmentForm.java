package com.github.cargoclean.infrastructure.adapter.web.presenter.routing;

import com.github.cargoclean.core.model.cargo.Itinerary;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.location.UnLocode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItineraryAssigmentForm {

    private TrackingId trackingId;

    private UnLocode cargoOrigin;

    private UnLocode cargoDestination;

    private List<Itinerary> candidateItineraries;

    private Itinerary selectedItinerary;
}
