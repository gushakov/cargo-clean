CREATE TABLE public.leg
(
    id                   serial    NOT NULL,
    load_location_id     int NULL,
    load_location_time   timestamp NOT NULL,
    unload_location_id   int NULL,
    unload_location_time timestamp NOT NULL,
    cargo_id             int NULL,
    leg_index            int NULL,
    CONSTRAINT leg_pk PRIMARY KEY (id),
    CONSTRAINT leg_load_location_fk FOREIGN KEY (load_location_id) REFERENCES public."location" (id),
    CONSTRAINT leg_itinerary_fk FOREIGN KEY (cargo_id) REFERENCES public.cargo(id) ON DELETE CASCADE,
    CONSTRAINT leg_unload_location_fk FOREIGN KEY (unload_location_id) REFERENCES public."location" (id)
);
