package com.github.cargoclean.core.model.handling;

import lombok.Builder;
import lombok.Value;

import static com.github.cargoclean.core.model.Assert.notNull;

@Value
public class EventId {

    public static EventId of(Long id) {
        return EventId.builder()
                .id(id)
                .build();
    }

    Long id;

    @Builder
    public EventId(Long id) {
        this.id = notNull(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
