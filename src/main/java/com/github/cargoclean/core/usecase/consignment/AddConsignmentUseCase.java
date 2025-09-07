package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.ConsignmentError;
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
            // Security check outside the transaction
            securityOps.assertThatUserIsAgent();

            // Load the Cargo aggregate outside the transaction
            Cargo cargo = gatewayOps.obtainCargoByTrackingId(TrackingId.of(cargoTrackingId));

            // Create a new Consignment outside the transaction
            Consignment consignment = Consignment.builder()
                    .consignmentId(ConsignmentId.of(consignmentId))
                    .quantityInContainers(quantityInContainers)
                    .build();

            txOps.doInTransaction(() -> {
                // Save the new Consignment
                gatewayOps.saveConsignment(consignment);

                //Add consignment id to cargo
                Cargo updatedCargo;
                try {
                    updatedCargo = cargo.addConsignmentId(consignment.getConsignmentId());
                } catch (ConsignmentError e) {
                    // problem with adding new consignment
                    txOps.doAfterRollback(() ->
                            presenter.presentErrorWhenConsignmentCouldNotBeAdded(consignmentId, cargoTrackingId, e.getMessage()));
                    return;
                }

                // Save the updated Cargo
                gatewayOps.saveCargo(updatedCargo);

                // do on success, i.e. when transactional lambda commits successfully
                txOps.doAfterCommit(() -> {
                    log.debug("[Consignment] Added consignment {} to cargo {}", consignmentId, cargoTrackingId);
                    presenter.presentConsignmentAdded(cargoTrackingId, consignmentId);
                });
            });


        } catch (Exception e) {
            presenter.presentError(e);
        }
    }
}