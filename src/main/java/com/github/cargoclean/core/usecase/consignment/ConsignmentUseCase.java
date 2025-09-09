package com.github.cargoclean.core.usecase.consignment;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.TrackingId;
import com.github.cargoclean.core.model.cargo.TransportStatus;
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
public class ConsignmentUseCase implements ConsignmentInputPort{

    ConsignmentPresenterOutputPort presenter;
    SecurityOutputPort securityOps;
    PersistenceGatewayOutputPort gatewayOps;
    TransactionOperationsOutputPort txOps;

    @Override
    public void agentInitializesConsignmentEntry(String cargoTrackingId) {
        // Present the view for entering consignment details
        presenter.presentConsignmentEntryForm(cargoTrackingId);
    }

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

            if (cargo.getDelivery().getTransportStatus() != TransportStatus.NOT_RECEIVED) {
                presenter.presentErrorWhenConsignmentCouldNotBeAdded(consignmentId, cargoTrackingId,
                        "Cannot add consignment to cargo with transport status: " + cargo.getDelivery().getTransportStatus());
                return;
            }

            txOps.doInTransaction(() -> {
                Consignment updatedConsignment = consignment.assignToCargo(cargo.getTrackingId());
                gatewayOps.saveConsignment(updatedConsignment);
                txOps.doAfterCommit(() -> presenter.presentConsignmentAdded(consignmentId, cargoTrackingId));
            });


        } catch (Exception e) {
            presenter.presentError(e);
        }
    }
}