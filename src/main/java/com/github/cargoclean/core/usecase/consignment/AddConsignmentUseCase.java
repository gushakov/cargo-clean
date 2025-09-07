package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.consignment.Consignment;
import com.github.cargoclean.core.model.consignment.ConsignmentId;
import com.github.cargoclean.core.port.persistence.PersistenceGatewayOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AddConsignmentUseCase implements AddConsignmentInputPort {

    AddConsignmentPresenterOutputPort presenter;
    SecurityOutputPort securityOps;
    PersistenceGatewayOutputPort gatewayOps;
    TransactionOperationsOutputPort txOps;

    @Override
    public void addConsignmentToCargo(String cargoTrackingId, String consignmentId, int quantityInContainers) {
        try {
            txOps.doInTransaction(() -> {
                securityOps.assertThatUserIsAgent();

                // Load the Cargo aggregate
                Cargo cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(cargoTrackingId));

                // Create a new Consignment
                Consignment consignment = Consignment.builder()
                        .consignmentId(ConsignmentId.of(consignmentId))
                        .quantityInContainers(quantityInContainers)
                        .build();

                // Save the new Consignment
                gatewayOps.saveConsignment(consignment);

                //Add consignment id to cargo
                Cargo updatedCargo = cargo.addConsignmentId(consignment.getConsignmentId());

                // Save the updated Cargo
                gatewayOps.saveCargo(updatedCargo);

                log.debug("[Consignment] Added consignment {} to cargo {}", consignmentId, cargoTrackingId);

                txOps.doAfterCommit(() -> presenter.presentConsignmentAdded(cargoTrackingId, consignmentId));
            });
        } catch (Exception e) {
            presenter.presentError(e);
        }
    }
}