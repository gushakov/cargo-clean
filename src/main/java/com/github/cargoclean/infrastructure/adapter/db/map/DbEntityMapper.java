package com.github.cargoclean.infrastructure.adapter.db.map;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.Delivery;
import com.github.cargoclean.core.model.cargo.RouteSpecification;
import com.github.cargoclean.core.model.handling.HandlingEvent;
import com.github.cargoclean.core.model.location.Location;
import com.github.cargoclean.core.model.report.ExpectedArrivals;
import com.github.cargoclean.infrastructure.adapter.db.cargo.CargoDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.DeliveryDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.cargo.RouteSpecificationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.handling.HandlingEventEntity;
import com.github.cargoclean.infrastructure.adapter.db.location.LocationDbEntity;
import com.github.cargoclean.infrastructure.adapter.db.report.ExpectedArrivalsQueryRow;
import com.github.cargoclean.infrastructure.adapter.map.IgnoreForMapping;

public interface DbEntityMapper {

    Location convert(LocationDbEntity locationDbEntity);

    CargoDbEntity convert(Cargo cargo);

    Cargo convert(CargoDbEntity cargoDbEntity);

    Delivery convert(DeliveryDbEntity deliveryDbEntity);

    DeliveryDbEntity convert(Delivery delivery);

    RouteSpecification convert(RouteSpecificationDbEntity routeSpecificationDbEntity);

    RouteSpecificationDbEntity convert(RouteSpecification routeSpecification);

    ExpectedArrivals convert(ExpectedArrivalsQueryRow arrivalsQueryRow);

    HandlingEvent convert(HandlingEventEntity handlingEventEntity);

    HandlingEventEntity convert(HandlingEvent handlingEvent);

}
