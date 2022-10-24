CREATE TABLE public.handling_event
(
    event_id          integer   NOT NULL,
    voyage_number     varchar NULL,
    unlocode          varchar   NOT NULL,
    cargo_id          varchar   NOT NULL,
    completion_time   timestamp NOT NULL,
    registration_time timestamp NOT NULL,
    "type"            varchar   NOT NULL,
    CONSTRAINT handling_event_pk PRIMARY KEY (event_id),
    CONSTRAINT handling_event_cargo_fk FOREIGN KEY (cargo_id) REFERENCES public.cargo (tracking_id),
    CONSTRAINT handling_event_location_fk FOREIGN KEY (unlocode) REFERENCES public."location" (unlocode)
);
