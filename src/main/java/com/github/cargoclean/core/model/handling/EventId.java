package com.github.cargoclean.core.model.handling;

import com.github.cargoclean.core.annotation.Default;
import com.github.cargoclean.core.model.InvalidDomainObjectError;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
public class EventId {

    public static EventId of(Long id) {
        return EventId.builder()
                .id(id)
                .build();
    }

    Long id;

    @Builder
    @Default
    public EventId(Long id) {
        this.id = Optional.ofNullable(id)
                .orElseThrow(() -> new InvalidDomainObjectError("Event ID cannot be null"));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
