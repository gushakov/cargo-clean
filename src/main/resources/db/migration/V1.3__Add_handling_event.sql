CREATE TABLE public.handling_event
(
    event_id int8 NOT NULL,
    voyage_number     varchar NULL,
    location          varchar   NOT NULL,
    cargo_id          varchar   NOT NULL,
    completion_time   timestamp NOT NULL,
    registration_time timestamp NOT NULL,
    "type"            varchar   NOT NULL,
    "version"             int   NULL,
    CONSTRAINT handling_event_pk PRIMARY KEY (event_id),
    CONSTRAINT handling_event_cargo_fk FOREIGN KEY (cargo_id) REFERENCES public.cargo (tracking_id),
    CONSTRAINT handling_event_location_fk FOREIGN KEY (location) REFERENCES public."location" (unlocode)
);
