package com.github.cargoclean.core.validator;

import com.github.cargoclean.core.model.cargo.Cargo;
import com.github.cargoclean.core.model.cargo.InvalidDestinationSpecificationError;
import com.github.cargoclean.core.model.location.Location;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Optional;
import java.util.Set;

/**
 * {@link Validator} implementation which delegates to a JSR-303 bean validator and
 * performs any business logic validation on domain entities. This service should
 * be injected into use cases (but can also be used in other layers).
 */
public class BeanValidator implements Validator {

    private final javax.validation.Validator delegate = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public <T> T validate(T toBeValidated) {

        // domain object should not be null

        if (toBeValidated == null){
            throw new InvalidDomainObjectError("Domain object cannot be null");
        }

        // check for any JSR-303 constraint violations

        Set<ConstraintViolation<T>> constraintViolations = delegate.validate(toBeValidated);
        Optional<ConstraintViolation<T>> firstOpt = constraintViolations.stream().findFirst();

        if (firstOpt.isPresent()){
            ConstraintViolation<T> error = firstOpt.get();
            throw new InvalidDomainObjectError("Invalid domain object: <%s>, property: <%s>, invalid value: <%s>, error: %s"
                    .formatted(toBeValidated.getClass().getSimpleName(),
                            error.getPropertyPath(),
                            error.getInvalidValue(),
                            error.getMessage()));
        }

        // perform any custom error checking depending on the type of the domain object

        if (Cargo.class.isAssignableFrom(toBeValidated.getClass())){
           return (T) validateCargo((Cargo) toBeValidated);
        }

        // consider valid

        return toBeValidated;
    }

    private Cargo validateCargo(Cargo cargo){

        // cargo origin and destination locations should be different

        Location destination = cargo.getRouteSpecification().getDestination();
        if (cargo.getOrigin().equals(destination)){
            throw new InvalidDestinationSpecificationError(cargo.getTrackingId(),
                    cargo.getOrigin().getUnLocode(),
                    destination.getUnLocode());
        }

        return cargo;

    }
}
